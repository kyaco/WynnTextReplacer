package net.kyaco.wynntr;

import net.fabricmc.api.ClientModInitializer;
import net.minecraft.item.ItemStack;
import net.minecraft.text.LiteralText;
import net.minecraft.text.Text;

public class WynnTextReplacer implements ClientModInitializer
{
	public static ENUSTranslationStorage translator;

	@Override
	public void onInitializeClient() {
		translator = new ENUSTranslationStorage();
		System.out.println("[WynnTextReplacer] Wynn Text Replacer is ready.");
	}

	public static Text reverseTranslateChatText(Text rawText) {
		return translator.ReverseTranslate(rawText);
	}

	public static Text reverseTranslateScoreboardObjective(Text displayName) {
		return translator.ReverseTranslate(displayName);
	}

	public static String reverseTranslateScoreboardPlayer(String playerName) {
		Text t = new LiteralText(playerName);
		return translator.ReverseTranslate(t).getString();
	}

	public static String reverseTranslateItemStack(ItemStack itemStack) {
		return null;
	}	
}
