package net.kyaco.wynntr.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import net.kyaco.wynntr.WynnTextReplacer;
import net.minecraft.client.network.packet.OpenContainerPacket;
import net.minecraft.text.Text;
import net.minecraft.util.PacketByteBuf;

@Mixin(OpenContainerPacket.class)
abstract public class MixinOpenContainerPacket {
	@Shadow private Text name;

	@Inject(method = "read", at = @At("RETURN"))
	public void readMixin(PacketByteBuf packetBytebuf, CallbackInfo ci)
	{
		name = WynnTextReplacer.reverseTranslateContainerName(name);
	}
}