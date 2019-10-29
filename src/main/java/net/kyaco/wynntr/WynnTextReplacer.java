package net.kyaco.wynntr;

import net.fabricmc.api.ClientModInitializer;

public class WynnTextReplacer implements ClientModInitializer
{
	public static ENUSTranslationStorage translator;

	@Override
	public void onInitializeClient() {
		translator = new ENUSTranslationStorage();
		System.out.println("[WynnTextReplacer] Wynn Text Replacer is ready.");
	}
}
