package net.kyaco.wynntr;

import java.util.List;
import java.util.Optional;

import com.google.gson.JsonParseException;

import net.fabricmc.api.ClientModInitializer;
import net.minecraft.client.MinecraftClient;
import net.minecraft.entity.data.DataTracker;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.item.WrittenBookItem;
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
		if (address == null) return false;
		return address.equals("play.wynncraft.com") || address.equals("play.wynncraft.net");
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
		return translator.ReverseTranslate(playerName, "scoreboard_player");
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

		translateListTag(disp, "Lore", "item_lore");
		if (stack.getItem() == Items.WRITTEN_BOOK) {
			if(WrittenBookItem.isValid(disp)) {
				translateListTag(disp, "pages", "written_book_page");
			}
		}
	}

	private static void translateListTag(CompoundTag tag, String listTagName, String context) {
		ListTag listtag = tag.getList(listTagName, 8);
		for (int i = 0; i < listtag.size(); i++) {
			String loreStr = listtag.getString(i);
			try {
				Text oldText = Text.Serializer.fromJson(loreStr);
				if (oldText != null) {
					Text newText = translator.ReverseTranslate(oldText, context);
					String newLoreStr = Text.Serializer.toJson(newText);
					listtag.setTag(i, new StringTag(newLoreStr));
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
		if (text == null) return null;
		return new LiteralText(translator.ReverseTranslate(text, "title").asString());
	}

	// PlayerListHeaderS2CPacket.class
	// TeamS2CPacket.class
	// book title and auther
}
