package team.lodestar.lodestone.systems.rendering;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.*;
import team.lodestar.lodestone.helpers.RenderHelper;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.GameRenderer;
import net.minecraft.client.render.ShaderProgram;
import net.minecraft.client.texture.Sprite;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.*;
import org.joml.Matrix4f;
import org.joml.Vector3f;
import org.joml.Vector4f;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.stream.Collectors;

public class VFXBuilders {
	public static ScreenVFXBuilder createScreen() {
		return new ScreenVFXBuilder();
	}

	public static class ScreenVFXBuilder {
		public float r = 1, g = 1, b = 1, a = 1;
		public int light = -1;
		public float u0 = 0, v0 = 0, u1 = 1, v1 = 1;
		public float x0 = 0, y0 = 0, x1 = 1, y1 = 1;
		public int zLevel;

		public VertexFormat format;
		public Supplier<ShaderProgram> shader = GameRenderer::getPositionTexShader;
		public Identifier texture;
		public ScreenVertexPlacementSupplier supplier;
		public BufferBuilder bufferbuilder = Tessellator.getInstance().getBufferBuilder();

		public ScreenVFXBuilder setPosTexDefaultFormat() {
			supplier = (b, l, x, y, u, v) -> b.vertex(l, x, y, this.zLevel).uv(u, v).next();
			format = VertexFormats.POSITION_TEXTURE;
			return this;
		}

		public ScreenVFXBuilder setPosColorDefaultFormat() {
			supplier = (b, l, x, y, u, v) -> b.vertex(l, x, y, this.zLevel).color(this.r, this.g, this.b, this.a).next();
			format = VertexFormats.POSITION_COLOR;
			return this;
		}

		public ScreenVFXBuilder setPosColorTexDefaultFormat() {
			supplier = (b, l, x, y, u, v) -> b.vertex(l, x, y, this.zLevel).color(this.r, this.g, this.b, this.a).uv(u, v).next();
			format = VertexFormats.POSITION_COLOR_TEXTURE;
			return this;
		}

		public ScreenVFXBuilder setPosColorTexLightmapDefaultFormat() {
			supplier = (b, l, x, y, u, v) -> b.vertex(l, x, y, this.zLevel).color(this.r, this.g, this.b, this.a).uv(u, v).light(this.light).next();
			format = VertexFormats.POSITION_COLOR_TEXTURE_LIGHT;
			return this;
		}


		public ScreenVFXBuilder setFormat(VertexFormat format) {
			this.format = format;
			return this;
		}

		public ScreenVFXBuilder setShaderTexture(Identifier texture) {
			this.texture = texture;
			return this;
		}

		public ScreenVFXBuilder setShader(Supplier<ShaderProgram> shader) {
			this.shader = shader;
			return this;
		}

		public ScreenVFXBuilder setShader(ShaderProgram shader) {
			this.shader = () -> shader;
			return this;
		}

		public ScreenVFXBuilder setVertexSupplier(ScreenVertexPlacementSupplier supplier) {
			this.supplier = supplier;
			return this;
		}

		public ScreenVFXBuilder overrideBufferBuilder(BufferBuilder builder) {
			this.bufferbuilder = builder;
			return this;
		}

		public ScreenVFXBuilder setLight(int light) {
			this.light = light;
			return this;
		}

		public ScreenVFXBuilder setColor(Color color) {
			return setColor(color.getRed(), color.getGreen(), color.getBlue());
		}

		public ScreenVFXBuilder setColor(Color color, float a) {
			return setColor(color).setAlpha(a);
		}

		public ScreenVFXBuilder setColor(float r, float g, float b, float a) {
			return setColor(r, g, b).setAlpha(a);
		}

		public ScreenVFXBuilder setColor(float r, float g, float b) {
			this.r = r / 255f;
			this.g = g / 255f;
			this.b = b / 255f;
			return this;
		}

		public ScreenVFXBuilder setAlpha(float a) {
			this.a = a;
			return this;
		}

		public ScreenVFXBuilder setPositionWithWidth(float x, float y, float width, float height) {
			return setPosition(x, y, x + width, y + height);
		}

