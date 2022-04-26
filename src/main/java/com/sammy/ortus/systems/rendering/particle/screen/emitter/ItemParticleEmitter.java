package com.sammy.ortus.systems.rendering.particle.screen.emitter;

import com.sammy.ortus.systems.rendering.particle.screen.base.ScreenParticle;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.item.ItemStack;

public interface ItemParticleEmitter {
    @Environment(EnvType.CLIENT)
    public void particleTick(ItemStack stack, float x, float y, ScreenParticle.RenderOrder renderOrder);
}
