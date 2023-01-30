package com.sammy.lodestone.helpers.util;


import net.minecraft.client.MinecraftClient;

public class AnimationTickHolder {

	private static int ticks;
	private static int pausedTicks;


	public static void tick() {
		if (!MinecraftClient.getInstance()
				.isPaused()) {
			ticks = (ticks + 1) % 1_728_000; // wrap around every 24 hours so we maintain enough floating point precision
		} else {
			pausedTicks = (pausedTicks + 1) % 1_728_000;
		}
	}


	public static float getPartialTicks() {
		MinecraftClient mc = MinecraftClient.getInstance();
		return (mc.isPaused() ? 1 : mc.getTickDelta());
	}
}
