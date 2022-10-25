package com.sammy.ortus.mixin;

import com.mojang.blaze3d.vertex.VertexConsumer;
import com.sammy.ortus.LodestoneLib;
import com.sammy.ortus.setup.LodestoneRenderLayers;
import com.sammy.ortus.systems.rendering.PositionTrackedEntity;
import com.sammy.ortus.systems.rendering.VFXBuilders;
import com.sammy.ortus.systems.rendering.particle.Easing;
import net.minecraft.client.render.entity.LivingEntityRenderer;
import org.quiltmc.loader.api.QuiltLoader;
import org.spongepowered.asm.mixin.Mixin;

import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.entity.EntityRenderer;
import net.minecraft.client.render.entity.EntityRendererFactory;
import net.minecraft.client.render.entity.model.EntityModel;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.passive.AllayEntity;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.math.Vector4f;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.awt.*;
import java.util.ArrayList;

import static com.sammy.ortus.handlers.RenderHandler.DELAYED_RENDER;
import static com.sammy.ortus.setup.LodestoneRenderLayers.queueUniformChanges;

@Mixin(LivingEntityRenderer.class)
public abstract class LivingEntityRendererMixin<T extends LivingEntity, M extends EntityModel<T>> extends EntityRenderer<T> {
	private static final Identifier LIGHT_TRAIL = new Identifier(LodestoneLib.MODID, "textures/energy_trail.png");
	private static final RenderLayer LIGHT_TYPE = LodestoneRenderLayers.SCROLLING_TEXTURE_TRIANGLE.apply(LIGHT_TRAIL);

	protected LivingEntityRendererMixin(EntityRendererFactory.Context ctx) {
		super(ctx);
	}

	// allay trail and twinkle
	@Inject(method = "render(Lnet/minecraft/entity/LivingEntity;FFLnet/minecraft/client/util/math/MatrixStack;Lnet/minecraft/client/render/VertexConsumerProvider;I)V", at = @At("TAIL"))
	public void render(T livingEntity, float entityYaw, float tickDelta, MatrixStack matrixStack, VertexConsumerProvider vertexConsumerProvider, int packedLightIn, CallbackInfo ci) {

		// new render
		if (QuiltLoader.isDevelopmentEnvironment() && livingEntity instanceof AllayEntity allayEntity) {

			// trail
			matrixStack.push();
			ArrayList<Vec3d> positions = new ArrayList<>(((PositionTrackedEntity) allayEntity).getPastPositions());
			VFXBuilders.WorldVFXBuilder builder = VFXBuilders.createWorld().setPosColorTexLightmapDefaultFormat();
			int amount = 1;
			for (int i = 0; i < amount; i++) {
				VertexConsumer balls = DELAYED_RENDER.getBuffer(queueUniformChanges(LodestoneRenderLayers.copy(i, LIGHT_TYPE),
						(instance -> instance.getUniformOrDefault("Speed").setFloat(1000 + 250f))));
				float size = 0.5f;
				float alpha = 0.9f;

				float x = (float) MathHelper.lerp(tickDelta, allayEntity.prevX, allayEntity.getX());
				float y = (float) MathHelper.lerp(tickDelta, allayEntity.prevY, allayEntity.getY());
				float z = (float) MathHelper.lerp(tickDelta, allayEntity.prevZ, allayEntity.getZ());

				builder.setColor(new Color(90, 200,255)).setOffset(-x, -y, -z)
						.setAlpha(alpha)
						.renderTrail(
								balls,
								matrixStack,
								positions.stream()
										.map(p -> new Vector4f((float) p.x, (float) p.y, (float) p.z, 1))
										.toList(),
								f -> (float) Math.cbrt(f) * size
						)
						.renderTrail(
								DELAYED_RENDER.getBuffer(LIGHT_TYPE),
								matrixStack,
								positions.stream()
										.map(p -> new Vector4f((float) p.x, (float) p.y, (float) p.z, 1))
										.toList(),
								f -> Easing.QUARTIC_IN_OUT.ease(f, 0, size, 1)
						);
			}
			matrixStack.pop();
		}
	}
}
