package net.kyaco.wynntr;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.resource.language.TranslationStorage;
import net.minecraft.network.MessageType;
import net.minecraft.resource.ResourceManager;
import net.minecraft.text.ClickEvent;
import net.minecraft.text.HoverEvent;
import net.minecraft.text.Style;
import net.minecraft.text.Text;
import net.minecraft.text.TranslatableText;

public class ENUSTranslationStorage extends TranslationStorage {
	private Map<String, String> rawReversDict = new HashMap<String, String>();
	private Map<Pattern, String> regxpReversDict = new HashMap<Pattern, String>();

	private final Pattern REGXP_KEY_PATTERN = Pattern.compile("^(.+)\\.regexp$");

	public void apply(ResourceManager rm) {
		super.load(rm, Arrays.asList("en_us"));

		rawReversDict.clear();
		regxpReversDict.clear();

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

	public Text ReverseTranslate(Text text, MessageType location) {
		if (!"Wynncraft".equals(MinecraftClient.getInstance().getCurrentServerEntry().name))
			return text;

		TranslatableText translatableText;
		translatableText = tryNormalReplace(text);
		if (translatableText == null) translatableText = tryRegexpReplace(text);
		if (translatableText == null) {
			recordUnregisterdText(text, location);
			return text;
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

	private void recordUnregisterdText(Text text, MessageType location) {
		System.out.println("[wynntr:unregisterd text of " + location + "] \"" + text.asFormattedString() + "\"");
		System.out.println("[wynntr:unicode escape version]" + convertToUnicode(text.asFormattedString()));
	}

	private Text tryToFixEvents(Text text, TranslatableText translatableText) {
		String str = translatableText.asFormattedString();

		for(Text subtext: text.getSiblings()) {
			Style style = subtext.getStyle();

			ClickEvent clickEvent = style.getClickEvent();
			HoverEvent hoverEvent = style.getHoverEvent();
			String insertion =style.getInsertion();
			if (clickEvent != null) {
				// todo
			}
			if (hoverEvent != null) {
				// todo
			}
			if (insertion != null) {
				// todo
			}
		}
		return translatableText;
	}

	private static String convertToUnicode(String original)
	{
		StringBuilder sb = new StringBuilder();
		for (int i = 0; i < original.length(); i++) {
			int code = Character.codePointAt(original, i);
			if (code < 128 || code == 167) {
				sb.append(original.charAt(i));
				continue;
			}
			sb.append(String.format("\\u%04X", code));
		}
		String unicode = sb.toString();
		return unicode;
	}
}