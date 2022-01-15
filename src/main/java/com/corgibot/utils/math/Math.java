package com.corgibot.utils.math;

import java.util.concurrent.ThreadLocalRandom;

public class Math {
    public static int random(int min, int max) {
        return ThreadLocalRandom.current().nextInt(min, max + 1);
    }

}
