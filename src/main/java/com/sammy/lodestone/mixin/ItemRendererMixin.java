package com.sammy.lodestone.mixin;

import com.sammy.lodestone.handlers.ScreenParticleHandler;
import net.minecraft.client.render.item.ItemRenderer;
import net.minecraft.client.render.model.BakedModel;
import net.minecraft.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ItemRenderer.class)
final class ItemRendererMixin {

	@Inject(at = @At(value = "INVOKE", target = "Lnet/minecraft/client/render/BufferBuilderStorage;getEntityVertexConsumers()Lnet/minecraft/client/render/VertexConsumerProvider$Immediate;"), method = "renderGuiItemModel")
	private void lodestone$itemParticleEmitter(ItemStack stack, int x, int y, BakedModel model, CallbackInfo ci) {
		ScreenParticleHandler.renderItem(stack);
	}
}
