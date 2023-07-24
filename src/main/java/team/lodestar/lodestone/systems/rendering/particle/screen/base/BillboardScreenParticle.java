package team.lodestar.lodestone.systems.rendering.particle.screen.base;

import com.mojang.blaze3d.vertex.BufferBuilder;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.util.math.MathHelper;
import org.joml.Vector3f;

public abstract class BillboardScreenParticle extends ScreenParticle {
	protected float quadSize = 0.1F * (this.random.nextFloat() * 0.5F + 0.5F) * 2.0F;
	protected BillboardScreenParticle(ClientWorld pLevel, double pX, double pY) {
		super(pLevel, pX, pY);
	}

	protected BillboardScreenParticle(ClientWorld pLevel, double pX, double pY, double pXSpeed, double pYSpeed) {
		super(pLevel, pX, pY, pXSpeed, pYSpeed);
	}

	@Override
	public void render(BufferBuilder bufferBuilder) {
		float tickDelta = MinecraftClient.getInstance().getTickDelta();
		float size = getQuadSize(tickDelta) * 10;
		float u0 = getMinU();
		float u1 = getMaxU();
		float v0 = getMinV();
		float v1 = getMaxV();
		float roll = MathHelper.lerp(tickDelta, this.prevAngle, this.angle);
		Vector3f[] vectors = new Vector3f[]{new Vector3f(-1.0F, -1.0F, 0.0F), new Vector3f(-1.0F, 1.0F, 0.0F), new Vector3f(1.0F, 1.0F, 0.0F), new Vector3f(1.0F, -1.0F, 0.0F)};
		for (int i = 0; i < 4; ++i) {
			Vector3f vector3f = vectors[i];
			vector3f.rotateZ(roll);
			vector3f.mul(size);
			vector3f.add((float) x, (float) y, 0);
		}
		float quadZ = getQuadZPosition();
		bufferBuilder.vertex(vectors[0].x(), vectors[0].y(), quadZ).uv(u1, v1).color(this.rCol, this.gCol, this.bCol, this.alpha).next();
		bufferBuilder.vertex(vectors[1].x(), vectors[1].y(), quadZ).uv(u1, v0).color(this.rCol, this.gCol, this.bCol, this.alpha).next();
		bufferBuilder.vertex(vectors[2].x(), vectors[2].y(), quadZ).uv(u0, v0).color(this.rCol, this.gCol, this.bCol, this.alpha).next();
		bufferBuilder.vertex(vectors[3].x(), vectors[3].y(), quadZ).uv(u0, v1).color(this.rCol, this.gCol, this.bCol, this.alpha).next();
	}

	public float getQuadSize(float tickDelta) {
		return this.quadSize;
	}

	public float getQuadZPosition() {
		return 390;
	}

	protected abstract float getMinU();

	protected abstract float getMaxU();

	protected abstract float getMinV();

	protected abstract float getMaxV();
}
