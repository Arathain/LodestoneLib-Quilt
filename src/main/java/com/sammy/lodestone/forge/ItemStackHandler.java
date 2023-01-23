package com.sammy.lodestone.forge;

import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtElement;
import net.minecraft.nbt.NbtList;
import net.minecraft.util.collection.DefaultedList;
import org.jetbrains.annotations.NotNull;

public class ItemStackHandler implements ItemHandler, ItemHandlerModifiable, INBTSerializable<NbtCompound> {
	protected DefaultedList<ItemStack> stacks;

	public ItemStackHandler() {
		this(1);
	}

	public ItemStackHandler(int size) {
		stacks = DefaultedList.ofSize(size, ItemStack.EMPTY);
	}

	public ItemStackHandler(DefaultedList<ItemStack> stacks) {
		this.stacks = stacks;
	}

	public void setSize(int size) {
		stacks = DefaultedList.ofSize(size, ItemStack.EMPTY);
	}

	@Override
	public void setStackInSlot(int slot, @NotNull ItemStack stack) {
		validateSlotIndex(slot);
		this.stacks.set(slot, stack);
		onContentsChanged(slot);
	}

	@Override
	public int getSlots() {
		return stacks.size();
	}

	@Override
	@NotNull
	public ItemStack getStackInSlot(int slot) {
		validateSlotIndex(slot);
		return this.stacks.get(slot);
	}

	@Override
	@NotNull
	public ItemStack insertItem(int slot, @NotNull ItemStack stack, boolean simulate) {
		if (stack.isEmpty())
			return ItemStack.EMPTY;

		if (!isItemValid(slot, stack))
			return stack;

		validateSlotIndex(slot);

		ItemStack existing = this.stacks.get(slot);

		int limit = getStackLimit(slot, stack);

		if (!existing.isEmpty())
		{
			if (!ItemHandlerHelper.canItemStacksStack(stack, existing))
				return stack;

			limit -= existing.getCount();
		}

		if (limit <= 0)
			return stack;

		boolean reachedLimit = stack.getCount() > limit;

		if (!simulate)
		{
			if (existing.isEmpty())
			{
				this.stacks.set(slot, reachedLimit ? ItemHandlerHelper.copyStackWithSize(stack, limit) : stack);
			}
			else
			{
				existing.increment(reachedLimit ? limit : stack.getCount());
			}
			onContentsChanged(slot);
		}

		return reachedLimit ? ItemHandlerHelper.copyStackWithSize(stack, stack.getCount()- limit) : ItemStack.EMPTY;
	}

	@Override
	@NotNull
	public ItemStack extractItem(int slot, int amount, boolean simulate)
	{
		if (amount == 0)
			return ItemStack.EMPTY;

		validateSlotIndex(slot);

		ItemStack existing = this.stacks.get(slot);

		if (existing.isEmpty())
			return ItemStack.EMPTY;

		int toExtract = Math.min(amount, existing.getMaxCount());

		if (existing.getCount() <= toExtract)
		{
			if (!simulate)
			{
				this.stacks.set(slot, ItemStack.EMPTY);
				onContentsChanged(slot);
				return existing;
			}
			else
			{
				return existing.copy();
			}
		}
		else
		{
			if (!simulate)
			{
				this.stacks.set(slot, ItemHandlerHelper.copyStackWithSize(existing, existing.getCount() - toExtract));
				onContentsChanged(slot);
			}

			return ItemHandlerHelper.copyStackWithSize(existing, toExtract);
		}
	}

	@Override
	public int getSlotLimit(int slot)
	{
		return 64;
	}

	protected int getStackLimit(int slot, @NotNull ItemStack stack)
	{
		return Math.min(getSlotLimit(slot), stack.getMaxCount());
	}

	@Override
	public boolean isItemValid(int slot, @NotNull ItemStack stack)
	{
		return true;
	}

	@Override
	public NbtCompound serializeNBT() {
		NbtList nbtTagList = new NbtList();
		for (int i = 0; i < stacks.size(); i++) {
			if (!stacks.get(i).isEmpty()) {
				NbtCompound itemTag = new NbtCompound();
				itemTag.putInt("Slot", i);
				stacks.get(i).writeNbt(itemTag);
				nbtTagList.add(itemTag);
			}
		}
		NbtCompound nbt = new NbtCompound();
		nbt.put("Items", nbtTagList);
		nbt.putInt("Size", stacks.size());
		return nbt;
	}

	@Override
	public void deserializeNBT(NbtCompound nbt) {
		setSize(nbt.contains("Size", NbtElement.INT_TYPE) ? nbt.getInt("Size") : stacks.size());
		NbtList tagList = nbt.getList("Items", NbtElement.COMPOUND_TYPE);
		for (int i = 0; i < tagList.size(); i++) {
			NbtCompound itemTags = tagList.getCompound(i);
			int slot = itemTags.getInt("Slot");

			if (slot >= 0 && slot < stacks.size()) {
				stacks.set(slot, ItemStack.fromNbt(itemTags));
			}
		}
		onLoad();
	}

	protected void validateSlotIndex(int slot) {
		if (slot < 0 || slot >= stacks.size())
			throw new RuntimeException("Slot " + slot + " not in valid range - [0," + stacks.size() + ")");
	}

	protected void onLoad() {

	}

	protected void onContentsChanged(int slot) {

	}
}
