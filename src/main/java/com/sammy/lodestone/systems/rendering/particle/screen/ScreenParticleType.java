package com.sammy.lodestone.systems.rendering.particle.screen;

import com.sammy.lodestone.systems.rendering.particle.screen.base.ScreenParticle;
import net.minecraft.world.World;

public class ScreenParticleType<T extends ScreenParticleEffect> {

    public Factory<T> factory;
    public ScreenParticleType() {
    }

    public interface Factory<T extends ScreenParticleEffect> {
        ScreenParticle createParticle(World clientWorld, T options, double pX, double pY, double pXSpeed, double pYSpeed);
    }
}
