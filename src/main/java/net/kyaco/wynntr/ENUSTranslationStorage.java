package net.kyaco.wynntr;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import net.minecraft.client.resource.language.TranslationStorage;
import net.minecraft.resource.ResourceManager;
import net.minecraft.text.Text;
import net.minecraft.text.TranslatableText;

public class ENUSTranslationStorage extends TranslationStorage  {
	private String targetLanguage;
	private Map<String, String> reversDict = new HashMap<String, String>();

	public ENUSTranslationStorage(String targetLanguage) {
		this.targetLanguage = targetLanguage;
	}

	public void apply(ResourceManager rm) {
		super.load(rm, Arrays.asList(targetLanguage));
		for (Map.Entry<String, String> entry : this.translations.entrySet()) {
			reversDict.put(entry.getValue(), entry.getKey());
		}
		System.out.println("[WynnTextReplacer] Revers translate map is ready.");
	}

	public Text ReverseTranslate(Text text) {
		String str = text.getString();
		System.out.println(str);
		return reversDict.containsKey(str) ? new TranslatableText(reversDict.get(str)) : text;
	}
}