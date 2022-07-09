package com.sammy.ortus.helpers;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.*;
import com.sammy.ortus.systems.rendering.ShaderHolder;
import com.sammy.ortus.systems.rendering.TrailPoint;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.*;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.math.*;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.function.Function;
import java.util.function.Supplier;

public final class RenderHelper {
	public static final int FULL_BRIGHT = 15728880;

	public static ShaderProgram getShader(RenderLayer type) {
		if (type instanceof RenderLayer.MultiPhase multiPhase) {
			Optional<Supplier<ShaderProgram>> shader = multiPhase.phases.shader.supplier;
			if (shader.isPresent()) {
				return shader.get().get();
			}
		}
		return null;
	}

	public static void blit(MatrixStack matrices, ShaderHolder shader, int x, int y, double w, double h, float u, float v, float xCanvasSize, float yCanvasSize) {
		innerBlit(matrices, shader, x, y, w, h, u / xCanvasSize, v / yCanvasSize, (float) w / xCanvasSize, (float) h / yCanvasSize);
	}

	public static void blit(MatrixStack matrices, ShaderHolder shader, int x, int y, double w, double h, float u, float v, float uw, float vh, float xCanvasSize, float yCanvasSize) {
		innerBlit(matrices, shader, x, y, w, h, u / xCanvasSize, v / yCanvasSize, uw / xCanvasSize, vh / yCanvasSize);
	}

	public static void blit(MatrixStack matrices, ShaderHolder shader, int x, int y, double w, double h, float u, float v, float canvasSize) {
		innerBlit(matrices, shader, x, y, w, h, u / canvasSize, v / canvasSize, (float) (x + w) / canvasSize, (float) (y + h) / canvasSize);
	}

	public static void blit(MatrixStack matrices, ShaderHolder shader, int x, int y, double w, double h, float u, float v, float uw, float vh, float canvasSize) {
		innerBlit(matrices, shader, x, y, w, h, u / canvasSize, v / canvasSize, uw / canvasSize, vh / canvasSize);
	}

	public static void blit(MatrixStack matrices, ShaderHolder shader, int x, int y, double w, double h, float r, float g, float b, float a, float u, float v, float xCanvasSize, float yCanvasSize) {
		innerBlit(matrices, shader, x, y, w, h, r, g, b, a, u / xCanvasSize, v / yCanvasSize, (float) w / xCanvasSize, (float) h / yCanvasSize);
	}

	public static void blit(MatrixStack matrices, ShaderHolder shader, int x, int y, double w, double h, float r, float g, float b, float a, float u, float v, float uw, float vh, float xCanvasSize, float yCanvasSize) {
		innerBlit(matrices, shader, x, y, w, h, r, g, b, a, u / xCanvasSize, v / yCanvasSize, uw / xCanvasSize, vh / yCanvasSize);
	}

	public static void blit(MatrixStack matrices, ShaderHolder shader, int x, int y, double w, double h, float r, float g, float b, float a, float u, float v, float canvasSize) {
		innerBlit(matrices, shader, x, y, w, h, r, g, b, a, u / canvasSize, v / canvasSize, (float) w / canvasSize, (float) h / canvasSize);
	}

	public static void blit(MatrixStack matrices, ShaderHolder shader, int x, int y, double w, double h, float r, float g, float b, float a, float u, float v, float uw, float vh, float canvasSize) {
		innerBlit(matrices, shader, x, y, w, h, r, g, b, a, u / canvasSize, v / canvasSize, uw / canvasSize, vh / canvasSize);
	}

	public static void innerBlit(MatrixStack matrices, ShaderHolder shader, int x, int y, double w, double h, float r, float g, float b, float a, float u, float v, float uw, float vh) {
		Matrix4f last = matrices.peek().getPosition();
		RenderSystem.setShader(shader.getInstance());
		BufferBuilder bufferbuilder = Tessellator.getInstance().getBufferBuilder();
		bufferbuilder.begin(VertexFormat.DrawMode.QUADS, VertexFormats.POSITION_COLOR_TEXTURE);
		bufferbuilder.vertex(last, (float) x, (float) y + (float) h, 0).color(r, g, b, a).uv(u, v + vh).next();
		bufferbuilder.vertex(last, (float) x + (float) w, (float) y + (float) h, 0).color(r, g, b, a).uv(u + uw, v + vh).next();
		bufferbuilder.vertex(last, (float) x + (float) w, (float) y, 0).color(r, g, b, a).uv(u + uw, v).next();
		bufferbuilder.vertex(last, (float) x, (float) y, 0).color(r, g, b, a).uv(u, v).next();
		BufferRenderer.draw(bufferbuilder.end());
	}

