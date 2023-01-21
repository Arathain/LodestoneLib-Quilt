package com.sammy.lodestone.forge;

import net.minecraft.inventory.Inventory;
import net.minecraft.item.ItemStack;
import org.jetbrains.annotations.NotNull;

public class InvWrapper implements ItemHandlerModifiable {
    private final Inventory inv;

    public InvWrapper(Inventory inv) {
        this.inv = inv;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (o == null || getClass() != o.getClass())
            return false;

        InvWrapper that = (InvWrapper) o;

        return getInv().equals(that.getInv());
    }

    @Override
    public int hashCode() {
        return getInv().hashCode();
    }

    @Override
    public int size() {
        return getInv().size();
    }

    @Override
    @NotNull
    public ItemStack getStack(int slot) {
        return getInv().getStack(slot);
    }

    @Override
    @NotNull
    public ItemStack insertItemStack(int slot, @NotNull ItemStack stack, boolean simulate)
    {
        if (stack.isEmpty())
            return ItemStack.EMPTY;

        ItemStack stackInSlot = getInv().getStack(slot);

        int m;
        if (!stackInSlot.isEmpty())
        {
            if (stackInSlot.getCount() >= Math.min(stackInSlot.getMaxCount(), getMaxCountForSlot(slot)))
                return stack;

            if (!ItemHandlerHelper.canItemStacksStack(stack, stackInSlot))
                return stack;

            if (!getInv().isValid(slot, stack))
                return stack;

            m = Math.min(stack.getMaxCount(), getMaxCountForSlot(slot)) - stackInSlot.getCount();

            if (stack.getCount() <= m)
            {
                if (!simulate)
                {
                    ItemStack copy = stack.copy();
                    copy.increment(stackInSlot.getCount());
                    getInv().setStack(slot, copy);
                    getInv().markDirty();
                }

                return ItemStack.EMPTY;
            }
            else
            {
                // copy the stack to not modify the original one
                stack = stack.copy();
                if (!simulate)
                {
                    ItemStack copy = stack.split(m);
                    copy.increment(stackInSlot.getCount());
                    getInv().setStack(slot, copy);
                    getInv().markDirty();
                    return stack;
                }
                else
                {
                    stack.decrement(m);
                    return stack;
                }
            }
        }
        else
        {
            if (!getInv().isValid(slot, stack))
                return stack;

            m = Math.min(stack.getMaxCount(), getMaxCountForSlot(slot));
            if (m < stack.getCount())
            {
                // copy the stack to not modify the original one
                stack = stack.copy();
                if (!simulate)
                {
                    getInv().setStack(slot, stack.split(m));
                    getInv().markDirty();
                    return stack;
                }
                else
                {
                    stack.decrement(m);
                    return stack;
                }
            }
            else
            {
                if (!simulate)
                {
                    getInv().setStack(slot, stack);
                    getInv().markDirty();
                }
                return ItemStack.EMPTY;
            }
        }

    }

    @Override
    @NotNull
    public ItemStack extractItemStack(int slot, int amount, boolean simulate) {
        if (amount == 0)
            return ItemStack.EMPTY;

        ItemStack stackInSlot = getInv().getStack(slot);

        if (stackInSlot.isEmpty())
            return ItemStack.EMPTY;

        if (simulate) {
            if (stackInSlot.getCount() < amount)
            {
                return stackInSlot.copy();
            }
            else
            {
                ItemStack copy = stackInSlot.copy();
                copy.setCount(amount);
                return copy;
            }
        }
        else {
            int m = Math.min(stackInSlot.getCount(), amount);

            ItemStack decrStackSize = getInv().removeStack(slot, m);
            getInv().markDirty();
            return decrStackSize;
        }
    }

    @Override
    public void setStackInSlot(int slot, @NotNull ItemStack stack) {
        getInv().setStack(slot, stack);
    }

    @Override
    public int getMaxCountForSlot(int slot) {
        return getInv().getMaxCountPerStack();
    }

    @Override
    public boolean isItemValid(int slot, @NotNull ItemStack stack) {
        return getInv().isValid
            (slot, stack);
    }

    public Inventory getInv() {
        return inv;
    }
}
