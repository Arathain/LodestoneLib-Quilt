package com.sammy.lodestone.component;

import com.sammy.lodestone.handlers.WorldEventHandler;
import com.sammy.lodestone.systems.worldevent.WorldEventInstance;
import dev.onyxstudios.cca.api.v3.component.sync.AutoSyncedComponent;
import dev.onyxstudios.cca.api.v3.component.tick.CommonTickingComponent;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.world.World;

import java.util.ArrayList;

public class LodestoneWorldComponent implements AutoSyncedComponent, CommonTickingComponent {
	public World world;
	public final ArrayList<WorldEventInstance> activeWorldEvents = new ArrayList<>();
	public final ArrayList<WorldEventInstance> inboundWorldEvents = new ArrayList<>();

	public LodestoneWorldComponent(World world){
		this.world = world;
	}


	@Override
	public void tick() {
		WorldEventHandler.worldTick(world);
		if(world.isClient()){
			WorldEventHandler.tick(world);
		}
	}

	@Override
	public void readFromNbt(NbtCompound nbt) {
		WorldEventHandler.deserializeNBT(this, nbt);
	}

	@Override
	public void writeToNbt(NbtCompound nbt) {
		WorldEventHandler.serializeNBT(this, nbt);
	}
}
