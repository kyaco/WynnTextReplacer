package net.kyaco.wynntr;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.minecraft.client.resource.language.TranslationStorage;
import net.minecraft.resource.ResourceManager;
import net.minecraft.text.HoverEvent;
import net.minecraft.text.LiteralText;
import net.minecraft.text.Style;
import net.minecraft.text.Text;
import net.minecraft.text.TranslatableText;
import net.minecraft.util.Formatting;

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
		if (text == null) return null;

		String formattedString = asFormattedString(text);
		TranslatableText translatableText;
		translatableText = tryNormalReplace(formattedString);
		if (translatableText == null)
		{
			translatableText = tryRegexpReplace(formattedString);
			if (translatableText == null)
			{
				recordUnregisterdText(formattedString, context);
				return text;
			}
		}
		if ("chat".equals(context) || "written_book_page".equals(context))
		{
			return getEventMatchedText(translatableText, text);
		}
		return translatableText;
	}





	private TranslatableText tryNormalReplace(String formattedString) {
		String text_id = rawReversDict.get(formattedString);
		if (text_id != null) return new TranslatableText(text_id);
		return null;
	}
	private TranslatableText tryRegexpReplace(String formattedString) {
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




















	 private Text getEventMatchedText(Text translatableText, Text text) {
		List<Text> rawTexts = asDeformatedTexts(translatableText);
		List<Text> eventTexts = getEventTexts(text);
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
	private List<Text> asDeformatedTexts(Text translatableText) {
		List<Text> rawTexts = new ArrayList<Text>();
		Set<Character> styleCharas = new LinkedHashSet<>();

		String strlist[] = translatableText.asString().split("§");

		if (strlist[0].length() > 0)
		{
			rawTexts.add(new LiteralText(strlist[0]));
		}
		for (int i = 1; i < strlist.length; i++) {
			String str = strlist[i];
			if (str.length() == 0) continue;

			char styleChar = str.charAt(0);
			String content = str.substring(1);

			if (styleChar == 'r'){
				styleCharas.clear();
			} else {
				styleCharas.remove(styleChar);
				styleCharas.add(styleChar);
			}
			if (content.length() > 0) {
				LiteralText t = new LiteralText(content);
				Style s = new Style();
				Iterator<Character> it = styleCharas.iterator();
				while(it.hasNext()) {
					Character c = it.next();
					switch (c) {
						case 'k': s.setObfuscated(true); break;
						case 'l': s.setBold(true); break;
						case 'm': s.setStrikethrough(true); break;
						case 'n': s.setUnderline(true); break;
						case 'o': s.setItalic(true); break;
						default: s.setColor(Formatting.byCode(c));
					}
				}
				t.setStyle(s);
				rawTexts.add(t);
			}
		}
		return rawTexts;
	}

	private List<Text> getEventTexts(Text text) {
		List<Text> eventTexts = new ArrayList<Text>();
		Iterator<Text> it = text.stream().iterator();
		while(it.hasNext()) {
			Text t = it.next();
			if (t.getString().length() == 0) continue;
			Style style = t.getStyle();
			if (style == null) continue;
			if (style.getClickEvent() != null || style.getHoverEvent() != null || style.getInsertion() != null) eventTexts.add(t);
		}
		return eventTexts;
	}











	private void recordUnregisterdText(String formattedString, String context) {
		if(unregisteredStrings.contains(formattedString)) return;
		unregisteredStrings.add(formattedString);
		ResourcepackWriter.addTranslationLine("WynnText", "wynntr", formattedString, context);
	}

	private String asFormattedString(Text text) {
		StringBuilder sb = new StringBuilder();
		String oldStyle = "";
		Iterator<Text> it = text.stream().iterator();
  
		while(it.hasNext()) {
		   Text t = it.next();
		   String str = t.asString().replaceAll("%", "%%");
		   if (!str.isEmpty()) {
				String style = t.getStyle().asString();
				if (!oldStyle.isEmpty()) {
					sb.append(Formatting.RESET);
				}
				sb.append(style);
				oldStyle = style;
				sb.append(str);
		   }
		}
  
		if (!oldStyle.isEmpty()) {
		   sb.append(Formatting.RESET);
		}
  
		return sb.toString();
	 }
}