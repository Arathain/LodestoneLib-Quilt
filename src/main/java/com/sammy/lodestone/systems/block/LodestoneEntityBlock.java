package com.sammy.lodestone.systems.block;

import com.sammy.lodestone.systems.blockentity.LodestoneBlockEntity;
import net.minecraft.block.Block;
import net.minecraft.block.BlockEntityProvider;
import net.minecraft.block.BlockState;
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
import net.minecraft.world.explosion.Explosion;
import org.jetbrains.annotations.Nullable;

public class LodestoneEntityBlock<T extends LodestoneBlockEntity> extends Block implements BlockEntityProvider {
	protected BlockEntityType<T> blockEntityType = null;
	protected BlockEntityTicker<T> ticker = null;

	public LodestoneEntityBlock(Settings properties) {
		super(properties);
	}

	@Nullable
	public static <E extends BlockEntity, A extends BlockEntity> BlockEntityTicker<A> checkType(BlockEntityType<A> givenType, BlockEntityType<E> expectedType, BlockEntityTicker<? super E> ticker) {
		return expectedType == givenType ? (BlockEntityTicker<A>) ticker : null;
	}

	public LodestoneEntityBlock<T> setBlockEntity(BlockEntityType<T> type) {
		this.blockEntityType = type;
		this.ticker = (l, p, s, t) -> t.tick();
		return this;
	}

	@Nullable
	@Override
	public BlockEntity createBlockEntity(BlockPos pos, BlockState state) {
		return hasTileEntity(state) ? blockEntityType.instantiate(pos, state) : null;
	}

	public boolean hasTileEntity(BlockState state) {
		return this.blockEntityType != null;
	}

	@Nullable
	@Override
	public <T extends BlockEntity> BlockEntityTicker<T> getTicker(World world, BlockState state, BlockEntityType<T> type) {
		return (BlockEntityTicker<T>) ticker;
	}

	@Override
	public void onPlaced(World world, BlockPos pos, BlockState state, @Nullable LivingEntity placer, ItemStack itemStack) {
		if (hasTileEntity(state)) {
			if (world.getBlockEntity(pos) instanceof LodestoneBlockEntity simpleBlockEntity) {
				simpleBlockEntity.onPlace(placer, itemStack);
			}
		}
		super.onPlaced(world, pos, state, placer, itemStack);
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
	public void onBreak(World world, BlockPos pos, BlockState state, PlayerEntity player) {
		onBlockBroken(state, world, pos, player);
		super.onBreak(world, pos, state, player);
	}

	@Override
	public void onDestroyedByExplosion(World world, BlockPos pos, Explosion explosion) {
		onBlockBroken(world.getBlockState(pos), world, pos, null);
		super.onDestroyedByExplosion(world, pos, explosion);
	}

	public void onBlockBroken(BlockState state, BlockView blockView, BlockPos pos, @Nullable PlayerEntity player) {
		if (hasTileEntity(state)) {
			if (blockView.getBlockEntity(pos) instanceof LodestoneBlockEntity simpleBlockEntity) {
				simpleBlockEntity.onBreak(player);
			}
		}
	}

	@Override
	public void onEntityCollision(BlockState state, World world, BlockPos pos, Entity entity) {
		if (hasTileEntity(state)) {
			if (world.getBlockEntity(pos) instanceof LodestoneBlockEntity simpleBlockEntity) {
				simpleBlockEntity.onEntityInside(state, world, pos, entity);
			}
		}
		super.onEntityCollision(state, world, pos, entity);
	}

	@Override
	public void neighborUpdate(BlockState state, World world, BlockPos pos, Block block, BlockPos fromPos, boolean notify) {
		if (hasTileEntity(state)) {
			if (world.getBlockEntity(pos) instanceof LodestoneBlockEntity simpleBlockEntity) {
				simpleBlockEntity.onNeighborUpdate(state, pos, fromPos);
			}
		}
		super.neighborUpdate(state, world, pos, block, fromPos, notify);
	}

	@Override
	public ActionResult onUse(BlockState state, World world, BlockPos pos, PlayerEntity player, Hand hand, BlockHitResult hit) {
		if (hasTileEntity(state)) {
			if (world.getBlockEntity(pos) instanceof LodestoneBlockEntity simpleBlockEntity) {
				return simpleBlockEntity.onUse(player, hand);
			}
		}
		return super.onUse(state, world, pos, player, hand, hit);
	}
}
