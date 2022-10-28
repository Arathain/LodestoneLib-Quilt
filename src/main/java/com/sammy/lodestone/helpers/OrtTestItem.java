package com.sammy.lodestone.helpers;

import com.sammy.lodestone.network.screenshake.PositionedScreenshakePacket;
import com.sammy.lodestone.systems.rendering.particle.Easing;
import io.netty.buffer.Unpooled;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemUsageContext;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.ActionResult;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.util.math.Vec3d;
import org.quiltmc.qsl.networking.api.ServerPlayNetworking;

public class OrtTestItem extends Item {
	public OrtTestItem(Settings settings) {
		super(settings);
	}

	@Override
	public ActionResult useOnBlock(ItemUsageContext context) {
		if(context.getWorld() instanceof ServerWorld s) {
			PlayerEntity user = context.getPlayer();
			s.getPlayers(players -> players.getWorld().isChunkLoaded(new ChunkPos(user.getBlockPos()).x, new ChunkPos(user.getBlockPos()).z)).forEach(players -> {
				PacketByteBuf buf = new PacketByteBuf(Unpooled.buffer());
				new PositionedScreenshakePacket(20, Vec3d.ofCenter(context.getBlockPos()),40f, 0.8f, 128f, Easing.EXPO_OUT).setIntensity(0f, 0.8f).setEasing(Easing.ELASTIC_OUT, Easing.EXPO_OUT).write(buf);
				ServerPlayNetworking.send(players, PositionedScreenshakePacket.ID, buf);
			});
		}
		return super.useOnBlock(context);
	}
}
