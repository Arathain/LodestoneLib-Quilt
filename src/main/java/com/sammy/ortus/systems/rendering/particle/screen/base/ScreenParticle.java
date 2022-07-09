package com.sammy.ortus.systems.rendering.particle.screen.base;

import com.mojang.blaze3d.vertex.BufferBuilder;
import net.minecraft.client.particle.ParticleTextureSheet;
import net.minecraft.util.random.RandomGenerator;
import net.minecraft.world.World;

public abstract class ScreenParticle {

    public enum RenderOrder{
        BEFORE_UI, BEFORE_TOOLTIPS, AFTER_EVERYTHING
    }

    public final World clientWorld; // this can't be a ClientWorld cause of server environment stuff
    public double prevX;
    public double prevY;
    public double x;
    public double y;
    public double velocityX;
    public double velocityY;
    public double totalX;
    public double totalY;
    public boolean removed;
    public final RandomGenerator random = RandomGenerator.createLegacy();
    public int age;
    public int maxAge;
    public float gravityStrength;
    public float scale = 1;
    public float red = 1.0F;
    public float green = 1.0F;
    public float blue = 1.0F;
    public float alpha = 1.0F;
    public float angle;
    public float prevAngle;
    public float velocityMultiplier = 0.98F;
    public RenderOrder renderOrder = RenderOrder.AFTER_EVERYTHING;

    protected ScreenParticle(World clientWorld, double pX, double pY) {
        this.clientWorld = clientWorld;
        this.setScale(0.2F);
        this.x = pX;
        this.y = pY;
        this.prevX = pX;
        this.prevY = pY;
        this.maxAge = (int) (4.0F / (this.random.nextFloat() * 0.9F + 0.1F));
    }

    public ScreenParticle(World clientWorld, double pX, double pY, double pXSpeed, double pYSpeed) {
        this(clientWorld, pX, pY);
        this.velocityX = pXSpeed + (Math.random() * 2.0D - 1.0D) * (double) 0.4F;
        this.velocityY = pYSpeed + (Math.random() * 2.0D - 1.0D) * (double) 0.4F;
        double d0 = (Math.random() + Math.random() + 1.0D) * (double) 0.15F;
        double d1 = Math.sqrt(this.velocityX * this.velocityX + this.velocityY * this.velocityY);
        this.velocityX = this.velocityX / d1 * d0 * (double) 0.4F;
        this.velocityY = this.velocityY / d1 * d0 * (double) 0.4F + (double) 0.1F;
    }

    public void setParticleSpeed(double pXd, double pYd) {
        this.velocityX = pXd;
        this.velocityY = pYd;
    }

    public ScreenParticle setScale(float size) {
        this.scale = size;
        return this;
    }

    public void setColor(float pParticleRed, float pParticleGreen, float pParticleBlue) {
        this.red = pParticleRed;
        this.green = pParticleGreen;
        this.blue = pParticleBlue;
    }

    protected void setAlpha(float pAlpha) {
        this.alpha = pAlpha;
    }

    public void setMaxAge(int pMaxAge) {
        this.maxAge = pMaxAge;
    }

    public int getMaxAge() {
        return this.maxAge;
    }

    public void setRenderOrder(RenderOrder renderOrder){
        this.renderOrder = renderOrder;
    }

    public RenderOrder getRenderOrder() {
        return renderOrder;
    }

    public void tick() {
        this.prevX = this.x;
        this.prevY = this.y;
        if (this.age++ >= this.maxAge) {
            this.remove();
        } else {
            this.velocityY -= 0.04D * (double) this.gravityStrength;
            this.velocityX *= this.velocityMultiplier;
            this.velocityY *= this.velocityMultiplier;
            this.x += velocityX;
            this.y += velocityY;
            this.totalX += velocityX;
            this.totalY += velocityY;
        }
    }

    public abstract void render(BufferBuilder bufferBuilder);

    public abstract ParticleTextureSheet getTextureSheet();

    public void remove() {
        this.removed = true;
    }

    public boolean isAlive() {
        return !this.removed;
    }
}
