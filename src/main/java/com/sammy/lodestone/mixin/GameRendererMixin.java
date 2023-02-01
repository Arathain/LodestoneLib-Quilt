package com.sammy.lodestone.mixin;

import com.mojang.blaze3d.shader.ShaderStage;
import com.mojang.datafixers.util.Pair;
import com.sammy.lodestone.handlers.PostProcessHandler;
import com.sammy.lodestone.setup.LodestoneShaderRegistry;
import net.minecraft.client.render.GameRenderer;
import net.minecraft.client.render.ShaderProgram;
import net.minecraft.resource.ResourceFactory;
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

	@Inject(method = "loadShaders", at = @At(value = "INVOKE_ASSIGN", target = "Ljava/util/List;add(Ljava/lang/Object;)Z", ordinal = 0), locals = LocalCapture.CAPTURE_FAILHARD)
	private void lodestone$registerShaders(ResourceFactory factory, CallbackInfo ci, List<ShaderStage> list,  List<Pair<ShaderProgram, Consumer<ShaderProgram>>> list2) throws IOException {
		LodestoneShaderRegistry.init(factory);
		list2.addAll(LodestoneShaderRegistry.shaderList);
	}
	@Inject(method = "onResized", at = @At(value = "HEAD"))
	public void injectionResizeListener(int width, int height, CallbackInfo ci) {
		PostProcessHandler.resize(width, height);
	}
}
