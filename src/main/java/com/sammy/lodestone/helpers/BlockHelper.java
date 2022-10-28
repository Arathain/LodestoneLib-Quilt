package com.sammy.lodestone.helpers;


import net.minecraft.block.BlockState;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.ai.pathing.Path;
import net.minecraft.entity.ai.pathing.PathNode;
import net.minecraft.entity.passive.ParrotEntity;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.state.property.Property;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import net.minecraft.world.chunk.Chunk;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Random;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

/**
 * A collection of various helper methods related to all your blocky needs.
 */
@SuppressWarnings("unused") // don't need warnings about unused, this is a library class
public class BlockHelper {
	// private constructor, prevent instantiation
	private BlockHelper() {
	}


	/**
	 * Copies all properties from oldState to newState, given that an individual property exists on the newState.
	 *
	 * @param oldState - State to copy properties from.
	 * @param newState - State to apply copied property values to.
	 * @return - NewState with adjusted properties.
	 */
	public static BlockState getBlockStateWithExistingProperties(BlockState oldState, BlockState newState) {
		BlockState finalState = newState;
		for (Property<?> property : oldState.getProperties()) {
			if (newState.contains(property)) {
				finalState = newStateWithOldProperty(oldState, finalState, property);
			}
		}
		return finalState;
	}

	/**
	 * Copies BlockState properties from a BlockState already in the world, and replaces it with a newState with matching property values.
	 */
	public static BlockState setBlockStateWithExistingProperties(World level, BlockPos pos, BlockState newState, int flags) {
		BlockState oldState = level.getBlockState(pos);
		BlockState finalState = getBlockStateWithExistingProperties(oldState, newState);
		level.updateListeners(pos, oldState, finalState, flags);
		level.setBlockState(pos, finalState, flags);
		return finalState;
	}

	public static <T extends Comparable<T>> BlockState newStateWithOldProperty(BlockState oldState, BlockState newState, Property<T> property) {
		return newState.with(property, oldState.get(property));
	}

	/**
	 * Saves a block position to nbt.
	 */
	public static void saveBlockPos(NbtCompound compoundNBT, BlockPos pos) {
		compoundNBT.putInt("X", pos.getX());
		compoundNBT.putInt("Y", pos.getY());
		compoundNBT.putInt("Z", pos.getZ());
	}

	/**
	 * Saves a block position to nbt with extra text to differentiate it.
	 */
	public static void saveBlockPos(NbtCompound compoundNBT, BlockPos pos, String extra) {
		compoundNBT.putInt(extra + "_X", pos.getX());
		compoundNBT.putInt(extra + "_Y", pos.getY());
		compoundNBT.putInt(extra + "_Z", pos.getZ());
	}

	/**
	 * Loads a block position from nbt.
	 */
	public static BlockPos loadBlockPos(NbtCompound tag) {
		return tag.contains("X") ? new BlockPos(tag.getInt("X"), tag.getInt("Y"), tag.getInt("Z")) : null;
	}

	/**
	 * Loads a block position from nbt with extra text as input.
	 */
	public static BlockPos loadBlockPos(NbtCompound tag, String extra) {
		return tag.contains(extra + "_X") ? new BlockPos(tag.getInt(extra + "_X"), tag.getInt(extra + "_Y"), tag.getInt(extra + "_Z")) : null;
	}

	/**
	 * Returns a list of block entities within a range, with a predicate for conditional checks.
	 */
	public static <T> Collection<T> getBlockEntities(Class<T> type, World level, BlockPos pos, int range, Predicate<T> predicate) {
		return getBlockEntitiesStream(type, level, pos, range, predicate).collect(Collectors.toSet());
	}

	/**
	 * Returns a list of block entities within a range, with a predicate for conditional checks, as stream
	 */
	public static <T> Stream<T> getBlockEntitiesStream(Class<T> type, World level, BlockPos pos, int range, Predicate<T> predicate) {
		return getBlockEntitiesStream(type, level, pos, range, range, range, predicate);
	}

