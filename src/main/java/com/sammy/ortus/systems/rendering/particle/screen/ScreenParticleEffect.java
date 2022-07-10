package com.sammy.ortus.systems.rendering.particle.screen;
import com.sammy.ortus.systems.rendering.particle.SimpleParticleEffect;
import com.sammy.ortus.systems.rendering.particle.screen.base.ScreenParticle;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.Vec2f;

public class ScreenParticleEffect extends SimpleParticleEffect {

    public final ScreenParticleType<?> type;
    public ScreenParticle.RenderOrder renderOrder;
    public ItemStack stack;
    public float xOrigin;
    public float yOrigin;
    public float xOffset;
    public float yOffset;

	public Vec2f startingVelocity = Vec2f.ZERO, endingMotion = Vec2f.ZERO;

    public ScreenParticleEffect(ScreenParticleType<?> type) {
        this.type = type;
    }
}
