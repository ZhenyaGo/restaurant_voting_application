package com.javaproject.topjava.util;

import org.springframework.lang.Nullable;

public class Util {
    private Util() {
    }

    public static <T extends Comparable<T>> boolean isBefore(T value, @Nullable T time) {
        return (time == null || value.compareTo(time) <= 0);
    }
}
