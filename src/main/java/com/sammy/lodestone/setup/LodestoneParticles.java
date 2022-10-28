package com.sammy.lodestone.setup;

import com.sammy.lodestone.helpers.DataHelper;
import com.sammy.lodestone.systems.rendering.particle.type.LodestoneParticleType;
import net.fabricmc.fabric.api.client.particle.v1.ParticleFactoryRegistry;
import net.minecraft.particle.ParticleType;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;

import java.util.function.BiConsumer;

public class LodestoneParticles {
	public static final LodestoneParticleType WISP_PARTICLE = new LodestoneParticleType();
	public static final LodestoneParticleType SMOKE_PARTICLE = new LodestoneParticleType();
	public static final LodestoneParticleType SPARKLE_PARTICLE = new LodestoneParticleType();
	public static final LodestoneParticleType TWINKLE_PARTICLE = new LodestoneParticleType();
	public static final LodestoneParticleType STAR_PARTICLE = new LodestoneParticleType();

	public static void init() {
		initParticles(bind(Registry.PARTICLE_TYPE));
	}

	public static void registerFactories() {
		ParticleFactoryRegistry.getInstance().register(WISP_PARTICLE, LodestoneParticleType.Factory::new);
		ParticleFactoryRegistry.getInstance().register(SMOKE_PARTICLE, LodestoneParticleType.Factory::new);
		ParticleFactoryRegistry.getInstance().register(SPARKLE_PARTICLE, LodestoneParticleType.Factory::new);
		ParticleFactoryRegistry.getInstance().register(TWINKLE_PARTICLE, LodestoneParticleType.Factory::new);
		ParticleFactoryRegistry.getInstance().register(STAR_PARTICLE, LodestoneParticleType.Factory::new);
	}
	// shamelessly stolen from Botania
	private static void initParticles(BiConsumer<ParticleType<?>, Identifier> registry) {
		registry.accept(WISP_PARTICLE, DataHelper.prefix("wisp"));
		registry.accept(SMOKE_PARTICLE, DataHelper.prefix("smoke"));
		registry.accept(SPARKLE_PARTICLE, DataHelper.prefix("sparkle"));
		registry.accept(TWINKLE_PARTICLE, DataHelper.prefix("twinkle"));
		registry.accept(STAR_PARTICLE, DataHelper.prefix("star"));
	}
	// guess where this one comes from
	private static <T> BiConsumer<T, Identifier> bind(Registry<? super T> registry) {
		return (t, id) -> Registry.register(registry, id, t);
	}
}

