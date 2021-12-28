package com.javaproject.topjava.util;

import org.springframework.lang.Nullable;

import java.time.LocalTime;

public class Util {

    public static final LocalTime LIMIT_TIME = LocalTime.of(11,0, 0);

    private Util() {
    }

    public static <T extends Comparable<T>> boolean isBefore(T value, @Nullable T time) {
        return (time == null || value.compareTo(time) <= 0);
    }
}
