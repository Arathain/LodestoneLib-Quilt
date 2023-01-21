package com.sammy.lodestone.forge;

import net.minecraft.item.ItemStack;
import org.jetbrains.annotations.NotNull;

public interface ItemHandlerModifiable extends ItemHandler{
    void setStackInSlot(int slot, @NotNull ItemStack stack);
}
