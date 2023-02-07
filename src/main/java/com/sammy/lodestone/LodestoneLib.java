package com.sammy.lodestone;

import com.sammy.lodestone.config.ClientConfig;
import com.sammy.lodestone.setup.LodestoneAttributeRegistry;
import com.sammy.lodestone.setup.LodestoneBlockEntityRegistry;
import com.sammy.lodestone.setup.LodestoneParticleRegistry;
import com.sammy.lodestone.systems.easing.Easing;
import com.sammy.lodestone.systems.particle.WorldParticleBuilder;
import com.sammy.lodestone.systems.particle.data.ColorParticleData;
import com.sammy.lodestone.systems.particle.data.GenericParticleData;
import com.sammy.lodestone.systems.particle.data.SpinParticleData;
import eu.midnightdust.lib.config.MidnightConfig;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.BlockWithEntity;
import net.minecraft.block.Blocks;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.BlockEntityTicker;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.entity.EntityType;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.random.RandomGenerator;
import net.minecraft.world.World;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.jetbrains.annotations.Nullable;
import org.joml.Vector3f;
import org.quiltmc.loader.api.ModContainer;
import org.quiltmc.loader.api.QuiltLoader;
import org.quiltmc.qsl.base.api.entrypoint.ModInitializer;
import org.quiltmc.qsl.block.entity.api.QuiltBlockEntityTypeBuilder;
import org.quiltmc.qsl.block.extensions.api.QuiltBlockSettings;
import org.quiltmc.qsl.item.setting.api.QuiltItemSettings;

import java.awt.*;

public class LodestoneLib implements ModInitializer {
	public static final Logger LOGGER = LogManager.getLogger("LodestoneLib");
	public static final String MODID = "lodestone";
	public static final Vector3f VEC3F_ZERO = new Vector3f();

	public static final RandomGenerator RANDOM = RandomGenerator.createLegacy();

	public static final Block TEST = new TestBlock(QuiltBlockSettings.copyOf(Blocks.STONE));
	public static final Item TEST_ITEM = new BlockItem(TEST, new QuiltItemSettings());
	public static final BlockEntityType TEST_ENTITY = QuiltBlockEntityTypeBuilder.create(TestBlockEntity::new , TEST).build();

	@Override
	public void onInitialize(ModContainer mod) {
		LOGGER.info("jesser where is the cocainer");
		MidnightConfig.init(MODID, ClientConfig.class);

		LodestoneAttributeRegistry.init();
		LodestoneParticleRegistry.init();
		LodestoneBlockEntityRegistry.init();

		if(QuiltLoader.isDevelopmentEnvironment()){
			Registry.register(Registries.BLOCK, new Identifier("lodestone", "test"), TEST);
			Registry.register(Registries.ITEM, new Identifier("lodestone", "test_item"), TEST_ITEM);
			Registry.register(Registries.BLOCK_ENTITY_TYPE, new Identifier("lodestone", "test_entity"), TEST_ENTITY);
		}
	}
	public static Identifier id(String path) {
		return new Identifier(MODID, path);
	}

	public static class TestBlock extends BlockWithEntity{

		public TestBlock(Settings settings) {
			super(settings);
		}

		@Nullable
		@Override
		public <T extends BlockEntity> BlockEntityTicker<T> getTicker(World world, BlockState state, BlockEntityType<T> type) {
			return (world1, pos, blockState, blockEntity) -> TestBlockEntity.tick(world, pos);
		}

		@Nullable
		@Override
		public BlockEntity createBlockEntity(BlockPos pos, BlockState state) {
			return new TestBlockEntity(pos, state);
		}
	}

	public static class TestBlockEntity extends BlockEntity{

		public TestBlockEntity(BlockPos pos, BlockState state) {
			super(LodestoneLib.TEST_ENTITY, pos, state);
		}

		public static void tick(World world, BlockPos pos) {
			Color color = new Color(243, 40, 143);
			Color endColor = new Color(243, 40, 143);
			double x = pos.getX();
			double y = pos.getY() + 2;
			double z = pos.getZ();
			float scaleMultiplier = 1f;
			int particleAge = 40;
			WorldParticleBuilder.create(LodestoneParticleRegistry.WISP_PARTICLE)
					.setTransparencyData(GenericParticleData.create(0.15f, 0.25f, 0f).build())
					.setColorData(ColorParticleData.create(color, endColor).setEasing(Easing.BOUNCE_IN_OUT).setCoefficient(0.8f).build())
					.setScaleData(GenericParticleData.create(0.225f*scaleMultiplier, 0).build())
					.setSpinData(SpinParticleData.create(0.1f + world.random.nextFloat() * 0.1f).build())
					.setLifetime(particleAge)
					.setRandomOffset(0.02f)
					.setRandomMotion(0.01f, 0.01f)
					.setRandomMotion(0.0025f, 0.0025f)
					.enableNoClip()
					.repeat(world, x, y, z, 1);
		}
	}
}
