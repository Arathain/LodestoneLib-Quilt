package com.sammy.ortus;

import com.sammy.ortus.config.ClientConfig;
import com.sammy.ortus.handlers.RenderHandler;
import com.sammy.ortus.network.screenshake.PositionedScreenshakePacket;
import com.sammy.ortus.network.screenshake.ScreenshakePacket;
import com.sammy.ortus.setup.OrtusRenderLayers;
import eu.midnightdust.lib.config.MidnightConfig;
import org.quiltmc.loader.api.ModContainer;
import org.quiltmc.qsl.base.api.entrypoint.client.ClientModInitializer;
import org.quiltmc.qsl.networking.api.client.ClientPlayNetworking;

import static com.sammy.ortus.OrtusLib.MODID;

public class OrtusLibClient implements ClientModInitializer {
	@Override
	public void onInitializeClient(ModContainer mod) {
		MidnightConfig.init(MODID, ClientConfig.class);

		OrtusRenderLayers.yea();
		RenderHandler.init();

		ClientPlayNetworking.registerGlobalReceiver(ScreenshakePacket.ID, (client, handler, buf, responseSender) -> new ScreenshakePacket(buf).apply(client.getNetworkHandler()));
		ClientPlayNetworking.registerGlobalReceiver(PositionedScreenshakePacket.ID, (client, handler, buf, responseSender) -> PositionedScreenshakePacket.fromBuf(buf).apply(client.getNetworkHandler()));
	}
}
