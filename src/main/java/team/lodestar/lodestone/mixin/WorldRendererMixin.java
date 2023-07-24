package team.lodestar.lodestone.mixin;

import com.mojang.blaze3d.framebuffer.Framebuffer;
import com.mojang.blaze3d.systems.RenderSystem;
import team.lodestar.lodestone.handlers.PostProcessHandler;
import team.lodestar.lodestone.handlers.RenderHandler;
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
	@Nullable
	private ShaderEffect transparencyShader;

	@Shadow
	@Nullable
	public abstract Framebuffer getCloudsFramebuffer();

	@Inject(method = "render", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gl/ShaderEffect;render(F)V", ordinal = 1))
	public void lodestone$injectionBeforeTransparencyChainProcess(CallbackInfo ci) {
		PostProcessHandler.copyDepthBuffer();
	}
	@Inject(method = "render", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/particle/ParticleManager;renderParticles(Lnet/minecraft/client/util/math/MatrixStack;Lnet/minecraft/client/render/VertexConsumerProvider$Immediate;Lnet/minecraft/client/render/LightmapTextureManager;Lnet/minecraft/client/render/Camera;F)V", shift = At.Shift.AFTER))
	public void lodestone$postRenderParticles(MatrixStack matrices, float tickDelta, long limitTime, boolean renderBlockOutline, Camera camera, GameRenderer gameRenderer, LightmapTextureManager lightmapTextureManager, Matrix4f matrix4f, CallbackInfo ci) {
		RenderHandler.MATRIX4F = new Matrix4f(RenderSystem.getModelViewMatrix());
	}
	@Inject(method = "render", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/render/WorldRenderer;renderWeather(Lnet/minecraft/client/render/LightmapTextureManager;FDDD)V", shift = At.Shift.AFTER))
	public void lodestone$postRenderWeather(MatrixStack matrices, float tickDelta, long limitTime, boolean renderBlockOutline, Camera camera, GameRenderer gameRenderer, LightmapTextureManager lightmapTextureManager, Matrix4f matrix4f, CallbackInfo ci) {
		Matrix4f last = new Matrix4f(RenderSystem.getModelViewMatrix());
		matrices.push();
		Vec3d cameraPos = camera.getPos();
		matrices.translate(-cameraPos.x, -cameraPos.y, -cameraPos.z);
		if (transparencyShader != null) {
			MinecraftClient.getInstance().getFramebuffer().beginWrite(false);
		}
		RenderHandler.beginBufferedRendering(matrices);
		RenderHandler.renderBufferedParticles(true);
		if (RenderHandler.MATRIX4F != null) {
			RenderSystem.getModelViewMatrix().set(RenderHandler.MATRIX4F);
		}
		RenderHandler.renderBufferedBatches(true);
		RenderHandler.renderBufferedBatches(false);
		RenderSystem.getModelViewMatrix().set(last);
		RenderHandler.renderBufferedParticles(false);

		RenderHandler.endBufferedRendering(matrices);
		if (transparencyShader != null) {
			getCloudsFramebuffer().beginWrite(false);
		}
		matrices.pop();
	}

}
