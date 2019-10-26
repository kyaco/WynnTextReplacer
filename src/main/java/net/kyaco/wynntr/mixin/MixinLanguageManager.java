package net.kyaco.wynntr.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import net.kyaco.wynntr.WynnTextReplacer;
import net.minecraft.client.resource.language.LanguageManager;
import net.minecraft.resource.ResourceManager;
import net.minecraft.resource.SynchronousResourceReloadListener;

@Mixin(LanguageManager.class)
abstract public class MixinLanguageManager implements SynchronousResourceReloadListener
{
	@Inject(method = "apply", at = @At("RETURN"))
	public void applyMixin(ResourceManager rm, CallbackInfo ci)
	{
		WynnTextReplacer.translator.apply(rm);
	}
}