		public ScreenVFXBuilder setPosition(float x0, float y0, float x1, float y1) {
			this.x0 = x0;
			this.y0 = y0;
			this.x1 = x1;
			this.y1 = y1;
			return this;
		}

		public ScreenVFXBuilder setZLevel(int z) {
			this.zLevel = z;
			return this;
		}

		public ScreenVFXBuilder setUVWithWidth(float u, float v, float width, float height, float canvasSize) {
			return setUVWithWidth(u, v, width, height, canvasSize, canvasSize);
		}

		public ScreenVFXBuilder setUVWithWidth(float u, float v, float width, float height, float canvasSizeX, float canvasSizeY) {
			return setUVWithWidth(u / canvasSizeX, v / canvasSizeY, width / canvasSizeX, height / canvasSizeY);
		}

		public ScreenVFXBuilder setUVWithWidth(float u, float v, float width, float height) {
			this.u0 = u;
			this.v0 = v;
			this.u1 = (u + width);
			this.v1 = (v + height);
			return this;
		}

		public ScreenVFXBuilder setUV(float u0, float v0, float u1, float v1, float canvasSize) {
			return setUV(u0, v0, u1, v1, canvasSize, canvasSize);
		}

		public ScreenVFXBuilder setUV(float u0, float v0, float u1, float v1, float canvasSizeX, float canvasSizeY) {
			return setUV(u0 / canvasSizeX, v0 / canvasSizeY, u1 / canvasSizeX, v1 / canvasSizeY);
		}

		public ScreenVFXBuilder setUV(float u0, float v0, float u1, float v1) {
			this.u0 = u0;
			this.v0 = v0;
			this.u1 = u1;
			this.v1 = v1;
			return this;
		}

		public ScreenVFXBuilder begin() {
			bufferbuilder.begin(VertexFormat.DrawMode.QUADS, format);
			return this;
		}

		public ScreenVFXBuilder blit(MatrixStack stack) {
			Matrix4f last = stack.peek().getModel();
			RenderSystem.setShader(shader);
			if (texture != null) {
				RenderSystem.setShaderTexture(0, texture);
			}
			supplier.placeVertex(bufferbuilder, last, x0, y1, u0, v1);
			supplier.placeVertex(bufferbuilder, last, x1, y1, u1, v1);
			supplier.placeVertex(bufferbuilder, last, x1, y0, u1, v0);
			supplier.placeVertex(bufferbuilder, last, x0, y0, u0, v0);
			return this;
		}

		public ScreenVFXBuilder blit(MatrixStack stack, Consumer<ScreenVFXBuilder> gradientConsumer) {
			Matrix4f last = stack.peek().getModel();
			RenderSystem.setShader(shader);
			if (texture != null) {
				RenderSystem.setShaderTexture(0, texture);
			}
			supplier.placeVertex(bufferbuilder, last, x0, y1, u0, v1);
			supplier.placeVertex(bufferbuilder, last, x1, y1, u1, v1);
			gradientConsumer.accept(this);
			supplier.placeVertex(bufferbuilder, last, x1, y0, u1, v0);
			supplier.placeVertex(bufferbuilder, last, x0, y0, u0, v0);
			return this;
		}


		public ScreenVFXBuilder run(Consumer<ScreenVFXBuilder> consumer) {
			consumer.accept(this);
			return this;
		}

		public ScreenVFXBuilder end() {
			BufferRenderer.drawWithShader(bufferbuilder.end());
			return this;
		}

		public ScreenVFXBuilder draw(MatrixStack stack) {
			if (bufferbuilder.isBuilding()) {
				bufferbuilder.end();
			}
			begin();
			blit(stack);
			end();
			return this;
		}


		public interface ScreenVertexPlacementSupplier {
			void placeVertex(BufferBuilder bufferBuilder, Matrix4f last, float x, float y, float u, float v);
		}
	}

	public static WorldVFXBuilder createWorld() {
		return new WorldVFXBuilder();
	}

	public static class WorldVFXBuilder {
		float r = 1, g = 1, b = 1, a = 1;
		float xOffset = 0, yOffset = 0, zOffset = 0;
		int light = RenderHelper.FULL_BRIGHT;
		float u0 = 0, v0 = 0, u1 = 1, v1 = 1;

