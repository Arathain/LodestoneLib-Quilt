package com.sammy.lodestone.handlers.screenparticle;



import com.mojang.blaze3d.vertex.Tessellator;
import com.mojang.datafixers.util.Pair;
import com.sammy.lodestone.LodestoneLib;
import com.sammy.lodestone.LodestoneLibConfig;
import com.sammy.lodestone.systems.particle.screen.LodestoneScreenParticleTextureSheet;
import com.sammy.lodestone.systems.particle.screen.ScreenParticleEffect;
import com.sammy.lodestone.systems.particle.screen.ScreenParticleType;
import com.sammy.lodestone.systems.particle.screen.base.ScreenParticle;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.ChatScreen;
import net.minecraft.client.gui.screen.GameModeSelectionScreen;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.item.ItemStack;

import java.util.*;

public class ScreenParticleHandler {
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
	 */
	public static final HashMap<Pair<Boolean, ItemStack>, HashMap<LodestoneScreenParticleTextureSheet, ArrayList<ScreenParticle>>> ITEM_STACK_BOUND_PARTICLES = new HashMap<>();
	private static HashMap<LodestoneScreenParticleTextureSheet, ArrayList<ScreenParticle>> cachedItemTarget = null;

	public static final Tessellator TESSELATOR = new Tessellator();
	public static boolean canSpawnParticles;

	public static boolean renderingHotbar;


	public static void tickParticles() {
		if (!LodestoneLibConfig.ENABLE_SCREEN_PARTICLES) {
			return;
		}
		tickParticles(EARLIEST_PARTICLES);
		tickParticles(EARLY_PARTICLES);
		tickParticles(LATE_PARTICLES);

		ITEM_STACK_BOUND_PARTICLES.values().forEach(ScreenParticleHandler::tickParticles);
		ITEM_STACK_BOUND_PARTICLES.values().removeIf(map -> map.values().stream().allMatch(ArrayList::isEmpty));
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
		if (!LodestoneLibConfig.ENABLE_SCREEN_PARTICLES) {
			return;
		}
		MinecraftClient minecraft = MinecraftClient.getInstance();
		if (minecraft.world != null && minecraft.player != null) {
			if (minecraft.isPaused()) {
				return;
			}
			if (!stack.isEmpty()) {
				ParticleEmitterHandler.ItemParticleSupplier emitter = ParticleEmitterHandler.EMITTERS.get(stack.getItem());
				if (emitter != null) {
					HashMap<LodestoneScreenParticleTextureSheet, ArrayList<ScreenParticle>> target = ITEM_STACK_BOUND_PARTICLES.computeIfAbsent(Pair.of(renderingHotbar, stack), s -> new HashMap<>());
					if (canSpawnParticles) {
						emitter.spawnParticles(target, minecraft.world, MinecraftClient.getInstance().getTickDelta(), stack, x+8, y+8);
					}
					cachedItemTarget = target;
				}
			}
		}
	}

	public static void renderItemStackLate() {
		if (cachedItemTarget != null) {
			renderParticles(cachedItemTarget);
			cachedItemTarget = null;
		}
	}

	public static void renderParticles() {
		if (!LodestoneLibConfig.ENABLE_SCREEN_PARTICLES) {
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
		if (!LodestoneLibConfig.ENABLE_SCREEN_PARTICLES) {
			return;
		}
		screenParticleTarget.forEach((renderType, particles) -> {
			renderType.begin(TESSELATOR.getBufferBuilder(), MinecraftClient.getInstance().getTextureManager());
			for (ScreenParticle next : particles) {
				next.render(TESSELATOR.getBufferBuilder());
			}
			renderType.draw(TESSELATOR);
		});
	}

	public static void clearParticles() {
		clearParticles(EARLIEST_PARTICLES);
		clearParticles(EARLY_PARTICLES);
		clearParticles(LATE_PARTICLES);
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
