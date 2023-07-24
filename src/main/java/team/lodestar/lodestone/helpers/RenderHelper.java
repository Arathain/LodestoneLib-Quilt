package team.lodestar.lodestone.helpers;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.*;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.*;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.math.*;
import org.joml.*;

import java.lang.Math;
import java.util.List;
import java.util.Optional;
import java.util.function.Consumer;
import java.util.function.Supplier;

public final class RenderHelper {
	public static final int FULL_BRIGHT = 15728880;

	public static ShaderProgram getShader(RenderLayer layer) {
		if (layer instanceof RenderLayer.MultiPhase multiPhase) {
			Optional<Supplier<ShaderProgram>> shader = multiPhase.phases.shader.supplier;
			if (shader.isPresent()) {
				return shader.get().get();
			}
		}
		return null;
	}
	public static RenderPhase.Transparency getTransparency(RenderLayer layer) {
		if (layer instanceof RenderLayer.MultiPhase multiPhase) {
			return multiPhase.phases.transparency;
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

	public static Vector3f parametricSphere(float u, float v, float r) {
		return new Vector3f(MathHelper.cos(u) * MathHelper.sin(v) * r, MathHelper.cos(v) * r, MathHelper.sin(u) * MathHelper.sin(v) * r);
	}

	public static Vec2f screenSpaceQuadOffsets(Vector4f start, Vector4f end, float width) {
		float x = -start.x();
		float y = -start.y();
		if (Math.abs(start.z()) > 0) {
			float ratio = end.z() / start.z();
			x = end.x() + x * ratio;
			y = end.y() + y * ratio;
		} else if (Math.abs(end.z()) <= 0) {
			x += end.x();
			y += end.y();
		}
		if (start.z() > 0) {
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
		return new Vector4f((a.x() + b.x()) * 0.5F, (a.y() + b.y()) * 0.5F, (a.z() + b.z()) * 0.5F, (a.w() + b.w()) * 0.5F);
	}

	public static Vec2f worldPosToTexCoord(Vector3f worldPos, MatrixStack viewModelStack) {
		Matrix4f viewMat = viewModelStack.peek().getModel();
		Matrix4f projMat = RenderSystem.getProjectionMatrix();

		Vector3f localPos = new Vector3f(worldPos);
		localPos.sub(MinecraftClient.getInstance().gameRenderer.getCamera().getPos().toVector3f());

		Vector4f pos = new Vector4f(localPos, 0);
		pos.mul(viewMat);
		pos.mul(projMat);
		pos = perspectiveDivide(pos);

		return new Vec2f((pos.x() + 1F) / 2F, (pos.y() + 1F) / 2F);
	}
	private static Vector4f perspectiveDivide(Vector4f vec) {
		return new Vector4f(vec.x/vec.w, vec.y/vec.w, vec.z/vec.w, 0);
	}

	/**
	 * Draw a segmented line between two points, subdividing the line into a number of segments
	 * @param buffer The vertex consumer provider to draw to
	 * @param ms The matrix stack to draw with
	 * @param lineWidth The width of the line
	 * @param points The points to draw between
	 **/

	public static void drawSteppedLineBetween(VertexConsumerProvider buffer, MatrixStack ms, List<Vec3d> points, float lineWidth, int r, int g, int b, int a) {
		Vec3d origin = points.get(0);
		for (int i = 1; i < points.size(); i++) {
			Vec3d target = points.get(i);
			drawLineBetween(buffer, ms, origin, target, lineWidth, r, g, b, a);
			origin = target;
		}
	}

	/**
	 * Draw a segmented line between two points, subdividing the line into a number of segments
	 * @param buffer The vertex consumer provider to draw to
	 * @param ps The matrix stack to draw with
	 * @param start The start point
	 * @param end The end point
	 * @param steps The number of steps to divide the line into
	 * @param lineWidth The width of the line
	 * @param pointConsumer A consumer to call for each point in the line
	 */

	public static void drawSteppedLineBetween(VertexConsumerProvider buffer, MatrixStack ps, Vec3d start, Vec3d end, int steps, float lineWidth, int r, int g, int b, int a, Consumer<Vec3d> pointConsumer) {
		Vec3d origin = start;
		for (int i = 1; i <= steps; i++) {
			Vec3d target = start.add(end.subtract(start).multiply(i / (float) steps));
			pointConsumer.accept(target);
			drawLineBetween(buffer, ps, origin, target, lineWidth, r, g, b, a);
			origin = target;
		}
	}

	/**
	 * Draw a line between two points
	 * @param buffer The vertex consumer provider to draw to
	 * @param ms The matrix stack to draw with
	 * @param local The start point
	 * @param target The end point
	 * @param lineWidth The width of the line
	 */

	public static void drawLineBetween(VertexConsumerProvider buffer, MatrixStack ms, Vec3d local, Vec3d target, float lineWidth, int r, int g, int b, int a) {
		VertexConsumer builder = buffer.getBuffer(RenderLayer.getLeash());

		//Calculate yaw
		float rotY = (float) MathHelper.atan2(target.x - local.x, target.z - local.z);

		//Calculate pitch
		double distX = target.x - local.x;
		double distZ = target.z - local.z;
		float rotX = (float) MathHelper.atan2(target.y - local.y, MathHelper.sqrt((float) (distX * distX + distZ * distZ)));

		ms.push();

		//Translate to start point
		ms.translate(local.x, local.y, local.z);
		//Rotate to point towards end point
		ms.multiply(new Quaternionf().rotateY(rotY));
		ms.multiply(new Quaternionf().rotateX(-rotX));

		//Calculate distance between points -> length of the line
		float distance = (float) local.distanceTo(target);

		Matrix4f matrix = ms.peek().getModel();
		float halfWidth = lineWidth / 2F;

		//Draw horizontal quad
		builder.vertex(matrix, -halfWidth, 0, 0).color(r, g, b, a).light(0xF000F0).next();
		builder.vertex(matrix, halfWidth, 0, 0).color(r, g, b, a).light(0xF000F0).next();
		builder.vertex(matrix, halfWidth, 0, distance).color(r, g, b, a).light(0xF000F0).next();
		builder.vertex(matrix, -halfWidth, 0, distance).color(r, g, b, a).light(0xF000F0).next();

		//Draw vertical Quad
		builder.vertex(matrix, 0, -halfWidth, 0).color(r, g, b, a).light(0xF000F0).next();
		builder.vertex(matrix, 0, halfWidth, 0).color(r, g, b, a).light(0xF000F0).next();
		builder.vertex(matrix, 0, halfWidth, distance).color(r, g, b, a).light(0xF000F0).next();
		builder.vertex(matrix, 0, -halfWidth, distance).color(r, g, b, a).light(0xF000F0).next();

		ms.pop();
	}
}
