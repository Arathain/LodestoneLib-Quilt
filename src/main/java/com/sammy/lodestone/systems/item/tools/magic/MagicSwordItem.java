package com.sammy.lodestone.systems.item.tools.magic;

import com.google.common.collect.ImmutableMultimap;
import com.sammy.lodestone.setup.LodestoneAttributeRegistry;
import com.sammy.lodestone.systems.item.tools.LodestoneSwordItem;
import net.minecraft.entity.attribute.EntityAttribute;
import net.minecraft.entity.attribute.EntityAttributeModifier;
import net.minecraft.item.Item;
import net.minecraft.item.ToolMaterial;

public class MagicSwordItem extends LodestoneSwordItem {
	public final float magicDamage;

	public MagicSwordItem(ToolMaterial material, int attackDamage, float attackSpeed, float magicDamage, Item.Settings settings) {
		super(material, attackDamage, attackSpeed, settings.maxDamage(material.getDurability()));
		this.magicDamage = magicDamage;
	}

	@Override
	public ImmutableMultimap.Builder<EntityAttribute, EntityAttributeModifier> createExtraAttributes() {
		ImmutableMultimap.Builder<EntityAttribute, EntityAttributeModifier> builder = new ImmutableMultimap.Builder<>();
		builder.put(LodestoneAttributeRegistry.MAGIC_DAMAGE, new EntityAttributeModifier(LodestoneAttributeRegistry.UUIDS.get(LodestoneAttributeRegistry.MAGIC_DAMAGE), "Weapon magic damage", magicDamage, EntityAttributeModifier.Operation.ADDITION));
		return builder;
	}
}
