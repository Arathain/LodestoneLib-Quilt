package team.lodestar.lodestone.systems.rendering.particle;

import team.lodestar.lodestone.helpers.BlockHelper;
import team.lodestar.lodestone.systems.rendering.particle.data.ColorParticleData;
import team.lodestar.lodestone.systems.rendering.particle.data.GenericParticleData;
import team.lodestar.lodestone.systems.rendering.particle.data.SpinParticleData;
import team.lodestar.lodestone.systems.rendering.particle.world.GenericParticle;
import team.lodestar.lodestone.systems.rendering.particle.world.WorldParticleEffect;
import net.minecraft.block.BlockState;
import net.minecraft.client.particle.ParticleTextureSheet;
import net.minecraft.particle.ParticleType;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.util.shape.VoxelShapes;
import net.minecraft.world.World;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;
import java.util.function.Consumer;
import java.util.function.Supplier;

public class WorldParticleBuilder {

	private static final Random RANDOM = new Random();

	final ParticleType<?> type;
	final WorldParticleEffect options;

	double xMotion = 0, yMotion = 0, zMotion = 0;
	double maxXSpeed = 0, maxYSpeed = 0, maxZSpeed = 0;
	double maxXOffset = 0, maxYOffset = 0, maxZOffset = 0;

	public static WorldParticleBuilder create(ParticleType<?> type) {
		return new WorldParticleBuilder(type);
	}

	protected WorldParticleBuilder(ParticleType<?> type) {
		this.type = type;
		this.options = new WorldParticleEffect(type);
	}

	public WorldParticleBuilder setColorData(ColorParticleData colorData) {
		options.colorData = colorData;
		return this;
	}

	public WorldParticleBuilder setScaleData(GenericParticleData scaleData) {
		options.scaleData = scaleData;
		return this;
	}

	public WorldParticleBuilder setTransparencyData(GenericParticleData transparencyData) {
		options.transparencyData = transparencyData;
		return this;
	}

	public WorldParticleBuilder setSpinData(SpinParticleData spinData) {
		options.spinData = spinData;
		return this;
	}

	public WorldParticleBuilder setGravity(float gravity) {
		options.gravity = gravity;
		return this;
	}

	public WorldParticleBuilder enableNoClip() {
		options.noClip = true;
		return this;
	}

	public WorldParticleBuilder disableNoClip() {
		options.noClip = false;
		return this;
	}

	public WorldParticleBuilder setSpritePicker(SimpleParticleEffect.ParticleSpritePicker spritePicker) {
		options.spritePicker = spritePicker;
		return this;
	}

	public WorldParticleBuilder setDiscardFunction(SimpleParticleEffect.ParticleDiscardFunctionType discardFunctionType) {
		options.discardFunctionType = discardFunctionType;
		return this;
	}

	public WorldParticleBuilder setRenderType(ParticleTextureSheet renderType) {
		options.textureSheet = renderType;
		return this;
	}

	public WorldParticleBuilder setLifetime(int lifetime) {
		options.lifetime = lifetime;
		return this;
	}

	public WorldParticleBuilder setRandomMotion(double maxSpeed) {
		return setRandomMotion(maxSpeed, maxSpeed, maxSpeed);
	}

	public WorldParticleBuilder setRandomMotion(double maxHSpeed, double maxVSpeed) {
		return setRandomMotion(maxHSpeed, maxVSpeed, maxHSpeed);
	}

	public WorldParticleBuilder setRandomMotion(double maxXSpeed, double maxYSpeed, double maxZSpeed) {
		this.maxXSpeed = maxXSpeed;
		this.maxYSpeed = maxYSpeed;
		this.maxZSpeed = maxZSpeed;
		return this;
	}

	public WorldParticleBuilder addMotion(double vx, double vy, double vz) {
		this.xMotion += vx;
		this.yMotion += vy;
		this.zMotion += vz;
		return this;
	}

	public WorldParticleBuilder setMotion(double vx, double vy, double vz) {
		this.xMotion = vx;
		this.yMotion = vy;
		this.zMotion = vz;
		return this;
	}

	public WorldParticleBuilder setRandomOffset(double maxDistance) {
		return setRandomOffset(maxDistance, maxDistance, maxDistance);
	}

