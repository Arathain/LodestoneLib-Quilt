package com.sammy.ortus.systems.fireeffect;


import net.minecraft.entity.Entity;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.sound.SoundEvents;

public class FireEffectType {
	public final String id;

	protected final int damage;
	protected final int tickInterval;

	public FireEffectType(String id, int damage, int tickInterval) {
		this.id = id;
		this.damage = damage;
		this.tickInterval = tickInterval;
	}

	public int getDamage(FireEffectInstance instance) {
		return damage;
	}

	public int getTickInterval(FireEffectInstance instance) {
		return tickInterval;
	}

	public void extinguish(FireEffectInstance instance, Entity target) {
		instance.duration = 0;

		//TODO: test if the sound actually plays
		target.world.playSound(null, target.getBlockPos(), SoundEvents.BLOCK_FIRE_EXTINGUISH, target.getSoundCategory(),0.7F, 1.6F + (target.world.getRandom().nextFloat() - target.world.getRandom().nextFloat()) * 0.4F);
	}

	public void tick(FireEffectInstance instance, Entity target) {
		target.damage(DamageSource.ON_FIRE, getDamage(instance));
	}

	public boolean isValid(FireEffectInstance instance) {
		return instance.duration > 0;
	}
}
