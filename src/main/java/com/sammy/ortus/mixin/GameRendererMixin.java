package com.sammy.ortus.mixin;

import com.mojang.blaze3d.shader.ShaderStage;
import com.mojang.datafixers.util.Pair;
import com.sammy.ortus.handlers.PostProcessHandler;
import com.sammy.ortus.handlers.RenderHandler;
import com.sammy.ortus.setup.LodestoneShaders;
import net.minecraft.client.render.GameRenderer;
import net.minecraft.client.render.ShaderProgram;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.resource.ResourceManager;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

import java.io.IOException;
import java.util.List;
import java.util.function.Consumer;

@Mixin(GameRenderer.class)
final class GameRendererMixin {
	@Inject(method = "renderWorld", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/render/WorldRenderer;render(Lnet/minecraft/client/util/math/MatrixStack;FJZLnet/minecraft/client/render/Camera;Lnet/minecraft/client/render/GameRenderer;Lnet/minecraft/client/render/LightmapTextureManager;Lnet/minecraft/util/math/Matrix4f;)V", shift = At.Shift.AFTER))
	private void lodestone$renderWorldLast(float tickDelta, long limitTime, MatrixStack matrix, CallbackInfo ci) {
		matrix.push();
		RenderHandler.renderLast(matrix);
		PostProcessHandler.renderLast(matrix);
		matrix.pop();
	}
	@Inject(method = "loadShaders", at = @At(value = "INVOKE_ASSIGN", target = "Ljava/util/List;add(Ljava/lang/Object;)Z", ordinal = 0), locals = LocalCapture.CAPTURE_FAILHARD)
	private void lodestone$registerShaders(ResourceManager manager, CallbackInfo ci, List<ShaderStage> list, List<Pair<ShaderProgram, Consumer<ShaderProgram>>> list2) throws IOException {
		LodestoneShaders.init(manager);
		list2.addAll(LodestoneShaders.shaderList);
	}
	@Inject(method = "onResized", at = @At(value = "HEAD"))
	public void injectionResizeListener(int width, int height, CallbackInfo ci) {
		PostProcessHandler.resize(width, height);
	}
}
