package com.sammy.lodestone.systems.recipe;

import net.minecraft.inventory.Inventory;
import net.minecraft.item.ItemStack;
import net.minecraft.recipe.Recipe;
import net.minecraft.world.World;

public abstract class ILodestoneRecipe implements Recipe<Inventory> {
	@Deprecated
	@Override
	public boolean matches(Inventory inv, World world) {
		return false;
	}

	@Deprecated
	@Override
	public ItemStack craft(Inventory inv) {
		return ItemStack.EMPTY;
	}

	@Deprecated
	@Override
	public boolean fits(int width, int height) {
		return false;
	}

	@Deprecated
	@Override
	public ItemStack getOutput() {
		return ItemStack.EMPTY;
	}

	@Override
	public boolean isIgnoredInRecipeBook() {
		return true;
	}

}
