package com.sammy.lodestone.systems.item.tools;

import com.google.common.collect.ImmutableMultimap;
import com.google.common.collect.Multimap;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.attribute.EntityAttribute;
import net.minecraft.entity.attribute.EntityAttributeModifier;
import net.minecraft.item.HoeItem;
import net.minecraft.item.ToolMaterial;

public class LodestoneHoeItem extends HoeItem {
	private Multimap<EntityAttribute, EntityAttributeModifier> attributes;

	public LodestoneHoeItem(ToolMaterial material, int attackDamage, float attackSpeed, Settings settings) {
		super(material, attackDamage - 2, attackSpeed, settings.maxDamage(material.getDurability()));
	}

	@Override
	public Multimap<EntityAttribute, EntityAttributeModifier> getAttributeModifiers(EquipmentSlot slot) {
		if (attributes == null) {
			ImmutableMultimap.Builder<EntityAttribute, EntityAttributeModifier> attributeBuilder = new ImmutableMultimap.Builder<>();
			attributeBuilder.putAll(attributeModifiers);
			attributeBuilder.putAll(createExtraAttributes().build());
			attributes = attributeBuilder.build();
		}
		return super.getAttributeModifiers(slot);
	}

	public ImmutableMultimap.Builder<EntityAttribute, EntityAttributeModifier> createExtraAttributes() {
		return new ImmutableMultimap.Builder<>();
	}
}
