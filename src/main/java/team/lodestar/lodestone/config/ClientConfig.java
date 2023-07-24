package team.lodestar.lodestone.config;

import eu.midnightdust.lib.config.MidnightConfig;

public class ClientConfig extends MidnightConfig {
	@Entry
	public static boolean ENABLE_SCREEN_PARTICLES = true;
	@Entry
	public static boolean DELAYED_RENDERING = true;
	@Entry(min=0,max=5)
	public static double SCREENSHAKE_INTENSITY = 1;

}