	/**
	 * Returns a list of block entities within an XZ range, with a predicate for conditional checks.
	 */
	public static <T> Collection<T> getBlockEntities(Class<T> type, World level, BlockPos pos, int x, int z, Predicate<T> predicate) {
		return getBlockEntitiesStream(type, level, pos, x, z, predicate).collect(Collectors.toSet());
	}

	/**
	 * Returns a list of block entities within an XZ range, with a predicate for conditional checks, as stream
	 */
	public static <T> Stream<T> getBlockEntitiesStream(Class<T> type, World level, BlockPos pos, int x, int z, Predicate<T> predicate) {
		return getBlockEntitiesStream(type, level, pos, x, z).filter(predicate);
	}

	/**
	 * Returns a list of block entities within an XYZ range, with a predicate for conditional checks.
	 */
	public static <T> Collection<T> getBlockEntities(Class<T> type, World level, BlockPos pos, int x, int y, int z, Predicate<T> predicate) {
		return getBlockEntitiesStream(type, level, pos, x, y, z, predicate).collect(Collectors.toSet());
	}

	/**
	 * Returns a list of block entities within an XYZ range, with a predicate for conditional checks, as streamq
	 */
	public static <T> Stream<T> getBlockEntitiesStream(Class<T> type, World level, BlockPos pos, int x, int y, int z, Predicate<T> predicate) {
		return getBlockEntitiesStream(type, level, pos, x, y, z).filter(predicate);
	}

	/**
	 * Returns a list of block entities within a radius around a position.
	 */
	public static <T> Collection<T> getBlockEntities(Class<T> type, World level, BlockPos pos, int range) {
		return getBlockEntities(type, level, pos, range, range, range);
	}

	/**
	 * Returns a list of block entities within a radius around a position, as stream
	 *
	 * @param type  - Class of the block entity to search for
	 * @param level - World to search in
	 * @param pos   - Position to search around
	 * @param range - Radius to search in
	 * @return - Stream of block entities
	 */
	public static <T> Stream<T> getBlockEntitiesStream(Class<T> type, World level, BlockPos pos, int range) {
		return getBlockEntitiesStream(type, level, pos, range, range, range);
	}

	/**
	 * Returns a list of block entities within an XZ radius around a position.
	 */
	public static <T> Collection<T> getBlockEntities(Class<T> type, World level, BlockPos pos, int x, int z) {
		return getBlockEntitiesStream(type, level, pos, x, z).collect(Collectors.toSet());
	}

	/**
	 * Returns a list of block entities within an XZ radius around a position, as stream
	 *
	 * @param type  - Class of the block entity to search for
	 * @param level - World to search in
	 * @param pos   - Position to search around
	 * @param x     - Radius to search in X
	 * @param z     - Radius to search in Z
	 * @return - Stream of block entities
	 */
	public static <T> Stream<T> getBlockEntitiesStream(Class<T> type, World level, BlockPos pos, int x, int z) {
		return getBlockEntitiesStream(type, level, new Box((double) pos.getX() - x, pos.getY(), (double) pos.getZ() - z, (double) pos.getX() + x, (double) pos.getY() + 1, (double) pos.getZ() + z));
	}


	/**
	 * Returns a list of block entities within an XYZ radius around a position.
	 */
	public static <T> Collection<T> getBlockEntities(Class<T> type, World level, BlockPos pos, int x, int y, int z) {
		return getBlockEntitiesStream(type, level, pos, x, y, z).collect(Collectors.toSet());
	}

	/**
	 * Returns a list of block entities within an XYZ radius around a position, as stream
	 *
	 * @param type  - Class of the block entity to search for
	 * @param level - World to search in
	 * @param pos   - Position to search around
	 * @param x     - Radius to search in X
	 * @param y     - Radius to search in Y
	 * @param z     - Radius to search in Z
	 * @return - Stream of block entities
	 */
	public static <T> Stream<T> getBlockEntitiesStream(Class<T> type, World level, BlockPos pos, int x, int y, int z) {
		return getBlockEntitiesStream(type, level, pos, -x, -y, -z, x, y, z);
	}