	public static void innerBlit(MatrixStack stack, ShaderHolder shader, int x, int y, double w, double h, float u, float v, float uw, float vh) {
		Matrix4f last = stack.peek().getPosition();
		RenderSystem.setShader(shader.getInstance());
		BufferBuilder bufferbuilder = Tessellator.getInstance().getBufferBuilder();
		bufferbuilder.begin(VertexFormat.DrawMode.QUADS, VertexFormats.POSITION_TEXTURE);
		bufferbuilder.vertex(last, (float) x, (float) y + (float) h, 0).uv(u, v + vh).next();
		bufferbuilder.vertex(last, (float) x + (float) w, (float) y + (float) h, 0).uv(u + uw, v + vh).next();
		bufferbuilder.vertex(last, (float) x + (float) w, (float) y, 0).uv(u + uw, v).next();
		bufferbuilder.vertex(last, (float) x, (float) y, 0).uv(u, v).next();
		BufferRenderer.draw(bufferbuilder.end());
	}

	public static void blit(MatrixStack matrices, int x, int y, double w, double h, float u, float v, float xCanvasSize, float yCanvasSize) {
		innerBlit(matrices, x, y, w, h, u / xCanvasSize, v / yCanvasSize, (float) w / xCanvasSize, (float) h / yCanvasSize);
	}

	public static void blit(MatrixStack matrices, int x, int y, double w, double h, float u, float v, float uw, float vh, float xCanvasSize, float yCanvasSize) {
		innerBlit(matrices, x, y, w, h, u / xCanvasSize, v / yCanvasSize, uw / xCanvasSize, vh / yCanvasSize);
	}

	public static void blit(MatrixStack matrices, int x, int y, double w, double h, float u, float v, float canvasSize) {
		innerBlit(matrices, x, y, w, h, u / canvasSize, v / canvasSize, (float) w / canvasSize, (float) h / canvasSize);
	}

	public static void blit(MatrixStack matrices, int x, int y, double w, double h, float u, float v, float uw, float vh, float canvasSize) {
		innerBlit(matrices, x, y, w, h, u / canvasSize, v / canvasSize, uw / canvasSize, vh / canvasSize);
	}

	public static void blit(MatrixStack matrices, int x, int y, double w, double h, float r, float g, float b, float a, float u, float v, float xCanvasSize, float yCanvasSize) {
		innerBlit(matrices, x, y, w, h, r, g, b, a, u / xCanvasSize, v / yCanvasSize, (float) w / xCanvasSize, (float) h / yCanvasSize);
	}

	public static void blit(MatrixStack matrices, int x, int y, double w, double h, float r, float g, float b, float a, float u, float v, float uw, float vh, float xCanvasSize, float yCanvasSize) {
		innerBlit(matrices, x, y, w, h, r, g, b, a, u / xCanvasSize, v / yCanvasSize, uw / xCanvasSize, vh / yCanvasSize);
	}

	public static void blit(MatrixStack matrices, int x, int y, double w, double h, float r, float g, float b, float a, float u, float v, float canvasSize) {
		innerBlit(matrices, x, y, w, h, r, g, b, a, u / canvasSize, v / canvasSize, (float) w / canvasSize, (float) h / canvasSize);
	}

	public static void blit(MatrixStack matrices, int x, int y, double w, double h, float r, float g, float b, float a, float u, float v, float uw, float vh, float canvasSize) {
		innerBlit(matrices, x, y, w, h, r, g, b, a, u / canvasSize, v / canvasSize, uw / canvasSize, vh / canvasSize);
	}

