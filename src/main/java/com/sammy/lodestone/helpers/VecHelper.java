package com.sammy.lodestone.helpers;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.Camera;
import net.minecraft.client.render.GameRenderer;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.math.*;
import net.minecraft.util.random.RandomGenerator;
import net.minecraft.world.World;
import org.joml.Quaternionf;
import org.joml.Vector3f;

import java.util.ArrayList;

public class VecHelper {
	//TODO: re-implement all NECESSARY functions into the new vecHelper method and remove ones that are not required.
	public static final Vec3d CENTER_OF_ORIGIN = new Vec3d(.5, .5, .5);

	/**
	 * Converts a block position into a Vec3d entry.
	 *
	 * @param pos the block position
	 * @return the vec3 representation.
	 */
	public static Vec3d fromBlockPos(BlockPos pos) {
		return new Vec3d(pos.getX(), pos.getY(), pos.getZ());
	}


	/**
	 * A method that returns a position on the perimeter of a circle around a given Vec3d position
	 *
	 * @param pos      - Defines the center of the circle
	 * @param distance - Defines the radius of your circle
	 * @param current  - Defines the current point we are calculating the position for on the circle
	 * @param total    - Defines the total amount of points in the circle
	 */
	public static Vec3d radialOffset(Vec3d pos, float distance, float current, float total) {
		double angle = current / total * (Math.PI * 2);
		double dx2 = (distance * Math.cos(angle));
		double dz2 = (distance * Math.sin(angle));

		Vec3d vector = new Vec3d(dx2, 0, dz2);
		double x = vector.x * distance;
		double z = vector.z * distance;
		return pos.add(new Vec3d(x, 0, z));
	}

	/**
	 * A method that returns an array list of positions on the perimeter of a circle around a given Vec3d position.
	 * These positions constantly rotate around the center of the circle based on gameTime
	 *
	 * @param pos      - Defines the center of the circle
	 * @param distance - Defines the radius of your circle
	 * @param total    - Defines the total amount of points in the circle
	 * @param gameTime - Defines the current game time value
	 * @param time     - Defines the total time for one position to complete a full rotation cycle
	 */
	public static ArrayList<Vec3d> rotatingRadialOffsets(Vec3d pos, float distance, float total, long gameTime, float time) {
		return rotatingRadialOffsets(pos, distance, distance, total, gameTime, time);
	}

	/**
	 * A method that returns an array list of positions on the perimeter of a sphere around a given Vec3d position.
	 * These positions constantly rotate around the center of the circle based on gameTime.
	 */
	public static ArrayList<Vec3d> rotatingRadialOffsets(Vec3d pos, float distanceX, float distanceZ, float total, long gameTime, float time) {
		ArrayList<Vec3d> positions = new ArrayList<>();
		for (int i = 0; i < total; i++) {
			positions.add(rotatingRadialOffset(pos, distanceX, distanceZ, i, total, gameTime, time));
		}
		return positions;
	}

	/**
	 * A method that returns a single position on the perimeter of a circle around a given Vec3d position.
	 * These positions constantly rotate around the center of the circle based on gameTime
	 */
	public static Vec3d rotatingRadialOffset(Vec3d pos, float distance, float current, float total, long gameTime, float time) {
		return rotatingRadialOffset(pos, distance, distance, current, total, gameTime, time);
	}

	/**
	 * A method that returns a single position on the perimeter of a circle around a given Vec3d position.
	 * These positions constantly rotate around the center of the circle based on gameTime
	 */
	public static Vec3d rotatingRadialOffset(Vec3d pos, float distanceX, float distanceZ, float current, float total, long gameTime, float time) {
		double angle = current / total * (Math.PI * 2);
		angle += ((gameTime % time) / time) * (Math.PI * 2);
		double dx2 = (distanceX * Math.cos(angle));
		double dz2 = (distanceZ * Math.sin(angle));

		Vec3d vector2f = new Vec3d(dx2, 0, dz2);
		double x = vector2f.x * distanceX;
		double z = vector2f.z * distanceZ;
		return pos.add(x, 0, z);
	}

	public static ArrayList<Vec3d> blockOutlinePositions(World world, BlockPos pos) {
		ArrayList<Vec3d> arrayList = new ArrayList<>();
		double d0 = 0.5625D;
		RandomGenerator random = world.random;
		for (Direction direction : Direction.values()) {
			BlockPos blockpos = pos.offset(direction);
			if (!world.getBlockState(blockpos).isOpaqueFullCube(world, blockpos)) {
				Direction.Axis direction$axis = direction.getAxis();
				double d1 = direction$axis == Direction.Axis.X ? 0.5D + d0 * (double) direction.getOffsetX() : (double) random.nextFloat();
				double d2 = direction$axis == Direction.Axis.Y ? 0.5D + d0 * (double) direction.getOffsetY() : (double) random.nextFloat();
				double d3 = direction$axis == Direction.Axis.Z ? 0.5D + d0 * (double) direction.getOffsetZ() : (double) random.nextFloat();
				arrayList.add(new Vec3d((double) pos.getX() + d1, (double) pos.getY() + d2, (double) pos.getZ() + d3));
			}
		}
		return arrayList;
	}

	public static Vec3d axisAlignedPlaneOf(Vec3d vec) {
		vec = vec.normalize();
		return new Vec3d(1, 1, 1).subtract(Math.abs(vec.x), Math.abs(vec.y), Math.abs(vec.z));
	}

	public static Vec3d rotate(Vec3d vec, double deg, Direction.Axis axis) {
		if (deg == 0)
			return vec;
		if (vec == Vec3d.ZERO)
			return vec;

		float angle = (float) (deg / 180f * Math.PI);
		double sin = MathHelper.sin(angle);
		double cos = MathHelper.cos(angle);
		double x = vec.x;
		double y = vec.y;
		double z = vec.z;

		if (axis == Direction.Axis.X)
			return new Vec3d(x, y * cos - z * sin, z * cos + y * sin);
		if (axis == Direction.Axis.Y)
			return new Vec3d(x * cos + z * sin, y, z * cos - x * sin);
		if (axis == Direction.Axis.Z)
			return new Vec3d(x * cos - y * sin, y * cos + x * sin, z);
		return vec;
	}
}
