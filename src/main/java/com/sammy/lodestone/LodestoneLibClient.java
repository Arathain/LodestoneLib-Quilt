package com.sammy.lodestone;

import com.sammy.lodestone.config.ClientConfig;
import com.sammy.lodestone.handlers.RenderHandler;
import com.sammy.lodestone.handlers.ScreenParticleHandler;
import com.sammy.lodestone.network.screenshake.PositionedScreenshakePacket;
import com.sammy.lodestone.network.screenshake.ScreenshakePacket;
import com.sammy.lodestone.setup.LodestoneRenderLayers;
import eu.midnightdust.lib.config.MidnightConfig;
import net.minecraft.item.Item;
import net.minecraft.item.Items;
import net.minecraft.util.Pair;
import org.quiltmc.loader.api.ModContainer;
import org.quiltmc.loader.api.QuiltLoader;
import org.quiltmc.qsl.base.api.entrypoint.client.ClientModInitializer;
import org.quiltmc.qsl.networking.api.client.ClientPlayNetworking;

import static com.sammy.lodestone.LodestoneLib.MODID;

public class LodestoneLibClient implements ClientModInitializer {
	@Override
	public void onInitializeClient(ModContainer mod) {
		MidnightConfig.init(MODID, ClientConfig.class);

		LodestoneRenderLayers.yea();
		RenderHandler.init();
		ScreenParticleHandler.registerParticleEmitters();


		ClientPlayNetworking.registerGlobalReceiver(ScreenshakePacket.ID, (client, handler, buf, responseSender) -> new ScreenshakePacket(buf).apply(client.getNetworkHandler()));
		ClientPlayNetworking.registerGlobalReceiver(PositionedScreenshakePacket.ID, (client, handler, buf, responseSender) -> PositionedScreenshakePacket.fromBuf(buf).apply(client.getNetworkHandler()));
	}
}