		VertexFormat format;
		WorldVertexPlacementSupplier supplier;

		public WorldVFXBuilder setPosColorDefaultFormat() {
			return setVertexSupplier((c, l, x, y, z, u, v) -> {
				if (l == null)
					c.vertex(x, y, z).color(this.r, this.g, this.b, this.a).next();
				else
					c.vertex(l, x, y, z).color(this.r, this.g, this.b, this.a).next();
			}).setFormat(VertexFormats.POSITION_COLOR);
		}

		public WorldVFXBuilder setPosColorLightmapDefaultFormat() {
			return setVertexSupplier((c, l, x, y, z, u, v) -> {
				if (l == null)
					c.vertex(x, y, z).color(this.r, this.g, this.b, this.a).light(this.light).next();
				else
					c.vertex(l, x, y, z).color(this.r, this.g, this.b, this.a).light(this.light).next();

			}).setFormat(VertexFormats.POSITION_COLOR_LIGHT);
		}

		public WorldVFXBuilder setPosTexDefaultFormat() {
			return setVertexSupplier((c, l, x, y, z, u, v) -> {
				if (l == null)
					c.vertex(x, y, z).uv(u, v).next();
				else
					c.vertex(l, x, y, z).uv(u, v).next();
			}).setFormat(VertexFormats.POSITION_TEXTURE);
		}

		public WorldVFXBuilder setPosColorTexDefaultFormat() {
			return setVertexSupplier((c, l, x, y, z, u, v) -> {
				if (l == null)
					c.vertex(x, y, z).color(this.r, this.g, this.b, this.a).uv(u, v).next();
				else
					c.vertex(l, x, y, z).color(this.r, this.g, this.b, this.a).uv(u, v).next();
			}).setFormat(VertexFormats.POSITION_COLOR_TEXTURE);
		}

		public WorldVFXBuilder setPosColorTexLightmapDefaultFormat() {
			return setVertexSupplier((c, l, x, y, z, u, v) -> {
				if (l == null)
					c.vertex(x, y, z).color(this.r, this.g, this.b, this.a).uv(u, v).light(this.light).next();
				else
					c.vertex(l, x, y, z).color(this.r, this.g, this.b, this.a).uv(u, v).light(this.light).next();
			}).setFormat(VertexFormats.POSITION_COLOR_TEXTURE_LIGHT);
		}

		public WorldVFXBuilder setFormat(VertexFormat format) {
			this.format = format;
			return this;
		}

		public WorldVFXBuilder setVertexSupplier(WorldVertexPlacementSupplier supplier) {
			this.supplier = supplier;
			return this;
		}

		public WorldVFXBuilder setColorWithAlpha(Color color) {
			return setColor(color.getRed(), color.getGreen(), color.getBlue(), color.getAlpha()/255f);
		}

		public WorldVFXBuilder setColor(Color color) {
			return setColor(color.getRed(), color.getGreen(), color.getBlue());
		}

		public WorldVFXBuilder setColor(Color color, float a) {
			return setColor(color).setAlpha(a);
		}

		public WorldVFXBuilder setColor(float r, float g, float b, float a) {
			return setColor(r, g, b).setAlpha(a);
		}

		public WorldVFXBuilder setColor(float r, float g, float b) {
			this.r = r / 255f;
			this.g = g / 255f;
			this.b = b / 255f;
			return this;
		}

		public WorldVFXBuilder setAlpha(float a) {
			this.a = a;
			return this;
		}

		public WorldVFXBuilder setOffset(float xOffset, float yOffset, float zOffset) {
			this.xOffset = xOffset;
			this.yOffset = yOffset;
			this.zOffset = zOffset;
			return this;
		}

		public WorldVFXBuilder setLight(int light) {
			this.light = light;
			return this;
		}

		public WorldVFXBuilder setUV(Sprite sprite) {
			return setUV(sprite.getMinU(), sprite.getMinV(), sprite.getMaxU(), sprite.getMaxV());
		}

		public WorldVFXBuilder setUV(float u0, float v0, float u1, float v1) {
			this.u0 = u0;
			this.v0 = v0;
			this.u1 = u1;
			this.v1 = v1;
			return this;
		}

