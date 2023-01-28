package com.sammy.lodestone.systems.block;

import com.sammy.lodestone.systems.blockentity.BlockTickHelper;
import com.sammy.lodestone.systems.blockentity.LodestoneBlockEntity;
import net.minecraft.block.*;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.BlockEntityTicker;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.BlockView;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

import javax.annotation.Nonnull;

public class LodestoneEntityBlock<T extends LodestoneBlockEntity> extends BlockWithEntity {
	protected BlockEntityType<T> blockEntityType = null;
	protected BlockEntityTicker<T> ticker = null;

	public LodestoneEntityBlock(Settings properties) {
		super(properties);

	}

	@Override
	public BlockRenderType getRenderType(BlockState state) {
		return BlockRenderType.MODEL;
	}


	/**
	 * Sets the block entity with a ticker enabled
	 */
	public LodestoneEntityBlock<T> setBlockEntity(BlockEntityType<T> type) {
		return setBlockEntity(type, true);
	}

	/**
	 * Sets the block entity with an optional ticker
	 */
	public LodestoneEntityBlock<T> setBlockEntity(BlockEntityType<T> type, boolean shouldTick) {
		this.blockEntityType = type;
		if (shouldTick) {
			this.ticker = BlockTickHelper::tickSided;
		}
		return this;
	}


	@Override
	public BlockEntity createBlockEntity(@Nonnull BlockPos pos, @Nonnull BlockState state) {
		return hasTileEntity(state) ? blockEntityType.instantiate(pos, state) : null;
	}

	public boolean hasTileEntity(BlockState state) {
		return this.blockEntityType != null;
	}

	@Override
	@Nullable
	public <B extends BlockEntity> BlockEntityTicker<B> getTicker(@Nonnull World level, @Nonnull BlockState state, @Nonnull BlockEntityType<B> type) {
		return (BlockEntityTicker<B>) ticker;
	}

	@Override
	public void onPlaced(@Nonnull World pLevel, @Nonnull BlockPos pPos, @Nonnull BlockState pState, @Nullable LivingEntity pPlacer, @Nonnull ItemStack pStack) {
		if (hasTileEntity(pState)) {
			if (pLevel.getBlockEntity(pPos) instanceof LodestoneBlockEntity simpleBlockEntity) {
				simpleBlockEntity.onPlace(pPlacer, pStack);
			}
		}
		super.onPlaced(pLevel, pPos, pState, pPlacer, pStack);
	}

	@Override
	public ItemStack getPickStack(BlockView world, BlockPos pos, BlockState state) {
		if (hasTileEntity(state)) {
			if (world.getBlockEntity(pos) instanceof LodestoneBlockEntity simpleBlockEntity) {
				ItemStack stack = simpleBlockEntity.onClone(state, world, pos);
				if (!stack.isEmpty()) {
					return stack;
				}
			}
		}
		return super.getPickStack(world, pos, state);
	}

	@Override
	public void onBreak(@Nonnull World level, @Nonnull BlockPos pos, @Nonnull BlockState state, @Nonnull PlayerEntity player) {
		onBlockBroken(state, level, pos, player);
		super.onBreak(level, pos, state, player);
	}

	public void onBlockBroken(BlockState state, BlockView level, BlockPos pos, @Nullable PlayerEntity player) {
		if (hasTileEntity(state)) {
			if (level.getBlockEntity(pos) instanceof LodestoneBlockEntity simpleBlockEntity) {
				simpleBlockEntity.onBreak(player);
			}
		}
	}

	@Override
	public void onEntityCollision(@Nonnull BlockState pState, @Nonnull World pLevel, @Nonnull BlockPos pPos, @Nonnull Entity pEntity) {
		if (hasTileEntity(pState)) {
			if (pLevel.getBlockEntity(pPos) instanceof LodestoneBlockEntity simpleBlockEntity) {
				simpleBlockEntity.onEntityInside(pState, pLevel, pPos, pEntity);
			}
		}
		super.onEntityCollision(pState, pLevel, pPos, pEntity);
	}

	@Override
	public void neighborUpdate(@Nonnull BlockState pState, @Nonnull World pLevel, @Nonnull BlockPos pPos, @Nonnull Block pBlock, @Nonnull BlockPos pFromPos, boolean pIsMoving) {
		if (hasTileEntity(pState)) {
			if (pLevel.getBlockEntity(pPos) instanceof LodestoneBlockEntity simpleBlockEntity) {
				simpleBlockEntity.onNeighborUpdate(pState, pPos, pFromPos);
			}
		}
		super.neighborUpdate(pState, pLevel, pPos, pBlock, pFromPos, pIsMoving);
	}


	@Nonnull
	@Override
	public ActionResult onUse(@Nonnull BlockState state, @Nonnull World level, @Nonnull BlockPos pos, @Nonnull PlayerEntity player, @Nonnull Hand hand, @Nonnull BlockHitResult ray) {
		if (hasTileEntity(state)) {
			if (level.getBlockEntity(pos) instanceof LodestoneBlockEntity simpleBlockEntity) {
				return simpleBlockEntity.onUse(player, hand);
			}
		}
		return super.onUse(state, level, pos, player, hand, ray);
	}
}
