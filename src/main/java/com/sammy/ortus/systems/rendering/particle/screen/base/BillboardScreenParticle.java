package com.sammy.ortus.systems.rendering.particle.screen.base;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.BufferBuilder;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Quaternion;
import net.minecraft.util.math.Vec3f;

import static com.sammy.ortus.helpers.RenderHelper.FULL_BRIGHT;

@Environment(EnvType.CLIENT)
public abstract class BillboardScreenParticle extends ScreenParticle {
    protected float quadSize = 0.1F * (this.random.nextFloat() * 0.5F + 0.5F) * 2.0F;

    protected BillboardScreenParticle(ClientWorld pWorld, double pX, double pY) {
        super(pWorld, pX, pY);
    }

    protected BillboardScreenParticle(ClientWorld pWorld, double pX, double pY, double pXSpeed, double pYSpeed) {
        super(pWorld, pX, pY, pXSpeed, pYSpeed);
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
        Vec3f[] vectors = new Vec3f[]{new Vec3f(-1.0F, -1.0F, 0.0F), new Vec3f(-1.0F, 1.0F, 0.0F), new Vec3f(1.0F, 1.0F, 0.0F), new Vec3f(1.0F, -1.0F, 0.0F)};
        Quaternion rotation = Vec3f.POSITIVE_Z.getRadialQuaternion(roll);
        for (int i = 0; i < 4; ++i) {
            Vec3f vector3f = vectors[i];
            vector3f.rotate(rotation);
            vector3f.scale(size);
            vector3f.add((float) x, (float) y, 0);
        }
      /*TODO: JEI tooltips render at 400 z, while the held by mouse item stack renders at around 380, we need a value between to be above the stack, but below JEI tooltips.
         There is definitely a better way of doing this.
       */
        int z = 390;
        bufferBuilder.vertex(vectors[0].getX(), vectors[0].getY(), z).texture(u1, v1).color(this.red, this.green, this.blue, this.alpha).light(FULL_BRIGHT).next();
        bufferBuilder.vertex(vectors[1].getX(), vectors[1].getY(), z).texture(u1, v0).color(this.red, this.green, this.blue, this.alpha).light(FULL_BRIGHT).next();
        bufferBuilder.vertex(vectors[2].getX(), vectors[2].getY(), z).texture(u0, v0).color(this.red, this.green, this.blue, this.alpha).light(FULL_BRIGHT).next();
        bufferBuilder.vertex(vectors[3].getX(), vectors[3].getY(), z).texture(u0, v1).color(this.red, this.green, this.blue, this.alpha).light(FULL_BRIGHT).next();
    }

    public float getQuadSize(float tickDelta) {
        return this.quadSize;
    }

    protected abstract float getMinU();

    protected abstract float getMaxU();

    protected abstract float getMinV();

    protected abstract float getMaxV();
}
