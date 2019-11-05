package net.kyaco.wynntr;

import com.google.gson.JsonParseException;

import net.fabricmc.api.ClientModInitializer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.StringTag;
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

	public static void reverseTranslateSlotStacks(ItemStack stack) {
		System.out.println("stack = " + stack);
		CompoundTag root = stack.getTag();
		if (root == null) return;
		if (!root.containsKey("display", 10)) return;
		CompoundTag disp = root.getCompound("display");

		if (disp.containsKey("Name", 8)) {
			String nameStr = disp.getString("Name");
			Text nameText = Text.Serializer.fromJson(nameStr);
			Text newNameText = translator.ReverseTranslate(nameText);
			String newNameStr = Text.Serializer.toJson(newNameText);
			disp.putString("Name", newNameStr);
		}

		ListTag loreList = disp.getList("Lore", 8);
		for (int i = 0; i < loreList.size(); i++) {
			String loreStr = loreList.getString(i);
			try {
				Text loreText = Text.Serializer.fromJson(loreStr);
				if (loreText != null) {
					Text newLoreText = translator.ReverseTranslate(loreText);
					String newLoreStr = Text.Serializer.toJson(newLoreText);
					loreList.setTag(i, new StringTag(newLoreStr));
				}
			} catch(JsonParseException e) {
				e.printStackTrace();
			}
		}
	}
}
