package com.sammy.lodestone.mixin;

import com.mojang.blaze3d.framebuffer.Framebuffer;
import com.mojang.blaze3d.systems.RenderSystem;
import com.sammy.lodestone.handlers.PostProcessHandler;
import com.sammy.lodestone.handlers.RenderHandler;
import com.sammy.lodestone.handlers.WorldEventHandler;
import com.sammy.lodestone.helpers.RenderHelper;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gl.ShaderEffect;
import net.minecraft.client.render.Camera;
import net.minecraft.client.render.GameRenderer;
import net.minecraft.client.render.LightmapTextureManager;
import net.minecraft.client.render.WorldRenderer;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.math.Vec3d;
import org.jetbrains.annotations.Nullable;
import org.joml.Matrix4f;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(WorldRenderer.class)
public abstract class WorldRendererMixin {
	@Shadow
	private @Nullable ShaderEffect transparencyShader;


	@Shadow
	public abstract @Nullable Framebuffer getCloudsFramebuffer();

	@Inject(method = "render", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gl/ShaderEffect;render(F)V", ordinal = 1))
	public void lodestone$injectionBeforeTransparencyChainProcess(CallbackInfo ci) {
		PostProcessHandler.copyDepthBuffer();
	}

	@Inject(method = "render", at = @At(value = "INVOKE", target = "Lnet/minecraft/util/profiler/Profiler;swap(Ljava/lang/String;)V", ordinal = 6))
	private void lodestone$afterSky(MatrixStack matrices, float tickDelta, long limitTime, boolean renderBlockOutline, Camera camera, GameRenderer gameRenderer, LightmapTextureManager lightmapTextureManager, Matrix4f matrix4f, CallbackInfo ci){
		Vec3d cameraPos = MinecraftClient.getInstance().gameRenderer.getCamera().getPos();
		matrices.push();
		matrices.translate(-cameraPos.getX(), -cameraPos.getY(), -cameraPos.getZ());
		WorldEventHandler.ClientOnly.renderWorldEvents(matrices, tickDelta);
		matrices.pop();
	}

	@Inject(method = "render", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/render/WorldRenderer;renderWeather(Lnet/minecraft/client/render/LightmapTextureManager;FDDD)V", ordinal = 0))
	private void lodestone$afterWeatherFirst(MatrixStack matrices, float tickDelta, long limitTime, boolean renderBlockOutline, Camera camera, GameRenderer gameRenderer, LightmapTextureManager lightmapTextureManager, Matrix4f matrix4f, CallbackInfo ci) {
		if (this.transparencyShader != null) {
			MinecraftClient.getInstance().getFramebuffer().beginWrite(false);
		}
		RenderHandler.beginBufferedRendering(matrices);

		RenderHandler.renderBufferedParticles(matrices);
		if (RenderHandler.MATRIX4F != null) {
			RenderSystem.getModelViewMatrix().set(RenderHandler.MATRIX4F);
		}
		RenderHandler.renderBufferedBatches(matrices);
		RenderHandler.endBufferedRendering(matrices);
		if (this.transparencyShader != null && this.getCloudsFramebuffer() != null) {
			this.getCloudsFramebuffer().beginWrite(false);
		}
	}

	@Inject(method = "render", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/render/WorldRenderer;renderWeather(Lnet/minecraft/client/render/LightmapTextureManager;FDDD)V", ordinal = 0))
	private void lodestone$afterWeatherSecond(MatrixStack matrices, float tickDelta, long limitTime, boolean renderBlockOutline, Camera camera, GameRenderer gameRenderer, LightmapTextureManager lightmapTextureManager, Matrix4f matrix4f, CallbackInfo ci) {

	}

	@Inject(method = "render", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/particle/ParticleManager;renderParticles(Lnet/minecraft/client/util/math/MatrixStack;Lnet/minecraft/client/render/VertexConsumerProvider$Immediate;Lnet/minecraft/client/render/LightmapTextureManager;Lnet/minecraft/client/render/Camera;F)V", ordinal = 0))
	private void lodestone$afterParticlesFirst(MatrixStack matrices, float tickDelta, long limitTime, boolean renderBlockOutline, Camera camera, GameRenderer gameRenderer, LightmapTextureManager lightmapTextureManager, Matrix4f matrix4f, CallbackInfo ci){
		RenderHandler.MATRIX4F = new Matrix4f(RenderSystem.getModelViewMatrix());
	}

	@Inject(method = "render", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/particle/ParticleManager;renderParticles(Lnet/minecraft/client/util/math/MatrixStack;Lnet/minecraft/client/render/VertexConsumerProvider$Immediate;Lnet/minecraft/client/render/LightmapTextureManager;Lnet/minecraft/client/render/Camera;F)V", ordinal = 1))
	private void lodestone$afterParticlesSecond(MatrixStack matrices, float tickDelta, long limitTime, boolean renderBlockOutline, Camera camera, GameRenderer gameRenderer, LightmapTextureManager lightmapTextureManager, Matrix4f matrix4f, CallbackInfo ci){
		RenderHandler.MATRIX4F =  new Matrix4f(RenderSystem.getModelViewMatrix());
	}


}
