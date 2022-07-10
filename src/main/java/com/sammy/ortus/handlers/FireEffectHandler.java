package com.sammy.ortus.handlers;

import com.sammy.ortus.component.OrtusEntityComponent;
import com.sammy.ortus.network.ClearFireEffectInstancePacket;
import com.sammy.ortus.setup.OrtusFireEffectRendererRegistry;
import com.sammy.ortus.systems.fireeffect.FireEffectInstance;
import com.sammy.ortus.systems.fireeffect.FireEffectRenderer;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.Camera;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.Entity;
import net.minecraft.nbt.NbtCompound;
import org.quiltmc.qsl.networking.api.PlayerLookup;

import static com.sammy.ortus.setup.OrtusComponents.ENTITY_COMPONENT;

public class FireEffectHandler {
	public static void entityUpdate(Entity entity) {
		FireEffectInstance instance = getFireEffectInstance(entity);
		if (instance != null) {
			instance.tick(entity);
			if (!instance.isValid()) {
				setCustomFireInstance(entity, null);
			}
		}
	}

	public static void onVanillaFireTimeUpdate(Entity entity) {
		setCustomFireInstance(entity, null);
	}

	public static FireEffectInstance getFireEffectInstance(Entity entity) {
		return entity.getComponent(ENTITY_COMPONENT).fireEffectInstance;
	}

	public static void setCustomFireInstance(Entity entity, FireEffectInstance instance) {
		OrtusEntityComponent cmp = entity.getComponent(ENTITY_COMPONENT);
		cmp.fireEffectInstance = instance;
		if (cmp.fireEffectInstance != null) {
			if (entity.getFireTicks() > 0) {
				entity.setFireTicks(0);
			}
			if (!entity.world.isClient) {
				cmp.fireEffectInstance.sync(entity);
			}
		}
		else if (!entity.world.isClient) {
			 ClearFireEffectInstancePacket.send(entity, PlayerLookup.tracking(entity));
		}
	}

	public static void writeNbt(OrtusEntityComponent component, NbtCompound tag) {
		if (component.fireEffectInstance != null) {
			component.fireEffectInstance.writeNbt(tag);
		}
	}

	public static void readNbt(OrtusEntityComponent component, NbtCompound tag) {
		component.fireEffectInstance = FireEffectInstance.readNbt(tag);
	}

	public static class ClientOnly {
		public static void renderUIMeteorFire(MinecraftClient pMinecraft, MatrixStack matrices) {
			if (pMinecraft.player != null) {
				if (getFireEffectInstance(pMinecraft.player) == null) {
					return;
				}
			}
			FireEffectInstance instance = getFireEffectInstance(pMinecraft.player);
			FireEffectRenderer<FireEffectInstance> renderer = OrtusFireEffectRendererRegistry.RENDERERS.get(instance.type);
			if (renderer != null) {
				if (renderer.canRender(instance)) {
					renderer.renderScreen(instance, pMinecraft, matrices);
				}
			}
		}

		public static void renderWorldMeteorFire(MatrixStack matrices, VertexConsumerProvider provider, Camera camera, Entity pEntity) {
			if (getFireEffectInstance(pEntity) == null) {
				return;
			}
			FireEffectInstance instance = getFireEffectInstance(pEntity);
			FireEffectRenderer<FireEffectInstance> renderer = OrtusFireEffectRendererRegistry.RENDERERS.get(instance.type);
			if (renderer != null) {
				if (renderer.canRender(instance)) {
					renderer.renderWorld(instance, matrices, provider, camera, pEntity);
				}
			}
		}
	}
}
