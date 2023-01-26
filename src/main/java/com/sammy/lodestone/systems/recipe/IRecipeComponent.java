package com.sammy.lodestone.systems.recipe;

import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;

import java.util.List;

public interface IRecipeComponent {
	ItemStack getStack();

	List<ItemStack> getStacks();

	Item getItem();

	int getCount();

	default boolean isValid() {
		return !getStack().isOf(Items.BARRIER);
	}

	boolean matches(ItemStack stack);
}
