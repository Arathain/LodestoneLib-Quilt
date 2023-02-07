package com.sammy.lodestone.systems.particle.world;

import com.sammy.lodestone.systems.particle.SimpleParticleEffect;
import net.fabricmc.fabric.impl.client.particle.FabricSpriteProviderImpl;
import net.minecraft.client.world.ClientWorld;

import java.util.ArrayList;

import static com.sammy.lodestone.systems.particle.SimpleParticleEffect.ParticleSpritePicker.FIRST_INDEX;

public class FrameSetParticle extends GenericParticle {
    public ArrayList<Integer> frameSet = new ArrayList<>();
    public FrameSetParticle(ClientWorld world, WorldParticleEffect data, FabricSpriteProviderImpl spriteSet, double x, double y, double z, double xd, double yd, double zd) {
        super(world, data, spriteSet, x, y, z, xd, yd, zd);
    }

	@Override
	public void tick() {
		if (age < frameSet.size()) {
			setSprite(frameSet.get(age));
		}
		super.tick();
	}

	@Override
	public SimpleParticleEffect.ParticleSpritePicker getSpritePicker() {
		return FIRST_INDEX;
	}

	protected void addLoop(int min, int max, int times) {
		for (int i = 0; i < times; i++) {
			addFrames(min, max);
		}
	}

	protected void addFrames(int min, int max) {
		for (int i = min; i <= max; i++) {
			frameSet.add(i);
		}
	}

	protected void insertFrames(int insertIndex, int min, int max) {
		for (int i = min; i <= max; i++) {
			frameSet.add(insertIndex, i);
		}
	}
}
