package com.sammy.lodestone.systems.worldgen;

import com.sammy.lodestone.helpers.BlockHelper;
import net.minecraft.block.BlockState;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.WorldAccess;

import java.util.HashMap;
import java.util.function.Function;

public class LodestoneBlockFiller {
	protected final HashMap<BlockPos, BlockStateEntry> entries = new HashMap<>();
	protected final boolean careful;

	public LodestoneBlockFiller(boolean careful) {
		this.careful = careful;
	}

	public void fill(WorldAccess level) {
		getEntries().forEach((pos, entry) -> {
			if (!isCareful() || entry.canPlace(level, pos)) {
				entry.place(level, pos);
			}
		});
	}

	public void replace(BlockPos pos, Function<BlockStateEntry, BlockStateEntry> entryFunction) {
		getEntries().replace(pos, entryFunction.apply(getEntries().get(pos)));
	}

	public HashMap<BlockPos, BlockStateEntry> getEntries() {
		return entries;
	}

	public boolean isCareful() {
		return careful;
	}


	/**
	 * A blockstate entry, contains a blockstate to place and a pos to place it at.
	 * Can be extended to implement further functionality, such as custom placement checks or placing additional blocks.
	 */
	public static class BlockStateEntry {
		protected final BlockState state;

		public BlockStateEntry(BlockState state) {
			this.state = state;
		}

		public BlockState getState() {
			return state;
		}

		public boolean canPlace(WorldAccess level, BlockPos pos) {
			if (level.isOutOfHeightLimit(pos)) {
				return false;
			}
			BlockState state = level.getBlockState(pos);
			return level.isAir(pos) || state.getMaterial().isReplaceable();
		}

		public void place(WorldAccess level, BlockPos pos) {
			level.setBlockState(pos, state, 19);
			if (level instanceof World) {
				BlockHelper.updateState((World) level, pos);
			}
		}
	}
}
