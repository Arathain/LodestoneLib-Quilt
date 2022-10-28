package com.sammy.lodestone.handlers;



import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.Tessellator;
import com.mojang.datafixers.util.Pair;
import com.sammy.lodestone.systems.rendering.particle.screen.GenericScreenParticle;
import com.sammy.lodestone.systems.rendering.particle.screen.ScreenParticleEffect;
import com.sammy.lodestone.systems.rendering.particle.screen.ScreenParticleType;
import com.sammy.lodestone.systems.rendering.particle.screen.base.ScreenParticle;
import com.sammy.lodestone.systems.rendering.particle.screen.emitter.ItemParticleEmitter;
import com.sammy.lodestone.systems.rendering.particle.screen.emitter.ParticleEmitter;
//import dev.emi.emi.screen.RecipeScreen;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.ChatScreen;
import net.minecraft.client.gui.screen.GameModeSelectionScreen;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.particle.ParticleTextureSheet;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.Matrix4f;
import org.quiltmc.loader.api.QuiltLoader;

import java.util.*;

import static com.sammy.lodestone.systems.rendering.particle.screen.base.ScreenParticle.RenderOrder.*;

public class ScreenParticleHandler {
	public static Map<Pair<ParticleTextureSheet, ScreenParticle.RenderOrder>, ArrayList<ScreenParticle>> PARTICLES = new HashMap<>();
	public static ArrayList<StackTracker> RENDERED_STACKS = new ArrayList<>();
	public static Map<Item, ParticleEmitter> EMITTERS = new HashMap<>();
	public static final Tessellator TESSELATOR = new Tessellator();
	public static boolean canSpawnParticles;
	public static boolean renderingHotbar;

	public static void clientTick() {
		PARTICLES.forEach((pair, particles) -> {
			Iterator<ScreenParticle> iterator = particles.iterator();
			while (iterator.hasNext()) {
				ScreenParticle particle = iterator.next();
				particle.tick();
				if (!particle.isAlive()) {
					iterator.remove();
				}
			}
		});
		canSpawnParticles = true;
	}

	public static void renderItem(ItemStack stack) {
		MinecraftClient minecraft = MinecraftClient.getInstance();
		if (minecraft.world != null && minecraft.player != null) {
			if (minecraft.isPaused()) {
				return;
			}
			if (!stack.isEmpty()) {
				ParticleEmitter emitter = ScreenParticleHandler.EMITTERS.get(stack.getItem());
				if (emitter != null) {
					MatrixStack matrixStack = RenderSystem.getModelViewStack();
					ScreenParticle.RenderOrder renderOrder = AFTER_EVERYTHING;
					Screen screen = minecraft.currentScreen;
					if (screen != null) {
						if (!QuiltLoader.isModLoaded("emi") /*|| !(screen instanceof RecipeScreen)*/) {
							renderOrder = BEFORE_TOOLTIPS;
						}
						if (renderingHotbar) {
							renderOrder = BEFORE_UI;
						}
					}
					Matrix4f last = matrixStack.peek().getModel();
					float x = last.a03;
					float y = last.a13;
					if (canSpawnParticles) {
						emitter.tick(stack, x, y, renderOrder);
					}
					RENDERED_STACKS.add(new StackTracker(stack, renderOrder, x, y));
				}
			}
		}
	}

	public static void renderParticles() {
		final MinecraftClient client = MinecraftClient.getInstance();
		Screen screen = client.currentScreen;
//		if (QuiltLoader.isModLoaded("emi") && screen instanceof RecipeScreen) {
//			renderParticles(AFTER_EVERYTHING);
//		}
		if (screen == null || screen instanceof ChatScreen || screen instanceof GameModeSelectionScreen) {
			renderParticles(AFTER_EVERYTHING, BEFORE_UI);
		}
		RENDERED_STACKS.clear();
		canSpawnParticles = false;
	}

	public static void renderParticles(ScreenParticle.RenderOrder... renderOrders) {
		final MinecraftClient client = MinecraftClient.getInstance();
		PARTICLES.forEach((pair, particles) -> {
			ParticleTextureSheet type = pair.getFirst();
			if (Arrays.stream(renderOrders).anyMatch(o -> o.equals(pair.getSecond()))) {
				type.begin(TESSELATOR.getBufferBuilder(), client.getTextureManager());
				for (ScreenParticle next : particles) {
					if (next instanceof GenericScreenParticle genericScreenParticle) {
						genericScreenParticle.trackStack();
					}
					next.render(TESSELATOR.getBufferBuilder());
				}
				type.draw(TESSELATOR);
			}
		});
	}

	@SuppressWarnings("ALL")
	public static <T extends ScreenParticleEffect> ScreenParticle addParticle(T options, double pX, double pY, double pXSpeed, double pYSpeed) {
		MinecraftClient minecraft = MinecraftClient.getInstance();
		ScreenParticleType<T> type = (ScreenParticleType<T>) options.type;
		ScreenParticleType.Factory<T> provider = type.factory;
		ScreenParticle particle = provider.createParticle(minecraft.world, options, pX, pY, pXSpeed, pYSpeed);
		ArrayList<ScreenParticle> list = PARTICLES.computeIfAbsent(Pair.of(particle.getTextureSheet(), particle.renderOrder), (a) -> new ArrayList<>());
		list.add(particle);
		return particle;
	}

	public static void wipeParticles(ScreenParticle.RenderOrder... renderOrders) {
		PARTICLES.forEach((pair, particles) -> {
			if (!particles.isEmpty()) {
				if (renderOrders.length == 0 || Arrays.stream(renderOrders)
						.anyMatch(o -> o.equals(pair.getSecond()))) {
					particles.clear();
				}
			}
		});
	}

	public static void registerItemParticleEmitter(Item item, ParticleEmitter.EmitterSupplier emitter) {
		EMITTERS.put(item, new ParticleEmitter(emitter));
	}

	public static void registerItemParticleEmitter(ParticleEmitter.EmitterSupplier emitter, Item... items) {
		for (Item item : items) {
			EMITTERS.put(item, new ParticleEmitter(emitter));
		}
	}

	public static void registerItemParticleEmitter(net.minecraft.util.Pair<ItemParticleEmitter, Item[]> pair) {
		registerItemParticleEmitter(pair.getLeft()::particleTick, pair.getRight());
	}

	public record StackTracker(ItemStack stack, ScreenParticle.RenderOrder order, float xOrigin, float yOrigin) {
	}
}