		public WorldVFXBuilder renderTrail(VertexConsumer vertexConsumer, MatrixStack stack, List<Vector4f> trailSegments, Function<Float, Float> widthFunc) {
			return renderTrail(vertexConsumer, stack, trailSegments, widthFunc, f -> {
			});
		}

		public WorldVFXBuilder renderTrail(VertexConsumer vertexConsumer, MatrixStack stack, List<Vector4f> trailSegments, Function<Float, Float> widthFunc, Consumer<Float> vfxOperator) {
			return renderTrail(vertexConsumer, stack.peek().getModel(), trailSegments, widthFunc, vfxOperator);
		}

		public WorldVFXBuilder renderTrail(VertexConsumer vertexConsumer, Matrix4f pose, List<Vector4f> trailSegments, Function<Float, Float> widthFunc, Consumer<Float> vfxOperator) {
			if (trailSegments.size() < 3) {
				return this;
			}
			trailSegments = trailSegments.stream().map(v -> new Vector4f(v.x(), v.y(), v.z(), v.w())).collect(Collectors.toList());
			for (Vector4f pos : trailSegments) {
				pos.add(xOffset, yOffset, zOffset, 0);
				pos.mul(pose);
			}

			int count = trailSegments.size() - 1;
			float increment = 1.0F / (count - 1);
			ArrayList<TrailPoint> points = new ArrayList<>();
			for (int i = 0; i < count; i++) {
				float width = widthFunc.apply(increment * i);
				Vector4f start = trailSegments.get(i);
				Vector4f end = trailSegments.get(i + 1);
				points.add(new TrailPoint(RenderHelper.midpoint(start, end), RenderHelper.screenSpaceQuadOffsets(start, end, width)));
			}
			return renderPoints(vertexConsumer, points, u0, v0, u1, v1, vfxOperator);
		}

		public WorldVFXBuilder renderPoints(VertexConsumer vertexConsumer, List<TrailPoint> trailPoints, float u0, float v0, float u1, float v1, Consumer<Float> vfxOperator) {
			int count = trailPoints.size() - 1;
			float increment = 1.0F / count;
			vfxOperator.accept(0f);
			trailPoints.get(0).renderStart(vertexConsumer, supplier, u0, v0, u1, MathHelper.lerp(increment, v0, v1));
			for (int i = 1; i < count; i++) {
				float current = MathHelper.lerp(i * increment, v0, v1);
				vfxOperator.accept(current);
				trailPoints.get(i).renderMid(vertexConsumer, supplier, u0, current, u1, current);
			}
			vfxOperator.accept(1f);
			trailPoints.get(count).renderEnd(vertexConsumer, supplier, u0, MathHelper.lerp((count) * increment, v0, v1), u1, v1);
			return this;
		}

		public WorldVFXBuilder renderBeam(VertexConsumer vertexConsumer, MatrixStack stack, Vec3d start, Vec3d end, float width) { //this doesn't work upstream
			MinecraftClient minecraft = MinecraftClient.getInstance();
			start.add(xOffset, yOffset, zOffset);
			end.add(xOffset, yOffset, zOffset);
			stack.translate(-start.x, -start.y, -start.z);
			Vec3d cameraPosition = minecraft.getBlockEntityRenderDispatcher().camera.getPos();
			Vec3d delta = end.subtract(start);
			Vec3d normal = start.subtract(cameraPosition).crossProduct(delta).normalize().multiply(width / 2f, width / 2f, width / 2f);
			Matrix4f last = stack.peek().getModel();
			Vec3d[] positions = new Vec3d[]{start.subtract(normal), start.add(normal), end.add(normal), end.subtract(normal)};

			supplier.placeVertex(vertexConsumer, last, (float) positions[0].x, (float) positions[0].y, (float) positions[0].z, u0, v1);
			supplier.placeVertex(vertexConsumer, last, (float) positions[1].x, (float) positions[1].y, (float) positions[1].z, u1, v1);
			supplier.placeVertex(vertexConsumer, last, (float) positions[2].x, (float) positions[2].y, (float) positions[2].z, u1, v0);
			supplier.placeVertex(vertexConsumer, last, (float) positions[3].x, (float) positions[3].y, (float) positions[3].z, u0, v0);
			stack.translate(start.x, start.y, start.z);
			return this;
		}