	public static void innerBlit(MatrixStack matrices, int x, int y, double w, double h, float r, float g, float b, float a, float u, float v, float uw, float vh) {
		Matrix4f last = matrices.peek().getPosition();
		RenderSystem.setShader(GameRenderer::getPositionColorTexShader);
		BufferBuilder bufferbuilder = Tessellator.getInstance().getBufferBuilder();
		bufferbuilder.begin(VertexFormat.DrawMode.QUADS, VertexFormats.POSITION_COLOR_TEXTURE);
		bufferbuilder.vertex(last, (float) x, (float) y + (float) h, 0).color(r, g, b, a).uv(u, v + vh).next();
		bufferbuilder.vertex(last, (float) x + (float) w, (float) y + (float) h, 0).color(r, g, b, a).uv(u + uw, v + vh).next();
		bufferbuilder.vertex(last, (float) x + (float) w, (float) y, 0).color(r, g, b, a).uv(u + uw, v).next();
		bufferbuilder.vertex(last, (float) x, (float) y, 0).color(r, g, b, a).uv(u, v).next();
		BufferRenderer.draw(bufferbuilder.end());
	}

	public static void innerBlit(MatrixStack matrices, int x, int y, double w, double h, float u, float v, float uw, float vh) {
		Matrix4f last = matrices.peek().getPosition();
		RenderSystem.setShader(GameRenderer::getPositionTexShader);
		BufferBuilder bufferbuilder = Tessellator.getInstance().getBufferBuilder();
		bufferbuilder.begin(VertexFormat.DrawMode.QUADS, VertexFormats.POSITION_TEXTURE);
		bufferbuilder.vertex(last, (float) x, (float) y + (float) h, 0).uv(u, v + vh).next();
		bufferbuilder.vertex(last, (float) x + (float) w, (float) y + (float) h, 0).uv(u + uw, v + vh).next();
		bufferbuilder.vertex(last, (float) x + (float) w, (float) y, 0).uv(u + uw, v).next();
		bufferbuilder.vertex(last, (float) x, (float) y, 0).uv(u, v).next();
		BufferRenderer.draw(bufferbuilder.end());
	}

