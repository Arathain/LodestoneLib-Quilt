package com.sammy.ortus.component;

import com.sammy.ortus.handlers.FireEffectHandler;
import com.sammy.ortus.systems.fireeffect.FireEffectInstance;
import dev.onyxstudios.cca.api.v3.component.sync.AutoSyncedComponent;
import net.minecraft.entity.Entity;
import net.minecraft.nbt.NbtCompound;

public class OrtusEntityComponent implements AutoSyncedComponent {

	public OrtusEntityComponent(Entity entity) {}

	public FireEffectInstance fireEffectInstance;

	@Override
	public void readFromNbt(NbtCompound tag) {
		FireEffectHandler.readNbt(this, tag);
	}

	@Override
	public void writeToNbt(NbtCompound tag) {
		FireEffectHandler.writeNbt(this, tag);
	}
}
