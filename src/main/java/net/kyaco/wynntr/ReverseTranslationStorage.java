package net.kyaco.wynntr;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Queue;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.resource.language.TranslationStorage;
import net.minecraft.resource.ResourceManager;
import net.minecraft.text.HoverEvent;
import net.minecraft.text.LiteralText;
import net.minecraft.text.Style;
import net.minecraft.text.Text;
import net.minecraft.text.TranslatableText;

public class ReverseTranslationStorage extends TranslationStorage {
	private Map<String, String> rawReversDict = new HashMap<String, String>();
	private Map<Pattern, String> regxpReversDict = new HashMap<Pattern, String>();
	private Set<String> unregisteredStrings = new HashSet<String>();

	private final Pattern REGXP_KEY_PATTERN = Pattern.compile("^(.+)\\.regexp$");

	public void apply(ResourceManager rm) {
		super.load(rm, Arrays.asList("wynntr"));

		rawReversDict.clear();
		regxpReversDict.clear();
		unregisteredStrings.clear();

		for (Map.Entry<String, String> entry : this.translations.entrySet()) {
			Matcher m = REGXP_KEY_PATTERN.matcher(entry.getKey());
			if (m.matches()) {
				String translateKey = m.group(1);
				Pattern p = Pattern.compile(entry.getValue());
				regxpReversDict.put(p, translateKey);
			} else {
				rawReversDict.put(entry.getValue(), entry.getKey());
			}
		}
		System.out.println("[WynnTextReplacer] Reloaded text maps.");
	}





	public String ReverseTranslate(String string, String context) {
		Text t = new LiteralText(string);
		t = ReverseTranslate(t, context);
		return t.asString();
	}

	public Text ReverseTranslate(Text text, String context) {
		if (text == null) return text;
		TranslatableText translatableText;
		translatableText = tryNormalReplace(text);
		if (translatableText == null)
		{
			translatableText = tryRegexpReplace(text);
			if (translatableText == null)
			{
				recordUnregisterdText(text, context);
				return text;
			}
		}
		return tryToFixEvents(text, translatableText);
	}




	private TranslatableText tryNormalReplace(Text text) {
		String formattedString = text.asFormattedString();
		String text_id = rawReversDict.get(formattedString);
		if (text_id != null) return new TranslatableText(text_id);
		return null;
	}
	private TranslatableText tryRegexpReplace(Text text) {
		String formattedString = text.asFormattedString();
		for (Map.Entry<Pattern, String> entry : this.regxpReversDict.entrySet()) {
			Matcher m = entry.getKey().matcher(formattedString);
			if (!m.matches())
				continue;
			Object obj[] = new Object[m.groupCount()];
			for (int i = 0; i < m.groupCount(); i++) {
				obj[i] = m.group(i + 1);
			}
			return new TranslatableText(entry.getValue(), obj);
		}
		return null;
	}

	private Text tryToFixEvents(Text text, TranslatableText translatableText) {
		List<Text> eventTexts = GetEventTexts(text);
		if (eventTexts.size() == 0) return translatableText;

		List<Text> rawTexts = new ArrayList<Text>();
		for (String str: translatableText.asFormattedString().split("§r *")) {
			if (str.matches("^$")) continue;
			rawTexts.add(new LiteralText(str + "§r"));
		}
		return getEventMatchedText(rawTexts, eventTexts);
	}
	
	private Text getEventMatchedText(List<Text> rawTexts, List<Text> eventTexts) {
		Text root = new LiteralText("");
		for (Text raw: rawTexts) {
			for (Text event: eventTexts) {
				if (event.asFormattedString().equals(raw.asFormattedString())){
					Style rawStyle = raw.getStyle();
					Style eventStyle = event.getStyle();
					rawStyle.setClickEvent(eventStyle.getClickEvent());
					rawStyle.setInsertion(eventStyle.getInsertion());
					if (eventStyle.getHoverEvent() != null) {
						HoverEvent.Action action = eventStyle.getHoverEvent().getAction();
						Text showText = eventStyle.getHoverEvent().getValue();
						showText = ReverseTranslate(showText, "show_text");
						rawStyle.setHoverEvent(new HoverEvent(action, showText));
					}
				}
			}
			root.append(raw);
		}
		return root;
	}

	private List<Text> GetEventTexts(Text text) {
		List<Text> eventTexts = new ArrayList<Text>();
		Queue<Text> q = new LinkedList<Text>();
		q.add(text);
		while(!q.isEmpty()) {
			Text head = q.poll();
			for(Text child: head.getSiblings()) q.add(child);
			if (head.getString().matches("^ *$")) continue;
			Style style = head.getStyle();
			if (style == null) continue;
			if (style.getClickEvent() != null || style.getHoverEvent() != null || style.getInsertion() != null) eventTexts.add(head);
		}
		return eventTexts;
	}

	private void recordUnregisterdText(Text text, String context) {
		String str = text.asFormattedString();
		if(unregisteredStrings.contains(str)) return;
		unregisteredStrings.add(str);
		
		String langCode = MinecraftClient.getInstance().getLanguageManager().getLanguage().getCode();
		ResourcepackWriter.addTranslationLine("WynnText", "wynntr", str, context);
		ResourcepackWriter.addTranslationLine("WynnText", langCode, str, context);
	}
}