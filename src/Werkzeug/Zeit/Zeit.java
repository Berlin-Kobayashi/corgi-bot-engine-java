package Werkzeug.Zeit;

import java.util.concurrent.TimeUnit;

public class Zeit {
    public static void warten(int milliSekunden) {
        try {
            TimeUnit.MILLISECONDS.sleep(milliSekunden);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
