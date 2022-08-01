package com.sammy.ortus.mixin;

import com.sammy.ortus.handlers.ScreenParticleHandler;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.hud.InGameHud;
import net.minecraft.client.util.math.MatrixStack;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(InGameHud.class)
final class InGameHudMixin {
	@Shadow @Final private MinecraftClient client;

	@Inject(at = @At("HEAD"), method = "renderHotbar")
	private void lodestone$renderHotbarStart(float l1, MatrixStack j1, CallbackInfo ci) {
		ScreenParticleHandler.renderingHotbar = true;
	}

	@Inject(at = @At("RETURN"), method = "renderHotbar")
	private void lodestone$renderHotbarEnd(float l1, MatrixStack j1, CallbackInfo ci) {
		ScreenParticleHandler.renderingHotbar = false;
	}
}
