package com.sammy.lodestone.systems.particle.type;

import com.mojang.serialization.Codec;
import com.sammy.lodestone.systems.particle.world.GenericParticle;
import com.sammy.lodestone.systems.particle.world.WorldParticleEffect;
import net.minecraft.client.particle.Particle;
import net.minecraft.client.particle.ParticleFactory;
import net.minecraft.client.particle.ParticleManager;
import net.minecraft.client.particle.SpriteProvider;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.particle.ParticleType;

public class LodestoneParticleType extends ParticleType<WorldParticleEffect> {
	public LodestoneParticleType() {
		super(false, WorldParticleEffect.DESERIALIZER);
	}

	@Override
	public Codec<WorldParticleEffect> getCodec() {
		return WorldParticleEffect.codecFor(this);
	}

	public static class Factory implements ParticleFactory<WorldParticleEffect> {
		private final SpriteProvider sprite;

		public Factory(SpriteProvider sprite) {
			this.sprite = sprite;
		}

		@Override
		public Particle createParticle(WorldParticleEffect data, ClientWorld world, double x, double y, double z, double mx, double my, double mz) {
			return new GenericParticle(world, data, (ParticleManager.SimpleSpriteProvider) sprite, x, y, z, mx, my, mz);
		}
	}
}
