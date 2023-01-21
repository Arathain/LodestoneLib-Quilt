package com.sammy.lodestone.forge;

import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.server.network.ServerPlayerEntity;
import org.jetbrains.annotations.NotNull;

public class PlayerMainInvWrapper extends RangedWrapper {
    private final PlayerInventory inventoryPlayer;

    public PlayerMainInvWrapper(PlayerInventory inv) {
        super(new InvWrapper(inv), 0, inv.main.size());
        inventoryPlayer = inv;
    }

    @Override
    @NotNull
    public ItemStack insertItemStack(int slot, @NotNull ItemStack stack, boolean simulate)
    {
        ItemStack rest = super.insertItemStack(slot, stack, simulate);
        if (rest.getCount()!= stack.getCount())
        {
            // the stack in the slot changed, animate it
            ItemStack inSlot = getStack(slot);
            if(!inSlot.isEmpty())
            {
                if (getInventoryPlayer().player.world.isClient())
                {
                    inSlot.setCooldown(5);
                }
                else if(getInventoryPlayer().player instanceof ServerPlayerEntity) {
                    getInventoryPlayer().player.currentScreenHandler.sendContentUpdates();
                }
            }
        }
        return rest;
    }

    public PlayerInventory getInventoryPlayer() {
        return inventoryPlayer;
    }
}
