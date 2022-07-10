package com.sammy.ortus.systems.fireeffect;

import com.sammy.ortus.helpers.NBTHelper;
import com.sammy.ortus.setup.OrtusComponents;
import com.sammy.ortus.setup.OrtusFireEffectRegistry;
import net.minecraft.entity.Entity;
import net.minecraft.nbt.NbtCompound;

/**
 * A FireEffectInstance is a custom instance of a fire effect, functioning pretty much exactly as a normal fire effect would do
 * You must register a type and can manage a players fire effect through the {@link com.sammy.ortus.handlers.FireEffectHandler}
 */
public class FireEffectInstance {
	public int duration;
	public final FireEffectType type;

	public FireEffectInstance(FireEffectType type) {
		this.type = type;
	}

	public FireEffectInstance extendDuration(int increase) {
		this.duration += increase;
		return this;
	}

	public FireEffectInstance setDuration(int duration) {
		this.duration = duration;
		return this;
	}

	public FireEffectInstance sync(Entity target) {
		OrtusComponents.ENTITY_COMPONENT.sync(target);
		return this;
	}

	public FireEffectInstance syncDuration(Entity target) {
		OrtusComponents.ENTITY_COMPONENT.sync(target);
		return this;
	}

	public void tick(Entity target) {
		if ((target.inPowderSnow || target.isInsideWaterOrBubbleColumn())) {
			type.extinguish(this, target);
		}
		if (canDamageTarget(target)) {
			duration--;
			if (type.isValid(this) && duration % type.getTickInterval(this) == 0) {
				type.tick(this, target);
			}
		} else {
			duration -= 4;
		}
	}

	public void entityAttack() {

	}

	public boolean canDamageTarget(Entity target) {
		return !target.isFireImmune();
	}

	public boolean isValid() {
		return type.isValid(this);
	}

	public void writeNbt(NbtCompound tag) {
		NbtCompound fireTag = new NbtCompound();
		fireTag.putString("type", type.id);
		fireTag.putInt("duration", duration);
		tag.put("fireEffect", fireTag);
	}

	public static FireEffectInstance readNbt(NbtCompound tag) {
		if (!tag.contains("fireEffect")) {
			return null;
		}
		NbtCompound fireTag = tag.getCompound("fireEffect");
		FireEffectInstance instance = new FireEffectInstance(OrtusFireEffectRegistry.FIRE_TYPES.get(fireTag.getString("type")));
		instance.setDuration(fireTag.getInt("duration"));
		return instance;
	}
}
