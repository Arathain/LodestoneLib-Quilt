package com.sammy.ortus.systems.rendering.particle.screen.base;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.particle.SpriteProvider;
import net.minecraft.client.texture.Sprite;
import net.minecraft.client.world.ClientWorld;

@Environment(EnvType.CLIENT)
public abstract class SpriteBillboardScreenParticle extends BillboardScreenParticle {
    protected Sprite sprite;

    protected SpriteBillboardScreenParticle(ClientWorld pWorld, double pX, double pY) {
        super(pWorld, pX, pY);
    }

    protected SpriteBillboardScreenParticle(ClientWorld pWorld, double pX, double pY, double pXSpeed, double pYSpeed) {
        super(pWorld, pX, pY, pXSpeed, pYSpeed);
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

    public void setSprite(SpriteProvider pSprite) {
        this.setSprite(pSprite.getSprite(this.random));
    }

    public void setSpriteForAge(SpriteProvider pSprite) {
        if (!this.removed) {
            this.setSprite(pSprite.getSprite(this.age, this.maxAge));
        }
    }
}
