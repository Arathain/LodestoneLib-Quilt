package com.sammy.lodestone.forge;

import net.minecraft.nbt.NbtCompound;

public interface INBTSerializable<T extends NbtCompound> {
    default T serializeNBT() {
        throw new RuntimeException("override serializeNBT!");
    }

    default void deserializeNBT(T nbt) {
        throw new RuntimeException("override deserializeNBT!");
    }
}
