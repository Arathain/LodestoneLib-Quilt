package com.sammy.lodestone.forge;

import net.minecraft.item.ItemStack;
import org.jetbrains.annotations.NotNull;

public class EmptyHandler implements ItemHandlerModifiable {
    public static final ItemHandler INSTANCE = new EmptyHandler();

    @Override
    public int size() {
        return 0;
    }

    @Override
    @NotNull
    public ItemStack getStack(int slot) {
        return ItemStack.EMPTY;
    }

    @Override
    @NotNull
    public ItemStack insertItemStack(int slot, @NotNull ItemStack stack, boolean simulate) {
        return stack;
    }

    @Override
    @NotNull
    public ItemStack extractItemStack(int slot, int amount, boolean simulate)
    {
        return ItemStack.EMPTY;
    }

    @Override
    public void setStackInSlot(int slot, @NotNull ItemStack stack)
    {
        // nothing to do here
    }

    @Override
    public int getSlotLimit(int slot) {
        return 0;
    }

    @Override
    public boolean isItemValid(int slot, @NotNull ItemStack stack) {
        return false;
    }


}
