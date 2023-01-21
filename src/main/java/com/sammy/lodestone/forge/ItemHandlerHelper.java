package com.sammy.lodestone.forge;

import net.minecraft.entity.ItemEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.world.World;
import org.jetbrains.annotations.NotNull;

public class ItemHandlerHelper {
    public static boolean canItemStacksStack(ItemStack first, ItemStack second) {
        if (first.isEmpty() || !first.isItemEqualIgnoreDamage(second) || first.hasNbt() != second.hasNbt()) return false;

        return !first.hasNbt() || first.getName().equals(second.getName());
    }

    public static ItemStack copyStackWithSize(ItemStack stack, int size) {
        if (size == 0) return ItemStack.EMPTY;
        ItemStack copy = stack.copy();
        copy.setCount(size);
        return copy;
    }

    @NotNull
    public static ItemStack insertItem(ItemHandler dest, @NotNull ItemStack stack, boolean simulate) {
        if (dest == null || stack.isEmpty())
            return stack;

        for (int i = 0; i < dest.size(); i++) {
            stack = dest.insertItemStack(i, stack, simulate);
            if (stack.isEmpty())
            {
                return ItemStack.EMPTY;
            }
        }
        return stack;
    }

    public static boolean canItemStacksStackRelaxed(@NotNull ItemStack a, @NotNull ItemStack b) {
        if (a.isEmpty() || b.isEmpty() || a.getItem() != b.getItem())
            return false;

        if (!a.isStackable())
            return false;

        if (a.hasNbt() != b.hasNbt())
            return false;

        return (!a.hasNbt() || a.getNbt().equals(b.getNbt()));
    }

    @NotNull
    public static ItemStack insertItemStacked(ItemHandler inventory, @NotNull ItemStack stack, boolean simulate) {
        if (inventory == null || stack.isEmpty())
            return stack;

        // not stackable -> just insert into a new slot
        if (!stack.isStackable()) {
            return insertItem(inventory, stack, simulate);
        }

        int sizeInventory = inventory.size();

        // go through the inventory and try to fill up already existing items
        for (int i = 0; i < sizeInventory; i++) {
            ItemStack slot = inventory.getStack(i);
            if (canItemStacksStackRelaxed(slot, stack)) {
                stack = inventory.insertItemStack(i, stack, simulate);

                if (stack.isEmpty()) {
                    break;
                }
            }
        }

        // insert remainder into empty slots
        if (!stack.isEmpty()) {
            // find empty slot
            for (int i = 0; i < sizeInventory; i++) {
                if (inventory.getStack(i).isEmpty()) {
                    stack = inventory.insertItemStack(i, stack, simulate);
                    if (stack.isEmpty()) {
                        break;
                    }
                }
            }
        }

        return stack;
    }

    public static void giveItemToPlayer(PlayerEntity player, @NotNull ItemStack stack) {
        giveItemToPlayer(player, stack, -1);
    }

    public static void giveItemToPlayer(PlayerEntity player, @NotNull ItemStack stack, int preferredSlot) {
        if (stack.isEmpty()) return;

        ItemHandler inventory = new PlayerMainInvWrapper(player.getInventory());
        World level = player.world;
        ItemStack remainder = stack;
        if (preferredSlot >= 0 && preferredSlot < inventory.size()) {
            remainder = inventory.insertItemStack(preferredSlot, stack, false);
        }

        if (!remainder.isEmpty()) {
            remainder = insertItemStacked(inventory, remainder, false);
        }

        if (remainder.isEmpty() || remainder.getCount() != stack.getCount()) {
            level.playSound(null, player.getX(), player.getY() + 0.5, player.getZ(),
                SoundEvents.ENTITY_ITEM_PICKUP, SoundCategory.PLAYERS, 0.2F, ((level.random.nextFloat() - level.random.nextFloat()) * 0.7F + 1.0F) * 2.0F);
        }

        if (!remainder.isEmpty() && !level.isClient())
        {
            ItemEntity entityitem = new ItemEntity(level, player.getX(), player.getY() + 0.5, player.getZ(), remainder);
            entityitem.setPickupDelay(40);
            entityitem.setVelocity(entityitem.getVelocity().multiply(0, 1, 0));

            level.spawnEntity(entityitem);
        }
    }
}
