package com.sammy.lodestone.forge;

import com.google.common.base.Preconditions;
import net.minecraft.item.ItemStack;
import org.jetbrains.annotations.NotNull;

public class RangedWrapper implements ItemHandlerModifiable {
    private final ItemHandlerModifiable compose;
    private final int minSlot;
    private final int maxSlot;

    public RangedWrapper(ItemHandlerModifiable compose, int minSlot, int maxSlotExclusive) {
        Preconditions.checkArgument(maxSlotExclusive > minSlot, "Max slot must be greater than min slot");
        this.compose = compose;
        this.minSlot = minSlot;
        this.maxSlot = maxSlotExclusive;
    }

    @Override
    public int size() {
        return maxSlot - minSlot;
    }

    @Override
    public @NotNull ItemStack getStack(int slot) {
        if (checkSlot(slot)) {
            return compose.getStack(slot + minSlot);
        }

        return ItemStack.EMPTY;
    }

    @Override
    public ItemStack insertItemStack(int slot, ItemStack itemStack, boolean simulate) {
        if (checkSlot(slot)) {
            return compose.insertItemStack(slot + minSlot, itemStack, simulate);
        }
        return itemStack;
    }

    @Override
    public ItemStack extractItemStack(int slot, int amount, boolean simulate) {
        if (checkSlot(slot)) {
            return compose.extractItemStack(slot + minSlot, amount, simulate);
        }
        return ItemStack.EMPTY;
    }

    @Override
    public void setStackInSlot(int slot, @NotNull ItemStack stack) {
        if (checkSlot(slot)) {
            compose.setStackInSlot(slot + minSlot, stack);
        }
    }

    @Override
    public int getSlotLimit(int slot) {
        if (checkSlot(slot)) {
            return compose.getSlotLimit(slot + minSlot);
        }
        return 0;
    }

    @Override
    public boolean isItemValid(int slot, @NotNull ItemStack stack) {
        if (checkSlot(slot)) {
            return compose.isItemValid(slot + minSlot, stack);
        }
        return false;
    }

    private boolean checkSlot(int localSlot) {
        return localSlot + minSlot < maxSlot;
    }
}