	public WorldParticleBuilder setRandomOffset(double maxHDist, double maxVDist) {
		return setRandomOffset(maxHDist, maxVDist, maxHDist);
	}

	public WorldParticleBuilder setRandomOffset(double maxXDist, double maxYDist, double maxZDist) {
		this.maxXOffset = maxXDist;
		this.maxYOffset = maxYDist;
		this.maxZOffset = maxZDist;
		return this;
	}

	public WorldParticleBuilder act(Consumer<WorldParticleBuilder> particleBuilderConsumer) {
		particleBuilderConsumer.accept(this);
		return this;
	}

	public WorldParticleBuilder addActor(Consumer<GenericParticle> particleActor) {
		options.actor = particleActor;
		return this;
	}

	public WorldParticleBuilder spawn(World level, double x, double y, double z) {
		double yaw = RANDOM.nextFloat() * Math.PI * 2, pitch = RANDOM.nextFloat() * Math.PI - Math.PI / 2, xSpeed = RANDOM.nextFloat() * maxXSpeed, ySpeed = RANDOM.nextFloat() * maxYSpeed, zSpeed = RANDOM.nextFloat() * maxZSpeed;
		this.xMotion += Math.sin(yaw) * Math.cos(pitch) * xSpeed;
		this.yMotion += Math.sin(pitch) * ySpeed;
		this.zMotion += Math.cos(yaw) * Math.cos(pitch) * zSpeed;
		double yaw2 = RANDOM.nextFloat() * Math.PI * 2, pitch2 = RANDOM.nextFloat() * Math.PI - Math.PI / 2, xDist = RANDOM.nextFloat() * maxXOffset, yDist = RANDOM.nextFloat() * maxYOffset, zDist = RANDOM.nextFloat() * maxZOffset;
		double xPos = Math.sin(yaw2) * Math.cos(pitch2) * xDist;
		double yPos = Math.sin(pitch2) * yDist;
		double zPos = Math.cos(yaw2) * Math.cos(pitch2) * zDist;

		level.addParticle(options, x + xPos, y + yPos, z + zPos, xMotion, yMotion, zMotion);
		return this;
	}

	public WorldParticleBuilder repeat(World level, double x, double y, double z, int n) {
		for (int i = 0; i < n; i++) spawn(level, x, y, z);
		return this;
	}

	public WorldParticleBuilder surroundBlock(World level, BlockPos pos, Direction... directions) {
		if (directions.length == 0) {
			directions = Direction.values();
		}
		for (Direction direction : directions) {
			double yaw = RANDOM.nextFloat() * Math.PI * 2, pitch = RANDOM.nextFloat() * Math.PI - Math.PI / 2, xSpeed = RANDOM.nextFloat() * maxXSpeed, ySpeed = RANDOM.nextFloat() * maxYSpeed, zSpeed = RANDOM.nextFloat() * maxZSpeed;
			this.xMotion += Math.sin(yaw) * Math.cos(pitch) * xSpeed;
			this.yMotion += Math.sin(pitch) * ySpeed;
			this.zMotion += Math.cos(yaw) * Math.cos(pitch) * zSpeed;

			Direction.Axis direction$axis = direction.getAxis();
			double d0 = 0.5625D;
			double xPos = direction$axis == Direction.Axis.X ? 0.5D + d0 * (double) direction.getOffsetX() : RANDOM.nextDouble();
			double yPos = direction$axis == Direction.Axis.Y ? 0.5D + d0 * (double) direction.getOffsetY() : RANDOM.nextDouble();
			double zPos = direction$axis == Direction.Axis.Z ? 0.5D + d0 * (double) direction.getOffsetZ() : RANDOM.nextDouble();

			level.addParticle(options, pos.getX() + xPos, pos.getY() + yPos, pos.getZ() + zPos, xMotion, yMotion, zMotion);

		}
		return this;
	}

	public WorldParticleBuilder repeatSurroundBlock(World level, BlockPos pos, int n) {
		for (int i = 0; i < n; i++) surroundBlock(level, pos);
		return this;
	}

	public WorldParticleBuilder repeatSurroundBlock(World level, BlockPos pos, int n, Direction... directions) {
		for (int i = 0; i < n; i++) surroundBlock(level, pos, directions);
		return this;
	}

