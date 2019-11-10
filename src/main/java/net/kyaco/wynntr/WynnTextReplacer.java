package net.kyaco.wynntr;

import java.util.List;
import java.util.Optional;

import com.google.gson.JsonParseException;

import net.fabricmc.api.ClientModInitializer;
import net.minecraft.client.MinecraftClient;
import net.minecraft.entity.data.DataTracker;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.StringTag;
import net.minecraft.text.LiteralText;
import net.minecraft.text.Text;

public class WynnTextReplacer implements ClientModInitializer
{
	public static ReverseTranslationStorage translator;

	@Override
	public void onInitializeClient() {
		translator = new ReverseTranslationStorage();
		System.out.println("[WynnTextReplacer] Wynn Text Replacer is ready.");
	}

	private static boolean isOnTargetServer() {
		String address = MinecraftClient.getInstance().getCurrentServerEntry().address;
		return address != null && address.equals("play.wynncraft.net");
	}

	public static Text reverseTranslateChatText(Text rawText) {
		if (!isOnTargetServer()) return rawText;
		return translator.ReverseTranslate(rawText, "chat");
	}

	public static Text reverseTranslateScoreboardObjective(Text displayName) {
		if (!isOnTargetServer()) return displayName;
		return translator.ReverseTranslate(displayName, "scoreboard_objective");
	}

	public static String reverseTranslateScoreboardPlayer(String playerName) {
		if (!isOnTargetServer()) return playerName;
		Text t = new LiteralText(playerName);
		return translator.ReverseTranslate(t, "scoreboard_player").getString();
	}

	public static void reverseTranslateSlotStacks(ItemStack stack) {
		if (!isOnTargetServer()) return;
		CompoundTag root = stack.getTag();
		if (root == null) return;
		if (!root.containsKey("display", 10)) return;
		CompoundTag disp = root.getCompound("display");

		if (disp.containsKey("Name", 8)) {
			String nameStr = disp.getString("Name");
			Text nameText = Text.Serializer.fromJson(nameStr);
			Text newNameText = translator.ReverseTranslate(nameText, "item_name");
			String newNameStr = Text.Serializer.toJson(newNameText);
			disp.putString("Name", newNameStr);
		}

		ListTag loreList = disp.getList("Lore", 8);
		for (int i = 0; i < loreList.size(); i++) {
			String loreStr = loreList.getString(i);
			try {
				Text loreText = Text.Serializer.fromJson(loreStr);
				if (loreText != null) {
					Text newLoreText = translator.ReverseTranslate(loreText, "item_lore");
					String newLoreStr = Text.Serializer.toJson(newLoreText);
					loreList.setTag(i, new StringTag(newLoreStr));
				}
			} catch(JsonParseException e) {
				e.printStackTrace();
			}
		}
	}

	public static Text reverseTranslateContainerName(Text name) {
		if (!isOnTargetServer()) return name;
		return translator.ReverseTranslate(name, "container_name");
	}

	public static void reverseTranslateEntityCustomName(List<DataTracker.Entry<?>> trackedValues) {
		if (!isOnTargetServer()) return;
		for(int i = 0; i < trackedValues.size(); i++) {
			DataTracker.Entry<?> entry = trackedValues.get(i);
			Object maybeOptionalObj = entry.get();
			
			try {
				@SuppressWarnings("unchecked")
				Optional<Object> optionalRawText = (Optional<Object>) maybeOptionalObj;
				if (!optionalRawText.isPresent()) return;
				Object maybetext = optionalRawText.get();
				if (!(maybetext instanceof Text)) return;
				Text rawText = (Text) maybetext;

				Text text = WynnTextReplacer.translator.ReverseTranslate(rawText, "entity_name");
				Optional<Text> optionalText = Optional.of(text);
				@SuppressWarnings("all")
				DataTracker.Entry<Optional<Text>> nentry = new DataTracker.Entry(entry.getData(), optionalText);
				trackedValues.set(i, nentry);
			}catch(ClassCastException e) {
			}catch(Exception e) {
				e.printStackTrace();
			}
		}
	}

	public static Text reverseTranslateBossBar(Text name) {
		if (!isOnTargetServer()) return name;
		return translator.ReverseTranslate(name, "boss_bar");
	}

	public static Text reverseTranslateTitleText(Text text) {
		if (!isOnTargetServer()) return text;
		return translator.ReverseTranslate(text, "title");
	}

	// PlayerListHeaderS2CPacket.class
	// TeamS2CPacket.class
}
