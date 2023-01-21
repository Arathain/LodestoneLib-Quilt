package com.sammy.lodestone.systems.blockentity;


import net.minecraft.block.BlockState;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class BlockTickHelper {


	public static <B extends LodestoneBlockEntity> void tickSided(World world, BlockPos pos, BlockState state, B blockEntity) {
		blockEntity.tick();
		if (world.isClient()) {
			blockEntity.clientTick();
		} else {
			blockEntity.tick();
		}
	}
}
