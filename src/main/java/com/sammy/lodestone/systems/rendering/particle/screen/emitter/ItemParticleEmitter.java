package com.sammy.lodestone.systems.rendering.particle.screen.emitter;

import com.sammy.lodestone.systems.rendering.particle.screen.base.ScreenParticle;
import net.minecraft.item.ItemStack;

public interface ItemParticleEmitter {
    void particleTick(ItemStack stack, float x, float y, ScreenParticle.RenderOrder renderOrder);
}
