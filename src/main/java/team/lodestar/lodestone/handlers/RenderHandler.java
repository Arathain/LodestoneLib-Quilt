package team.lodestar.lodestone.handlers;

import com.mojang.blaze3d.shader.FogShape;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.BufferBuilder;
import team.lodestar.lodestone.helpers.RenderHelper;
import team.lodestar.lodestone.systems.rendering.ExtendedShaderProgram;
import team.lodestar.lodestone.systems.rendering.ShaderUniformHandler;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.*;
import net.minecraft.client.util.math.MatrixStack;
import org.joml.Matrix4f;
import org.quiltmc.loader.api.QuiltLoader;
import team.lodestar.lodestone.systems.rendering.Phases;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;

/**
 * A handler responsible for all the backend rendering processes.
 * To have additive transparency work in a minecraft environment, we need to buffer our rendering till after clouds and water have rendered.
 * This happens for particles, as well as all of our custom RenderLayers
 */
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

	public static void cacheFogData(float red, float green, float blue) {
		FOG_RED = red;
		FOG_GREEN = green;
		FOG_BLUE = blue;
	}

	public static void beginBufferedRendering(MatrixStack matrices) {
		matrices.push();
		LightmapTextureManager lightTexture = MinecraftClient.getInstance().gameRenderer.getLightmapTextureManager();
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

	public static void renderBufferedParticles(boolean transparentOnly) {
		renderBufferedBatches(DELAYED_PARTICLE_RENDER, PARTICLE_BUFFERS, transparentOnly);
	}

	public static void renderBufferedBatches(boolean transparentOnly) {
		renderBufferedBatches(DELAYED_RENDER, BUFFERS, transparentOnly);
	}

	private static void renderBufferedBatches(VertexConsumerProvider.Immediate vcp, HashMap<RenderLayer, BufferBuilder> buffer, boolean transparentOnly) {
		if (transparentOnly) {
			Collection<RenderLayer> transparentRenderLayers = new ArrayList<>();
			for (RenderLayer RenderLayer : buffer.keySet()) {
				RenderPhase.Transparency transparency = RenderHelper.getTransparency(RenderLayer);
				if (transparency.equals(Phases.NORMAL_TRANSPARENCY)) {
					transparentRenderLayers.add(RenderLayer);
				}
			}
			endBatches(vcp, transparentRenderLayers);
		}
		else {
			endBatches(vcp, buffer.keySet());
		}
	}

	public static void endBufferedRendering(MatrixStack matrices) {
		LightmapTextureManager lightTexture = MinecraftClient.getInstance().gameRenderer.getLightmapTextureManager();
		RenderSystem.setShaderFogStart(FOG_NEAR);
		RenderSystem.setShaderFogEnd(FOG_FAR);
		RenderSystem.setShaderFogShape(FOG_SHAPE);
		RenderSystem.setShaderFogColor(FOG_RED, FOG_GREEN, FOG_BLUE);

		matrices.pop();
		lightTexture.disable();
		RenderSystem.disableDepthTest();
		RenderSystem.depthMask(true);
	}

	public static void endBatches(VertexConsumerProvider.Immediate vcp, Collection<RenderLayer> RenderLayers) {
		for (RenderLayer layer : RenderLayers) {
			ShaderProgram instance = RenderHelper.getShader(layer);
			if (UNIFORM_HANDLERS.containsKey(layer)) {
				ShaderUniformHandler handler = UNIFORM_HANDLERS.get(layer);
				handler.updateShaderData(instance);
			}
			vcp.draw(layer);
			if (instance instanceof ExtendedShaderProgram extendedShaderProgramInstance) {
				extendedShaderProgramInstance.setUniformDefaults();
			}
		}
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
