package main.java.com.corgibot.engine.Mathe;

import java.util.concurrent.ThreadLocalRandom;

public class Zufall {
    public static int zahl(int von, int bis){
        return ThreadLocalRandom.current().nextInt(von, bis + 1);
    }
}
