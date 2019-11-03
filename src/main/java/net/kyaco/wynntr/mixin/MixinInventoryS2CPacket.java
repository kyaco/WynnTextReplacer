package net.kyaco.wynntr.mixin;

import java.util.List;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import net.minecraft.client.network.packet.InventoryS2CPacket;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.network.Packet;
import net.minecraft.network.listener.ClientPlayPacketListener;
import net.minecraft.util.PacketByteBuf;

@Mixin(InventoryS2CPacket.class)
abstract public class MixinInventoryS2CPacket implements Packet<ClientPlayPacketListener>
{
	@Shadow List<ItemStack> slotStackList;

	@Inject(method = "read", at = @At("RETURN"))
	public void readMixin(PacketByteBuf packetBytebuf, CallbackInfo ci){
		for(ItemStack itemStack: slotStackList) {
			Item item = itemStack.getItem();
			String key = item.getTranslationKey();
			if (key.equals("block.minecraft.air")) continue;
			System.out.println("name: " + item.getName());
			break;
		}
	}
}