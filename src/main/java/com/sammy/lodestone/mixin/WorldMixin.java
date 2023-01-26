package com.sammy.lodestone.mixin;

import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;

@Mixin(value = World.class, priority = 1100)
public abstract class WorldMixin {
}
