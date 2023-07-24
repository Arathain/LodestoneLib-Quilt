package team.lodestar.lodestone.mixin;

import team.lodestar.lodestone.handlers.screenparticle.ScreenParticleHandler;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screen.Screen;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Screen.class)
final class ScreenMixin {
	@Inject(at = @At("HEAD"), method = "renderBackground(Lnet/minecraft/client/gui/GuiGraphics;)V")
	private void lodestone$beforeBackgroundParticle(GuiGraphics graphics, CallbackInfo ci) {
		ScreenParticleHandler.renderEarliestParticles();
	}
}