		public WorldVFXBuilder renderQuad(VertexConsumer vertexConsumer, MatrixStack stack, float size) {
			return renderQuad(vertexConsumer, stack, size, size);
		}

		public WorldVFXBuilder renderQuad(VertexConsumer vertexConsumer, MatrixStack stack, float width, float height) {
			Vector3f[] positions = new Vector3f[]{new Vector3f(-1, -1, 0), new Vector3f(1, -1, 0), new Vector3f(1, 1, 0), new Vector3f(-1, 1, 0)};
			return renderQuad(vertexConsumer, stack, positions, width, height);
		}

		public WorldVFXBuilder renderQuad(VertexConsumer vertexConsumer, MatrixStack stack, Vector3f[] positions, float size) {
			return renderQuad(vertexConsumer, stack, positions, size, size);
		}

		public WorldVFXBuilder renderQuad(VertexConsumer vertexConsumer, MatrixStack stack, Vector3f[] positions, float width, float height) {
			for (Vector3f position : positions) {
				position.mul(width, height, width);
			}
			return renderQuad(vertexConsumer, stack, positions);
		}

		public WorldVFXBuilder renderQuad(VertexConsumer vertexConsumer, MatrixStack stack, Vector3f[] positions) {
			Matrix4f last = stack.peek().getModel();
			stack.translate(xOffset, yOffset, zOffset);
			supplier.placeVertex(vertexConsumer, last, positions[0].x(), positions[0].y(), positions[0].z(), u0, v1);
			supplier.placeVertex(vertexConsumer, last, positions[1].x(), positions[1].y(), positions[1].z(), u1, v1);
			supplier.placeVertex(vertexConsumer, last, positions[2].x(), positions[2].y(), positions[2].z(), u1, v0);
			supplier.placeVertex(vertexConsumer, last, positions[3].x(), positions[3].y(), positions[3].z(), u0, v0);
			stack.translate(-xOffset, -yOffset, -zOffset);
			return this;
		}

		public WorldVFXBuilder renderSphere(VertexConsumer vertexConsumer, MatrixStack stack, float radius, int longs, int lats) {
			Matrix4f last = stack.peek().getModel();
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
					Vector3f p0 = RenderHelper.parametricSphere(u, v, radius);
					Vector3f p1 = RenderHelper.parametricSphere(u, vn, radius);
					Vector3f p2 = RenderHelper.parametricSphere(un, v, radius);
					Vector3f p3 = RenderHelper.parametricSphere(un, vn, radius);

					float textureU = u / endU * radius;
					float textureV = v / endV * radius;
					float textureUN = un / endU * radius;
					float textureVN = vn / endV * radius;
					RenderHelper.vertexPosColorUVLight(vertexConsumer, last, p0.x(), p0.y(), p0.z(), r, g, b, a, textureU, textureV, light);
					RenderHelper.vertexPosColorUVLight(vertexConsumer, last, p2.x(), p2.y(), p2.z(), r, g, b, a, textureUN, textureV, light);
					RenderHelper.vertexPosColorUVLight(vertexConsumer, last, p1.x(), p1.y(), p1.z(), r, g, b, a, textureU, textureVN, light);

					RenderHelper.vertexPosColorUVLight(vertexConsumer, last, p3.x(), p3.y(), p3.z(), r, g, b, a, textureUN, textureVN, light);
					RenderHelper.vertexPosColorUVLight(vertexConsumer, last, p1.x(), p1.y(), p1.z(), r, g, b, a, textureU, textureVN, light);
					RenderHelper.vertexPosColorUVLight(vertexConsumer, last, p2.x(), p2.y(), p2.z(), r, g, b, a, textureUN, textureV, light);
				}
			}
			return this;
		}


		public interface WorldVertexPlacementSupplier {
			void placeVertex(VertexConsumer consumer, Matrix4f last, float x, float y, float z, float u, float v);
		}
	}
}
