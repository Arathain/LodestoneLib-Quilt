package com.sammy.lodestone.systems.blockentity;

import com.sammy.lodestone.setup.LodestoneBlockEntityRegistry;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.block.entity.SignBlockEntity;
import net.minecraft.util.math.BlockPos;

public class LodestoneSignBlockEntity extends SignBlockEntity {
	public LodestoneSignBlockEntity(BlockPos pos, BlockState state) {
		super(pos, state);
	}

	@Override
	public BlockEntityType<?> getType() {
		return LodestoneBlockEntityRegistry.SIGN;
	}
}
