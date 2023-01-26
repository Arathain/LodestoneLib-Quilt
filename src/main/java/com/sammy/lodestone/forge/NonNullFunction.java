package com.sammy.lodestone.forge;

import java.util.Objects;
import java.util.function.Function;

@FunctionalInterface
public interface NonNullFunction<T, R> extends Function<T, R> {
    default <V> NonNullFunction<T, V> andThen(NonNullFunction<? super R, ? extends V> after) {
        Objects.requireNonNull(after);
        return t -> after.apply(apply(t));
    }
}
