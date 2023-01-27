package com.sammy.lodestone.systems.blockentity;

import com.sammy.lodestone.helpers.BlockHelper;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import org.jetbrains.annotations.Nullable;

public abstract class ItemHolderBlockEntity extends LodestoneBlockEntity {
	public LodestoneBlockEntityInventory inventory;

	public ItemHolderBlockEntity(BlockEntityType<?> type, BlockPos pos, BlockState state) {
		super(type, pos, state);
		inventory = new LodestoneBlockEntityInventory(1, 64) {
			@Override
			public void onContentsChanged(int slot) {
				super.onContentsChanged(slot);
				needsSync = true;
				BlockHelper.updateAndNotifyState(world, pos);
			}
		};
	}

	@Override
	public ActionResult onUse(PlayerEntity player, Hand hand) {
		inventory.interact(player.world, player, hand);
		return ActionResult.SUCCESS;
	}

	@Override
	public void onBreak(@Nullable PlayerEntity player) {
		inventory.dumpItems(world, pos);
	}

	@Override
	protected void writeNbt(NbtCompound compound) {
		inventory.save(compound);
		super.writeNbt(compound);
	}

	@Override
	public void readNbt(NbtCompound compound) {
		inventory.deserializeNBT(compound);
		super.readNbt(compound);
	}
}
