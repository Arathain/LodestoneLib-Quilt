package com.sammy.lodestone.forge;

import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtElement;
import net.minecraft.nbt.NbtList;
import net.minecraft.util.collection.DefaultedList;
import org.jetbrains.annotations.NotNull;

public class ItemStackHandler implements ItemHandler , ItemHandlerModifiable{

    protected DefaultedList<ItemStack> inventory;

    public ItemStackHandler() {
        this(1);
    }

    public ItemStackHandler(int inventorySize) {
        inventory = DefaultedList.ofSize(inventorySize, ItemStack.EMPTY);
    }

    @SuppressWarnings("ConstantConditions")
    public static boolean canItemStacksStack(ItemStack left, ItemStack right) {
        if (left.isEmpty() || !left.isItemEqualIgnoreDamage(right) || left.hasNbt() != right.hasNbt()) {
            return false;
        }

        return (!left.hasNbt() || left.getNbt().equals(right.getNbt()));
    }

    public static ItemStack copyStackWithNewSize(ItemStack itemStack, int newSize) {
        if (newSize == 0) {
            return ItemStack.EMPTY;
        }

        ItemStack copy = itemStack.copy();
        copy.setCount(newSize);

        return copy;
    }

    @Override
    public int size() {
        return inventory.size();
    }

    @Override
    public ItemStack getStack(int slot) {
        validateSlotIndex(slot);
        return inventory.get(slot);
    }

    @Override
    public void setStackInSlot(int slot, @NotNull ItemStack stack) {
        validateSlotIndex(slot);
        this.inventory.set(slot, stack);
        onContentsChanged(slot);
    }

    @Override
    public ItemStack insertItemStack(int slot, ItemStack stack, boolean simulate) {
        if (stack.isEmpty() || !isItemValid(slot, stack)) {
            return stack;
        }

        validateSlotIndex(slot);

        ItemStack invItemStack = inventory.get(slot);
        int limit = getStackLimit(slot, invItemStack);

        if (!invItemStack.isEmpty()) {
            if (!canItemStacksStack(stack, invItemStack)) {
                return stack;
            }

            limit -= invItemStack.getCount();
        }

        if (limit <= 0) {
            return stack;
        }

        boolean reachedLimit = stack.getCount() > limit;

        if (!simulate) {
            if (invItemStack.isEmpty()) {
                inventory.set(slot, reachedLimit ? copyStackWithNewSize(stack, limit) : stack);
            } else {
                invItemStack.increment(reachedLimit ? limit : stack.getCount());
            }
            onInventorySlotChanged(slot);
        }

        return reachedLimit ? copyStackWithNewSize(stack, stack.getCount() - limit) : ItemStack.EMPTY;
    }

    @Override
    public ItemStack extractItemStack(int slot, int amount, boolean simulate) {
        if (amount == 0) {
            return ItemStack.EMPTY;
        }

        validateSlotIndex(slot);

        ItemStack invItemStack = inventory.get(slot);

        if (invItemStack.isEmpty()) {
            return ItemStack.EMPTY;
        }

        int nbrToExtract = Math.min(amount, invItemStack.getMaxCount());

        if (invItemStack.getCount() <= nbrToExtract) {
            if (!simulate) {
                inventory.set(slot, ItemStack.EMPTY);
                onInventorySlotChanged(slot);

                return invItemStack;
            } else {
                return invItemStack.copy();
            }
        } else {
            if (!simulate) {
                inventory.set(slot, copyStackWithNewSize(invItemStack, invItemStack.getCount() - nbrToExtract));
                onInventorySlotChanged(slot);
            }

            return copyStackWithNewSize(invItemStack, nbrToExtract);
        }
    }

    @Override
    public int getMaxCountForSlot(int slot) {
        return 64;
    }

    @Override
    public boolean isItemValid(int slot, @NotNull ItemStack stack) {
        return false;
    }

    public DefaultedList<ItemStack> getItems() {
        return inventory;
    }

    protected void onInventoryLoaded() {
        // Do nothing on basic itemstack handler when inventory is loaded
    }

    protected void onInventorySlotChanged(int slot) {
        // Do nothing on basic itemstack handler when inventory slot is changed
    }

    public void setSize(int size) {
        inventory = DefaultedList.ofSize(size, ItemStack.EMPTY);
    }

    protected void validateSlotIndex(int slot) {
        if (slot < 0 || slot >= inventory.size())
            throw new RuntimeException("Slot " + slot + " not in valid range - [0," + inventory.size() + ")");
    }

    protected int getStackLimit(int slot, ItemStack stack) {
        return Math.min(getMaxCountForSlot(slot), stack.getMaxCount());
    }

    public NbtCompound writeNbt(NbtCompound nbtCompound) {
        NbtList itemListTag = new NbtList();
        for (int i = 0; i < inventory.size(); i++) {
            if (!inventory.get(i).isEmpty()) {
                NbtCompound itemTag = new NbtCompound();
                itemTag.putInt("TagKeySlot", i);
                inventory.get(i).writeNbt(itemTag);
                itemListTag.add(itemTag);
            }
        }

        nbtCompound.put("TagKeyItemList", itemListTag);
        nbtCompound.putInt("TagKeySize", inventory.size());

        return nbtCompound;
    }

    public void readNbt(NbtCompound tag) {
        setSize(tag.contains("TagKeySize", NbtElement.INT_TYPE) ? tag.getInt("TagKeySize") : inventory.size());
        NbtList itemListTag = tag.getList("TagKeyItemList", NbtElement.COMPOUND_TYPE);
        for (int i = 0; i < itemListTag.size(); i++) {
            NbtCompound itemTag = itemListTag.getCompound(i);
            int slot = itemTag.getInt("TagKeySlot");
            if (slot >= 0 && slot <= inventory.size()) {
                inventory.set(slot, ItemStack.fromNbt(itemTag));
            }
        }

        onInventoryLoaded();
    }

    protected void onContentsChanged(int slot)
    {

    }

}
