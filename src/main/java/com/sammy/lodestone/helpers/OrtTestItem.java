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
			s.getPlayers().forEach(players -> {
				if (players.getWorld().isChunkLoaded(new ChunkPos(user.getBlockPos()).x, new ChunkPos(user.getBlockPos()).z)) {
					PacketByteBuf buf = new PacketByteBuf(Unpooled.buffer());
					new PositionedScreenshakePacket(70, Vec3d.ofCenter(context.getBlockPos()), 20f, 0.3f, 25f, Easing.CIRC_IN).setIntensity(0f, 1f, 0f).setEasing(Easing.CIRC_OUT, Easing.CIRC_IN).write(buf);
					ServerPlayNetworking.send(players, PositionedScreenshakePacket.ID, buf);
				}
			});
		}
		return super.useOnBlock(context);
	}
}