	/**
	 * Returns a list of block entities within set coordinates.
	 */
	public static <T> Collection<T> getBlockEntities(Class<T> type, World level, BlockPos pos, int x1, int y1, int z1, int x2, int y2, int z2) {
		return getBlockEntitiesStream(type, level, pos, x1, y1, z1, x2, y2, z2).collect(Collectors.toSet());
	}

	/**
	 * Returns a list of block entities within set coordinates, as stream
	 */
	public static <T> Stream<T> getBlockEntitiesStream(Class<T> type, World level, BlockPos pos, int x1, int y1, int z1, int x2, int y2, int z2) {
		return getBlockEntitiesStream(type, level, new Box((double) pos.getX() + 1.5f +  x1, (double) pos.getY() + 1.5f + y1, (double) pos.getZ() + 1.5f + z1, (double) pos.getX() + 0.5f + x2, (double) pos.getY() + 0.5f + y2, (double) pos.getZ() + 0.5f + z2));
	}

	/**
	 * Returns a list of block entities within an Box.
	 */
	public static <T> Collection<T> getBlockEntities(Class<T> type, World world, Box bb) {
		return getBlockEntitiesStream(type, world, bb).collect(Collectors.toSet());
	}

	/**
	 * Returns a list of block entities within an Box, as stream
	 */
	public static <T> Stream<T> getBlockEntitiesStream(Class<T> type, World world, Box bb) {

		// Int stream from min to max + 16 (to get all chunks), step 16
		return IntStream.iterate((int) Math.floor(bb.minX), i -> i < Math.ceil(bb.maxX) + 16, i -> i + 16)
				.boxed()
				.flatMap(i ->
						// Int stream from min to max + 16 (to get all chunks), step 16
						IntStream.iterate((int) Math.floor(bb.minZ), j -> j < Math.ceil(bb.maxZ) + 16, j -> j + 16)
								.boxed().flatMap(j -> {
									// get chunk access
									Chunk c = world.getChunk(new BlockPos(i, 0, j));
									// get block entities in chunk
									return c.getBlockEntityPositions().stream();
								})
				)
				.filter(p -> bb.contains(p.getX() + 0.5, p.getY() + 0.5, p.getZ() + 0.5))
				.map(world::getBlockEntity)
				.filter(type::isInstance)
				.map(type::cast);
	}


	/**
	 * Returns a list of block positions within a radius around a position, with a predicate for conditional checks.
	 */
	public static Collection<BlockPos> getBlocks(BlockPos pos, int range, Predicate<BlockPos> predicate) {
		return getBlocksStream(pos, range, predicate).collect(Collectors.toSet());
	}

	/**
	 * Returns a list of block positions within a radius around a position, with a predicate for conditional checks, as stream
	 */
	public static Stream<BlockPos> getBlocksStream(BlockPos pos, int range, Predicate<BlockPos> predicate) {
		return getBlocksStream(pos, range, range, range, predicate);
	}


	/**
	 * Returns a list of block positions within a XYZ radius around a position, with a predicate for conditional checks.
	 */
	public static Collection<BlockPos> getBlocks(BlockPos pos, int x, int y, int z, Predicate<BlockPos> predicate) {
		return getBlocksStream(pos, x, y, z, predicate).collect(Collectors.toSet());
	}

	/**
	 * Returns a list of block positions within a XYZ radius around a position, with a predicate for conditional checks, as stream
	 */
	public static Stream<BlockPos> getBlocksStream(BlockPos pos, int x, int y, int z, Predicate<BlockPos> predicate) {
		return getBlocksStream(pos, x, y, z).filter(predicate);
	}

	/**
	 * Returns a list of block positions within a XYZ radius around a position.
	 */
	public static Collection<BlockPos> getBlocks(BlockPos pos, int x, int y, int z) {
		return getBlocksStream(pos, x, y, z).collect(Collectors.toSet());
	}

