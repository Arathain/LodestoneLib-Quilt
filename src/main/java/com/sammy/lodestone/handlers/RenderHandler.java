package com.sammy.lodestone.handlers;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.BufferBuilder;
import com.sammy.lodestone.config.ClientConfig;
import com.sammy.lodestone.helpers.RenderHelper;
import com.sammy.lodestone.setup.LodestoneRenderLayers;
import com.sammy.lodestone.systems.rendering.ExtendedShader;
import com.sammy.lodestone.systems.rendering.ShaderUniformHandler;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.ShaderProgram;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.util.math.MatrixStack;
import org.joml.Matrix4f;
import org.quiltmc.loader.api.QuiltLoader;

import java.util.HashMap;

public class RenderHandler {
	public static HashMap<RenderLayer, BufferBuilder> EARLY_BUFFERS = new HashMap<>();
	public static HashMap<RenderLayer, BufferBuilder> BUFFERS = new HashMap<>();
	public static HashMap<RenderLayer, BufferBuilder> LATE_BUFFERS = new HashMap<>();
	public static HashMap<RenderLayer, ShaderUniformHandler> HANDLERS = new HashMap<>();
	public static VertexConsumerProvider.Immediate EARLY_DELAYED_RENDER;
	public static VertexConsumerProvider.Immediate DELAYED_RENDER;
	public static VertexConsumerProvider.Immediate LATE_DELAYED_RENDER;
	public static Matrix4f PARTICLE_MATRIX = null;

	public static void init() {
		EARLY_DELAYED_RENDER = VertexConsumerProvider.immediate(EARLY_BUFFERS, new BufferBuilder(QuiltLoader.isModLoaded("sodium") ? 262144 : 256));
		DELAYED_RENDER = VertexConsumerProvider.immediate(BUFFERS, new BufferBuilder(QuiltLoader.isModLoaded("sodium") ? 2097152 : 256));
		LATE_DELAYED_RENDER = VertexConsumerProvider.immediate(LATE_BUFFERS, new BufferBuilder(QuiltLoader.isModLoaded("sodium") ? 262144 : 256));
	}
	public static void renderLast(MatrixStack stack) {
		stack.push();
		if (ClientConfig.DELAYED_RENDERING) {
			RenderSystem.getModelViewStack().push();
			RenderSystem.getModelViewStack().loadIdentity();
			if (PARTICLE_MATRIX != null) {
				RenderSystem.getModelViewStack().multiplyMatrix(PARTICLE_MATRIX);
			}
			RenderSystem.applyModelViewMatrix();
			DELAYED_RENDER.draw(LodestoneRenderLayers.TRANSPARENT_PARTICLE);
			DELAYED_RENDER.draw(LodestoneRenderLayers.ADDITIVE_PARTICLE);
			RenderSystem.getModelViewStack().pop();
			RenderSystem.applyModelViewMatrix();
		}
		draw(EARLY_DELAYED_RENDER, EARLY_BUFFERS);
		draw(DELAYED_RENDER, BUFFERS);
		draw(LATE_DELAYED_RENDER, LATE_BUFFERS);
		stack.pop();
	}

	public static void draw(VertexConsumerProvider.Immediate source, HashMap<RenderLayer, BufferBuilder> buffers) {
		for (RenderLayer type : buffers.keySet()) {
			ShaderProgram instance = RenderHelper.getShader(type);
			if (HANDLERS.containsKey(type)) {
				ShaderUniformHandler handler = HANDLERS.get(type);
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
		RenderHandler.EARLY_BUFFERS.put(type, new BufferBuilder(type.getExpectedBufferSize()));
		RenderHandler.BUFFERS.put(type, new BufferBuilder(type.getExpectedBufferSize()));
		RenderHandler.LATE_BUFFERS.put(type, new BufferBuilder(type.getExpectedBufferSize()));
	}
}
