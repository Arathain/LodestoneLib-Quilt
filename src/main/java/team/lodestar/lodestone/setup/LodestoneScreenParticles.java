package team.lodestar.lodestone.setup;

import team.lodestar.lodestone.helpers.DataHelper;
import team.lodestar.lodestone.mixin.FabricSpriteProviderImplAccessor;
import team.lodestar.lodestone.systems.rendering.particle.screen.ScreenParticleEffect;
import team.lodestar.lodestone.systems.rendering.particle.screen.ScreenParticleType;
import team.lodestar.lodestone.systems.rendering.particle.type.LodestoneScreenParticleType;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.particle.SpriteProvider;
import net.minecraft.util.Identifier;

import java.util.ArrayList;

public class LodestoneScreenParticles {
	public static final ArrayList<ScreenParticleType<?>> PARTICLE_TYPES = new ArrayList<>();
	public static final ScreenParticleType<ScreenParticleEffect> WISP = registerType(new LodestoneScreenParticleType());
	public static final ScreenParticleType<ScreenParticleEffect> SMOKE = registerType(new LodestoneScreenParticleType());
	public static final ScreenParticleType<ScreenParticleEffect> SPARKLE = registerType(new LodestoneScreenParticleType());
	public static final ScreenParticleType<ScreenParticleEffect> TWINKLE = registerType(new LodestoneScreenParticleType());
	public static final ScreenParticleType<ScreenParticleEffect> STAR = registerType(new LodestoneScreenParticleType());

	public static void registerParticleFactories() {
		registerProvider(WISP, new LodestoneScreenParticleType.Factory(getSpriteSet(DataHelper.prefix("wisp"))));
		registerProvider(SMOKE, new LodestoneScreenParticleType.Factory(getSpriteSet(DataHelper.prefix("smoke"))));
		registerProvider(SPARKLE, new LodestoneScreenParticleType.Factory(getSpriteSet(DataHelper.prefix("sparkle"))));
		registerProvider(TWINKLE, new LodestoneScreenParticleType.Factory(getSpriteSet(DataHelper.prefix("twinkle"))));
		registerProvider(STAR, new LodestoneScreenParticleType.Factory(getSpriteSet(DataHelper.prefix("star"))));
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
