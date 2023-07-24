package team.lodestar.lodestone.helpers;

import team.lodestar.lodestone.network.screenshake.PositionedScreenshakePacket;
import team.lodestar.lodestone.setup.LodestoneParticles;
import team.lodestar.lodestone.systems.rendering.particle.Easing;
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
import team.lodestar.lodestone.systems.rendering.particle.LodestoneWorldParticleTextureSheet;
import team.lodestar.lodestone.systems.rendering.particle.WorldParticleBuilder;
import team.lodestar.lodestone.systems.rendering.particle.data.ColorParticleData;
import team.lodestar.lodestone.systems.rendering.particle.data.GenericParticleData;
import team.lodestar.lodestone.systems.rendering.particle.data.SpinParticleData;

import java.awt.*;

public class OrtTestItem extends Item {
	public OrtTestItem(Settings settings) {
		super(settings);
	}

	@Override
	public ActionResult useOnBlock(ItemUsageContext context) {
		if(context.getWorld() instanceof ServerWorld s) {
			PlayerEntity user = context.getPlayer();
			s.getPlayers().stream().filter(player -> player.getWorld().isChunkLoaded(new ChunkPos(user.getBlockPos()).x, new ChunkPos(user.getBlockPos()).z)).forEach(players -> {
				PacketByteBuf buf = new PacketByteBuf(Unpooled.buffer());
				new PositionedScreenshakePacket(70, Vec3d.ofCenter(context.getBlockPos()),20f, 0.3f, 25f, Easing.CIRC_IN).setIntensity(0f, 1f, 0f).setEasing(Easing.CIRC_OUT, Easing.CIRC_IN).write(buf);
				ServerPlayNetworking.send(players, PositionedScreenshakePacket.ID, buf);
			});
		}
		return super.useOnBlock(context);
	}
}
