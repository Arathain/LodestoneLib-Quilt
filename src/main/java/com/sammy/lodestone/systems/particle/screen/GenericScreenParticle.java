package com.sammy.lodestone.systems.particle.screen;

import com.sammy.lodestone.systems.particle.SimpleParticleEffect;
import com.sammy.lodestone.systems.particle.data.ColorParticleData;
import com.sammy.lodestone.systems.particle.data.GenericParticleData;
import com.sammy.lodestone.systems.particle.data.SpinParticleData;
import com.sammy.lodestone.systems.particle.screen.base.SpriteBillboardScreenParticle;
import net.fabricmc.fabric.impl.client.particle.FabricSpriteProviderImpl;
import net.minecraft.client.particle.ParticleManager;
import net.minecraft.client.util.ColorUtil;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;
import org.joml.Vector3d;

import java.awt.*;
import java.util.function.Consumer;

import static com.sammy.lodestone.systems.particle.SimpleParticleEffect.ParticleDiscardFunctionType.ENDING_CURVE_INVISIBLE;
import static com.sammy.lodestone.systems.particle.SimpleParticleEffect.ParticleSpritePicker.*;

public class GenericScreenParticle extends SpriteBillboardScreenParticle {
	private final LodestoneScreenParticleTextureSheet renderType;
	protected final FabricSpriteProviderImpl spriteSet;
	protected final SimpleParticleEffect.ParticleSpritePicker spritePicker;
	protected final SimpleParticleEffect.ParticleDiscardFunctionType discardFunctionType;
	protected final ColorParticleData colorData;
	protected final GenericParticleData transparencyData;
	protected final GenericParticleData scaleData;
	protected final SpinParticleData spinData;
	protected final Consumer<GenericScreenParticle> actor;

	private boolean reachedPositiveAlpha;
	private boolean reachedPositiveScale;

	float[] hsv1 = new float[3], hsv2 = new float[3];

	public GenericScreenParticle(World world, ScreenParticleEffect options, FabricSpriteProviderImpl spriteSet, double x, double y, double xMotion, double yMotion) {
		super(world, x, y);
		this.renderType = options.renderType;
		this.spriteSet = spriteSet;
		this.spritePicker = options.spritePicker;
		this.discardFunctionType = options.discardFunctionType;
		this.colorData = options.colorData;
		this.transparencyData = options.transparencyData;
		this.scaleData = options.scaleData;
		this.spinData = options.spinData;
		this.actor = options.actor;
		this.angle = options.spinData.spinOffset + options.spinData.startingValue;
		this.velocityX = xMotion;
		this.velocityY = yMotion;

		this.setMaxAge(options.lifetime);
		this.gravityStrength = options.gravity;
		this.velocityMultiplier = 1;
		Color.RGBtoHSB((int) (255 * Math.min(1.0f, colorData.r1)), (int) (255 * Math.min(1.0f, colorData.g1)), (int) (255 * Math.min(1.0f, colorData.b1)), hsv1);
		Color.RGBtoHSB((int) (255 * Math.min(1.0f, colorData.r2)), (int) (255 * Math.min(1.0f, colorData.g2)), (int) (255 * Math.min(1.0f, colorData.b2)), hsv2);
		updateTraits();
		if (getSpritePicker().equals(RANDOM_SPRITE)) {
			setSprite(spriteSet);
		}
		if (getSpritePicker().equals(FIRST_INDEX) || getSpritePicker().equals(WITH_AGE)) {
			setSprite(0);
		}
		if (getSpritePicker().equals(LAST_INDEX)) {
			setSprite(spriteSet.getSprites().size() - 1);
		}
		updateTraits();
	}

	public SimpleParticleEffect.ParticleSpritePicker getSpritePicker() {
		return spritePicker;
	}

	public void setSprite(int spriteIndex) {
		if (spriteIndex < spriteSet.getSprites().size() && spriteIndex >= 0) {
			setSprite(spriteSet.getSprites().get(spriteIndex));
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
		boolean shouldAttemptRemoval = discardFunctionType == SimpleParticleEffect.ParticleDiscardFunctionType.INVISIBLE;
		if (discardFunctionType == ENDING_CURVE_INVISIBLE) {

			if (scaleData.getProgress(age, maxAge) > 0.5f || transparencyData.getProgress(age, maxAge) > 0.5f) {
				shouldAttemptRemoval = true;
			}
		}
		if (shouldAttemptRemoval) {
			if ((reachedPositiveAlpha && alpha <= 0) || (reachedPositiveScale && quadSize <= 0)) {
				remove();
				return;
			}
		}

		if (!reachedPositiveAlpha && alpha > 0) {
			reachedPositiveAlpha = true;
		}
		if (!reachedPositiveScale && quadSize > 0) {
			reachedPositiveScale = true;
		}
		pickColor(colorData.colorCurveEasing.ease(colorData.getProgress(age, maxAge), 0, 1, 1));

		quadSize = scaleData.getValue(age, maxAge);
		alpha = transparencyData.getValue(age, maxAge);
		prevAngle = angle;
		angle += spinData.getValue(age, maxAge);

		if (actor != null) {
			actor.accept(this);
		}
	}

	@Override
	public void tick() {
		updateTraits();
		if (getSpritePicker().equals(WITH_AGE)) {
			setSpriteForAge(spriteSet);
		}
		super.tick();
	}

	@Override
	public LodestoneScreenParticleTextureSheet getTextureSheet() {
		return renderType;
	}

	public void setParticleSpeed(Vector3d speed) {
		setParticleSpeed(speed.x, speed.y);
	}
}
