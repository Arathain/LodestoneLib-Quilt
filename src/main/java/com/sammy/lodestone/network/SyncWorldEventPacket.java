package com.sammy.lodestone.network;

import com.sammy.lodestone.LodestoneLib;
import com.sammy.lodestone.handlers.WorldEventHandler;
import com.sammy.lodestone.setup.worldevent.LodestoneWorldEventTypeRegistry;
import com.sammy.lodestone.systems.worldevent.WorldEventType;
import io.netty.buffer.Unpooled;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayNetworkHandler;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.Identifier;
import org.quiltmc.qsl.networking.api.PacketSender;
import org.quiltmc.qsl.networking.api.ServerPlayNetworking;

public class SyncWorldEventPacket {
	public static final Identifier ID = new Identifier(LodestoneLib.MODID, "world_event");

	public static void send(PlayerEntity player, String type, boolean start, NbtCompound eventData) {
		PacketByteBuf buf = new PacketByteBuf(Unpooled.buffer());

		buf.writeString(type);
		buf.writeBoolean(start);
		buf.writeNbt(eventData);

		ServerPlayNetworking.send((ServerPlayerEntity) player, ID, buf);
	}


	public static void handle(MinecraftClient client, ClientPlayNetworkHandler network, PacketByteBuf buf, PacketSender sender) {
		String type = buf.readString();
		boolean start = buf.readBoolean();
		NbtCompound eventData = buf.readNbt();
		ClientWorld world = MinecraftClient.getInstance().world;
		client.execute(() -> {
			WorldEventType eventType = LodestoneWorldEventTypeRegistry.EVENT_TYPES.get(type);
			WorldEventHandler.addWorldEvent(world, start, eventType.createInstance(eventData));
		});
	}
}
