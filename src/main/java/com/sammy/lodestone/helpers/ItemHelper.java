package com.sammy.lodestone.helpers;


import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.Entity;
import net.minecraft.entity.ItemEntity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

import java.util.ArrayList;
import java.util.List;

public final class ItemHelper {
	public static void applyEnchantments(LivingEntity user, Entity target, ItemStack stack) {
		EnchantmentHelper.Consumer consumer = (enchantment, level) ->
				enchantment.onTargetDamaged(user, target, level);
		if (user != null) {
			EnchantmentHelper.forEachEnchantment(consumer, user.getItemsEquipped());
		}

		if (user instanceof PlayerEntity) {
			EnchantmentHelper.forEachEnchantment(consumer, stack);
		}
	}
	public static ArrayList<ItemStack> copyWithNewCount(List<ItemStack> stacks, int newCount) {
		ArrayList<ItemStack> newStacks = new ArrayList<>();
		for (ItemStack stack : stacks) {
			ItemStack copy = stack.copy();
			copy.setCount(newCount);
			newStacks.add(copy);
		}
		return newStacks;
	}
	public static ItemStack copyWithNewCount(ItemStack stack, int newCount) {
		ItemStack newStack = stack.copy();
		newStack.setCount(newCount);
		return newStack;
	}

	public static void giveItemToEntity(ItemStack item, LivingEntity entity) {
		if (entity instanceof PlayerEntity) {
			((PlayerEntity) entity).giveItemStack(item);
		} else {
			ItemEntity itemEntity = new ItemEntity(entity.world, entity.getX(), entity.getY() + 0.5, entity.getZ(), item);
			itemEntity.setPickupDelay(40);
			itemEntity.setVelocity(itemEntity.getVelocity().multiply(0, 1, 0));
			entity.world.spawnEntity(itemEntity);
		}
	}
}
