package com.sammy.lodestone.setup;

import net.minecraft.entity.attribute.ClampedEntityAttribute;
import net.minecraft.entity.attribute.EntityAttribute;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.UUID;

import static com.sammy.lodestone.LodestoneLib.MODID;


public class LodestoneAttributeRegistry {
	public static final Map<Identifier, EntityAttribute> ATTRIBUTES = new LinkedHashMap<>();;
	public static final HashMap<EntityAttribute, UUID> UUIDS = new HashMap<>();

	public static EntityAttribute MAGIC_RESISTANCE = register("magic_resistance", new ClampedEntityAttribute("attribute.name.generic." + MODID + ".magic_resistance", 0.0D, 0.0D, 2048.0D).setTracked(true));
	public static EntityAttribute MAGIC_PROFICIENCY = register("magic_resistance", new ClampedEntityAttribute("attribute.name.generic." + MODID + ".magic_proficiency", 0.0D, 0.0D, 2048.0D).setTracked(true));
	public static EntityAttribute MAGIC_DAMAGE = register("magic_resistance", new ClampedEntityAttribute("attribute.name.generic." + MODID + ".magic_damage", 0.0D, 0.0D, 2048.0D).setTracked(true));




	private static <T extends EntityAttribute> EntityAttribute register(String id, EntityAttribute attribute) {
		ATTRIBUTES.put(new Identifier(MODID, id), attribute);
		UUIDS.put(attribute, UUID.randomUUID());
		return attribute;
	}
	public static void init() {
		ATTRIBUTES.forEach((id, attribute) -> Registry.register(Registries.ENTITY_ATTRIBUTE, id, attribute));
	}
}
