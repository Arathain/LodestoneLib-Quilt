package com.sammy.lodestone.systems.multiblock;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.item.BlockItem;
import net.minecraft.item.ItemPlacementContext;

import java.util.function.Supplier;

public class MultiBlockItem extends BlockItem {
	public final Supplier<? extends MultiBlockStructure> structure;

	public MultiBlockItem(Block block, Settings settings, Supplier<? extends MultiBlockStructure> structure) {
		super(block, settings);
		this.structure = structure;
	}

	@Override
	protected boolean canPlace(ItemPlacementContext context, BlockState state) {
		if (!structure.get().canPlace(context)) {
			return false;
		}
		return super.canPlace(context, state);
	}

	@Override
	protected boolean place(ItemPlacementContext context, BlockState state) {
		structure.get().place(context);
		return super.place(context, state);
	}
}
