package com.sammy.lodestone.systems.particle.screen.base;

import com.mojang.blaze3d.vertex.BufferBuilder;
import net.minecraft.client.MinecraftClient;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;
import org.joml.Quaternionf;
import org.joml.Vector3f;

public abstract class BillboardScreenParticle extends ScreenParticle {
	protected float quadSize = 0.1F * (this.random.nextFloat() * 0.5F + 0.5F) * 2.0F;
	protected BillboardScreenParticle(World pLevel, double pX, double pY) {
		super(pLevel, pX, pY);
	}

	protected BillboardScreenParticle(World pLevel, double pX, double pY, double pXSpeed, double pYSpeed) {
		super(pLevel, pX, pY, pXSpeed, pYSpeed);
	}

    @Override
    public void render(BufferBuilder bufferBuilder) {
		float partialTicks = MinecraftClient.getInstance().getTickDelta();
		float size = getQuadSize(partialTicks) * 10;
		float u0 = getMinU();
		float u1 = getMaxU();
		float v0 = getMinV();
		float v1 = getMaxV();
		float roll = MathHelper.lerp(partialTicks, this.prevAngle, this.angle);
		Vector3f[] vectors = new Vector3f[]{new Vector3f(-1.0F, -1.0F, 0.0F), new Vector3f(-1.0F, 1.0F, 0.0F), new Vector3f(1.0F, 1.0F, 0.0F), new Vector3f(1.0F, -1.0F, 0.0F)};
		Quaternionf rotation = new Quaternionf().rotationZ(roll);
		for (int i = 0; i < 4; ++i) {
			Vector3f vector3f = vectors[i];
			vector3f.rotate(rotation);
			vector3f.mul(size);
			vector3f.add((float) x, (float) y, 0);
		}
		float quadZ = getQuadZPosition();
		bufferBuilder.vertex(vectors[0].x(), vectors[0].y(), quadZ).uv(u1, v1).color(this.red, this.green, this.blue, this.alpha).next();
		bufferBuilder.vertex(vectors[1].x(), vectors[1].y(), quadZ).uv(u1, v0).color(this.red, this.green, this.blue, this.alpha).next();
		bufferBuilder.vertex(vectors[2].x(), vectors[2].y(), quadZ).uv(u0, v0).color(this.red, this.green, this.blue, this.alpha).next();
		bufferBuilder.vertex(vectors[3].x(), vectors[3].y(), quadZ).uv(u0, v1).color(this.red, this.green, this.blue, this.alpha).next();
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
