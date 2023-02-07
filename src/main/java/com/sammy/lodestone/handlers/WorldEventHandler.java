package com.sammy.lodestone.handlers;

import com.sammy.lodestone.component.LodestoneComponents;
import com.sammy.lodestone.component.LodestoneWorldComponent;
import com.sammy.lodestone.setup.worldevent.LodestoneWorldEventRendererRegistry;
import com.sammy.lodestone.setup.worldevent.LodestoneWorldEventTypeRegistry;
import com.sammy.lodestone.systems.worldevent.WorldEventInstance;
import com.sammy.lodestone.systems.worldevent.WorldEventRenderer;
import com.sammy.lodestone.systems.worldevent.WorldEventType;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.world.World;

import java.util.Iterator;


public class WorldEventHandler {

	public static class ClientOnly {
		public static void renderWorldEvents(MatrixStack stack, float partialTicks) {
			LodestoneComponents.LODESTONE_WORLD_COMPONENT.maybeGet(MinecraftClient.getInstance().world).ifPresent(component -> {
				for (WorldEventInstance instance : component.activeWorldEvents) {
					WorldEventRenderer<WorldEventInstance> renderer = LodestoneWorldEventRendererRegistry.RENDERERS.get(instance.type);
					if (renderer != null) {
						if (renderer.canRender(instance)) {
							renderer.render(instance, stack, RenderHandler.DELAYED_RENDER, partialTicks);
						}
					}
				}
			});
		}
	}

	public static <T extends WorldEventInstance> T addWorldEvent(World world, T instance) {
		return addWorldEvent(world, true, instance);
	}

	public static <T extends WorldEventInstance> T addWorldEvent(World world, boolean shouldStart, T instance) {
		LodestoneComponents.LODESTONE_WORLD_COMPONENT.maybeGet(world).ifPresent(comp -> {
			comp.inboundWorldEvents.add(instance);
			if (shouldStart) {
				instance.start(world);
			}
			instance.sync(world);
		});
		return instance;
	}

	public static void playerJoin(LivingEntity livingEntity) {
		if (livingEntity instanceof PlayerEntity player) {
			if (player.world instanceof ServerWorld world) {
				LodestoneComponents.PLAYER_COMPONENT.maybeGet(player).flatMap(playerComponent -> LodestoneComponents.LODESTONE_WORLD_COMPONENT.maybeGet(world)).ifPresent(worldComponent -> {
					if (player instanceof ServerPlayerEntity serverPlayer) {
						for (WorldEventInstance instance : worldComponent.activeWorldEvents) {
							if (instance.isClientSynced()) {
								WorldEventInstance.sync(instance, serverPlayer);
							}
						}
					}
				});
			}
		}
	}

	public static void worldTick(World world) {
		if (!world.isClient()) {
			tick(world);
		}
	}

	public static void tick(World world) {
		LodestoneComponents.LODESTONE_WORLD_COMPONENT.maybeGet(world).ifPresent(c -> {
			c.activeWorldEvents.addAll(c.inboundWorldEvents);
			c.inboundWorldEvents.clear();
			Iterator<WorldEventInstance> iterator = c.activeWorldEvents.iterator();
			while (iterator.hasNext()) {
				WorldEventInstance instance = iterator.next();
				if (instance.discarded) {
					iterator.remove();
				} else {
					instance.tick(world);
				}
			}
		});
	}

	public static void serializeNBT(LodestoneWorldComponent capability, NbtCompound tag) {
		NbtCompound worldTag = new NbtCompound();
		worldTag.putInt("worldEventCount", capability.activeWorldEvents.size());
		for (int i = 0; i < capability.activeWorldEvents.size(); i++) {
			WorldEventInstance instance = capability.activeWorldEvents.get(i);
			NbtCompound instanceTag = new NbtCompound();
			instance.writeToNbt(instanceTag);
			worldTag.put("worldEvent_" + i, instanceTag);
		}
		tag.put("worldEventData", worldTag);
	}

	public static void deserializeNBT(LodestoneWorldComponent capability, NbtCompound tag) {
		capability.activeWorldEvents.clear();
		NbtCompound worldTag = tag.getCompound("worldEventData");
		int worldEventCount = worldTag.getInt("worldEventCount");
		for (int i = 0; i < worldEventCount; i++) {
			NbtCompound instanceTag = worldTag.getCompound("worldEvent_" + i);
			WorldEventType reader = LodestoneWorldEventTypeRegistry.EVENT_TYPES.get(instanceTag.getString("type"));
			WorldEventInstance eventInstance = reader.createInstance(instanceTag);
			capability.activeWorldEvents.add(eventInstance);
		}
	}
}
