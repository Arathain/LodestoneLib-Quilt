package team.lodestar.lodestone.handlers.screenparticle;


import com.mojang.blaze3d.vertex.Tessellator;
import com.mojang.datafixers.util.Pair;
import team.lodestar.lodestone.config.ClientConfig;
import team.lodestar.lodestone.systems.rendering.particle.screen.LodestoneScreenParticleTextureSheet;
import team.lodestar.lodestone.systems.rendering.particle.screen.ScreenParticleEffect;
import team.lodestar.lodestone.systems.rendering.particle.screen.ScreenParticleType;
import team.lodestar.lodestone.systems.rendering.particle.screen.base.ScreenParticle;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.ChatScreen;
import net.minecraft.client.gui.screen.GameModeSelectionScreen;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.item.ItemStack;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;


/**
 * A handler for screen particles.
 * Particles are spawned during rendering once per tick.
 * We also track all present ItemStacks on the screen to allow our particles to more optimally follow a given ItemStacks position
 * Use {@link ScreenParticleHandler#addParticle(HashMap, ScreenParticleEffect, double, double, double, double)} to create a screen particle, which will then be ticked.
 */
public class ScreenParticleHandler {
	public static int x, y;

	/**
	 * Earliest Screen Particles are rendered before nearly every piece of user interface.
	 */
	public static final HashMap<LodestoneScreenParticleTextureSheet, ArrayList<ScreenParticle>> EARLIEST_PARTICLES = new HashMap<>();

	/**
	 * Early Screen Particles are rendered after other UI elements, but before things like tooltips or other overlays.
	 */
	public static final HashMap<LodestoneScreenParticleTextureSheet, ArrayList<ScreenParticle>> EARLY_PARTICLES = new HashMap<>();

	/**
	 * Late Screen Particles are rendered after everything else.
	 */
	public static final HashMap<LodestoneScreenParticleTextureSheet, ArrayList<ScreenParticle>> LATE_PARTICLES = new HashMap<>();

	/**
	 * Item Stack Bound Particles are rendered just after an item stack in the inventory. They are ticked the same as other particles.
	 * We use a pair of a boolean and the ItemStack as a key. The boolean sorts item particles based on if the ItemStack in question is in the hotbar or not.
	 */
	public static final HashMap<Pair<Boolean, ItemStack>, HashMap<LodestoneScreenParticleTextureSheet, ArrayList<ScreenParticle>>> ITEM_PARTICLES = new HashMap<>();
	public static final HashMap<Pair<Boolean, Pair<Integer, Integer>>, ItemStack> ITEM_STACK_CACHE = new HashMap<>();

	public static HashMap<LodestoneScreenParticleTextureSheet, ArrayList<ScreenParticle>> cachedItemTarget = null;
	public static int currentItemX, currentItemY;

	public static final Tessellator TESSELLATOR = new Tessellator();
	public static boolean canSpawnParticles;

	public static boolean renderingHotbar;


	public static void tickParticles() {
		if (!ClientConfig.ENABLE_SCREEN_PARTICLES) {
			return;
		}
		tickParticles(EARLIEST_PARTICLES);
		tickParticles(EARLY_PARTICLES);
		tickParticles(LATE_PARTICLES);

		ITEM_PARTICLES.values().forEach(ScreenParticleHandler::tickParticles);
		ITEM_PARTICLES.values().removeIf(map -> map.values().stream().allMatch(ArrayList::isEmpty));
		canSpawnParticles = true;
	}

	public static void tickParticles(HashMap<LodestoneScreenParticleTextureSheet, ArrayList<ScreenParticle>> screenParticleTarget) {
		screenParticleTarget.forEach((pair, particles) -> {
			Iterator<ScreenParticle> iterator = particles.iterator();
			while (iterator.hasNext()) {
				ScreenParticle particle = iterator.next();
				particle.tick();
				if (!particle.isAlive()) {
					iterator.remove();
				}
			}
		});
	}

