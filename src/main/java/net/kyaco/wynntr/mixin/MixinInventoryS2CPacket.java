package net.kyaco.wynntr.mixin;

import java.util.List;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import net.kyaco.wynntr.WynnTextReplacer;
import net.minecraft.client.network.packet.InventoryS2CPacket;
import net.minecraft.item.ItemStack;
import net.minecraft.network.Packet;
import net.minecraft.network.listener.ClientPlayPacketListener;
import net.minecraft.util.PacketByteBuf;

@Mixin(InventoryS2CPacket.class)
abstract public class MixinInventoryS2CPacket implements Packet<ClientPlayPacketListener>
{
	@Shadow private List<ItemStack> slotStackList;

	@Inject(method = "read", at = @At("RETURN"))
	public void readMixin(PacketByteBuf packetBytebuf, CallbackInfo ci)
	{
		for(ItemStack stack: slotStackList) {
			WynnTextReplacer.reverseTranslateSlotStacks(stack);
		}
	}
}