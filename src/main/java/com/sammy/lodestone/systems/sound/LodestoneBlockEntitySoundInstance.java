package com.sammy.lodestone.systems.sound;

import com.sammy.lodestone.systems.blockentity.LodestoneBlockEntity;
import net.minecraft.client.sound.MovingSoundInstance;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvent;
import net.minecraft.util.random.RandomGenerator;

public class LodestoneBlockEntitySoundInstance<T extends LodestoneBlockEntity> extends MovingSoundInstance {

	public T blockEntity;

	public LodestoneBlockEntitySoundInstance(T blockEntity, SoundEvent soundEvent, float volume, float pitch, RandomGenerator randomSource) {
		super(soundEvent, SoundCategory.BLOCKS, randomSource);
		this.blockEntity = blockEntity;
		this.volume = volume;
		this.pitch = pitch;
		this.repeatDelay = 0;
		this.repeat = true;
	}

	@Override
	public void tick() {
		if (blockEntity.isRemoved()) {
			setDone();
		}
	}
}
