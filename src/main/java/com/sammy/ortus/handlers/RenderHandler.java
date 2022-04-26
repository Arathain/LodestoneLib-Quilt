package com.sammy.ortus.handlers;

import com.mojang.blaze3d.systems.RenderSystem;
import com.sammy.ortus.helpers.RenderHelper;
import com.sammy.ortus.setup.OrtusRenderLayers;
import com.sammy.ortus.systems.rendering.ExtendedShader;
import com.sammy.ortus.systems.rendering.ShaderUniformHandler;
import net.minecraft.client.render.*;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.math.Matrix4f;

import java.util.HashMap;

public class RenderHandler {
	public static final HashMap<RenderLayer, BufferBuilder> BUFFERS = new HashMap<>();
	public static final HashMap<RenderLayer, ShaderUniformHandler> HANDLERS = new HashMap<>();
	public static VertexConsumerProvider.Immediate DELAYED_RENDER;
	public static Matrix4f PARTICLE_MATRIX = null;

	public static void init() {
		DELAYED_RENDER = VertexConsumerProvider.immediate(BUFFERS, new BufferBuilder(256));
	}

	public static void renderLast(MatrixStack stack) {
		stack.push();
		/*if (ClientConfig.DELAYED_PARTICLE_RENDERING) {*/
			RenderSystem.getModelViewStack().push();
			RenderSystem.getModelViewStack().loadIdentity();
			if (PARTICLE_MATRIX != null) RenderSystem.getModelViewStack().method_34425(PARTICLE_MATRIX);
			RenderSystem.applyModelViewMatrix();
			DELAYED_RENDER.draw(OrtusRenderLayers.ADDITIVE_PARTICLE);
			DELAYED_RENDER.draw(OrtusRenderLayers.ADDITIVE_BLOCK_PARTICLE);
			RenderSystem.getModelViewStack().pop();
			RenderSystem.applyModelViewMatrix();
		/*}*/
		for (RenderLayer layer : BUFFERS.keySet()) {
			Shader shader = RenderHelper.getShader(layer);
			if (HANDLERS.containsKey(layer)) {
				ShaderUniformHandler handler = HANDLERS.get(layer);
				handler.updateShaderData(shader);
			}
			DELAYED_RENDER.draw(layer);

			if (shader instanceof ExtendedShader extendedShaderInstance) {
				extendedShaderInstance.setUniformDefaults();
			}
		}
		DELAYED_RENDER.draw();
		stack.pop();
	}
}
