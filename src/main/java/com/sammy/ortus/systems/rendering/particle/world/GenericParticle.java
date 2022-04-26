package com.sammy.ortus.systems.rendering.particle.world;

import com.sammy.ortus.systems.rendering.particle.SimpleParticleEffect;
import net.fabricmc.fabric.impl.client.particle.FabricSpriteProviderImpl;
import net.minecraft.client.gui.hud.BackgroundHelper;
import net.minecraft.client.particle.ParticleTextureSheet;
import net.minecraft.client.particle.SpriteBillboardParticle;
import net.minecraft.client.render.Camera;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.util.math.MathHelper;

import java.awt.*;

public class GenericParticle extends SpriteBillboardParticle {
    protected WorldParticleEffect data;
    private final ParticleTextureSheet textureSheet;
    protected final FabricSpriteProviderImpl spriteProvider;
    float[] hsv1 = new float[3], hsv2 = new float[3];
    public GenericParticle(ClientWorld world, WorldParticleEffect data, FabricSpriteProviderImpl spriteProvider, double x, double y, double z, double velocityX, double velocityY, double velocityZ) {
        super(world, x, y, z);
        this.data = data;
        this.textureSheet = data.textureSheet;
        this.spriteProvider = spriteProvider;
        this.angle = data.spinOffset + data.spin1;
        if (!data.forcedMotion) {
            this.velocityX = velocityX;
            this.velocityY = velocityY;
            this.velocityZ = velocityZ;
        }
        this.setMaxAge(data.lifetime);
        this.gravityStrength = data.gravity ? 1 : 0;
        this.collidesWithWorld = !data.noClip;
        this.velocityMultiplier = 1;
        Color.RGBtoHSB((int) (255 * Math.min(1.0f, data.r1)), (int) (255 * Math.min(1.0f, data.g1)), (int) (255 * Math.min(1.0f, data.b1)), hsv1);
        Color.RGBtoHSB((int) (255 * Math.min(1.0f, data.r2)), (int) (255 * Math.min(1.0f, data.g2)), (int) (255 * Math.min(1.0f, data.b2)), hsv2);
        if (getAnimator().equals(SimpleParticleEffect.Animator.RANDOM_SPRITE)) {
            setSprite(spriteProvider);
        }
        if (getAnimator().equals(SimpleParticleEffect.Animator.FIRST_INDEX) || getAnimator().equals(SimpleParticleEffect.Animator.WITH_AGE)) {
            pickSprite(0);
        }
        if (getAnimator().equals(SimpleParticleEffect.Animator.LAST_INDEX)) {
            pickSprite(spriteProvider.getSprites().size() - 1);
        }
        updateTraits();
    }
    public void pickSprite(int spriteIndex) {
        if (spriteIndex < spriteProvider.getSprites().size() && spriteIndex >= 0) {
            setSprite(spriteProvider.getSprites().get(spriteIndex));
        }
    }
    public void pickColor(float colorCoeff) {
        float h = MathHelper.lerpAngleDegrees(colorCoeff, 360f * hsv1[0], 360f * hsv2[0]) / 360f;
        float s = MathHelper.lerp(colorCoeff, hsv1[1], hsv2[1]);
        float v = MathHelper.lerp(colorCoeff, hsv1[2], hsv2[2]);
        int packed = Color.HSBtoRGB(h, s, v);
        float r = BackgroundHelper.ColorMixer.getRed(packed) / 255.0f;
        float g = BackgroundHelper.ColorMixer.getGreen(packed) / 255.0f;
        float b = BackgroundHelper.ColorMixer.getBlue(packed) / 255.0f;
        setColor(r, g, b);
    }

    public float getCurve(float multiplier) {
        return MathHelper.clamp((age * multiplier) / (float)maxAge, 0, 1);
    }
    public SimpleParticleEffect.Animator getAnimator() {
        return data.animator;
    }
    protected void updateTraits() {
        pickColor(data.colorCurveEasing.ease(getCurve(data.colorCurveMultiplier), 0, 1, 1));
        if (data.isTrinaryScale()) {
            float trinaryAge = getCurve(data.scaleCurveMultiplier);
            if (trinaryAge >= 0.5f) {
                scale = MathHelper.lerp(data.scaleCurveEndEasing.ease(trinaryAge - 0.5f, 0, 1, 0.5f), data.scale2, data.scale3);
            } else {
                scale = MathHelper.lerp(data.scaleCurveStartEasing.ease(trinaryAge, 0, 1, 0.5f), data.scale1, data.scale2);
            }
        } else {
            scale = MathHelper.lerp(data.scaleCurveStartEasing.ease(getCurve(data.scaleCurveMultiplier), 0, 1, 1), data.scale1, data.scale2);
        }
        if (data.isTrinaryAlpha()) {
            float trinaryAge = getCurve(data.alphaCurveMultiplier);
            if (trinaryAge >= 0.5f) {
                colorAlpha = MathHelper.lerp(data.alphaCurveStartEasing.ease(trinaryAge-0.5f, 0, 1, 0.5f), data.alpha2, data.alpha3);
            } else {
                colorAlpha = MathHelper.lerp(data.alphaCurveStartEasing.ease(trinaryAge, 0, 1, 0.5f), data.alpha1, data.alpha2);
            }
        } else {
            colorAlpha = MathHelper.lerp(data.alphaCurveStartEasing.ease(getCurve(data.alphaCurveMultiplier), 0, 1, 1), data.alpha1, data.alpha2);
        }
        prevAngle = angle;
        angle += MathHelper.lerp(data.spinEasing.ease(getCurve(data.spinCurveMultiplier), 0, 1, 1), data.spin1, data.spin2);
        if (data.forcedMotion) {
            float motionAge = getCurve(data.motionCurveMultiplier);
            velocityX = MathHelper.lerp(data.motionEasing.ease(motionAge, 0, 1, 1), data.startingMotion.getX(), data.endingMotion.getX());
            velocityY = MathHelper.lerp(data.motionEasing.ease(motionAge, 0, 1, 1), data.startingMotion.getY(), data.endingMotion.getY());
            velocityZ = MathHelper.lerp(data.motionEasing.ease(motionAge, 0, 1, 1), data.startingMotion.getZ(), data.endingMotion.getZ());
        } else {
            velocityX *= data.motionCurveMultiplier;
            velocityY *= data.motionCurveMultiplier;
            velocityZ *= data.motionCurveMultiplier;
        }
    }
    @Override
    public void tick() {
        updateTraits();
        if (data.animator.equals(SimpleParticleEffect.Animator.WITH_AGE)) {
            setSpriteForAge(spriteProvider);
        }
        super.tick();
    }

    @Override
    public void buildGeometry(VertexConsumer vertexConsumer, Camera camera, float tickDelta) {
        super.buildGeometry(/*ClientConfig.DELAYED_PARTICLE_RENDERING ? RenderHandler.DELAYED_RENDER.getBuffer(RenderLayers.ADDITIVE_PARTICLE) :*/ vertexConsumer, camera, tickDelta);
    }
    @Override
    public ParticleTextureSheet getType() {
        return textureSheet;
    }
}