	public WorldParticleBuilder surroundVoxelShape(World level, BlockPos pos, VoxelShape voxelShape, int max) {
		int[] c = new int[1];
		int perBoxMax = max / voxelShape.getBoundingBoxes().size();
		Supplier<Boolean> r = () -> {
			c[0]++;
			if (c[0] >= perBoxMax) {
				c[0] = 0;
				return true;
			}
			return false;
		};
		Vec3d v = BlockHelper.fromBlockPos(pos);
		voxelShape.forEachBox(
				(x1, y1, z1, x2, y2, z2) -> {
					Vec3d b = v.add(x1, y1, z1);
					Vec3d e = v.add(x2, y2, z2);
					List<Runnable> runs = new ArrayList<>();
					runs.add(() -> spawnLine(level, b, v.add(x2, y1, z1)));
					runs.add(() -> spawnLine(level, b, v.add(x1, y2, z1)));
					runs.add(() -> spawnLine(level, b, v.add(x1, y1, z2)));
					runs.add(() -> spawnLine(level, v.add(x1, y2, z1), v.add(x2, y2, z1)));
					runs.add(() -> spawnLine(level, v.add(x1, y2, z1), v.add(x1, y2, z2)));
					runs.add(() -> spawnLine(level, e, v.add(x2, y2, z1)));
					runs.add(() -> spawnLine(level, e, v.add(x1, y2, z2)));
					runs.add(() -> spawnLine(level, e, v.add(x2, y1, z2)));
					runs.add(() -> spawnLine(level, v.add(x2, y1, z1), v.add(x2, y1, z2)));
					runs.add(() -> spawnLine(level, v.add(x1, y1, z2), v.add(x2, y1, z2)));
					runs.add(() -> spawnLine(level, v.add(x2, y1, z1), v.add(x2, y2, z1)));
					runs.add(() -> spawnLine(level, v.add(x1, y1, z2), v.add(x1, y2, z2)));
					Collections.shuffle(runs);
					for (Runnable runnable : runs) {
						runnable.run();
						if (r.get()) {
							break;
						}
					}
				}
		);
		return this;
	}

	public WorldParticleBuilder surroundVoxelShape(World level, BlockPos pos, BlockState state, int max) {
		VoxelShape voxelShape = state.getOutlineShape(level, pos);
		if (voxelShape.isEmpty()) {
			voxelShape = VoxelShapes.fullCube();
		}
		return surroundVoxelShape(level, pos, voxelShape, max);
	}

	public WorldParticleBuilder spawnAtRandomFace(World level, BlockPos pos) {
		Direction direction = Direction.values()[RANDOM.nextInt(Direction.values().length)];
		double yaw = RANDOM.nextFloat() * Math.PI * 2, pitch = RANDOM.nextFloat() * Math.PI - Math.PI / 2, xSpeed = RANDOM.nextFloat() * maxXSpeed, ySpeed = RANDOM.nextFloat() * maxYSpeed, zSpeed = RANDOM.nextFloat() * maxZSpeed;
		this.xMotion += Math.sin(yaw) * Math.cos(pitch) * xSpeed;
		this.yMotion += Math.sin(pitch) * ySpeed;
		this.zMotion += Math.cos(yaw) * Math.cos(pitch) * zSpeed;

		Direction.Axis direction$axis = direction.getAxis();
		double d0 = 0.5625D;
		double xPos = direction$axis == Direction.Axis.X ? 0.5D + d0 * (double) direction.getOffsetX() : RANDOM.nextDouble();
		double yPos = direction$axis == Direction.Axis.Y ? 0.5D + d0 * (double) direction.getOffsetY() : RANDOM.nextDouble();
		double zPos = direction$axis == Direction.Axis.Z ? 0.5D + d0 * (double) direction.getOffsetZ() : RANDOM.nextDouble();

		level.addParticle(options, pos.getX() + xPos, pos.getY() + yPos, pos.getZ() + zPos, xMotion, yMotion, zMotion);
		return this;
	}

	public WorldParticleBuilder repeatRandomFace(World level, BlockPos pos, int n) {
		for (int i = 0; i < n; i++) spawnAtRandomFace(level, pos);
		return this;
	}

