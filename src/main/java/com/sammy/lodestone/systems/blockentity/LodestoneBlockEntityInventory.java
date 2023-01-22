package com.sammy.lodestone.systems.blockentity;


import com.sammy.lodestone.forge.INBTSerializable;
import com.sammy.lodestone.forge.ItemHandlerHelper;
import com.sammy.lodestone.forge.ItemStackHandler;
import com.sammy.lodestone.helpers.BlockHelper;
import net.minecraft.entity.ItemEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.util.Hand;
import net.minecraft.util.collection.DefaultedList;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Collectors;

public class LodestoneBlockEntityInventory extends ItemStackHandler {
	public final int slotCount;
	public final int allowedItemSize;
	public Predicate<ItemStack> inputPredicate;
	public Predicate<ItemStack> outputPredicate;

	public ArrayList<ItemStack> nonEmptyItemStacks = new ArrayList<>();

	public int emptyItemAmount;
	public int nonEmptyItemAmount;
	public int firstEmptyItemIndex;

	public LodestoneBlockEntityInventory(int slotCount, int allowedItemSize, Predicate<ItemStack> inputPredicate, Predicate<ItemStack> outputPredicate) {
		this(slotCount, allowedItemSize, inputPredicate);
		this.outputPredicate = outputPredicate;
	}

	public LodestoneBlockEntityInventory(int slotCount, int allowedItemSize, Predicate<ItemStack> inputPredicate) {
		this(slotCount, allowedItemSize);
		this.inputPredicate = inputPredicate;
	}

	public LodestoneBlockEntityInventory(int slotCount, int allowedItemSize) {
		super(slotCount);
		this.slotCount = slotCount;
		this.allowedItemSize = allowedItemSize;
		updateData();
	}

	@Override
	protected void onInventorySlotChanged(int slot) {
		updateData();
	}

	@Override
	public int size() {
		return slotCount;
	}

	@Override
	public int getMaxCountForSlot(int slot) {
		return allowedItemSize;
	}

	@Override
	public boolean isItemValid(int slot, @NotNull ItemStack stack) {
		if (inputPredicate != null) {
			if (!inputPredicate.test(stack)) {
				return false;
			}
		}
		return super.isItemValid(slot, stack);
	}

	@Override
	public ItemStack extractItemStack(int slot, int amount, boolean simulate) {
		if (outputPredicate != null) {
			if (!outputPredicate.test(super.extractItemStack(slot, amount, true))) {
				return ItemStack.EMPTY;
			}
		}
		return super.extractItemStack(slot, amount, simulate);
	}


	public void updateData() {
		DefaultedList<ItemStack> stacks = getStacks();
		nonEmptyItemStacks = stacks.stream().filter(s -> !s.isEmpty()).collect(Collectors.toCollection(ArrayList::new));
		nonEmptyItemAmount = nonEmptyItemStacks.size();
		emptyItemAmount = (int) stacks.stream().filter(ItemStack::isEmpty).count();
		firstEmptyItemIndex = this.inventory.indexOf(ItemStack.EMPTY);
	}

	public void readNbt(NbtCompound compound) {
		readNbt(compound, "inventory");
	}

	public void readNbt(NbtCompound compound, String name) {
		deserializeNBT(compound.getCompound(name));
		if (inventory.size() != slotCount) {
			int missing = slotCount - inventory.size();
			for (int i = 0; i < missing; i++) {
				inventory.add(ItemStack.EMPTY);
			}
		}
		updateData();
	}

	public void save(NbtCompound compound) {
		save(compound, "inventory");
	}


	public void save(NbtCompound compound, String name) {
		compound.put(name, serializeNBT());
	}

	public DefaultedList<ItemStack> getStacks() {
		return inventory;
	}

	public boolean isEmpty() {
		return nonEmptyItemAmount == 0;
	}

	public void clear() {
		for (int i = 0; i < slotCount; i++) {
			setStackInSlot(i, ItemStack.EMPTY);
		}
	}

	public void dumpItems(World world, BlockPos pos) {
		dumpItems(world, BlockHelper.fromBlockPos(pos).add(0.5, 0.5, 0.5));
	}

	public void dumpItems(World world, Vec3d pos) {
		for (int i = 0; i < slotCount; i++) {
			if (!getStack(i).isEmpty()) {
				world.spawnEntity(new ItemEntity(world, pos.getX(), pos.getY(), pos.getZ(), getStack(i)));
			}
			setStackInSlot(i, ItemStack.EMPTY);
		}
	}

	public ItemStack interact(World world, PlayerEntity player, Hand handIn) {
		ItemStack held = player.getStackInHand(handIn);
		player.swingHand(handIn, true);
		int size = nonEmptyItemStacks.size() - 1;
		if ((held.isEmpty() || firstEmptyItemIndex == -1) && size != -1) {
			ItemStack takeOutStack = nonEmptyItemStacks.get(size);
			if (takeOutStack.getItem().equals(held.getItem())) {
				return insertItem(world, held);
			}
			ItemStack extractedStack = extractItemStack(world, held, player);
			boolean success = !extractedStack.isEmpty();
			if (success) {
				insertItem(world, held);
			}
			return extractedStack;
		} else {
			return insertItem(world, held);
		}
	}

	public ItemStack extractItemStack(World world, ItemStack heldStack, PlayerEntity player) {
		if (!world.isClient()) {
			List<ItemStack> nonEmptyStacks = this.nonEmptyItemStacks;
			if (nonEmptyStacks.isEmpty()) {
				return heldStack;
			}
			ItemStack takeOutStack = nonEmptyStacks.get(nonEmptyStacks.size() - 1);
			int slot = inventory.indexOf(takeOutStack);
			if (extractItemStack(slot, takeOutStack.getCount(), true).equals(ItemStack.EMPTY)) {
				return heldStack;
			}
			extractItemStack(player, takeOutStack, slot);
			return takeOutStack;
		}
		return ItemStack.EMPTY;
	}

	public void extractItemStack(PlayerEntity playerEntity, ItemStack stack, int slot) {
		ItemHandlerHelper.giveItemToPlayer(playerEntity, stack, playerEntity.getInventory().selectedSlot);
		setStackInSlot(slot, ItemStack.EMPTY);
	}

	public ItemStack insertItem(World world, ItemStack stack) {
		if (!world.isClient()) {
			if (!stack.isEmpty()) {
				ItemStack simulate = insertItem(stack, true);
				if (simulate.equals(stack)) {
					return ItemStack.EMPTY;
				}
				int count = stack.getCount() - simulate.getCount();
				if (count > allowedItemSize) {
					count = allowedItemSize;
				}
				ItemStack input = stack.split(count);
				insertItem(input, false);
				return input;
			}
		}
		return ItemStack.EMPTY;
	}

	public ItemStack insertItem(ItemStack stack, boolean simulate) {
		return ItemHandlerHelper.insertItem(this, stack, simulate);
	}
}
