package team.lodestar.lodestone.systems.rendering.particle.world;

import com.mojang.blaze3d.vertex.VertexConsumer;
import team.lodestar.lodestone.config.ClientConfig;
import team.lodestar.lodestone.handlers.RenderHandler;
import team.lodestar.lodestone.systems.rendering.particle.LodestoneWorldParticleTextureSheet;
import team.lodestar.lodestone.systems.rendering.particle.SimpleParticleEffect;
import team.lodestar.lodestone.systems.rendering.particle.data.ColorParticleData;
import team.lodestar.lodestone.systems.rendering.particle.data.GenericParticleData;
import team.lodestar.lodestone.systems.rendering.particle.data.SpinParticleData;
import net.fabricmc.fabric.impl.client.particle.FabricSpriteProviderImpl;
import net.minecraft.client.particle.ParticleTextureSheet;
import net.minecraft.client.particle.SpriteBillboardParticle;
import net.minecraft.client.render.Camera;
import net.minecraft.client.util.ColorUtil;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;

import java.awt.*;
import java.util.function.Consumer;

public class GenericParticle extends SpriteBillboardParticle {
	private final ParticleTextureSheet textureSheet;
	protected final FabricSpriteProviderImpl spriteSet;
	protected final SimpleParticleEffect.ParticleSpritePicker spritePicker;
	protected final SimpleParticleEffect.ParticleDiscardFunctionType discardFunctionType;
	protected final ColorParticleData colorData;
	protected final GenericParticleData transparencyData;
	protected final GenericParticleData scaleData;
	protected final SpinParticleData spinData;
	protected final Consumer<GenericParticle> actor;
	private boolean reachedPositiveAlpha;
	private boolean reachedPositiveScale;
    float[] hsv1 = new float[3], hsv2 = new float[3];
    public GenericParticle(ClientWorld world, WorldParticleEffect data, FabricSpriteProviderImpl spriteProvider, double x, double y, double z, double velocityX, double velocityY, double velocityZ) {
        super(world, x, y, z);
		this.textureSheet = data.textureSheet == null ? LodestoneWorldParticleTextureSheet.ADDITIVE : data.textureSheet;
		this.spriteSet = spriteProvider;

		this.spritePicker = data.spritePicker;
		this.discardFunctionType = data.discardFunctionType;
		this.colorData = data.colorData;
		this.transparencyData = data.transparencyData;
		this.scaleData = data.scaleData;
		this.spinData = data.spinData;
		this.actor = data.actor;
		this.angle = data.spinData.spinOffset + data.spinData.startingValue;
		this.velocityX = velocityX;
		this.velocityY = velocityY;
		this.velocityZ = velocityZ;
		this.setMaxAge(data.lifetime);
		this.gravityStrength = data.gravity;
		this.collidesWithWorld = !data.noClip;
		this.velocityMultiplier = 1;
		Color.RGBtoHSB((int) (255 * Math.min(1.0f, colorData.r1)), (int) (255 * Math.min(1.0f, colorData.g1)), (int) (255 * Math.min(1.0f, colorData.b1)), hsv1);
		Color.RGBtoHSB((int) (255 * Math.min(1.0f, colorData.r2)), (int) (255 * Math.min(1.0f, colorData.g2)), (int) (255 * Math.min(1.0f, colorData.b2)), hsv2);

		if (spriteSet != null) {
			if (getSpritePicker().equals(SimpleParticleEffect.ParticleSpritePicker.RANDOM_SPRITE)) {
				setSprite(spriteSet);
			}
			if (getSpritePicker().equals(SimpleParticleEffect.ParticleSpritePicker.FIRST_INDEX) || getSpritePicker().equals(SimpleParticleEffect.ParticleSpritePicker.WITH_AGE)) {
				setSprite(0);
			}
			if (getSpritePicker().equals(SimpleParticleEffect.ParticleSpritePicker.LAST_INDEX)) {
				setSprite(spriteSet.getSprites().size() - 1);
			}
		}
		updateTraits();
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
        return MathHelper.clamp((age * multiplier) / (float)maxAge, 0, 1);
    }
	protected void updateTraits() {
		boolean shouldAttemptRemoval = discardFunctionType == SimpleParticleEffect.ParticleDiscardFunctionType.INVISIBLE;
		if (discardFunctionType == SimpleParticleEffect.ParticleDiscardFunctionType.ENDING_CURVE_INVISIBLE) {

			if (scaleData.getProgress(age, maxAge) > 0.5f || transparencyData.getProgress(age, maxAge) > 0.5f) {
				shouldAttemptRemoval = true;
			}
		}
		if (shouldAttemptRemoval) {
			if ((reachedPositiveAlpha && colorAlpha <= 0) || (reachedPositiveScale && scale <= 0)) {
				markDead();
				return;
			}
		}

		if (!reachedPositiveAlpha && colorAlpha > 0) {
			reachedPositiveAlpha = true;
		}
		if (!reachedPositiveScale && scale > 0) {
			reachedPositiveScale = true;
		}
		pickColor(colorData.colorCurveEasing.ease(colorData.getProgress(age, maxAge), 0, 1, 1));

		scale = scaleData.getValue(age, maxAge);
		colorAlpha = transparencyData.getValue(age, maxAge);
		prevAngle = angle;
		angle += spinData.getValue(age, maxAge);

		if (actor != null) {
			actor.accept(this);
		}
	}

	@Override
    public void tick() {
		updateTraits();
		if (spriteSet != null) {
			if (getSpritePicker().equals(SimpleParticleEffect.ParticleSpritePicker.WITH_AGE)) {
				setSpriteForAge(spriteSet);
			}
		}
		super.tick();
    }

	public Vec3d getParticleVelocity() {
		return new Vec3d(velocityX, velocityY, velocityZ);
	}

	public void setParticleVelocity(Vec3d speed) {
		setVelocity(speed.x, speed.y, speed.z);
	}

    @Override
    public void buildGeometry(VertexConsumer vertexConsumer, Camera camera, float tickDelta) {
		VertexConsumer consumer = vertexConsumer;
		if (ClientConfig.DELAYED_RENDERING && textureSheet instanceof LodestoneWorldParticleTextureSheet shit) {
			consumer = RenderHandler.DELAYED_PARTICLE_RENDER.getBuffer(shit.getRenderLayer());
		}
        super.buildGeometry(consumer, camera, tickDelta);
    }
    @Override
    public ParticleTextureSheet getType() {
        return textureSheet;
    }
}
