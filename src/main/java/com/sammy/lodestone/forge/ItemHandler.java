package com.sammy.lodestone.forge;

import net.minecraft.item.ItemStack;
import org.jetbrains.annotations.NotNull;

public interface ItemHandler  {

    int size();

    @NotNull
    ItemStack getStack(int slot);

    ItemStack insertItemStack(int slot, ItemStack itemStack, boolean simulate);

    ItemStack extractItemStack(int slot, int amount, boolean simulate);

    int getMaxCountForSlot(int slot);

    boolean isItemValid(int slot, @NotNull ItemStack stack);
}
