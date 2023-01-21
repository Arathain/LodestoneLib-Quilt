package com.sammy.lodestone.systems.multiblock;

import net.minecraft.item.ItemPlacementContext;
import net.minecraft.state.property.Properties;

import java.util.ArrayList;
import java.util.List;

public class HorizontalDirectionStructure extends MultiBlockStructure {
	public HorizontalDirectionStructure(ArrayList<StructurePiece> structurePieces) {
		super(structurePieces);
	}

	@Override
	public void place(ItemPlacementContext context) {
		structurePieces.forEach(s -> s.place(context.getBlockPos(), context.getWorld(), s.state.with(Properties.HORIZONTAL_FACING, context.getPlayerFacing().getOpposite())));
	}


	public static HorizontalDirectionStructure of(StructurePiece... pieces) {
		return new HorizontalDirectionStructure(new ArrayList<>(List.of(pieces)));
	}
}
