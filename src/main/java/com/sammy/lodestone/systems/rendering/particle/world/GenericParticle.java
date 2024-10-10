package com.sammy.lodestone.systems.rendering.particle.world;

import com.mojang.blaze3d.vertex.VertexConsumer;
import com.sammy.lodestone.config.ClientConfig;
import com.sammy.lodestone.handlers.RenderHandler;
import com.sammy.lodestone.setup.LodestoneRenderLayers;
import com.sammy.lodestone.systems.rendering.particle.ParticleTextureSheets;
import com.sammy.lodestone.systems.rendering.particle.SimpleParticleEffect;
import net.fabricmc.fabric.impl.client.particle.FabricSpriteProviderImpl;
import net.minecraft.client.particle.ParticleTextureSheet;
import net.minecraft.client.particle.SpriteBillboardParticle;
import net.minecraft.client.render.Camera;
import net.minecraft.client.util.ColorUtil;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3f;

import java.awt.*;
import java.util.function.Consumer;

public class GenericParticle extends SpriteBillboardParticle {
    protected WorldParticleEffect data;
    private final ParticleTextureSheet textureSheet;
    protected final FabricSpriteProviderImpl spriteProvider;
	private final Vec3f startingVelocity;
	private boolean reachedPositiveAlpha;
	private boolean reachedPositiveScale;
	public Consumer<GenericParticle> actor;
    float[] hsv1 = new float[3], hsv2 = new float[3];
    public GenericParticle(ClientWorld world, WorldParticleEffect data, FabricSpriteProviderImpl spriteProvider, double x, double y, double z, double velocityX, double velocityY, double velocityZ) {
        super(world, x, y, z);
        this.data = data;
        this.textureSheet = data.textureSheet;
        this.spriteProvider = spriteProvider;
        this.angle = data.spinOffset + data.spin1;
		this.actor = data.actor;
        if (!data.forcedMotion) {
            this.velocityX = velocityX;
            this.velocityY = velocityY;
            this.velocityZ = velocityZ;
        }
        this.setMaxAge(data.lifetime);
        this.gravityStrength = data.gravity;
        this.collidesWithWorld = !data.noClip;
        this.velocityMultiplier = 1;
		this.startingVelocity = data.motionStyle == SimpleParticleEffect.MotionStyle.START_TO_END ? data.startingVelocity : new Vec3f((float)velocityX, (float)velocityY, (float)velocityZ);
		Color.RGBtoHSB((int) (255 * Math.min(1.0f, data.r1)), (int) (255 * Math.min(1.0f, data.g1)), (int) (255 * Math.min(1.0f, data.b1)), hsv1);
        Color.RGBtoHSB((int) (255 * Math.min(1.0f, data.r2)), (int) (255 * Math.min(1.0f, data.g2)), (int) (255 * Math.min(1.0f, data.b2)), hsv2);
		if (spriteProvider != null) {
			if (getAnimator().equals(SimpleParticleEffect.Animator.RANDOM_SPRITE)) {
				setSprite(spriteProvider);
			}
			if (getAnimator().equals(SimpleParticleEffect.Animator.FIRST_INDEX) || getAnimator().equals(SimpleParticleEffect.Animator.WITH_AGE)) {
				setSprite(0);
			}
			if (getAnimator().equals(SimpleParticleEffect.Animator.LAST_INDEX)) {
				setSprite(spriteProvider.getSprites().size() - 1);
			}
		}
		updateTraits();
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
        return MathHelper.clamp((age * multiplier) / (float)maxAge, 0, 1);
    }
    public SimpleParticleEffect.Animator getAnimator() {
        return data.animator;
    }
    protected void updateTraits() {
		if (data.removalProtocol == SimpleParticleEffect.SpecialRemovalProtocol.INVISIBLE ||
				(data.removalProtocol == SimpleParticleEffect.SpecialRemovalProtocol.ENDING_CURVE_INVISIBLE && (getCurve(data.scaleCoefficient) > 0.5f || getCurve(data.alphaCoefficient) > 0.5f))) {
			if ((reachedPositiveAlpha && colorAlpha <= 0) || (reachedPositiveScale && scale <= 0)) {
				markDead();
				return;
			}
		}
		if (colorAlpha > 0) {
			reachedPositiveAlpha = true;
		}
		if (scale > 0) {
			reachedPositiveScale = true;
		}
		pickColor(data.colorCurveEasing.ease(getCurve(data.colorCoefficient), 0, 1, 1));
		if (data.isTrinaryScale()) {
			float trinaryAge = getCurve(data.scaleCoefficient);
			if (trinaryAge >= 0.5f) {
				scale = MathHelper.lerp(data.scaleCurveEndEasing.ease(trinaryAge - 0.5f, 0, 1, 0.5f), data.scale2, data.scale3);
			} else {
				scale = MathHelper.lerp(data.scaleCurveStartEasing.ease(trinaryAge, 0, 1, 0.5f), data.scale1, data.scale2);
			}
		} else {
			scale = MathHelper.lerp(data.scaleCurveStartEasing.ease(getCurve(data.scaleCoefficient), 0, 1, 1), data.scale1, data.scale2);
		}
		if (data.isTrinaryAlpha()) {
			float trinaryAge = getCurve(data.alphaCoefficient);
			if (trinaryAge >= 0.5f) {
				colorAlpha = MathHelper.lerp(data.alphaCurveStartEasing.ease(trinaryAge - 0.5f, 0, 1, 0.5f), data.alpha2, data.alpha3);
			} else {
				colorAlpha = MathHelper.lerp(data.alphaCurveStartEasing.ease(trinaryAge, 0, 1, 0.5f), data.alpha1, data.alpha2);
			}
		} else {
			colorAlpha = MathHelper.lerp(data.alphaCurveStartEasing.ease(getCurve(data.alphaCoefficient), 0, 1, 1), data.alpha1, data.alpha2);
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
			Vec3f currentMotion = data.motionStyle == SimpleParticleEffect.MotionStyle.START_TO_END ? startingVelocity : new Vec3f((float) velocityX, (float) velocityY, (float) velocityZ);
			velocityX = MathHelper.lerp(data.motionEasing.ease(motionAge, 0, 1, 1), currentMotion.getX(), data.endingMotion.getX());
			velocityY = MathHelper.lerp(data.motionEasing.ease(motionAge, 0, 1, 1), currentMotion.getY(), data.endingMotion.getY());
			velocityZ = MathHelper.lerp(data.motionEasing.ease(motionAge, 0, 1, 1), currentMotion.getZ(), data.endingMotion.getZ());
		} else {
			velocityX *= data.motionCoefficient;
			velocityY *= data.motionCoefficient;
			velocityZ *= data.motionCoefficient;
		}
		if (actor != null) {
			actor.accept(this);
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
		VertexConsumer consumer = vertexConsumer;
		if (ClientConfig.DELAYED_RENDERING) {
			if (getType().equals(ParticleTextureSheets.ADDITIVE)) {
				consumer = RenderHandler.DELAYED_RENDER.getBuffer(LodestoneRenderLayers.ADDITIVE_PARTICLE);
			}
			if (getType().equals(ParticleTextureSheets.TRANSPARENT)) {
				consumer = RenderHandler.DELAYED_RENDER.getBuffer(LodestoneRenderLayers.TRANSPARENT_PARTICLE);
			}
		}
        super.buildGeometry(consumer, camera, tickDelta);
    }
    @Override
    public ParticleTextureSheet getType() {
        return textureSheet;
    }
}