	public static VertexBuilder create() {
		return new VertexBuilder();
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

	public static class VertexBuilder {
		float r = 1, g = 1, b = 1, a = 1;
		float xOffset = 0, yOffset = 0, zOffset = 0;
		int light = FULL_BRIGHT;
		float u0 = 0, v0 = 0, u1 = 1, v1 = 1;


		public VertexBuilder setColor(Color color) {
			return setColor(color.getRed(), color.getGreen(), color.getBlue());
		}

		public VertexBuilder setColor(Color color, float a) {
			return setColor(color).setAlpha(a);
		}

		public VertexBuilder setColor(float r, float g, float b, float a) {
			return setColor(r, g, b).setAlpha(a);
		}

		public VertexBuilder setColor(float r, float g, float b) {
			this.r = r / 255f;
			this.g = g / 255f;
			this.b = b / 255f;
			return this;
		}

		public VertexBuilder setAlpha(float a) {
			this.a = a;
			return this;
		}

		public VertexBuilder setOffset(float xOffset, float yOffset, float zOffset) {
			this.xOffset = xOffset;
			this.yOffset = yOffset;
			this.zOffset = zOffset;
			return this;
		}

		public VertexBuilder setLight(int light) {
			this.light = light;
			return this;
		}

		public VertexBuilder setUV(float u0, float v0, float u1, float v1) {
			this.u0 = u0;
			this.v0 = v0;
			this.u1 = u1;
			this.v1 = v1;
			return this;
		}

		public VertexBuilder renderTriangle(VertexConsumer vertexConsumer, MatrixStack matrices, float size) {
			return renderTriangle(vertexConsumer, matrices, size, size);
		}

		public VertexBuilder renderTriangle(VertexConsumer vertexConsumer, MatrixStack matrices, float width, float height) {
			Matrix4f last = matrices.peek().getPosition();

			vertexPosColorUVLight(vertexConsumer, last, -width, -height, 0, r, g, b, a, 0, 1, light);
			vertexPosColorUVLight(vertexConsumer, last, width, -height, 0, r, g, b, a, 1, 1, light);
			vertexPosColorUVLight(vertexConsumer, last, 0, height, 0, r, g, b, a, 0.5f, 0, light);
			return this;
		}

		public VertexBuilder renderBeam(VertexConsumer vertexConsumer, MatrixStack matrices, Vec3d start, Vec3d end, float width) {
			MinecraftClient client = MinecraftClient.getInstance();
			start.add(xOffset, yOffset, zOffset);
			end.add(xOffset, yOffset, zOffset);
			matrices.translate(-start.x, -start.y, -start.z);
			Vec3d cameraPosition = client.getBlockEntityRenderDispatcher().camera.getPos();
			Vec3d delta = end.subtract(start);
			Vec3d normal = start.subtract(cameraPosition).crossProduct(delta).normalize().multiply(width / 2f, width / 2f, width / 2f);
			Matrix4f last = matrices.peek().getPosition();
			Vec3d[] positions = new Vec3d[]{start.subtract(normal), start.add(normal), end.add(normal), end.subtract(normal)};
			vertexPosColorUVLight(vertexConsumer, last, (float) positions[0].x, (float) positions[0].y, (float) positions[0].z, r, g, b, a, u0, v1, light);
			vertexPosColorUVLight(vertexConsumer, last, (float) positions[1].x, (float) positions[1].y, (float) positions[1].z, r, g, b, a, u1, v1, light);
			vertexPosColorUVLight(vertexConsumer, last, (float) positions[2].x, (float) positions[2].y, (float) positions[2].z, r, g, b, a, u1, v0, light);
			vertexPosColorUVLight(vertexConsumer, last, (float) positions[3].x, (float) positions[3].y, (float) positions[3].z, r, g, b, a, u0, v0, light);
			matrices.translate(start.x, start.y, start.z);
			return this;
		}

		public VertexBuilder renderQuad(VertexConsumer vertexConsumer, MatrixStack matrices, float size) {
			return renderQuad(vertexConsumer, matrices, size, size);
		}

		public VertexBuilder renderQuad(VertexConsumer vertexConsumer, MatrixStack matrices, float width, float height) {
			Matrix4f last = matrices.peek().getPosition();
			matrices.translate(xOffset, yOffset, zOffset);
			Vec3d[] positions = new Vec3d[]{new Vec3d(-width, -height, 0), new Vec3d(width, -height, 0), new Vec3d(width, height, 0), new Vec3d(-width, height, 0)};
			vertexPosColorUVLight(vertexConsumer, last, (float) positions[0].x, (float) positions[0].y, (float) positions[0].z, r, g, b, a, u0, v1, light);
			vertexPosColorUVLight(vertexConsumer, last, (float) positions[1].x, (float) positions[1].y, (float) positions[1].z, r, g, b, a, u1, v1, light);
			vertexPosColorUVLight(vertexConsumer, last, (float) positions[2].x, (float) positions[2].y, (float) positions[2].z, r, g, b, a, u1, v0, light);
			vertexPosColorUVLight(vertexConsumer, last, (float) positions[3].x, (float) positions[3].y, (float) positions[3].z, r, g, b, a, u0, v0, light);
			matrices.translate(-xOffset, -yOffset, -zOffset);
			return this;
		}

		public VertexBuilder renderTrail(VertexConsumer vertexConsumer, MatrixStack stack, java.util.List<Vector4f> trailSegments, Function<Float, Float> widthFunc) {
			return renderTrail(vertexConsumer, stack.peek().getPosition(), trailSegments, widthFunc);
		}
		public VertexBuilder renderTrail(VertexConsumer vertexConsumer, Matrix4f matrix, java.util.List<Vector4f> trailSegments, Function<Float, Float> widthFunc) {
			if (trailSegments.size() < 3) {
				return this;
			}
			for (Vector4f pos : trailSegments) {
				pos.add(xOffset, yOffset, zOffset, 0);
				pos.transform(matrix);
			}

			int count = trailSegments.size() - 1;
			float increment = 1.0F / (count - 1);
			ArrayList<TrailPoint> points = new ArrayList<>();
			for (int i = 0; i < count; i++) {
				float width = widthFunc.apply(increment * i);
				Vector4f start = trailSegments.get(i);
				Vector4f end = trailSegments.get(i + 1);
				Vector4f mid = midpoint(start, end);
				Vec2f offset = corners(start, end, width);
				Vector4f positions = new Vector4f(mid.getX() + offset.x, mid.getX() - offset.x, mid.getY() + offset.y, mid.getY() - offset.y);
				points.add(new TrailPoint(positions.getX(), positions.getY(), positions.getZ(), positions.getW(), mid.getZ()));
			}
			return renderPoints(vertexConsumer, points, u0, v0, u1, v1);
		}
		public VertexBuilder renderPoints(VertexConsumer vertexConsumer, List<TrailPoint> trailPoints, float u0, float v0, float u1, float v1) {
			int count = trailPoints.size();
			float increment = 1.0F / count;
			for (int i = 1; i < count; i++) {
				float current = MathHelper.lerp(i * increment, v0, v1);
				float next = MathHelper.lerp((i + 1) * increment, v0, v1);
				TrailPoint previousPoint = trailPoints.get(i - 1);
				TrailPoint point = trailPoints.get(i);
				previousPoint.renderStart(vertexConsumer, light, r, g, b, a, u0, current, u1, next);
				point.renderEnd(vertexConsumer, light, r, g, b, a, u0, current, u1, next);
			}
			return this;
		}

		public VertexBuilder renderSphere(VertexConsumer vertexConsumer, MatrixStack matrices, float radius, int longs, int lats) {
			Matrix4f last = matrices.peek().getPosition();
			float startU = 0;
			float startV = 0;
			float endU = MathHelper.PI * 2;
			float endV = MathHelper.PI;
			float stepU = (endU - startU) / longs;
			float stepV = (endV - startV) / lats;
			for (int i = 0; i < longs; ++i) {
				// U-points
				for (int j = 0; j < lats; ++j) {
					// V-points
					float u = i * stepU + startU;
					float v = j * stepV + startV;
					float un = (i + 1 == longs) ? endU : (i + 1) * stepU + startU;
					float vn = (j + 1 == lats) ? endV : (j + 1) * stepV + startV;
					Vec3f p0 = parametricSphere(u, v, radius);
					Vec3f p1 = parametricSphere(u, vn, radius);
					Vec3f p2 = parametricSphere(un, v, radius);
					Vec3f p3 = parametricSphere(un, vn, radius);

					float uvU = u / endU * radius;
					float uvV = v / endV * radius;
					float uvUN = un / endU * radius;
					float uvVN = vn / endV * radius;
					vertexPosColorUVLight(vertexConsumer, last, p0.getX(), p0.getY(), p0.getZ(), r, g, b, a, uvU, uvV, light);
					vertexPosColorUVLight(vertexConsumer, last, p2.getX(), p2.getY(), p2.getZ(), r, g, b, a, uvUN, uvV, light);
					vertexPosColorUVLight(vertexConsumer, last, p1.getX(), p1.getY(), p1.getZ(), r, g, b, a, uvU, uvVN, light);

					vertexPosColorUVLight(vertexConsumer, last, p3.getX(), p3.getY(), p3.getZ(), r, g, b, a, uvUN, uvVN, light);
					vertexPosColorUVLight(vertexConsumer, last, p1.getX(), p1.getY(), p1.getZ(), r, g, b, a, uvU, uvVN, light);
					vertexPosColorUVLight(vertexConsumer, last, p2.getX(), p2.getY(), p2.getZ(), r, g, b, a, uvUN, uvV, light);
				}
			}
			return this;
		}
	}
	public static Vec2f corners(Vector4f start, Vector4f end, float width) {
		float x = -start.getX();
		float y = -start.getY();
		float z = Math.abs(start.getZ());
		if (z <= 0) {
			x += end.getX();
			y += end.getY();
		} else if (z > 0) {
			float ratio = end.getZ() / start.getZ();
			x = end.getX() + x * ratio;
			y = end.getY() + y * ratio;
		}
		if (start.getZ() > 0) {
			x = -x;
			y = -y;
		}
		float distance = DataHelper.distance(x, y);
		if (distance > 0F) {
			float normalize = width * 0.5F / (float) Math.sqrt(distance);
			x *= normalize;
			y *= normalize;
		}
		return new Vec2f(-y, x);
	}

	public static Vector4f midpoint(Vector4f a, Vector4f b) {
		return new Vector4f((a.getX() + b.getX()) * 0.5F, (a.getY() + b.getY()) * 0.5F, (a.getZ() + b.getZ()) * 0.5F, (a.getW() + b.getW()) * 0.5F);
	}
}
