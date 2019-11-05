package net.kyaco.wynntr.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import net.kyaco.wynntr.WynnTextReplacer;
import net.minecraft.client.network.packet.GuiSlotUpdateS2CPacket;
import net.minecraft.item.ItemStack;
import net.minecraft.network.Packet;
import net.minecraft.network.listener.ClientPlayPacketListener;
import net.minecraft.util.PacketByteBuf;

@Mixin(GuiSlotUpdateS2CPacket.class)
abstract public class MixinGuiSlotUpdateS2CPacket implements Packet<ClientPlayPacketListener> {
	@Shadow private ItemStack stack;

	@Inject(method = "read", at = @At("RETURN"))
	public void readMixin(PacketByteBuf packetBytebuf, CallbackInfo ci) {
		WynnTextReplacer.reverseTranslateSlotStacks(stack);
	}
}