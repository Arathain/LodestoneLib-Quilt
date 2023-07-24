package team.lodestar.lodestone.handlers.screenparticle;

import team.lodestar.lodestone.helpers.DataHelper;
import team.lodestar.lodestone.systems.rendering.particle.screen.LodestoneScreenParticleTextureSheet;
import team.lodestar.lodestone.systems.rendering.particle.screen.base.ScreenParticle;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.registry.Registries;
import net.minecraft.world.World;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class ParticleEmitterHandler {
	public static final Map<Item, ItemParticleSupplier> EMITTERS = new HashMap<>();

	public static void registerParticleEmitters() {
		DataHelper.takeAll(new ArrayList<>(Registries.ITEM.stream().toList()), i -> i instanceof ItemParticleSupplier).forEach(i -> {
					ItemParticleSupplier emitter = (ItemParticleSupplier) i;
					registerItemParticleEmitter(i, emitter);
				}
		);
	}

	public static void registerItemParticleEmitter(Item item, ItemParticleSupplier emitter) {
		EMITTERS.put(item, emitter);
	}

	public static void registerItemParticleEmitter(ItemParticleSupplier emitter, Item... items) {
		for (Item item : items) {
			EMITTERS.put(item, emitter);
		}
	}

	public interface ItemParticleSupplier {
		void spawnParticles(HashMap<LodestoneScreenParticleTextureSheet, ArrayList<ScreenParticle>> target, World world, float tickDelta, ItemStack stack, float x, float y);
	}
}
