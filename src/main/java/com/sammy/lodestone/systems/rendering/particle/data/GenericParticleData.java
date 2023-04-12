package com.sammy.lodestone.systems.rendering.particle.data;

import com.sammy.lodestone.systems.rendering.particle.Easing;
import net.minecraft.util.math.MathHelper;

public class GenericParticleData {
	public final float startingValue, middleValue, endingValue;
	public final float coefficient;
	public final Easing startToMiddleEasing, middleToEndEasing;

	protected GenericParticleData(float startingValue, float middleValue, float endingValue, float coefficient, Easing startToMiddleEasing, Easing middleToEndEasing) {
		this.startingValue = startingValue;
		this.middleValue = middleValue;
		this.endingValue = endingValue;
		this.coefficient = coefficient;
		this.startToMiddleEasing = startToMiddleEasing;
		this.middleToEndEasing = middleToEndEasing;
	}

	public boolean isTrinary() {
		return endingValue != -1;
	}

	public float getProgress(float age, float lifetime) {
		return MathHelper.clamp((age * coefficient) / lifetime, 0, 1);
	}

	public float getValue(float age, float lifetime) {
		float progress = getProgress(age, lifetime);
		if (isTrinary()) {
			if (progress >= 0.5f) {
				return MathHelper.lerp(middleToEndEasing.ease(progress - 0.5f, 0, 1, 0.5f), middleValue, endingValue);
			} else {
				return MathHelper.lerp(startToMiddleEasing.ease(progress, 0, 1, 0.5f), startingValue, middleValue);
			}
		} else {
			return MathHelper.lerp(startToMiddleEasing.ease(progress, 0, 1, 1), startingValue, middleValue);
		}
	}

	public static GenericParticleDataBuilder create(float value) {
		return new GenericParticleDataBuilder(value, value, -1);
	}

	public static GenericParticleDataBuilder create(float startingValue, float endingValue) {
		return new GenericParticleDataBuilder(startingValue, endingValue, -1);
	}

	public static GenericParticleDataBuilder create(float startingValue, float middleValue, float endingValue) {
		return new GenericParticleDataBuilder(startingValue, middleValue, endingValue);
	}

	public static class GenericParticleDataBuilder {
		protected final float startingValue, middleValue, endingValue;
		protected float coefficient = 1f;
		protected Easing startToMiddleEasing = Easing.LINEAR, middleToEndEasing = Easing.LINEAR;

		protected GenericParticleDataBuilder(float startingValue, float middleValue, float endingValue) {
			this.startingValue = startingValue;
			this.middleValue = middleValue;
			this.endingValue = endingValue;
		}

		public GenericParticleDataBuilder setCoefficient(float coefficient) {
			this.coefficient = coefficient;
			return this;
		}

		public GenericParticleDataBuilder setEasing(Easing easing) {
			this.startToMiddleEasing = easing;
			return this;
		}

		public GenericParticleDataBuilder setEasing(Easing easing, Easing middleToEndEasing) {
			this.startToMiddleEasing = easing;
			this.middleToEndEasing = easing;
			return this;
		}


		public GenericParticleData build() {
			return new GenericParticleData(startingValue, middleValue, endingValue, coefficient, startToMiddleEasing, middleToEndEasing);
		}
	}
}
