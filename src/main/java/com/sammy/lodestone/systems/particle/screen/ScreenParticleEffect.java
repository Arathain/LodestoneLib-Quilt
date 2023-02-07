package com.sammy.lodestone.systems.particle.screen;
import com.sammy.lodestone.systems.particle.SimpleParticleEffect;

import java.util.function.Consumer;

public class ScreenParticleEffect extends SimpleParticleEffect {

	public final ScreenParticleType<?> type;
	public LodestoneScreenParticleTextureSheet renderType = LodestoneScreenParticleTextureSheet.ADDITIVE;
	public Consumer<GenericScreenParticle> actor;


	public ScreenParticleEffect(ScreenParticleType<?> type) {
        this.type = type;
    }
}
