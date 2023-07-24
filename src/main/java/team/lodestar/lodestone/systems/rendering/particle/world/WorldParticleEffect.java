package team.lodestar.lodestone.systems.rendering.particle.world;

import com.mojang.brigadier.StringReader;
import com.mojang.serialization.Codec;
import team.lodestar.lodestone.systems.rendering.particle.SimpleParticleEffect;
import net.minecraft.client.particle.ParticleTextureSheet;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.particle.ParticleEffect;
import net.minecraft.particle.ParticleType;

import java.util.function.Consumer;

public class WorldParticleEffect extends SimpleParticleEffect implements ParticleEffect {

	public final ParticleType<?> type;
	public ParticleTextureSheet textureSheet;
	public Consumer<GenericParticle> actor;

	public boolean noClip = false;

	public WorldParticleEffect(ParticleType<?> type) {
		this.type = type;
	}

    public static Codec<WorldParticleEffect> codecFor(ParticleType<?> type) {
        return Codec.unit(() -> new WorldParticleEffect(type));
    }

    @Override
    public ParticleType<?> getType() {
        return type;
    }

    @Override
    public void write(PacketByteBuf buf) {

    }

    public String asString() {
        return "";
    }
    public static final Factory<WorldParticleEffect> DESERIALIZER = new Factory<>() {
        @Override
        public WorldParticleEffect read(ParticleType<WorldParticleEffect> type, StringReader reader) {
            return new WorldParticleEffect(type);
        }

        @Override
        public WorldParticleEffect read(ParticleType<WorldParticleEffect> type, PacketByteBuf buf) {
            return new WorldParticleEffect(type);
        }
    };
}
