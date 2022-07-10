package com.sammy.ortus.systems.rendering.particle.screen;

import com.sammy.ortus.handlers.ScreenParticleHandler;
import com.sammy.ortus.systems.rendering.particle.SimpleParticleEffect;
import com.sammy.ortus.systems.rendering.particle.screen.base.SpriteBillboardScreenParticle;
import net.fabricmc.fabric.impl.client.particle.FabricSpriteProviderImpl;
import net.minecraft.client.particle.ParticleTextureSheet;
import net.minecraft.client.util.ColorUtil;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec2f;
import net.minecraft.world.World;

import java.awt.*;

public class GenericScreenParticle extends SpriteBillboardScreenParticle {
    public ScreenParticleEffect data;
    private final ParticleTextureSheet textureSheet;
    protected final FabricSpriteProviderImpl spriteProvider;

	private final Vec2f startingVelocity;
    float[] hsv1 = new float[3], hsv2 = new float[3];

    public GenericScreenParticle(World clientWorld, ScreenParticleEffect data, FabricSpriteProviderImpl spriteSet, double x, double y, double xMotion, double yMotion) {
        super(clientWorld, x, y);
        this.data = data;
        this.textureSheet = data.textureSheet;
        this.spriteProvider = spriteSet;
        this.angle = data.spinOffset + data.spin1;
        if (!data.forcedMotion) {
            this.velocityX = xMotion;
            this.velocityY = yMotion;
        }
        this.setRenderOrder(data.renderOrder);
        this.setMaxAge(data.lifetime);
        this.gravityStrength = data.gravity;
        this.velocityMultiplier = 1;

		this.startingVelocity = data.motionStyle == SimpleParticleEffect.MotionStyle.START_TO_END ? data.startingVelocity : new Vec2f((float)xMotion, (float)yMotion);
		Color.RGBtoHSB((int) (255 * Math.min(1.0f, data.r1)), (int) (255 * Math.min(1.0f, data.g1)), (int) (255 * Math.min(1.0f, data.b1)), hsv1);
        Color.RGBtoHSB((int) (255 * Math.min(1.0f, data.r2)), (int) (255 * Math.min(1.0f, data.g2)), (int) (255 * Math.min(1.0f, data.b2)), hsv2);
        updateTraits();
        if (getAnimator().equals(SimpleParticleEffect.Animator.RANDOM_SPRITE)) {
            setSprite(spriteProvider);
        }
        if (getAnimator().equals(SimpleParticleEffect.Animator.FIRST_INDEX) || getAnimator().equals(SimpleParticleEffect.Animator.WITH_AGE)) {
            setSprite(0);
        }
        if (getAnimator().equals(SimpleParticleEffect.Animator.LAST_INDEX)) {
            setSprite(spriteProvider.getSprites().size() - 1);
        }
        updateTraits();
    }

    public SimpleParticleEffect.Animator getAnimator() {
        return data.animator;
    }

    public void setSprite(int spriteIndex) {
        if (spriteIndex < spriteProvider.getSprites().size() && spriteIndex >= 0) {
            setSprite(spriteProvider.getSprites().get(spriteIndex));
        }
    }

    public void pickColor(float colorCoeff) {
        float h = MathHelper.lerpAngleDegrees(colorCoeff, 360f * hsv1[0], 360f * hsv2[0]) / 360f;
        float s = MathHelper.lerp(colorCoeff, hsv1[1], hsv2[1]);
        float v = MathHelper.lerp(colorCoeff, hsv1[2], hsv2[2]);
        int packed = Color.HSBtoRGB(h, s, v);
        float r = ColorUtil.ARGB32.getRed(packed) / 255.0f;
        float g = ColorUtil.ARGB32.getGreen(packed) / 255.0f;
        float b = ColorUtil.ARGB32.getBlue(packed) / 255.0f;
        setColor(r, g, b);
    }

    public float getCurve(float multiplier) {
        return MathHelper.clamp((age * multiplier) / (float) maxAge, 0, 1);
    }

    protected void updateTraits() {
		pickColor(data.colorCurveEasing.ease(getCurve(data.colorCoefficient), 0, 1, 1));
		if (data.isTrinaryScale()) {
			float trinaryAge = getCurve(data.scaleCoefficient);
			if (trinaryAge >= 0.5f) {
				quadSize = MathHelper.lerp(data.scaleCurveEndEasing.ease(trinaryAge - 0.5f, 0, 1, 0.5f), data.scale2, data.scale3);
			} else {
				quadSize = MathHelper.lerp(data.scaleCurveStartEasing.ease(trinaryAge, 0, 1, 0.5f), data.scale1, data.scale2);
			}
		} else {
			quadSize = MathHelper.lerp(data.scaleCurveStartEasing.ease(getCurve(data.scaleCoefficient), 0, 1, 1), data.scale1, data.scale2);
		}
		if (data.isTrinaryAlpha()) {
			float trinaryAge = getCurve(data.alphaCoefficient);
			if (trinaryAge >= 0.5f) {
				alpha = MathHelper.lerp(data.alphaCurveEndEasing.ease(trinaryAge - 0.5f, 0, 1, 0.5f), data.alpha2, data.alpha3);
			} else {
				alpha = MathHelper.lerp(data.alphaCurveStartEasing.ease(trinaryAge, 0, 1, 0.5f), data.alpha1, data.alpha2);
			}
		} else {
			alpha = MathHelper.lerp(data.alphaCurveStartEasing.ease(getCurve(data.alphaCoefficient), 0, 1, 1), data.alpha1, data.alpha2);
		}
		prevAngle = angle;

		if (data.isTrinarySpin()) {
			float trinaryAge = getCurve(data.spinCoefficient);
			if (trinaryAge >= 0.5f) {
				angle += MathHelper.lerp(data.spinCurveEndEasing.ease(trinaryAge - 0.5f, 0, 1, 0.5f), data.spin2, data.spin3);
			} else {
				angle += MathHelper.lerp(data.spinCurveStartEasing.ease(trinaryAge, 0, 1, 0.5f), data.spin1, data.spin2);
			}
		} else {
			angle += MathHelper.lerp(data.spinCurveStartEasing.ease(getCurve(data.alphaCoefficient), 0, 1, 1), data.spin1, data.spin2);
		}
		if (data.forcedMotion) {
			float motionAge = getCurve(data.motionCoefficient);
			Vec2f currentMotion = data.motionStyle == SimpleParticleEffect.MotionStyle.START_TO_END ? startingVelocity : new Vec2f((float) velocityX, (float) velocityY);
			velocityX = MathHelper.lerp(data.motionEasing.ease(motionAge, 0, 1, 1), currentMotion.x, data.endingMotion.x);
			velocityY = MathHelper.lerp(data.motionEasing.ease(motionAge, 0, 1, 1), currentMotion.y, data.endingMotion.y);
		} else {
			velocityX *= data.motionCoefficient;
			velocityY *= data.motionCoefficient;
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

    public void trackStack() {
        for (ScreenParticleHandler.StackTracker renderedStack : ScreenParticleHandler.RENDERED_STACKS) {
            //&& renderedStack.xOrigin() == data.xOrigin && renderedStack.yOrigin() == data.yOrigin
            if (renderedStack.stack().equals(data.stack) && renderedStack.order().equals(data.renderOrder)) {
                x = renderedStack.xOrigin() + data.xOffset + totalX;
                y = renderedStack.yOrigin() + data.yOffset + totalY;
                break;
            }
        }
    }

    @Override
    public ParticleTextureSheet getTextureSheet() {
        return textureSheet;
    }
}
