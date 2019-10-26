package net.kyaco.wynntr.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import net.kyaco.wynntr.WynnTextReplacer;
import net.minecraft.client.network.packet.ChatMessageS2CPacket;
import net.minecraft.network.Packet;
import net.minecraft.network.listener.ClientPlayPacketListener;
import net.minecraft.text.Text;
import net.minecraft.util.PacketByteBuf;

@Mixin(ChatMessageS2CPacket.class)
abstract public class MixinChatMessageS2CPacket implements Packet<ClientPlayPacketListener>
{
	@Shadow
	private Text message;

	@Inject(method = "read", at = @At("RETURN"))
	public void readMixin(PacketByteBuf packetBytebuf, CallbackInfo ci)
	{
		message = WynnTextReplacer.translator.ReverseTranslate(message);
	}
}
