package com.sammy.lodestone.mixin;

import com.sammy.lodestone.handlers.RenderHandler;
import net.minecraft.client.render.BackgroundRenderer;
import net.minecraft.client.render.Camera;
import net.minecraft.client.render.CameraSubmersionType;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.entity.Entity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

@Mixin(BackgroundRenderer.class)
public class BackgroundRendererMixin {

	@Shadow
	private static float red;

	@Shadow
	private static float green;

	@Shadow
	private static float blue;

	@Inject(method = "render", at = @At(value = "INVOKE", target = "Lcom/mojang/blaze3d/systems/RenderSystem;clearColor(FFFF)V", ordinal = 1), locals = LocalCapture.CAPTURE_FAILHARD)
	private static void lodestone$getFogColor(Camera camera, float tickDelta, ClientWorld world, int viewDistance, float skyDarkness, CallbackInfo ci){
		RenderHandler.cacheFogData(red, green, blue);

	}

	@Inject( method = "applyFog", at = @At(value = "INVOKE", target = "Lcom/mojang/blaze3d/systems/RenderSystem;setShaderFogShape(Lcom/mojang/blaze3d/shader/FogShape;)V", shift = At.Shift.BEFORE), locals = LocalCapture.CAPTURE_FAILHARD)
	private static void lodestone$getFog(Camera camera, BackgroundRenderer.FogType fogType, float viewDistance, boolean thickFog, float tickDelta, CallbackInfo ci, CameraSubmersionType cameraSubmersionType, Entity entity, BackgroundRenderer.FogParameters fogParameters) {
		RenderHandler.cacheFogData(fogParameters.fogStart, fogParameters.fogEnd, fogParameters.shape);



		//onFogRender(fogParameters, fogType, camera, tickDelta, viewDistance, fogParameters.fogStart, fogParameters.fogEnd, fogParameters.shape);QM
		//onFogRender(FogRenderer.FogMode mode, FogType type, Camera camera, float partialTick, float renderDistance, float nearDistance, float farDistance, FogShape shape)ForgEvent
	}
}
