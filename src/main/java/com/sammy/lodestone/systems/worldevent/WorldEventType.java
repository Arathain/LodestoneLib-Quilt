package com.sammy.lodestone.systems.worldevent;

import net.minecraft.nbt.NbtCompound;

public class WorldEventType {

	public final String id;
	public final EventInstanceSupplier supplier;

	public WorldEventType(String id, EventInstanceSupplier supplier) {
		this.id = id;
		this.supplier = supplier;
	}

	public WorldEventInstance createInstance(NbtCompound nbt) {
		return supplier.getInstance().readFromNbtInstance(nbt);
	}

	public interface EventInstanceSupplier {
		WorldEventInstance getInstance();
	}
}
