package com.sammy.ortus.systems.screenshake;

import net.minecraft.client.render.Camera;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.math.Vec3f;

public class PositionedScreenshakeInstance extends ScreenshakeInstance{
	public Vec3d position;
	public float falloffDistance;
	public float maxDistance;

	public PositionedScreenshakeInstance(Vec3d position, float falloffDistance, float maxDistance, float intensity, float falloffTransformSpeed, int timeBeforeFastFalloff, float slowFalloff, float fastFalloff) {
		super(intensity, falloffTransformSpeed, timeBeforeFastFalloff, slowFalloff, fastFalloff);
		this.position = position;
		this.falloffDistance = falloffDistance;
		this.maxDistance = maxDistance;
	}

	@Override
	public float updateIntensity(Camera camera, float falloff) {
		float intensity = super.updateIntensity(camera, falloff);
		float distance = (float) position.distanceTo(camera.getPos());
		if (distance > maxDistance)
		{
			return 0;
		}
		float distanceMultiplier = 1;
		if (distance > falloffDistance)
		{
			float remaining = maxDistance-falloffDistance;
			float current = distance-falloffDistance;
			distanceMultiplier = 1-current/remaining;
		}
		Vec3f lookDirection = camera.getHorizontalPlane();
		Vec3d directionToScreenshake = position.subtract(camera.getPos()).normalize();
		float angle = lookDirection.dot(new Vec3f(directionToScreenshake));
		if (angle < 0)
		{
			return 0;
		}
		return intensity * distanceMultiplier * angle;
	}
}
