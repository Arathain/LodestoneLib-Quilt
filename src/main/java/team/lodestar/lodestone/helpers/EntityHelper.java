package team.lodestar.lodestone.helpers;

import net.minecraft.util.math.Vec3d;

import java.util.ArrayList;

public class EntityHelper {
	/**
	 * Tracks the travel path of an entity or other object
	 *
	 * @param pastPositions     An ArrayList that houses all the past positions.
	 * @param currentPosition   The current position to be added to the list.
	 * @param distanceThreshold the minimum distance from the latest PastPos before a new position is added.
	 */
	public static void trackPastPositions(ArrayList<PastPosition> pastPositions, Vec3d currentPosition, float distanceThreshold) {
		for (PastPosition pastPosition : pastPositions) {
			pastPosition.time++;
		}
		if (!pastPositions.isEmpty()) {
			PastPosition latest = pastPositions.get(pastPositions.size() - 1);
			float distance = (float) latest.position.distanceTo(currentPosition);
			if (distance > distanceThreshold) {
				pastPositions.add(new PastPosition(currentPosition, 0));
			}
		} else {
			pastPositions.add(new PastPosition(currentPosition, 0));
		}
	}

	public static class PastPosition {
		public Vec3d position;
		public int time;

		public PastPosition(Vec3d position, int time) {
			this.position = position;
			this.time = time;
		}
	}
}
