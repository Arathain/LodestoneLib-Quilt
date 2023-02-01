package com.sammy.lodestone;

import eu.midnightdust.lib.config.MidnightConfig;

public class LodestoneLibConfig extends MidnightConfig {

	@Entry public static boolean DELAYED_PARTICLE_RENDERING = true;

	@Entry(min=0D,max=5D) public static double SCREENSHAKE_INTENSITY = 1.0D;

	@Entry public static boolean ENABLE_SCREEN_PARTICLES = true;
}
