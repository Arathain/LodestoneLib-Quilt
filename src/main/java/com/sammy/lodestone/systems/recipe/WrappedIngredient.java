package com.sammy.lodestone.systems.recipe;

import com.sammy.lodestone.helpers.ItemHelper;
import com.sammy.lodestone.systems.recipe.IRecipeComponent;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.recipe.Ingredient;

import java.util.List;
import java.util.Optional;

public class WrappedIngredient implements IRecipeComponent {
	public final Ingredient ingredient;

	public WrappedIngredient(Ingredient ingredient) {
		this.ingredient = ingredient;
	}

	@Override
	public ItemStack getStack() {
		return new ItemStack(getItem(), getCount(), Optional.of(ingredient.getMatchingStacks()[0].getNbt()));
	}

	@Override
	public List<ItemStack> getStacks() {
		return ItemHelper.copyWithNewCount(List.of(ingredient.getMatchingStacks()), getCount());
	}

	@Override
	public Item getItem() {
		return ingredient.getMatchingStacks()[0].getItem();
	}

	@Override
	public int getCount() {
		return 1;
	}

	@Override
	public boolean matches(ItemStack stack) {
		return ingredient.test(stack) && stack.getCount() >= getCount();
	}
}
