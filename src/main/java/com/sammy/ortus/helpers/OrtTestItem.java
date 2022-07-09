package com.sammy.ortus.helpers;

import com.sammy.ortus.network.screenshake.PositionedScreenshakePacket;
import com.sammy.ortus.network.screenshake.ScreenshakePacket;
import com.sammy.ortus.systems.rendering.particle.Easing;
import io.netty.buffer.Unpooled;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.Hand;
import net.minecraft.util.TypedActionResult;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.world.World;
import org.quiltmc.qsl.networking.api.ServerPlayNetworking;

public class OrtTestItem extends Item {
	public OrtTestItem(Settings settings) {
		super(settings);
	}

	@Override
	public TypedActionResult<ItemStack> use(World world, PlayerEntity user, Hand hand) {
		if(world instanceof ServerWorld s) {
			s.getPlayers(players -> players.getWorld().isChunkLoaded(new ChunkPos(user.getBlockPos()).x, new ChunkPos(user.getBlockPos()).z)).forEach(players -> {
				PacketByteBuf buf = new PacketByteBuf(Unpooled.buffer());
				new ScreenshakePacket(40).setIntensity(0.4f, 1f).setEasing(Easing.EXPO_OUT).write(buf);
				ServerPlayNetworking.send(players, PositionedScreenshakePacket.ID, buf);
			});
		}
		return super.use(world, user, hand);
	}
}
