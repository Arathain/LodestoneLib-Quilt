package com.sammy.lodestone.mixin;

import com.sammy.lodestone.handlers.screenparticle.ScreenParticleHandler;
import net.minecraft.client.render.item.ItemRenderer;
import net.minecraft.client.render.model.BakedModel;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ItemRenderer.class)
final class ItemRendererMixin {

	@Inject(at = @At(value = "INVOKE", target = "Lnet/minecraft/client/render/BufferBuilderStorage;getEntityVertexConsumers()Lnet/minecraft/client/render/VertexConsumerProvider$Immediate;", ordinal = 0), method = "renderItemInGui(Lnet/minecraft/client/util/math/MatrixStack;Lnet/minecraft/item/ItemStack;IILnet/minecraft/client/render/model/BakedModel;)V")
	private void lodestone$itemParticleEmitter(MatrixStack matrices, ItemStack stack, int x, int y, BakedModel model, CallbackInfo ci) {
		ScreenParticleHandler.renderItemStackEarly(stack, x+ScreenParticleHandler.x, y+ScreenParticleHandler.y);
	}

	@Inject(at = @At(value = "TAIL"), method = "renderItemInGui(Lnet/minecraft/client/util/math/MatrixStack;Lnet/minecraft/item/ItemStack;IILnet/minecraft/client/render/model/BakedModel;)V")
	private void lodestone$renderGuiItemLate(MatrixStack matrices, ItemStack stack, int x, int y, BakedModel model, CallbackInfo ci) {
		ScreenParticleHandler.renderItemStackLate();
	}
}
