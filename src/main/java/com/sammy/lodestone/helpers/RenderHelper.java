package com.sammy.lodestone.helpers;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.*;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.*;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.math.*;

import java.util.Optional;
import java.util.function.Supplier;

public final class RenderHelper {
	public static final int FULL_BRIGHT = 15728880;
	public static ShaderProgram getShader(RenderLayer type) {
		if (type instanceof RenderLayer.MultiPhase compositeRenderType) {
			Optional<Supplier<ShaderProgram>> shader = compositeRenderType.phases.shader.supplier;
			if (shader.isPresent()) {
				return shader.get().get();
			}
		}
		return null;
	}

	public static void vertexPos(VertexConsumer vertexConsumer, Matrix4f last, float x, float y, float z) {
		vertexConsumer.vertex(last, x, y, z).next();
	}

	public static void vertexPosUV(VertexConsumer vertexConsumer, Matrix4f last, float x, float y, float z, float u, float v) {
		vertexConsumer.vertex(last, x, y, z).uv(u, v).next();
	}

	public static void vertexPosUVLight(VertexConsumer vertexConsumer, Matrix4f last, float x, float y, float z, float u, float v, int light) {
		vertexConsumer.vertex(last, x, y, z).uv(u, v).light(light).next();
	}

	public static void vertexPosColor(VertexConsumer vertexConsumer, Matrix4f last, float x, float y, float z, float r, float g, float b, float a) {
		vertexConsumer.vertex(last, x, y, z).color(r, g, b, a).next();
	}

	public static void vertexPosColorUV(VertexConsumer vertexConsumer, Matrix4f last, float x, float y, float z, float r, float g, float b, float a, float u, float v) {
		vertexConsumer.vertex(last, x, y, z).color(r, g, b, a).uv(u, v).next();
	}

	public static void vertexPosColorUVLight(VertexConsumer vertexConsumer, Matrix4f last, float x, float y, float z, float r, float g, float b, float a, float u, float v, int light) {
		vertexConsumer.vertex(last, x, y, z).color(r, g, b, a).uv(u, v).light(light).next();
	}

	public static Vec3f parametricSphere(float u, float v, float r) {
		return new Vec3f(MathHelper.cos(u) * MathHelper.sin(v) * r, MathHelper.cos(v) * r, MathHelper.sin(u) * MathHelper.sin(v) * r);
	}

	public static Vec2f screenSpaceQuadOffsets(Vector4f start, Vector4f end, float width) {
		float x = -start.getX();
		float y = -start.getY();
		if (Math.abs(start.getZ()) > 0) {
			float ratio = end.getZ() / start.getZ();
			x = end.getX() + x * ratio;
			y = end.getY() + y * ratio;
		} else if (Math.abs(end.getZ()) <= 0) {
			x += end.getX();
			y += end.getY();
		}
		if (start.getZ() > 0) {
			x = -x;
			y = -y;
		}
		if (x * x + y * y > 0F) {
			float normalize = width * 0.5F / DataHelper.distance(x, y);
			x *= normalize;
			y *= normalize;
		}
		return new Vec2f(-y, x);
	}

	public static Vector4f midpoint(Vector4f a, Vector4f b) {
		return new Vector4f((a.getX() + b.getX()) * 0.5F, (a.getY() + b.getY()) * 0.5F, (a.getZ() + b.getZ()) * 0.5F, (a.getW() + b.getW()) * 0.5F);
	}

	public static Vec2f worldPosToTexCoord(Vec3f worldPos, MatrixStack viewModelStack) {
		Matrix4f viewMat = viewModelStack.peek().getModel();
		Matrix4f projMat = RenderSystem.getProjectionMatrix();

		Vec3f localPos = worldPos.copy();
		localPos.subtract(new Vec3f(MinecraftClient.getInstance().gameRenderer.getCamera().getPos()));

		Vector4f pos = new Vector4f(localPos);
		pos.transform(viewMat);
		pos.transform(projMat);
		pos.normalizeProjectiveCoordinates();

		return new Vec2f((pos.getX()+1F)/2F, (pos.getY()+1F)/2F);
	}
}
