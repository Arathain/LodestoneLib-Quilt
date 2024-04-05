package com.sammy.lodestone;

import com.sammy.lodestone.helpers.OrtTestItem;
import com.sammy.lodestone.setup.LodestoneParticles;
import net.minecraft.item.ItemGroup;
import net.minecraft.util.Identifier;
import net.minecraft.util.Rarity;
import net.minecraft.util.random.RandomGenerator;
import net.minecraft.util.registry.Registry;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.quiltmc.loader.api.ModContainer;
import org.quiltmc.loader.api.QuiltLoader;
import org.quiltmc.qsl.base.api.entrypoint.ModInitializer;

import org.quiltmc.qsl.item.setting.api.QuiltItemSettings;

public class LodestoneLib implements ModInitializer {
	public static final Logger LOGGER = LogManager.getLogger("LodestoneLib");
	public static final String MODID= "lodestone";
	public static final RandomGenerator RANDOM = RandomGenerator.createLegacy();

	@Override
	public void onInitialize(ModContainer mod) {
		LodestoneParticles.init();
		if(QuiltLoader.isDevelopmentEnvironment()) {
			Registry.register(Registry.ITEM, id("ort"), new OrtTestItem(new QuiltItemSettings().rarity(Rarity.EPIC).group(ItemGroup.MISC)));
		}
	}
	public static Identifier id(String path) {
		return new Identifier(MODID, path);
	}
}
