package team.lodestar.lodestone.systems.screenshake;

import team.lodestar.lodestone.systems.rendering.particle.Easing;
import net.minecraft.client.render.Camera;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.random.RandomGenerator;
import org.joml.Vector3f;

public class PositionedScreenshakeInstance extends ScreenshakeInstance{
	public Vec3d position;
	public float falloffDistance;
	public float maxDistance;
	public float minDot;
	public final Easing falloffEasing;

	public PositionedScreenshakeInstance(int duration, Vec3d position, float falloffDistance, float minDot, float maxDistance, Easing falloffEasing) {
		super(duration);
		this.position = position;
		this.falloffDistance = falloffDistance;
		this.minDot = minDot;
		this.maxDistance = maxDistance;
		this.falloffEasing = falloffEasing;
	}

	@Override
	public float updateIntensity(Camera camera, RandomGenerator random) {
		float intensity = super.updateIntensity(camera, random);
		float distance = (float) position.distanceTo(camera.getPos());
		if (distance > maxDistance) {
			return 0;
		}
		float distanceMultiplier = 1;
		if (distance > falloffDistance) {
			float remaining = maxDistance-falloffDistance;
			float current = distance-falloffDistance;
			distanceMultiplier = 1-current/remaining;
		}

		Vector3f lookDirection = camera.getHorizontalPlane();
		Vec3d directionToScreenshake = position.subtract(camera.getPos()).normalize();
		float angle = Math.max(minDot, lookDirection.dot(directionToScreenshake.toVector3f()));
		return intensity * distanceMultiplier * angle;
	}
}
