package com.sammy.lodestone.systems.item;

import net.minecraft.entity.LivingEntity;
import net.minecraft.item.ItemStack;

public interface IEventResponderItem {

	default void takeDamageEvent(LivingEntity attacker, LivingEntity attacked, ItemStack stack) {

	}

	default void hurtEvent(LivingEntity attacker, LivingEntity target, ItemStack stack) {

	}


	default void killEvent(LivingEntity attacker, LivingEntity target, ItemStack stack) {

	}
}
