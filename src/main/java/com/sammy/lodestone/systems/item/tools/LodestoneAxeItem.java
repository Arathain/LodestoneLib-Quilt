package com.sammy.lodestone.systems.item.tools;

import com.google.common.collect.ImmutableMultimap;
import com.google.common.collect.Multimap;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.attribute.EntityAttribute;
import net.minecraft.entity.attribute.EntityAttributeModifier;
import net.minecraft.item.AxeItem;
import net.minecraft.item.ToolMaterial;

public class LodestoneAxeItem extends AxeItem {
	private Multimap<EntityAttribute, EntityAttributeModifier> attributes;

	public LodestoneAxeItem(ToolMaterial material, float attackDamage, float attackSpeed, Settings settings) {
		super(material, attackDamage, attackSpeed, settings);
	}

	@Override
	public Multimap<EntityAttribute, EntityAttributeModifier> getAttributeModifiers(EquipmentSlot slot) {
		if (attributes == null) {
			ImmutableMultimap.Builder<EntityAttribute, EntityAttributeModifier> attributeBuilder = new ImmutableMultimap.Builder<>();
			attributeBuilder.putAll(attributeModifiers);
			attributeBuilder.putAll(createExtraAttributes().build());
			attributes = attributeBuilder.build();
		}
		return slot == EquipmentSlot.MAINHAND ? this.attributes : super.getAttributeModifiers(slot);
	}

	public ImmutableMultimap.Builder<EntityAttribute, EntityAttributeModifier> createExtraAttributes() {
		return new ImmutableMultimap.Builder<>();
	}
}