	public WorldParticleBuilder createCircle(World level, double x, double y, double z, double distance, double currentCount, double totalCount) {
		double xSpeed = RANDOM.nextFloat() * maxXSpeed, ySpeed = RANDOM.nextFloat() * maxYSpeed, zSpeed = RANDOM.nextFloat() * maxZSpeed;
		double theta = (Math.PI * 2) / totalCount;
		double finalAngle = (currentCount / totalCount) + (theta * currentCount);
		double dx2 = (distance * Math.cos(finalAngle));
		double dz2 = (distance * Math.sin(finalAngle));

		Vec3d vector2f = new Vec3d(dx2, 0, dz2);
		this.xMotion = vector2f.x * xSpeed;
		this.zMotion = vector2f.z * zSpeed;

		double yaw2 = RANDOM.nextFloat() * Math.PI * 2, pitch2 = RANDOM.nextFloat() * Math.PI - Math.PI / 2, xDist = RANDOM.nextFloat() * maxXOffset, yDist = RANDOM.nextFloat() * maxYOffset, zDist = RANDOM.nextFloat() * maxZOffset;
		double xPos = Math.sin(yaw2) * Math.cos(pitch2) * xDist;
		double yPos = Math.sin(pitch2) * yDist;
		double zPos = Math.cos(yaw2) * Math.cos(pitch2) * zDist;
		level.addParticle(options, x + xPos + dx2, y + yPos, z + zPos + dz2, xMotion, ySpeed, zMotion);
		return this;
	}

	public WorldParticleBuilder repeatCircle(World level, double x, double y, double z, double distance, int times) {
		for (int i = 0; i < times; i++) createCircle(level, x, y, z, distance, i, times);
		return this;
	}

	public WorldParticleBuilder createBlockOutline(World level, BlockPos pos, BlockState state) {
		VoxelShape voxelShape = state.getOutlineShape(level, pos);
		double d = 0.25;
		voxelShape.forEachBox(
				(x1, y1, z1, x2, y2, z2) -> {
					Vec3d v = BlockHelper.fromBlockPos(pos);
					Vec3d b = BlockHelper.fromBlockPos(pos).add(x1, y1, z1);
					Vec3d e = BlockHelper.fromBlockPos(pos).add(x2, y2, z2);
					spawnLine(level, b, v.add(x2, y1, z1));
					spawnLine(level, b, v.add(x1, y2, z1));
					spawnLine(level, b, v.add(x1, y1, z2));
					spawnLine(level, v.add(x1, y2, z1), v.add(x2, y2, z1));
					spawnLine(level, v.add(x1, y2, z1), v.add(x1, y2, z2));
					spawnLine(level, e, v.add(x2, y2, z1));
					spawnLine(level, e, v.add(x1, y2, z2));
					spawnLine(level, e, v.add(x2, y1, z2));
					spawnLine(level, v.add(x2, y1, z1), v.add(x2, y1, z2));
					spawnLine(level, v.add(x1, y1, z2), v.add(x2, y1, z2));
					spawnLine(level, v.add(x2, y1, z1), v.add(x2, y2, z1));
					spawnLine(level, v.add(x1, y1, z2), v.add(x1, y2, z2));
				}
		);
		return this;
	}

	public WorldParticleBuilder spawnLine(World level, Vec3d one, Vec3d two) {
		double yaw = RANDOM.nextFloat() * Math.PI * 2, pitch = RANDOM.nextFloat() * Math.PI - Math.PI / 2, xSpeed = RANDOM.nextFloat() * maxXSpeed, ySpeed = RANDOM.nextFloat() * maxYSpeed, zSpeed = RANDOM.nextFloat() * maxZSpeed;
		this.xMotion += Math.sin(yaw) * Math.cos(pitch) * xSpeed;
		this.yMotion += Math.sin(pitch) * ySpeed;
		this.zMotion += Math.cos(yaw) * Math.cos(pitch) * zSpeed;
		Vec3d pos = one.lerp(two, RANDOM.nextDouble());
		level.addParticle(options, pos.x, pos.y, pos.z, xMotion, yMotion, zMotion);
		return this;
	}
}
