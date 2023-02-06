package com.sammy.lodestone.systems.client;

import net.minecraft.client.MinecraftClient;

public class ClientTickCounter {
	public static long ticksInGame = 0L;
	public static float partialTicks = 0.0F;

	public static float getTotal() {
		return (float) ticksInGame + partialTicks;
	}

	public static void renderTick(float tick) {
		partialTicks = tick;
	}

	public static void clientTick() {
		if (!MinecraftClient.getInstance().isPaused()) {
			++ticksInGame;
			partialTicks = 0.0F;
		}
	}
}
