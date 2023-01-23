package com.sammy.lodestone.systems.entity;

import net.minecraft.entity.EntityType;
import net.minecraft.entity.vehicle.BoatEntity;
import net.minecraft.item.Item;
import net.minecraft.world.World;

public class LodestoneBoatEntity extends BoatEntity {
	private final Item boatItem;
	private final Item plankItem;

	public LodestoneBoatEntity(EntityType<? extends BoatEntity> entityType, World world, Item boatItem, Item plankItem) {
		super(entityType, world);
		this.boatItem = boatItem;
		this.plankItem = plankItem;
	}
}
