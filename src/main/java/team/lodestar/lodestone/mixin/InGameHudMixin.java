package team.lodestar.lodestone.mixin;

import team.lodestar.lodestone.handlers.screenparticle.ScreenParticleHandler;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.hud.InGameHud;
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
	private void lodestone$renderHotbarStart(float tickDelta, GuiGraphics graphics, CallbackInfo ci) {
		ScreenParticleHandler.renderingHotbar = true;
		ScreenParticleHandler.x = 0;
		ScreenParticleHandler.y = 0;
	}

	@Inject(at = @At("RETURN"), method = "renderHotbar")
	private void lodestone$renderHotbarEnd(float tickDelta, GuiGraphics graphics, CallbackInfo ci) {
		ScreenParticleHandler.renderingHotbar = false;
	}
}
