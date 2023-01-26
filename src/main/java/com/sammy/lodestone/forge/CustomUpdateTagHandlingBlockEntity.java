package com.sammy.lodestone.forge;

import net.minecraft.block.entity.BlockEntity;
import net.minecraft.nbt.NbtCompound;

public interface CustomUpdateTagHandlingBlockEntity {
	default void handleUpdateTag(NbtCompound nbtCompound) {
		((BlockEntity) this).readNbt(nbtCompound);
	}
}
