package com.sammy.lodestone.systems.worldgen;

import com.sammy.lodestone.helpers.BlockHelper;
import net.minecraft.block.BlockState;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.WorldAccess;

import java.util.ArrayList;
import java.util.function.Function;

public class LodestoneBlockFiller {
	public ArrayList<BlockStateEntry> entries = new ArrayList<>();
	public final boolean careful;

	/**
	 * @param careful - a careful block filler will avoid replacing existing worlds into the world, replaceable blocks such as foliage are still replaced.
	 */
	public LodestoneBlockFiller(boolean careful) {
		this.careful = careful;
	}

	/**
	 * Places all the cached blockstate entries into the world.
	 */
	public void fill(WorldAccess accessor) {
		for (BlockStateEntry entry : entries) {
			if (accessor.isOutOfHeightLimit(entry.pos)) {
				continue;
			}
			if (careful && !entry.canPlace(accessor)) {
				continue;
			}
			entry.place(accessor);
		}
	}

	public void replace(int index, Function<BlockStateEntry, BlockStateEntry> function) {
		entries.set(index, function.apply(entries.get(index)));
	}

	/**
	 * A blockstate entry, contains a blockstate to place and a pos to place it at.
	 * Can be extended to implement further functionality, such as custom placement checks or placing additional blocks.
	 */
	public static class BlockStateEntry {
		public BlockState state;
		public final BlockPos pos;

		public BlockStateEntry(BlockState state, BlockPos pos) {
			this.state = state;
			this.pos = pos;
		}

		public BlockStateEntry replaceState(BlockState state) {
			this.state = state;
			return this;
		}

		public boolean canPlace(WorldAccess accessor) {
			return canPlace(accessor, pos);
		}

		public boolean canPlace(WorldAccess accessor, BlockPos pos) {
			BlockState state = accessor.getBlockState(pos);
			return accessor.isAir(pos) || state.getMaterial().isReplaceable();
		}

		public void place(WorldAccess accessor) {
			accessor.setBlockState(pos, state, 19);
			if (accessor instanceof World world) {
				BlockHelper.updateState(world, pos);
			}
		}
	}
}
