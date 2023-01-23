package com.sammy.lodestone.forge;

import net.minecraft.item.ItemStack;
import org.jetbrains.annotations.NotNull;

public interface ItemHandler {

	int getSlots();

	@NotNull
	ItemStack getStackInSlot(int slot);

	@NotNull
	ItemStack insertItem(int slot, @NotNull ItemStack stack, boolean simulate);

	@NotNull
	ItemStack extractItem(int slot, int amount, boolean simulate);

	int getSlotLimit(int slot);

	boolean isItemValid(int slot, @NotNull ItemStack stack);
}
