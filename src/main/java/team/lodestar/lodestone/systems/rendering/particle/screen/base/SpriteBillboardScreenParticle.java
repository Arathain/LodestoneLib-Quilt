package team.lodestar.lodestone.systems.rendering.particle.screen.base;

import net.minecraft.client.particle.SpriteProvider;
import net.minecraft.client.texture.Sprite;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.util.random.RandomGenerator;

public abstract class SpriteBillboardScreenParticle extends BillboardScreenParticle {
	protected Sprite sprite;

	protected SpriteBillboardScreenParticle(ClientWorld pLevel, double pX, double pY) {
		super(pLevel, pX, pY);
	}

	protected SpriteBillboardScreenParticle(ClientWorld pLevel, double pX, double pY, double pXSpeed, double pYSpeed) {
		super(pLevel, pX, pY, pXSpeed, pYSpeed);
	}

	protected void setSprite(Sprite pSprite) {
		this.sprite = pSprite;
	}

	protected float getMinU() {
		return this.sprite.getMinU();
	}

	protected float getMaxU() {
		return this.sprite.getMaxU();
	}

	protected float getMinV() {
		return this.sprite.getMinV();
	}

	protected float getMaxV() {
		return this.sprite.getMaxV();
	}

	public void pickSprite(SpriteProvider pSprite) {
		this.setSprite(pSprite.getRandom(RandomGenerator.createLegacy()));
	}

	public void setSpriteFromAge(SpriteProvider pSprite) {
		if (!this.removed) {
			this.setSprite(pSprite.getSprite(this.age, this.maxAge));
		}
	}
}
