package com.sammy.lodestone.forge;

import net.minecraft.item.ItemStack;
import org.jetbrains.annotations.NotNull;

public interface ItemHandler {

	int size();

	@NotNull
	ItemStack getStack(int slot);

	@NotNull
	ItemStack insertItemStack(int slot, @NotNull ItemStack stack, boolean simulate);

	@NotNull
	ItemStack extractItemStack(int slot, int amount, boolean simulate);

	int getSlotLimit(int slot);

	boolean isItemValid(int slot, @NotNull ItemStack stack);
}
