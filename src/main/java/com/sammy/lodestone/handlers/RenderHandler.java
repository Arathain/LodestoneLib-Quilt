package com.sammy.lodestone.handlers;

import com.mojang.blaze3d.shader.FogShape;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.BufferBuilder;
import com.sammy.lodestone.helpers.RenderHelper;
import com.sammy.lodestone.setup.LodestoneRenderLayers;
import com.sammy.lodestone.systems.rendering.ExtendedShader;
import com.sammy.lodestone.systems.rendering.ShaderUniformHandler;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.LightmapTextureManager;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.ShaderProgram;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.util.math.MatrixStack;
import org.joml.Matrix4f;
import org.quiltmc.loader.api.QuiltLoader;

import java.util.HashMap;

public class RenderHandler {
	public static HashMap<RenderLayer, BufferBuilder> BUFFERS = new HashMap<>();
	public static HashMap<RenderLayer, BufferBuilder> PARTICLE_BUFFERS = new HashMap<>();
	public static boolean LARGER_BUFFER_SOURCES = QuiltLoader.isModLoaded("sodium");

	public static HashMap<RenderLayer, ShaderUniformHandler> UNIFORM_HANDLERS = new HashMap<>();
	public static VertexConsumerProvider.Immediate DELAYED_RENDER;
	public static VertexConsumerProvider.Immediate DELAYED_PARTICLE_RENDER;

	public static Matrix4f MATRIX4F;

	public static float FOG_NEAR;
	public static float FOG_FAR;
	public static FogShape FOG_SHAPE;
	public static float FOG_RED, FOG_GREEN, FOG_BLUE;

	public static void init() {
		int size = LARGER_BUFFER_SOURCES ? 262144 : 256;
		DELAYED_RENDER = VertexConsumerProvider.immediate(BUFFERS, new BufferBuilder(size));
		DELAYED_PARTICLE_RENDER = VertexConsumerProvider.immediate(PARTICLE_BUFFERS, new BufferBuilder(size));
	}
	public static void cacheFogData(float near, float far, FogShape shape) {
		FOG_NEAR = near;
		FOG_FAR = far;
		FOG_SHAPE = shape;
	}

	public static void cacheFogData(float r, float g, float b) {
		FOG_RED = r;
		FOG_GREEN = g;
		FOG_BLUE = b;
	}

	public static void beginBufferedRendering(MatrixStack matrixStack) {
		matrixStack.push();
		var lightTexture = MinecraftClient.getInstance().gameRenderer.getLightmapTextureManager();
		lightTexture.enable();
		RenderSystem.activeTexture(org.lwjgl.opengl.GL13.GL_TEXTURE2);
		RenderSystem.enableCull();
		RenderSystem.enableDepthTest();
		RenderSystem.depthMask(false);

		float fogRed = RenderSystem.getShaderFogColor()[0];
		float fogGreen = RenderSystem.getShaderFogColor()[1];
		float fogBlue = RenderSystem.getShaderFogColor()[2];
		float shaderFogStart = RenderSystem.getShaderFogStart();
		float shaderFogEnd = RenderSystem.getShaderFogEnd();
		FogShape shaderFogShape = RenderSystem.getShaderFogShape();

		RenderSystem.setShaderFogStart(FOG_NEAR);
		RenderSystem.setShaderFogEnd(FOG_FAR);
		RenderSystem.setShaderFogShape(FOG_SHAPE);
		RenderSystem.setShaderFogColor(FOG_RED, FOG_GREEN, FOG_BLUE);

		FOG_RED = fogRed;
		FOG_GREEN = fogGreen;
		FOG_BLUE = fogBlue;

		FOG_NEAR = shaderFogStart;
		FOG_FAR = shaderFogEnd;
		FOG_SHAPE = shaderFogShape;
	}

	public static void renderBufferedBatches(MatrixStack matrixStack) {
		draw(DELAYED_RENDER, BUFFERS);
	}

	public static void renderBufferedParticles(MatrixStack matrixStack) {
		DELAYED_PARTICLE_RENDER.draw(LodestoneRenderLayers.TRANSPARENT_PARTICLE);
		DELAYED_PARTICLE_RENDER.draw(LodestoneRenderLayers.ADDITIVE_PARTICLE);
		draw(DELAYED_PARTICLE_RENDER, PARTICLE_BUFFERS);
	}

	public static void endBufferedRendering(MatrixStack poseStack) {
		LightmapTextureManager lightTexture = MinecraftClient.getInstance().gameRenderer.getLightmapTextureManager();
		RenderSystem.setShaderFogStart(FOG_NEAR);
		RenderSystem.setShaderFogEnd(FOG_FAR);
		RenderSystem.setShaderFogShape(FOG_SHAPE);
		RenderSystem.setShaderFogColor(FOG_RED, FOG_GREEN, FOG_BLUE);

		poseStack.pop();
		lightTexture.disable();
		RenderSystem.disableCull();
		RenderSystem.disableDepthTest();
		RenderSystem.depthMask(true);
	}

	public static void draw(VertexConsumerProvider.Immediate source, HashMap<RenderLayer, BufferBuilder> buffers) {
		for (RenderLayer type : buffers.keySet()) {
			ShaderProgram instance = RenderHelper.getShader(type);
			if (UNIFORM_HANDLERS.containsKey(type)) {
				ShaderUniformHandler handler = UNIFORM_HANDLERS.get(type);
				handler.updateShaderData(instance);
			}
			source.draw(type);
			if (instance instanceof ExtendedShader extendedShaderInstance) {
				extendedShaderInstance.setUniformDefaults();
			}
		}
		source.draw();
	}

	public static void addRenderLayer(RenderLayer type) {
		int size = LARGER_BUFFER_SOURCES ? 262144 : type.getExpectedBufferSize();
		HashMap<RenderLayer, BufferBuilder> buffers = BUFFERS;
		if (type.name.contains("particle")) {
			buffers = PARTICLE_BUFFERS;
		}
		buffers.put(type, new BufferBuilder(size));
	}
}
