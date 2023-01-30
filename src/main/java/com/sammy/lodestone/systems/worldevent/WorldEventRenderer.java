package com.sammy.lodestone.systems.worldevent;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.util.math.MatrixStack;

@Environment(EnvType.CLIENT)
public abstract class WorldEventRenderer<T extends WorldEventInstance> {

	public boolean canRender(T instance) {
		return false;
	}

	public void render (T instance, MatrixStack matrixStack, VertexConsumerProvider vertexConsumerProvider, float tickDelta) {

	}
}
