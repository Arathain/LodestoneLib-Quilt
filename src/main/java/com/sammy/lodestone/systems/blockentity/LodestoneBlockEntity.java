package com.sammy.lodestone.systems.blockentity;

import com.sammy.lodestone.forge.CustomDataPacketHandlingBlockEntity;
import com.sammy.lodestone.forge.CustomUpdateTagHandlingBlockEntity;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.network.ClientConnection;
import net.minecraft.network.packet.s2c.play.BlockEntityUpdateS2CPacket;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.BlockView;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

public class LodestoneBlockEntity extends BlockEntity implements CustomDataPacketHandlingBlockEntity, CustomUpdateTagHandlingBlockEntity {
	public boolean needsSync;

	public LodestoneBlockEntity(BlockEntityType<?> type, BlockPos pos, BlockState state) {
		super(type, pos, state);
	}

	public void onBreak(@Nullable PlayerEntity player) {
		//TODO invalidateCaps();
	}

	public void onPlace(LivingEntity placer, ItemStack stack) {
	}

	public void onNeighborUpdate(BlockState state, BlockPos pos, BlockPos neighbor) {
	}

	public ItemStack onClone(BlockState state, BlockView blockView, BlockPos pos) {
		return ItemStack.EMPTY;
	}

	public ActionResult onUse(PlayerEntity player, Hand hand) {
		return ActionResult.PASS;
	}

	public void onEntityInside(BlockState state, World world, BlockPos pos, Entity entity) {

	}


	@Override
	public void readNbt(NbtCompound pTag) {
		needsSync = true;
		super.readNbt(pTag);
	}


	@Override
	public BlockEntityUpdateS2CPacket toUpdatePacket() {
		return BlockEntityUpdateS2CPacket.of(this);
	}
	@Override
	public void onDataPacket(ClientConnection connection, BlockEntityUpdateS2CPacket packet) {
		handleUpdateTag(toUpdatePacket().getNbt());
	}




	/**
	 * Called on both sides to tick the block entity as part of the ticker
	 */
	public void tick() {
		if (needsSync) {
			init();
			needsSync = false;
		}
	}

	/**
	 * Called only for server side when ticking
	 */
	public void serverTick() {

	}

	/**
	 * Called only for client side when ticking
	 */
	public void clientTick() {

	}

	/**
	 * Called on both sides to force an udpate after reload
	 */
	public void init() {

	}
}
