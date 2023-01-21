package com.sammy.lodestone.systems.item;

import com.google.common.collect.ImmutableMultimap;
import com.google.common.collect.Multimap;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.attribute.EntityAttribute;
import net.minecraft.entity.attribute.EntityAttributeModifier;
import net.minecraft.item.ArmorItem;
import net.minecraft.item.ArmorMaterial;

public abstract class LodestoneArmorItem extends ArmorItem {
	private Multimap<EntityAttribute, EntityAttributeModifier> attributes;

	public LodestoneArmorItem(ArmorMaterial materialIn, EquipmentSlot slot, Settings builder) {
		super(materialIn, slot, builder);
	}

	public ImmutableMultimap.Builder<EntityAttribute, EntityAttributeModifier> createExtraAttributes(EquipmentSlot slot) {
		return new ImmutableMultimap.Builder<>();
	}

	@Override
	public Multimap<EntityAttribute, EntityAttributeModifier> getAttributeModifiers(EquipmentSlot equipmentSlot) {
		if (attributes == null) {
			ImmutableMultimap.Builder<EntityAttribute, EntityAttributeModifier> attributeBuilder = new ImmutableMultimap.Builder<>();
			attributeBuilder.putAll(attributeModifiers);
			attributeBuilder.putAll(createExtraAttributes(slot).build());
			attributes = attributeBuilder.build();
		}
		return equipmentSlot == this.slot ? this.attributes : ImmutableMultimap.of();
	}
}
