package com.sammy.lodestone.systems.recipe;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.sammy.lodestone.helpers.ItemHelper;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.recipe.Ingredient;
import net.minecraft.util.JsonHelper;

import java.util.List;
import java.util.Optional;

public class IngredientWithCount implements IRecipeComponent{
	public final Ingredient ingredient;
	public final int count;

	public IngredientWithCount(Ingredient ingredient, int count) {
		this.ingredient = ingredient;
		this.count = count;
	}

	@Override
	public ItemStack getStack() {
		return new ItemStack(getItem(), getCount(), Optional.ofNullable(ingredient.getMatchingStacks()[0].getNbt()));
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
		return count;
	}

	@Override
	public boolean matches(ItemStack stack) {
		return ingredient.test(stack) && stack.getCount() >= getCount();
	}

	public static IngredientWithCount read(PacketByteBuf buffer) {
		Ingredient ingredient = Ingredient.fromPacket(buffer);
		int count = buffer.readByte();
		return new IngredientWithCount(ingredient, count);
	}

	public void write(PacketByteBuf buffer) {
		ingredient.write(buffer);
		buffer.writeByte(count);
	}

	public static IngredientWithCount fromJson(JsonObject object) {
		Ingredient input = object.has("ingredient_list") ? Ingredient.fromJson(object.get("ingredient_list")) : Ingredient.fromJson(object);
		int count = JsonHelper.getInt(object, "count", 1);
		return new IngredientWithCount(input, count);
	}

	public JsonObject toJson() {
		JsonObject object = new JsonObject();
		JsonElement serialize = ingredient.toJson();
		if (serialize.isJsonObject()) {
			object = serialize.getAsJsonObject();
		} else {
			object.add("ingredient_list", ingredient.toJson());
		}
		object.addProperty("count", count);
		return object;
	}
}
