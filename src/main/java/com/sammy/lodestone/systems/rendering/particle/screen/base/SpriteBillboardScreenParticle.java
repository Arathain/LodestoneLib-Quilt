package com.sammy.lodestone.systems.rendering.particle.screen.base;

import net.minecraft.client.particle.SpriteProvider;
import net.minecraft.client.texture.Sprite;
import net.minecraft.world.World;

public abstract class SpriteBillboardScreenParticle extends BillboardScreenParticle {
    protected Sprite sprite;

    protected SpriteBillboardScreenParticle(World clientWorld, double pX, double pY) {
        super(clientWorld, pX, pY);
    }

    protected SpriteBillboardScreenParticle(World clientWorld, double pX, double pY, double pXSpeed, double pYSpeed) {
        super(clientWorld, pX, pY, pXSpeed, pYSpeed);
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
        this.setSprite(pSprite.m_mqkdmdiv(this.random));
    }

    public void setSpriteForAge(SpriteProvider pSprite) {
        if (!this.removed) {
            this.setSprite(pSprite.getSprite(this.age, this.maxAge));
        }
    }
}
