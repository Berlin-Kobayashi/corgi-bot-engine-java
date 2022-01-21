package com.corgibot.utils.random;

import com.corgibot.engine.game.Position;

import java.awt.*;
import java.util.concurrent.ThreadLocalRandom;

public class Random {
    public static boolean coinToss() {
        return number(0, 1) == 0;
    }

    public static int number(int min, int max) {
        return ThreadLocalRandom.current().nextInt(min, max + 1);
    }

    public static Color color() {
        return new Color(Random.number(0, 255), Random.number(0, 255), Random.number(0, 255));
    }

    public static Position position(int width, int height) {
        return new Position(number(0, width), number(0, height));
    }
}
