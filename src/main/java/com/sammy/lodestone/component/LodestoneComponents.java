package com.sammy.lodestone.component;

import com.sammy.lodestone.LodestoneLib;
import dev.onyxstudios.cca.api.v3.component.ComponentKey;
import dev.onyxstudios.cca.api.v3.component.ComponentRegistry;
import dev.onyxstudios.cca.api.v3.entity.EntityComponentFactoryRegistry;
import dev.onyxstudios.cca.api.v3.entity.EntityComponentInitializer;
import dev.onyxstudios.cca.api.v3.entity.RespawnCopyStrategy;
import dev.onyxstudios.cca.api.v3.world.WorldComponentFactoryRegistry;
import dev.onyxstudios.cca.api.v3.world.WorldComponentInitializer;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;

public class LodestoneComponents implements WorldComponentInitializer, EntityComponentInitializer {
	public static final ComponentKey<LodestoneWorldComponent> LODESTONE_WORLD_COMPONENT = ComponentRegistry.getOrCreate(LodestoneLib.id("world_event"), LodestoneWorldComponent.class);
	public static final ComponentKey<LodestonePlayerComponent> PLAYER_COMPONENT = ComponentRegistry.getOrCreate(LodestoneLib.id("player"), LodestonePlayerComponent.class);


	@Override
	public void registerWorldComponentFactories(WorldComponentFactoryRegistry registry) {
		registry.register(LODESTONE_WORLD_COMPONENT, LodestoneWorldComponent::new);
	}

	@Override
	public void registerEntityComponentFactories(EntityComponentFactoryRegistry registry) {
		registry.beginRegistration(PlayerEntity.class, PLAYER_COMPONENT).respawnStrategy(RespawnCopyStrategy.ALWAYS_COPY).end(LodestonePlayerComponent::new);

	}
}