	public static void renderItemStackEarly(ItemStack stack, int x, int y) {
		if (!ClientConfig.ENABLE_SCREEN_PARTICLES) {
			return;
		}
		MinecraftClient minecraft = MinecraftClient.getInstance();
		if (minecraft.world != null && minecraft.player != null) {
			if (minecraft.isPaused()) {
				return;
			}
			if (!stack.isEmpty()) {
				currentItemX = x+8;
				currentItemY = y+8;
				ParticleEmitterHandler.ItemParticleSupplier emitter = ParticleEmitterHandler.EMITTERS.get(stack.getItem());
				if (emitter != null) {
					HashMap<LodestoneScreenParticleTextureSheet, ArrayList<ScreenParticle>> target = ITEM_PARTICLES.computeIfAbsent(Pair.of(renderingHotbar, stack), s -> new HashMap<>());

					pullFromParticleVault(stack, target);
					if (canSpawnParticles) {
						emitter.spawnParticles(target, minecraft.world, MinecraftClient.getInstance().getTickDelta(), stack, currentItemX, currentItemY);
					}
					cachedItemTarget = target;
				}
			}
		}
	}

	public static void pullFromParticleVault(ItemStack currentStack, HashMap<LodestoneScreenParticleTextureSheet, ArrayList<ScreenParticle>> target) {
		Pair<Boolean, Pair<Integer, Integer>> cacheKey = Pair.of(renderingHotbar, Pair.of(currentItemX, currentItemY));
		if (ITEM_STACK_CACHE.containsKey(cacheKey)) {
			ItemStack oldStack = ITEM_STACK_CACHE.get(cacheKey);
			if (oldStack != currentStack && oldStack.getItem().equals(currentStack.getItem())) {
				Pair<Boolean, ItemStack> particleKey = Pair.of(renderingHotbar, oldStack);
				HashMap<LodestoneScreenParticleTextureSheet, ArrayList<ScreenParticle>> oldParticles = ITEM_PARTICLES.get(particleKey);
				if (oldParticles != null) {
					target.putAll(oldParticles);
				}
				ITEM_STACK_CACHE.remove(cacheKey);
				ITEM_PARTICLES.remove(particleKey);
			}
		}
		ITEM_STACK_CACHE.put(cacheKey, currentStack);
	}

	public static void renderItemStackLate() {
		if (cachedItemTarget != null) {
			renderParticles(cachedItemTarget);
			cachedItemTarget = null;
		}
	}

	public static void renderParticles() {
		if (!ClientConfig.ENABLE_SCREEN_PARTICLES) {
			return;
		}
		Screen screen = MinecraftClient.getInstance().currentScreen;

		if (screen == null || screen instanceof ChatScreen || screen instanceof GameModeSelectionScreen) {
			renderEarliestParticles();
		}
		renderLateParticles();
		canSpawnParticles = false;
	}

	public static void renderEarliestParticles() {
		renderParticles(EARLIEST_PARTICLES);
	}

	public static void renderEarlyParticles() {
		renderParticles(EARLY_PARTICLES);
	}

	public static void renderLateParticles() {
		renderParticles(LATE_PARTICLES);
	}

	private static void renderParticles(HashMap<LodestoneScreenParticleTextureSheet, ArrayList<ScreenParticle>> screenParticleTarget) {
		if (!ClientConfig.ENABLE_SCREEN_PARTICLES) {
			return;
		}
		screenParticleTarget.forEach((renderType, particles) -> {
			renderType.begin(TESSELLATOR.getBufferBuilder(), MinecraftClient.getInstance().getTextureManager());
			for (ScreenParticle next : particles) {
				next.render(TESSELLATOR.getBufferBuilder());
			}
			renderType.end(TESSELLATOR);
		});
	}

	public static void clearParticles() {
		clearParticles(EARLIEST_PARTICLES);
		clearParticles(EARLY_PARTICLES);
		clearParticles(LATE_PARTICLES);
		ITEM_PARTICLES.values().forEach(ScreenParticleHandler::clearParticles);
	}

	public static void clearParticles(HashMap<LodestoneScreenParticleTextureSheet, ArrayList<ScreenParticle>> screenParticleTarget) {
		screenParticleTarget.values().forEach(ArrayList::clear);
	}

	@SuppressWarnings("unchecked")
	public static <T extends ScreenParticleEffect> ScreenParticle addParticle(HashMap<LodestoneScreenParticleTextureSheet, ArrayList<ScreenParticle>> screenParticleTarget, T options, double x, double y, double xMotion, double yMotion) {
		MinecraftClient minecraft = MinecraftClient.getInstance();
		ScreenParticleType<T> type = (ScreenParticleType<T>) options.type;
		ScreenParticle particle = type.factory.createParticle(minecraft.world, options, x, y, xMotion, yMotion);
		ArrayList<ScreenParticle> list = screenParticleTarget.computeIfAbsent(options.renderType, (a) -> new ArrayList<>());
		list.add(particle);
		return particle;
	}
}
