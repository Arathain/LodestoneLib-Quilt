package com.sammy.lodestone.helpers;

import com.sammy.lodestone.setup.LodestoneScreenParticles;
import com.sammy.lodestone.systems.rendering.particle.Easing;
import com.sammy.lodestone.systems.rendering.particle.ParticleBuilders;
import com.sammy.lodestone.systems.rendering.particle.screen.base.ScreenParticle;
import com.sammy.lodestone.systems.rendering.particle.screen.emitter.ItemParticleEmitter;
import net.minecraft.client.MinecraftClient;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

import java.awt.*;

public class OrtEmitter implements ItemParticleEmitter {
	@Override
	public void particleTick(ItemStack stack, float x, float y, ScreenParticle.RenderOrder renderOrder) {
		final MinecraftClient client = MinecraftClient.getInstance();
		World world = client.world;
		float gameTime = world.getTime() + client.getTickDelta();
		Color firstColor = Color.BLUE;
		Color secondColor = Color.CYAN;
		ParticleBuilders.create(LodestoneScreenParticles.STAR)
				.setAlpha(0.03f, 0f)
				.setLifetime(8)
				.setScale((float) (0.75f + Math.sin(gameTime * 0.05f) * 0.15f), 0)
				.setColor(firstColor, secondColor)
				.setColorCoefficient(1.25f)
				.randomOffset(0.05f)
				.setSpinOffset(0.025f * gameTime % 6.28f)
				.setSpin(0, 1)
				.setSpinEasing(Easing.EXPO_IN_OUT)
				.setAlphaEasing(Easing.QUINTIC_IN)
				.overrideRenderOrder(renderOrder)
				.centerOnStack(stack, -2, 2)
				.repeat(x, y, 1)
				.setScale((float) (0.75f - Math.sin(gameTime * 0.075f) * 0.15f), 0)
				.setColor(secondColor, firstColor)
				.setSpinOffset(0.785f - 0.01f * gameTime % 6.28f)
				.repeat(x, y, 1)
				.setScale((float) (0.9f - Math.sin(gameTime * 0.1f) * 0.175f), 0)
				.setColor(secondColor, firstColor)
				.setSpinOffset(0.8f - 0.01f * gameTime % 6.28f)
				.repeat(x, y, 1);
		gameTime += 31.4f;
		ParticleBuilders.create(LodestoneScreenParticles.STAR)
				.setAlpha(0.028f, 0f)
				.setLifetime(8)
				.setScale((float) (0.75f + Math.sin(gameTime * 0.05f) * 0.125f), 0)
				.setColor(firstColor, secondColor)
				.setColorCoefficient(1.25f)
				.randomOffset(0.05f)
				.setSpinOffset(0.025f * gameTime % 6.28f)
				.setAlphaEasing(Easing.QUINTIC_IN)
				.overrideRenderOrder(renderOrder)
				.centerOnStack(stack, 3, -3)
				.repeat(x, y, 1)
				.setScale((float) (0.85f - Math.sin(gameTime * 0.075f) * 0.15f), 0)
				.setColor(secondColor, firstColor)
				.setSpinOffset(0.785f - 0.01f * gameTime % 6.28f)
				.repeat(x, y, 1)
				.setScale((float) (0.95f - Math.sin(gameTime * 0.1f) * 0.175f), 0)
				.setColor(secondColor, firstColor)
				.setSpinOffset(0.8f - 0.01f * gameTime % 6.28f)
				.repeat(x, y, 1);
	}
}
