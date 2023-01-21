package com.sammy.lodestone.systems.multiblock;


import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3i;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;

public interface IMultiBlockCore {
	ArrayList<BlockPos> getComponentPositions();

	@Nullable
	MultiBlockStructure getStructure();

	default void setupMultiblock(BlockPos pos) {
		if (getStructure() == null) {
			return;
		}
		getStructure().structurePieces.forEach(p -> {
			Vec3i offset = p.offset;
			getComponentPositions().add(pos.add(offset));
		});
	}

	default boolean isModular() {
		return false;
	}

	default void destroyMultiblock(@Nullable PlayerEntity player, World world, BlockPos pos) {
		if (isModular()) {
			return;
		}
		getComponentPositions().forEach(p -> {
			if (world.getBlockEntity(p) instanceof MultiBlockComponentEntity) {
				world.breakBlock(p, false);
			}
		});
		boolean dropBlock = player == null || !player.isCreative();
		if (world.getBlockEntity(pos) instanceof MultiBlockCoreEntity) {
			world.breakBlock(pos, dropBlock);
		}
	}
}
