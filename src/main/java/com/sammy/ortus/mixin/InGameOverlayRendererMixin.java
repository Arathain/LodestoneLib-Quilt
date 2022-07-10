package com.sammy.ortus.mixin;

import com.sammy.ortus.config.ClientConfig;
import com.sammy.ortus.handlers.FireEffectHandler;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.hud.InGameOverlayRenderer;
import net.minecraft.client.util.math.MatrixStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(InGameOverlayRenderer.class)
public class InGameOverlayRendererMixin {
	@Inject(method = "renderOverlays", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/network/ClientPlayerEntity;isSpectator()Z"))
	private static void ortusFireEffectRendering(MinecraftClient mc, MatrixStack overlay, CallbackInfo ci) {
		FireEffectHandler.ClientOnly.renderUIMeteorFire(mc, overlay);
	}

	@Inject(method = "renderFireOverlay", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/util/math/MatrixStack;translate(DDD)V"))
	private static void ortusFireEffectOffset(MinecraftClient minecraft, MatrixStack stack, CallbackInfo ci) {
		stack.translate(0, -(ClientConfig.FIRE_OVERLAY_OFFSET) * 0.3f, 0);
	}
}
