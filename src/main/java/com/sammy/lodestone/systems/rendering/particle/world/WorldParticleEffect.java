package com.sammy.lodestone.systems.rendering.particle.world;

import com.mojang.brigadier.StringReader;
import com.mojang.serialization.Codec;
import com.sammy.lodestone.systems.rendering.particle.SimpleParticleEffect;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.particle.ParticleEffect;
import net.minecraft.particle.ParticleType;
import org.joml.Vector3f;

public class WorldParticleEffect extends SimpleParticleEffect implements ParticleEffect {

    public ParticleType<?> type;
    public Vector3f startingVelocity = new Vector3f(0,0,0), endingMotion = new Vector3f(0,0,0);
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
