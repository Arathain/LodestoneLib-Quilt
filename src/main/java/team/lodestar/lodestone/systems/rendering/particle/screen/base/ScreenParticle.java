package team.lodestar.lodestone.systems.rendering.particle.screen.base;

import com.mojang.blaze3d.vertex.BufferBuilder;
import team.lodestar.lodestone.systems.rendering.particle.screen.LodestoneScreenParticleTextureSheet;
import net.minecraft.client.world.ClientWorld;

import java.util.Random;

public abstract class ScreenParticle {

	public final ClientWorld level;
	public double prevX;
	public double prevY;
	public double x;
	public double y;
	public double velocityX;
	public double velocityY;
	public double xMoved;
	public double yMoved;
	public boolean removed;
	public final Random random = new Random();
	public int age;
	public int maxAge;
	public float gravity;
	public float size = 1;
	public float rCol = 1.0F;
	public float gCol = 1.0F;
	public float bCol = 1.0F;
	public float alpha = 1.0F;
	public float angle;
	public float prevAngle;
	public float friction = 0.98F;

	protected ScreenParticle(ClientWorld pLevel, double pX, double pY) {
		this.level = pLevel;
		this.setSize(0.2F);
		this.x = pX;
		this.y = pY;
		this.prevX = pX;
		this.prevY = pY;
		this.maxAge = (int) (4.0F / (this.random.nextFloat() * 0.9F + 0.1F));
	}

	public ScreenParticle(ClientWorld pLevel, double pX, double pY, double pXSpeed, double pYSpeed) {
		this(pLevel, pX, pY);
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

	public ScreenParticle setSize(float size) {
		this.size = size;
		return this;
	}

	public void setColor(float pParticleRed, float pParticleGreen, float pParticleBlue) {
		this.rCol = pParticleRed;
		this.gCol = pParticleGreen;
		this.bCol = pParticleBlue;
	}

	protected void setAlpha(float pAlpha) {
		this.alpha = pAlpha;
	}

	public void setMaxAge(int pParticleLifeTime) {
		this.maxAge = pParticleLifeTime;
	}

	public int getMaxAge() {
		return this.maxAge;
	}

	public void tick() {
		this.prevX = this.x;
		this.prevY = this.y;
		if (this.age++ >= this.maxAge) {
			this.remove();
		} else {
			this.velocityY -= 0.04D * (double) this.gravity;
			this.velocityX *= this.friction;
			this.velocityY *= this.friction;
			this.x += velocityX;
			this.y += velocityY;
			this.xMoved += velocityX;
			this.yMoved += velocityY;
		}
	}

	public abstract void render(BufferBuilder bufferBuilder);

	public abstract LodestoneScreenParticleTextureSheet getRenderType();

	public void remove() {
		this.removed = true;
	}

	public boolean isAlive() {
		return !this.removed;
	}
}
