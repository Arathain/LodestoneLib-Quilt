package com.sammy.lodestone.systems.multiblock;

import net.minecraft.util.math.BlockPos;

public interface IModularMultiBlockCore extends IMultiBlockCore {

	@Override
	default boolean isModular() {
		return true;
	}

	void separate(BlockPos pos);

	void connect(BlockPos pos);

	void reset();
}
