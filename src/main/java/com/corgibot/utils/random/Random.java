package com.corgibot.utils.random;

import java.awt.*;
import java.util.concurrent.ThreadLocalRandom;

public class Random {
    public static int number(int min, int max) {
        return ThreadLocalRandom.current().nextInt(min, max + 1);
    }

    public static Color color() {
        return new Color(Random.number(0, 255), Random.number(0, 255), Random.number(0, 255));
    }
}
