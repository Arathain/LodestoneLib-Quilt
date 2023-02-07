package com.sammy.lodestone.mixin;

import com.sammy.lodestone.handlers.screenparticle.ScreenParticleHandler;
import net.minecraft.client.render.item.ItemRenderer;
import net.minecraft.client.render.model.BakedModel;
import net.minecraft.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ItemRenderer.class)
final class ItemRendererMixin {

	@Inject(method = "renderGuiItemModel", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/util/math/MatrixStack;scale(FFF)V", ordinal = 0))
	private void lodestone$renderGuiItem(ItemStack pStack, int pX, int pY, BakedModel pBakedmodel, CallbackInfo ci) {
		ScreenParticleHandler.renderItemStackEarly(pStack, pX, pY);
	}
	@Inject(method = "renderGuiItemModel", at = @At(value = "TAIL"))
	private void lodestone$renderGuiItemLate(ItemStack pStack, int pX, int pY, BakedModel pBakedmodel, CallbackInfo ci) {
		ScreenParticleHandler.renderItemStackLate();
	}
}
