package com.sammy.ortus.network;

import com.sammy.ortus.OrtusLib;
import com.sammy.ortus.handlers.FireEffectHandler;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayNetworkHandler;
import net.minecraft.entity.Entity;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.Identifier;
import org.quiltmc.qsl.networking.api.PacketByteBufs;
import org.quiltmc.qsl.networking.api.PacketSender;
import org.quiltmc.qsl.networking.api.ServerPlayNetworking;

import javax.annotation.Nullable;
import java.util.Collection;
import java.util.Objects;


public class ClearFireEffectInstancePacket {
	public static final Identifier ID = OrtusLib.id("clearfireeffect");

	public static void send(@Nullable Entity entity, Collection<ServerPlayerEntity> players) {
		PacketByteBuf buf = PacketByteBufs.create();

		if(entity != null)
			buf.writeInt(entity.getId());

		ServerPlayNetworking.send(players, ID, buf);
	}

	public static void handle(MinecraftClient client, ClientPlayNetworkHandler handler, PacketByteBuf buf, PacketSender sender) {
		int entityId = buf.isReadable() ? buf.readInt() : -1;
		if(MinecraftClient.getInstance().world != null && MinecraftClient.getInstance().world.getEntityById(entityId) != null) {
			FireEffectHandler.setCustomFireInstance(MinecraftClient.getInstance().world.getEntityById(entityId), null);
		}
	}
}
