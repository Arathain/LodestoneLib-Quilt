package com.sammy.lodestone.systems.item.tools;

import com.google.common.collect.ImmutableMultimap;
import com.google.common.collect.Multimap;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.attribute.EntityAttribute;
import net.minecraft.entity.attribute.EntityAttributeModifier;
import net.minecraft.item.SwordItem;
import net.minecraft.item.ToolMaterial;

public class LodestoneSwordItem extends SwordItem {
	private Multimap<EntityAttribute, EntityAttributeModifier> attributes;

	public LodestoneSwordItem(ToolMaterial toolMaterial, int attackDamage, float attackSpeed, Settings settings) {
		super(toolMaterial, attackDamage + 3, attackSpeed - 2.4f, settings.maxDamage(toolMaterial.getDurability()));
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
