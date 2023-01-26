package com.sammy.lodestone.systems.multiblock;


import net.minecraft.block.BlockState;
import net.minecraft.block.ShapeContext;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemPlacementContext;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3i;
import net.minecraft.world.World;

import java.util.ArrayList;
import java.util.List;

public class MultiBlockStructure {
	public final ArrayList<StructurePiece> structurePieces;

	public MultiBlockStructure(ArrayList<StructurePiece> structurePieces) {
		this.structurePieces = structurePieces;
	}

	public boolean canPlace(ItemPlacementContext context) {
		return structurePieces.stream().allMatch(p -> p.canPlace(context));
	}

	public void place(ItemPlacementContext context) {
		structurePieces.forEach(s -> s.place(context.getBlockPos(), context.getWorld()));
	}

	public static MultiBlockStructure of(StructurePiece... pieces) {
		return new MultiBlockStructure(new ArrayList<>(List.of(pieces)));
	}

	public static class StructurePiece {
		public final Vec3i offset;
		public final BlockState state;

		public StructurePiece(int xOffset, int yOffset, int zOffset, BlockState state) {
			this.offset = new Vec3i(xOffset, yOffset, zOffset);
			this.state = state;
		}

		public boolean canPlace(ItemPlacementContext context) {
			World world = context.getWorld();
			PlayerEntity player = context.getPlayer();
			BlockPos pos = context.getBlockPos().add(offset);
			BlockState existingState = world.getBlockState(pos);
			ShapeContext collisioncontext = player == null ? ShapeContext.absent() : ShapeContext.of(player);
			return existingState.getMaterial().isReplaceable() && world.canPlace(state, pos, collisioncontext);
		}

		public void place(BlockPos core, World world) {
			place(core, world, state);
		}

		public void place(BlockPos core, World world, BlockState state) {
			BlockPos pos = core.add(offset);
			world.setBlockState(pos, state, 3);
			if (world.getBlockEntity(pos) instanceof MultiBlockComponentEntity component) {
				component.corePos = core;
			}
		}
	}
}
