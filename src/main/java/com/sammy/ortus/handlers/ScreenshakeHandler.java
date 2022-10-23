package com.sammy.ortus.handlers;

import com.sammy.ortus.config.ClientConfig;
import com.sammy.ortus.systems.screenshake.ScreenshakeInstance;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.Camera;
import net.minecraft.util.math.noise.PerlinNoiseSampler;
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
		double sum = Math.min(INSTANCES.stream().mapToDouble(i1 -> i1.updateIntensity(camera, random)).sum(), ClientConfig.SCREENSHAKE_INTENSITY);

		intensity = (float) Math.pow(sum, 3);
		INSTANCES.removeIf(i -> i.progress >= i.duration);
	}

	public static void addScreenshake(ScreenshakeInstance instance) {
		INSTANCES.add(instance);
	}

	public static float randomizeOffset(RandomGenerator random) {
		PerlinNoiseSampler sampler = new PerlinNoiseSampler(random);
		float min = -intensity * 2;
		float max = intensity * 2;
		return min >= max ? min : (float) sampler.sample((MinecraftClient.getInstance().world.getTime() + MinecraftClient.getInstance().getTickDelta())/20, 0, 0) * (max - min) + min;
	}
}
