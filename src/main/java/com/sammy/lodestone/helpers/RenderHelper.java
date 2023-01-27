package com.sammy.lodestone.helpers;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.*;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.*;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.math.*;
import org.joml.Matrix4f;
import org.joml.Vector3f;
import org.joml.Vector4f;

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
		vertexConsumer.m_rkxaaknb(last, x, y, z).next();
	}

	public static void vertexPosUV(VertexConsumer vertexConsumer, Matrix4f last, float x, float y, float z, float u, float v) {
		vertexConsumer.m_rkxaaknb(last, x, y, z).uv(u, v).next();
	}

	public static void vertexPosUVLight(VertexConsumer vertexConsumer, Matrix4f last, float x, float y, float z, float u, float v, int light) {
		vertexConsumer.m_rkxaaknb(last, x, y, z).uv(u, v).light(light).next();
	}

	public static void vertexPosColor(VertexConsumer vertexConsumer, Matrix4f last, float x, float y, float z, float r, float g, float b, float a) {
		vertexConsumer.m_rkxaaknb(last, x, y, z).color(r, g, b, a).next();
	}

	public static void vertexPosColorUV(VertexConsumer vertexConsumer, Matrix4f last, float x, float y, float z, float r, float g, float b, float a, float u, float v) {
		vertexConsumer.m_rkxaaknb(last, x, y, z).color(r, g, b, a).uv(u, v).next();
	}

	public static void vertexPosColorUVLight(VertexConsumer vertexConsumer, Matrix4f last, float x, float y, float z, float r, float g, float b, float a, float u, float v, int light) {
		vertexConsumer.m_rkxaaknb(last, x, y, z).color(r, g, b, a).uv(u, v).light(light).next();
	}

	public static Vector3f parametricSphere(float u, float v, float r) {
		return new Vector3f(MathHelper.cos(u) * MathHelper.sin(v) * r, MathHelper.cos(v) * r, MathHelper.sin(u) * MathHelper.sin(v) * r);
	}

	public static Vec2f screenSpaceQuadOffsets(Vector4f start, Vector4f end, float width) {
		float x = -start.x;
		float y = -start.y;
		if (Math.abs(start.z) > 0) {
			float ratio = end.z / start.z;
			x = end.x + x * ratio;
			y = end.y + y * ratio;
		} else if (Math.abs(end.z) <= 0) {
			x += end.x;
			y += end.y;
		}
		if (start.z > 0) {
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
		return new Vector4f((a.x + b.x) * 0.5F, (a.y + b.y) * 0.5F, (a.z + b.z) * 0.5F, (a.w + b.w) * 0.5F);
	}

	public static Vec2f worldPosToTexCoord(Vector3f worldPos, MatrixStack viewModelStack) {
		Matrix4f viewMat = viewModelStack.peek().getModel();
		Matrix4f projMat = RenderSystem.getProjectionMatrix();

		Vector3f localPos = new Vector3f(worldPos);
		localPos.sub(MinecraftClient.getInstance().gameRenderer.getCamera().getPos().m_sruzucpd());

		Vector4f pos = new Vector4f();
		pos.mul(viewMat);
		pos.mul(projMat);
		pos.normalize();

		return new Vec2f((pos.x+1F)/2F, (pos.y+1F)/2F);
	}
}
