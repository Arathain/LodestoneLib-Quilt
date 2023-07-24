package team.lodestar.lodestone.systems.rendering.particle.screen;
import team.lodestar.lodestone.systems.rendering.particle.SimpleParticleEffect;

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
