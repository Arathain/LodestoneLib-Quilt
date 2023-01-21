package com.sammy.lodestone.helpers;

import net.minecraft.nbt.NbtCompound;

import java.util.function.Function;

public final class NbtHelper {
	public static int getOrDefaultInt(Function<NbtCompound, Integer> getter, int defaultValue, NbtCompound nbt) {
		try {
			return getter.apply(nbt);
		} catch (Exception ignored) {
			return defaultValue;
		}
	}

	public static int getOrThrowInt(NbtCompound nbt, String value) {
		if (!nbt.contains(value)) {
			throw new NullPointerException();
		}
		return nbt.getInt(value);
	}
}
