package com.sammy.lodestone.forge;

import java.util.Objects;
import java.util.function.Consumer;

@FunctionalInterface
public interface NonNullConsumer<T> extends Consumer<T> {
    default NonNullConsumer<T> andThen(NonNullConsumer<? super T> after) {
        Objects.requireNonNull(after);
        return (T t) -> { accept(t); after.accept(t); };
    }

    static <T> NonNullConsumer<T> noop() {
        return t -> {};
    }
}
