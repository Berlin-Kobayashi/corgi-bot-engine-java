package com.corgibot.engine.time;

import java.util.concurrent.TimeUnit;

public class Time {
    public static void sleep(int milliSeconds) {
        try {
            TimeUnit.MILLISECONDS.sleep(milliSeconds);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
