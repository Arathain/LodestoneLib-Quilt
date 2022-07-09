package com.sammy.ortus.setup;

import com.sammy.ortus.helpers.DataHelper;
import com.sammy.ortus.mixin.FabricSpriteProviderImplAccessor;
import com.sammy.ortus.systems.rendering.particle.screen.ScreenParticleEffect;
import com.sammy.ortus.systems.rendering.particle.screen.ScreenParticleType;
import com.sammy.ortus.systems.rendering.particle.type.OrtusScreenParticleType;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.particle.SpriteProvider;
import net.minecraft.util.Identifier;

import java.util.ArrayList;

public class OrtusScreenParticles {
	public static final ArrayList<ScreenParticleType<?>> PARTICLE_TYPES = new ArrayList<>();
	public static final ScreenParticleType<ScreenParticleEffect> WISP = registerType(new OrtusScreenParticleType());
	public static final ScreenParticleType<ScreenParticleEffect> SMOKE = registerType(new OrtusScreenParticleType());
	public static final ScreenParticleType<ScreenParticleEffect> SPARKLE = registerType(new OrtusScreenParticleType());
	public static final ScreenParticleType<ScreenParticleEffect> TWINKLE = registerType(new OrtusScreenParticleType());
	public static final ScreenParticleType<ScreenParticleEffect> STAR = registerType(new OrtusScreenParticleType());

	public static void registerParticleFactories() {
		registerProvider(WISP, new OrtusScreenParticleType.Factory(getSpriteSet(DataHelper.prefix("wisp"))));
		registerProvider(SMOKE, new OrtusScreenParticleType.Factory(getSpriteSet(DataHelper.prefix("smoke"))));
		registerProvider(SPARKLE, new OrtusScreenParticleType.Factory(getSpriteSet(DataHelper.prefix("sparkle"))));
		registerProvider(TWINKLE, new OrtusScreenParticleType.Factory(getSpriteSet(DataHelper.prefix("twinkle"))));
		registerProvider(STAR, new OrtusScreenParticleType.Factory(getSpriteSet(DataHelper.prefix("star"))));
	}

	public static <T extends ScreenParticleEffect> ScreenParticleType<T> registerType(ScreenParticleType<T> type) {
		PARTICLE_TYPES.add(type);
		return type;
	}

	public static <T extends ScreenParticleEffect> void registerProvider(ScreenParticleType<T> type, ScreenParticleType.Factory<T> provider) {
		type.factory = provider;
	}

	public static SpriteProvider getSpriteSet(Identifier resourceLocation) {
		final MinecraftClient client = MinecraftClient.getInstance();
		return FabricSpriteProviderImplAccessor.FabricSpriteProviderImpl(client.particleManager, client.particleManager.spriteAwareFactories.get(resourceLocation));
	}

}
