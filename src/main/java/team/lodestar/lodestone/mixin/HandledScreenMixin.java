package team.lodestar.lodestone.mixin;

import team.lodestar.lodestone.handlers.screenparticle.ScreenParticleHandler;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screen.ingame.HandledScreen;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(HandledScreen.class)
final class HandledScreenMixin {
	@Shadow
	protected int x;

	@Shadow
	protected int y;

	@Inject(at = @At("RETURN"), method = "render")
	private void lodestone$beforeTooltipParticle(GuiGraphics graphics, int mouseX, int mouseY, float delta, CallbackInfo ci) {
		ScreenParticleHandler.renderEarlyParticles();
	}

	@Inject(at = @At("HEAD"), method = "render")
	private void lodestone$renderHotbarStart(GuiGraphics graphics, int mouseX, int mouseY, float delta, CallbackInfo ci) {
		ScreenParticleHandler.renderingHotbar = true;
		ScreenParticleHandler.x = this.x;
		ScreenParticleHandler.y = this.y;
	}
}
