package com.sammy.ortus;

import net.minecraft.util.Identifier;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.quiltmc.loader.api.ModContainer;
import org.quiltmc.qsl.base.api.entrypoint.ModInitializer;

import org.slf4j.LoggerFactory;

import java.util.Random;

public class OrtusLib implements ModInitializer {
	public static final Logger LOGGER = LogManager.getLogger();
	public static final String MODID= "ortus";
	public static final Random RANDOM = new Random();

	@Override
	public void onInitialize(ModContainer mod) {
		LOGGER.info("jesser where is the cocainer");
	}
	public static Identifier id(String path) {
		return new Identifier(MODID, path);
	}
}
