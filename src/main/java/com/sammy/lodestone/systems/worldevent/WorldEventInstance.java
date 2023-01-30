package com.sammy.lodestone.systems.worldevent;

import com.sammy.lodestone.component.LodestoneComponents;
import com.sammy.lodestone.network.SyncWorldEventPacket;
import com.sammy.lodestone.setup.worldevent.LodestoneWorldEventTypeRegistry;
import dev.onyxstudios.cca.api.v3.component.sync.AutoSyncedComponent;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.world.World;
import org.quiltmc.qsl.networking.api.PlayerLookup;

import java.util.UUID;

public class WorldEventInstance implements AutoSyncedComponent {
	public UUID uuid; //TODO: figure out why this is here.
	public WorldEventType type;
	public boolean discarded;

	public WorldEventInstance(WorldEventType type) {
		this.uuid = UUID.randomUUID();
		this.type = type;
	}

	/**
	 * Syncs the world event to all players.
	 */
	public void sync(World world) {
		if (!world.isClient() && isClientSynced()) {
			LodestoneComponents.LODESTONE_WORLD_COMPONENT.sync(world);
		}
	}

	/**
	 * Should this event exist on the client? It will be automatically synced in {@link #sync(World)}
	 */
	public boolean isClientSynced() {
		return false;
	}

	public void start(World world) {
	}


	public void tick(World world) {

	}

	public void end(World world) {
		discarded = true;
	}

	@Override
	public void writeToNbt(NbtCompound tag) {
		tag.putUuid("uuid", uuid);
		tag.putString("type", type.id);
		tag.putBoolean("discarded", discarded);

	}

	@Override
	public void readFromNbt(NbtCompound tag) {
		uuid = tag.getUuid("uuid");
		type = LodestoneWorldEventTypeRegistry.EVENT_TYPES.get(tag.getString("type"));
		discarded = tag.getBoolean("discarded");
	}

	public WorldEventInstance readFromNbtInstance(NbtCompound tag) {
		readFromNbt(tag);
		return this;
	}

	public NbtCompound writeToNbtInstance(NbtCompound tag) {
		writeToNbt(tag);
		return tag;
	}

	public static <T extends WorldEventInstance> void sync(T instance, ServerPlayerEntity player) {
		PlayerLookup.tracking(player).forEach(track -> SyncWorldEventPacket.send(track, instance.type.id, false, instance.writeToNbtInstance(new NbtCompound())));
	}
}
