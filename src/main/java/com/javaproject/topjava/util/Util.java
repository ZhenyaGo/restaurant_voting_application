package com.javaproject.topjava.util;

import lombok.experimental.UtilityClass;

import java.time.LocalTime;

@UtilityClass
public class Util {

    public static final LocalTime LIMIT_TIME = LocalTime.of(13,0, 0);

    public static boolean isBeforeDeadline(LocalTime lt) {
        return  lt.compareTo(LIMIT_TIME) <= 0;
    }
}
