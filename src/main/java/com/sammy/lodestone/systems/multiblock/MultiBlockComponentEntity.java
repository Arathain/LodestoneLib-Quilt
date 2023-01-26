package com.sammy.lodestone.systems.multiblock;


import com.sammy.lodestone.helpers.BlockHelper;
import com.sammy.lodestone.setup.LodestoneBlockEntityRegistry;
import com.sammy.lodestone.systems.blockentity.LodestoneBlockEntity;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import org.jetbrains.annotations.Nullable;

public class MultiBlockComponentEntity extends LodestoneBlockEntity {
	public BlockPos corePos;

	public MultiBlockComponentEntity(BlockEntityType<?> type, BlockPos pos, BlockState state) {
		super(type, pos, state);
	}

	public MultiBlockComponentEntity(BlockPos pos, BlockState state) {
		super(LodestoneBlockEntityRegistry.MULTIBLOCK_COMPONENT, pos, state);
	}

	@Override
	protected void writeNbt(NbtCompound nbt) {
		if (corePos != null) {
			BlockHelper.saveBlockPos(nbt, corePos, "core_position_");
		}
		super.writeNbt(nbt);
	}

	@Override
	public void readNbt(NbtCompound nbt) {
		corePos = BlockHelper.loadBlockPos(nbt, "core_position_");
		super.readNbt(nbt);
	}

	@Override
	public ActionResult onUse(PlayerEntity player, Hand hand) {
		if (corePos != null && world.getBlockEntity(corePos) instanceof MultiBlockCoreEntity core) {
			return core.onUse(player, hand);
		}
		return super.onUse(player, hand);
	}

	@Override
	public void onBreak(@Nullable PlayerEntity player) {
		if (corePos != null && world.getBlockEntity(corePos) instanceof MultiBlockCoreEntity core) {
			core.onBreak(player);
		}
		super.onBreak(player);
	}

}
