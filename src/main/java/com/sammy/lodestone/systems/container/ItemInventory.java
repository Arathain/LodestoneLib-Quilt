package com.sammy.lodestone.systems.container;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.SimpleInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtList;

public class ItemInventory extends SimpleInventory {
	private final ItemStack stack;

	public ItemInventory(ItemStack stack, int expectedSize) {
		super(expectedSize);
		this.stack = stack;

		NbtList list = stack.getOrCreateNbt().getList("items", 10);
		int i = 0;
		for (; i < expectedSize && i < list.size(); i++) {
			setStack(i, ItemStack.fromNbt(list.getCompound(i)));
		}
	}

	@Override
	public boolean canPlayerUse(PlayerEntity player) {
		return !stack.isEmpty();
	}

	@Override
	public void markDirty() {
		super.markDirty();
		NbtList list = new NbtList();
		for (int i = 0; i < size(); i++) {
			list.add(getStack(i).writeNbt(new NbtCompound()));
		}
		stack.getOrCreateNbt().put("items", list);
	}
}
