package com.sammy.lodestone.config;

import eu.midnightdust.lib.config.MidnightConfig;

public class ClientConfig extends MidnightConfig {
	@Entry(min=0,max=5)
	public static double SCREENSHAKE_INTENSITY = 1;

	@Entry
	public static boolean DELAYED_PARTICLE_RENDERING = true;

	@Entry
	public static boolean ENABLE_SCREEN_PARTICLES = true;

}
