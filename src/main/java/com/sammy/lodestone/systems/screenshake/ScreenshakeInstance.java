package com.sammy.lodestone.systems.screenshake;

import com.sammy.lodestone.systems.rendering.particle.Easing;
import net.minecraft.client.render.Camera;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.random.RandomGenerator;


public class ScreenshakeInstance {
	public int progress;
	public final int duration;
	public float intensity1, intensity2, intensity3;
	public Easing intensityCurveStartEasing = Easing.LINEAR, intensityCurveEndEasing = Easing.LINEAR;

	public ScreenshakeInstance(int duration) {
		this.duration = duration;
	}

	public ScreenshakeInstance setIntensity(float intensity) {
		return setIntensity(intensity, intensity);
	}

	public ScreenshakeInstance setIntensity(float intensity1, float intensity2) {
		return setIntensity(intensity1, intensity2, intensity2);
	}

	public ScreenshakeInstance setIntensity(float intensity1, float intensity2, float intensity3) {
		this.intensity1 = intensity1;
		this.intensity2 = intensity2;
		this.intensity3 = intensity3;
		return this;
	}

	public ScreenshakeInstance setEasing(Easing easing) {
		return setEasing(easing, easing);
	}

	public ScreenshakeInstance setEasing(Easing intensityCurveStartEasing, Easing intensityCurveEndEasing) {
		this.intensityCurveStartEasing = intensityCurveStartEasing;
		this.intensityCurveEndEasing = intensityCurveEndEasing;
		return this;
	}

	public float updateIntensity(Camera camera, RandomGenerator random) {
		progress++;
		float percentage = progress / (float) duration;
		if (intensity2 != intensity3) {
			if (percentage >= 0.5f) {
				return MathHelper.lerp(intensityCurveEndEasing.ease(percentage - 0.5f, 0, 1, 0.5f), intensity2, intensity1);
			} else {
				return MathHelper.lerp(intensityCurveStartEasing.ease(percentage, 0, 1, 0.5f), intensity1, intensity2);
			}
		} else {
			return MathHelper.lerp(intensityCurveStartEasing.ease(percentage, 0, 1, 1), intensity1, intensity2);
		}
	}
}
