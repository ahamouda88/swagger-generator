package com.swagger.document.utils;

import java.util.Arrays;
import java.util.function.Consumer;

public final class ArrayUtils {

    public static <T> void applyConsumerToArray(T[] array, Consumer<T> consumer) {
        if (array == null || consumer == null) return;

        Arrays.asList(array).forEach(obj -> consumer.accept(obj));
    }
}