	/**
	 * Returns a list of block positions within a XYZ radius around a position, as stream
	 */
	public static Stream<BlockPos> getBlocksStream(BlockPos pos, int x, int y, int z) {
		return getBlocksStream(pos, -x, -y, -z, x, y, z);
	}

	/**
	 * Returns a list of block positions within set coordinates.
	 */
	public static Collection<BlockPos> getBlocks(BlockPos pos, int x1, int y1, int z1, int x2, int y2, int z2) {
		return getBlocksStream(pos, x1, y1, z1, x2, y2, z2).collect(Collectors.toSet());
	}

	/**
	 * Returns a list of block positions within set coordinates, as stream
	 */
	public static Stream<BlockPos> getBlocksStream(BlockPos pos, int x1, int y1, int z1, int x2, int y2, int z2) {
		return IntStream.rangeClosed(x1, x2)
				.boxed()
				.flatMap(i ->
						IntStream.rangeClosed(y1, y2)
								.boxed().flatMap(j ->
										IntStream.rangeClosed(z1, z2)
												.boxed().map(k ->
													pos.add(i, j, k)
												)
								)
				);
	}

	public static Collection<BlockPos> getPlaneOfBlocks(BlockPos pos, int range, Predicate<BlockPos> predicate) {
		return getPlaneOfBlocksStream(pos, range, predicate).collect(Collectors.toSet());
	}

	public static Stream<BlockPos> getPlaneOfBlocksStream(BlockPos pos, int range, Predicate<BlockPos> predicate) {
		return getPlaneOfBlocksStream(pos, range, range, predicate);
	}

	public static Collection<BlockPos> getPlaneOfBlocks(BlockPos pos, int x, int z, Predicate<BlockPos> predicate) {
		return getPlaneOfBlocksStream(pos, x, z, predicate).collect(Collectors.toSet());
	}

	public static Stream<BlockPos> getPlaneOfBlocksStream(BlockPos pos, int x, int z, Predicate<BlockPos> predicate) {
		return getPlaneOfBlocksStream(pos, x, z).filter(predicate);
	}

	public static Collection<BlockPos> getPlaneOfBlocks(BlockPos pos, int x, int z) {
		return getPlaneOfBlocksStream(pos, x, z).collect(Collectors.toSet());
	}

	public static Stream<BlockPos> getPlaneOfBlocksStream(BlockPos pos, int x, int z) {
		return getPlaneOfBlocksStream(pos, -x, -z, x, z);
	}

	public static Collection<BlockPos> getPlaneOfBlocks(BlockPos pos, int x1, int z1, int x2, int z2) {
		return getPlaneOfBlocksStream(pos, x1, z1, x2, z2).collect(Collectors.toSet());
	}

	public static Stream<BlockPos> getPlaneOfBlocksStream(BlockPos pos, int x1, int z1, int x2, int z2) {
		return IntStream.rangeClosed(x1, x2)
				.boxed()
				.flatMap(x ->
						IntStream.rangeClosed(z1, z2)
								.boxed().map(z ->
										pos.add(x, 0, z)
								)
				);
	}

	public static Collection<BlockPos> getSphereOfBlocks(BlockPos pos, float range, Predicate<BlockPos> predicate) {
		return getSphereOfBlocksStream(pos, range, predicate).collect(Collectors.toSet());
	}

	public static Stream<BlockPos> getSphereOfBlocksStream(BlockPos pos, float range, Predicate<BlockPos> predicate) {
		return getSphereOfBlocksStream(pos, range, range).filter(predicate);
	}


	public static Collection<BlockPos> getSphereOfBlocks(BlockPos pos, float width, float height, Predicate<BlockPos> predicate) {
		return getSphereOfBlocksStream(pos, width, height, predicate).collect(Collectors.toSet());
	}

	public static Stream<BlockPos> getSphereOfBlocksStream(BlockPos pos, float width, float height, Predicate<BlockPos> predicate) {
		return getSphereOfBlocksStream(pos, width, height).filter(predicate);
	}

