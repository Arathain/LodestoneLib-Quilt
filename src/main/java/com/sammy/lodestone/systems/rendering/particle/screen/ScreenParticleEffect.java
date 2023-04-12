package com.sammy.lodestone.systems.rendering.particle.screen;
import com.sammy.lodestone.systems.rendering.particle.SimpleParticleEffect;
import com.sammy.lodestone.systems.rendering.particle.screen.base.ScreenParticle;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.Vec2f;

import java.util.function.Consumer;

public class ScreenParticleEffect extends SimpleParticleEffect {

	public final ScreenParticleType<?> type;
	public LodestoneScreenParticleTextureSheet renderType = LodestoneScreenParticleTextureSheet.ADDITIVE;
	public Consumer<GenericScreenParticle> actor;
	public boolean tracksStack;
	public double stackTrackXOffset;
	public double stackTrackYOffset;

    public ScreenParticleEffect(ScreenParticleType<?> type) {
        this.type = type;
    }
}
