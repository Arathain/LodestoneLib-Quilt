package team.lodestar.lodestone.systems.rendering.particle.screen;

import com.mojang.blaze3d.vertex.BufferBuilder;
import team.lodestar.lodestone.handlers.screenparticle.ScreenParticleHandler;
import team.lodestar.lodestone.systems.rendering.particle.SimpleParticleEffect;
import team.lodestar.lodestone.systems.rendering.particle.data.ColorParticleData;
import team.lodestar.lodestone.systems.rendering.particle.data.GenericParticleData;
import team.lodestar.lodestone.systems.rendering.particle.data.SpinParticleData;
import team.lodestar.lodestone.systems.rendering.particle.screen.base.SpriteBillboardScreenParticle;
import net.fabricmc.fabric.impl.client.particle.FabricSpriteProviderImpl;
import net.minecraft.client.util.ColorUtil;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;

import java.awt.*;
import java.util.function.Consumer;

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
	private final boolean tracksStack;
	private double stackTrackXOffset;
	private double stackTrackYOffset;

	private boolean reachedPositiveAlpha;
	private boolean reachedPositiveScale;

	float[] hsv1 = new float[3], hsv2 = new float[3];

	public GenericScreenParticle(ClientWorld world, ScreenParticleEffect options, FabricSpriteProviderImpl spriteSet, double x, double y, double xMotion, double yMotion) {
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
		this.tracksStack = options.tracksStack;
		this.stackTrackXOffset = options.stackTrackXOffset;
		this.stackTrackYOffset = options.stackTrackYOffset;
		this.angle = options.spinData.spinOffset + options.spinData.startingValue;
		this.velocityX = xMotion;
		this.velocityY = yMotion;

		this.setMaxAge(options.lifetime);
		this.gravity = options.gravity;
		this.friction = 1;
		Color.RGBtoHSB((int) (255 * Math.min(1.0f, colorData.r1)), (int) (255 * Math.min(1.0f, colorData.g1)), (int) (255 * Math.min(1.0f, colorData.b1)), hsv1);
		Color.RGBtoHSB((int) (255 * Math.min(1.0f, colorData.r2)), (int) (255 * Math.min(1.0f, colorData.g2)), (int) (255 * Math.min(1.0f, colorData.b2)), hsv2);
		updateTraits();
		if (getSpritePicker().equals(SimpleParticleEffect.ParticleSpritePicker.RANDOM_SPRITE)) {
			pickSprite(spriteSet);
		}
		if (getSpritePicker().equals(SimpleParticleEffect.ParticleSpritePicker.FIRST_INDEX) || getSpritePicker().equals(SimpleParticleEffect.ParticleSpritePicker.WITH_AGE)) {
			pickSprite(0);
		}
		if (getSpritePicker().equals(SimpleParticleEffect.ParticleSpritePicker.LAST_INDEX)) {
			pickSprite(spriteSet.getSprites().size() - 1);
		}
		updateTraits();
	}

	public SimpleParticleEffect.ParticleSpritePicker getSpritePicker() {
		return spritePicker;
	}

	public void pickSprite(int spriteIndex) {
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
		if (discardFunctionType == SimpleParticleEffect.ParticleDiscardFunctionType.ENDING_CURVE_INVISIBLE) {

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
	public void render(BufferBuilder bufferBuilder) {
		if (tracksStack) {
			x = ScreenParticleHandler.currentItemX+stackTrackXOffset+xMoved;
			y = ScreenParticleHandler.currentItemY+stackTrackYOffset+yMoved;
		}
		super.render(bufferBuilder);
	}

	@Override
	public void tick() {
		updateTraits();
		if (getSpritePicker().equals(SimpleParticleEffect.ParticleSpritePicker.WITH_AGE)) {
			setSpriteFromAge(spriteSet);
		}
		super.tick();
	}

	@Override
	public LodestoneScreenParticleTextureSheet getRenderType() {
		return renderType;
	}

	public void setParticleSpeed(Vec3d speed) {
		setParticleSpeed(speed.x, speed.y);
	}
}