	public static Collection<BlockPos> getSphereOfBlocks(BlockPos pos, float range) {
		return getSphereOfBlocksStream(pos, range).collect(Collectors.toSet());
	}

	public static Stream<BlockPos> getSphereOfBlocksStream(BlockPos pos, float range) {
		return getSphereOfBlocksStream(pos, range, range);
	}

	public static Collection<BlockPos> getSphereOfBlocks(BlockPos pos, float width, float height) {
		return getSphereOfBlocksStream(pos, width, height).collect(Collectors.toSet());
	}

	public static Stream<BlockPos> getSphereOfBlocksStream(BlockPos pos, float width, float height) {
		return IntStream.rangeClosed((int) -width, (int) width)
				.boxed()
				.flatMap(x ->
						IntStream.rangeClosed((int) -height, (int) height)
								.boxed().flatMap(y ->
										IntStream.rangeClosed((int) -width, (int) width)
												.boxed().filter(z -> {
													double d = Math.sqrt(x * x + y * y + z * z);
													return d <= width;
												}).map(z ->
														pos.add(x, y, z)
												)
								)
				);
	}

	/**
	 * Quick method to get all blocks neighboring a block.
	 */
	public static Collection<BlockPos> getNeighboringBlocks(BlockPos current) {
		return getBlocks(current, -1, -1, -1, 1, 1, 1);
	}

	/**
	 * Quick method to get all blocks neighboring a block, as stream.
	 */
	public static Stream<BlockPos> getNeighboringBlocksStream(BlockPos current) {
		return getBlocksStream(current, -1, -1, -1, 1, 1, 1);
	}

	/* Javadoc
	 * @param inclusive Whether to include the start and the end pos itself in the list.
	 * */
	public static Collection<BlockPos> getPath(BlockPos start, BlockPos end, int speed, boolean inclusive, World level) {
		ParrotEntity parrot = new ParrotEntity(EntityType.PARROT, level);
		parrot.setPos(start.getX() + 0.5, start.getY() - 0.5, start.getZ() + 0.5);
		parrot.getNavigation().startMovingTo(end.getX() + 0.5, end.getY() - 0.5, end.getZ() + 0.5, speed);
		Path path = parrot.getNavigation().getCurrentPath();
		parrot.discard();
		int nodes = path != null ? path.getLength() : 0;
		ArrayList<BlockPos> positions = new ArrayList<>();
		for (int i = 0; i < nodes; i++) {
			PathNode node = path.getNode(i);
			positions.add(new BlockPos(node.x, node.y - 0.5, node.z));
		}
		if (!inclusive) {
			positions.remove(0);
			positions.remove(positions.size() - 1);
		}
		return positions;
	}

	public static void updateState(World level, BlockPos pos) {
		updateState(level.getBlockState(pos), level, pos);
	}

	public static void updateState(BlockState state, World level, BlockPos pos) {
		level.updateListeners(pos, state, state, 2);
		level.markDirty(pos);
	}

	public static void updateAndNotifyState(World level, BlockPos pos) {
		updateAndNotifyState(level.getBlockState(pos), level, pos);
	}

	public static void updateAndNotifyState(BlockState state, World level, BlockPos pos) {
		updateState(state, level, pos);
		state.updateNeighbors(level, pos, 2);
		level.updateComparators(pos, state.getBlock());
	}


	/**
	 * Converts a block position into a Vec3 entry.
	 *
	 * @param pos the block position
	 * @return the vec3 representation.
	 */
	public static Vec3d fromBlockPos(BlockPos pos) {
		return new Vec3d(pos.getX(), pos.getY(), pos.getZ());
	}

	/**
	 * Generates a randomly picked position within a block
	 * @param rand an instance of random
	 * @param pos the position the block is centered around
	 * @return The randomized vector position
	 */
	public static Vec3d withinBlock(Random rand, BlockPos pos) {
		double x = pos.getX() + rand.nextDouble();
		double y = pos.getY() + rand.nextDouble();
		double z = pos.getZ() + rand.nextDouble();
		return new Vec3d(x, y, z);
	}
}
