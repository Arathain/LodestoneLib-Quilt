package com.sammy.lodestone.mixin;

import com.sammy.lodestone.handlers.ScreenParticleHandler;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.util.math.MatrixStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import static com.sammy.lodestone.systems.rendering.particle.screen.base.ScreenParticle.RenderOrder.BEFORE_UI;

@Mixin(Screen.class)
final class ScreenMixin {
	@Inject(at = @At("HEAD"), method = "renderBackground(Lnet/minecraft/client/util/math/MatrixStack;I)V")
	private void lodestone$beforeBackgroundParticle(MatrixStack pPoseStack, int pVOffset, CallbackInfo ci) {
		ScreenParticleHandler.renderParticles(BEFORE_UI);
	}
}
