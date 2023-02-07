package com.sammy.lodestone.component;

import dev.onyxstudios.cca.api.v3.component.sync.AutoSyncedComponent;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.NbtCompound;

public class LodestonePlayerComponent implements AutoSyncedComponent {
	private final PlayerEntity player;

	public boolean firstTimeJoin;

	public LodestonePlayerComponent(PlayerEntity player) {
		this.player = player;
	}

	@Override
	public void readFromNbt(NbtCompound tag) {
		firstTimeJoin = tag.getBoolean("firstTimeJoin");
	}

	@Override
	public void writeToNbt(NbtCompound tag) {
		tag.putBoolean("firstTimeJoin", firstTimeJoin);
	}
}
