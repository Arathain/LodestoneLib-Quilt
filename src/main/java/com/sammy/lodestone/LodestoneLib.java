package com.sammy.lodestone;

import com.sammy.lodestone.helpers.OrtTestItem;
import com.sammy.lodestone.setup.LodestoneParticles;
import net.fabricmc.fabric.api.itemgroup.v1.ItemGroupEvents;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemGroups;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;
import net.minecraft.util.Rarity;
import net.minecraft.util.random.RandomGenerator;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.quiltmc.loader.api.ModContainer;
import org.quiltmc.loader.api.QuiltLoader;
import org.quiltmc.qsl.base.api.entrypoint.ModInitializer;

import org.quiltmc.qsl.item.setting.api.QuiltItemSettings;

public class LodestoneLib implements ModInitializer {
	public static final Logger LOGGER = LogManager.getLogger("LodestoneLib");
	public static final String MODID = "lodestone";
	public static final RandomGenerator RANDOM = RandomGenerator.createLegacy();

	@Override
	public void onInitialize(ModContainer mod) {
		LOGGER.info("jesser where is the cocainer");
		LodestoneParticles.init();
		if (QuiltLoader.isDevelopmentEnvironment()) {
			Item ort = Registry.register(Registries.ITEM, id("ort"), new OrtTestItem(new QuiltItemSettings().rarity(Rarity.EPIC)));
			ItemGroupEvents.modifyEntriesEvent(ItemGroups.TOOLS_AND_UTILITIES).register(content -> {
				content.addItem(ort);
			});
		}
	}
	public static Identifier id(String path) {
		return new Identifier(MODID, path);
	}
}
