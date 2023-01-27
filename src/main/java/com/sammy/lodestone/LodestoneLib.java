package com.sammy.lodestone;

import com.sammy.lodestone.setup.LodestoneAttributeRegistry;
import com.sammy.lodestone.setup.LodestoneBlockEntityRegistry;
import com.sammy.lodestone.setup.LodestoneParticles;
import net.minecraft.util.Identifier;
import net.minecraft.util.random.RandomGenerator;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.joml.Vector3f;
import org.quiltmc.loader.api.ModContainer;
import org.quiltmc.qsl.base.api.entrypoint.ModInitializer;

public class LodestoneLib implements ModInitializer {
	public static final Logger LOGGER = LogManager.getLogger("LodestoneLib");
	public static final String MODID = "lodestone";
	public static final Vector3f VEC3F_ZERO = new Vector3f();

	public static final RandomGenerator RANDOM = RandomGenerator.createLegacy();

	@Override
	public void onInitialize(ModContainer mod) {
		LOGGER.info("jesser where is the cocainer");
		LodestoneAttributeRegistry.init();
		LodestoneParticles.init();
		LodestoneBlockEntityRegistry.init();

	}
	public static Identifier id(String path) {
		return new Identifier(MODID, path);
	}
}
