package com.sammy.ortus.config;

import eu.midnightdust.lib.config.MidnightConfig;

public class ClientConfig extends MidnightConfig {
	@Entry
	public static boolean DELAYED_RENDERING = true;
	@Entry(min=0,max=5)
	public static double SCREENSHAKE_INTENSITY = 1;

	@Entry(min=0,max=1)
	public static double FIRE_OVERLAY_OFFSET = 0;
}
