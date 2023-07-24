package team.lodestar.lodestone.helpers;

import team.lodestar.lodestone.handlers.screenparticle.ParticleEmitterHandler;
import team.lodestar.lodestone.setup.LodestoneScreenParticles;
import team.lodestar.lodestone.systems.rendering.particle.Easing;
import team.lodestar.lodestone.systems.rendering.particle.ScreenParticleBuilder;
import team.lodestar.lodestone.systems.rendering.particle.data.ColorParticleData;
import team.lodestar.lodestone.systems.rendering.particle.data.GenericParticleData;
import team.lodestar.lodestone.systems.rendering.particle.data.SpinParticleData;
import team.lodestar.lodestone.systems.rendering.particle.screen.LodestoneScreenParticleTextureSheet;
import team.lodestar.lodestone.systems.rendering.particle.screen.base.ScreenParticle;
import net.minecraft.client.MinecraftClient;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

import java.awt.*;
import java.util.ArrayList;
import java.util.HashMap;

public class OrtEmitter implements ParticleEmitterHandler.ItemParticleSupplier {

	@Override
	public void spawnParticles(HashMap<LodestoneScreenParticleTextureSheet, ArrayList<ScreenParticle>> target, World world, float tickDelta, ItemStack stack, float x, float y) {
		final MinecraftClient client = MinecraftClient.getInstance();
		float gameTime = world.getTime() + client.getTickDelta();
		Color firstColor = Color.BLUE;
		Color secondColor = Color.CYAN;
		ScreenParticleBuilder.create(LodestoneScreenParticles.STAR, target)
				.setColorData(ColorParticleData
						.create(firstColor, secondColor)
						.setCoefficient(1.25f)
						.build())
				.setLifetime(8)
				.setTransparencyData(GenericParticleData.create(0.2f, 0).setEasing(Easing.QUINTIC_IN).build())
				.setScaleData(GenericParticleData.create((float) (0.75f + Math.sin(gameTime * 0.05f) * 0.15f), 0).build())
				.setRandomOffset(0.05f)
				.setSpinData(SpinParticleData.create(0, 1).setSpinOffset(0.025f * gameTime % 6.28f).setEasing(Easing.EXPO_IN_OUT).build())
				.spawnOnStack(0, 0)
				.setColorData(ColorParticleData
						.create(secondColor, firstColor)
						.setCoefficient(1.25f)
						.build())
				.setScaleData(GenericParticleData.create((float) (0.75f - Math.sin(gameTime * 0.075f) * 0.15f), 0).build())
				.setSpinData(SpinParticleData.create(0, 1).setSpinOffset(0.785f - 0.01f * gameTime % 6.28f).setEasing(Easing.EXPO_IN_OUT).build())
				.spawnOnStack(0, 0)
				.setScaleData(GenericParticleData.create((float) (0.9f - Math.sin(gameTime * 0.1f) * 0.175f), 0).build())
				.setSpinData(SpinParticleData.create(0, 1).setSpinOffset(0.8f - 0.01f * gameTime % 6.28f).setEasing(Easing.EXPO_IN_OUT).build())
				.spawnOnStack(0, 0);
		gameTime += 31.4f;
//		ScreenParticleBuilder.create(LodestoneScreenParticles.STAR, target)
//				.setAlpha(0.028f, 0f)
//				.setLifetime(8)
//				.setScale((float) (0.75f + Math.sin(gameTime * 0.05f) * 0.125f), 0)
//				.setColor(firstColor, secondColor)
//				.setColorCoefficient(1.25f)
//				.randomOffset(0.05f)
//				.setSpinOffset(0.025f * gameTime % 6.28f)
//				.setAlphaEasing(Easing.QUINTIC_IN)
//				.centerOnStack(stack, 3, -3)
//				.repeat(x, y, 1)
//				.setScale((float) (0.85f - Math.sin(gameTime * 0.075f) * 0.15f), 0)
//				.setColor(secondColor, firstColor)
//				.setSpinOffset(0.785f - 0.01f * gameTime % 6.28f)
//				.repeat(x, y, 1)
//				.setScale((float) (0.95f - Math.sin(gameTime * 0.1f) * 0.175f), 0)
//				.setColor(secondColor, firstColor)
//				.setSpinOffset(0.8f - 0.01f * gameTime % 6.28f)
//				.repeat(x, y, 1);
	}
}
