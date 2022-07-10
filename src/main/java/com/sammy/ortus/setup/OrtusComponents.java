package com.sammy.ortus.setup;

import com.sammy.ortus.component.OrtusEntityComponent;
import com.sammy.ortus.helpers.DataHelper;
import dev.onyxstudios.cca.api.v3.component.ComponentKey;
import dev.onyxstudios.cca.api.v3.component.ComponentRegistry;
import dev.onyxstudios.cca.api.v3.entity.EntityComponentFactoryRegistry;
import dev.onyxstudios.cca.api.v3.entity.EntityComponentInitializer;
import net.minecraft.entity.Entity;

public class OrtusComponents implements EntityComponentInitializer {
	public static final ComponentKey<OrtusEntityComponent> ENTITY_COMPONENT = ComponentRegistry.getOrCreate(DataHelper.prefix("entity"), OrtusEntityComponent.class);

	@Override
	public void registerEntityComponentFactories(EntityComponentFactoryRegistry registry) {
		registry.registerFor(Entity.class, ENTITY_COMPONENT, OrtusEntityComponent::new);

	}
}
