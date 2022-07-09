package com.sammy.ortus.handlers;

import com.sammy.ortus.config.ClientConfig;
import com.sammy.ortus.systems.screenshake.ScreenshakeInstance;
import net.minecraft.client.render.Camera;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.random.RandomGenerator;

import java.util.ArrayList;
public class ScreenshakeHandler {

	public static final ArrayList<ScreenshakeInstance> INSTANCES = new ArrayList<>();
	public static float intensity;
	public static float yawOffset;
	public static float pitchOffset;

	public static void cameraTick(Camera camera, RandomGenerator random) {
		if (intensity >= 0.1) {
			yawOffset = randomizeOffset(random);
			pitchOffset = randomizeOffset(random);
			camera.setRotation(camera.getYaw() + yawOffset, camera.getPitch() + pitchOffset);
		}
	}

	public static void clientTick(Camera camera, RandomGenerator random) {
		intensity = (float) (INSTANCES.stream().mapToDouble(i1 -> i1.updateIntensity(camera, random)).sum() * ClientConfig.SCREENSHAKE_INTENSITY);
		INSTANCES.removeIf(i -> i.progress >= i.duration);
	}

	public static void addScreenshake(ScreenshakeInstance instance) {
		INSTANCES.add(instance);
	}

	public static float randomizeOffset(RandomGenerator random) {
		return MathHelper.nextFloat(random, -intensity * 2, intensity * 2);
	}
}
