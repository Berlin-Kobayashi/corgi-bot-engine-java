package Werkzeug.Steuerung;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.HashMap;
import java.util.Map;

public class Tastatur {

    private static final Map<Integer, Runnable> keyMapping = new HashMap<>();

    public static void wennTaste(int taste, Runnable aktion) {
        keyMapping.put(taste, aktion);
    }

    public static class Listener implements KeyListener {
        @Override
        public void keyTyped(KeyEvent e) {
        }

        @Override
        public void keyPressed(KeyEvent e) {
            if (keyMapping.containsKey(e.getKeyCode())) {
                keyMapping.get(e.getKeyCode()).run();
            }
        }

        @Override
        public void keyReleased(KeyEvent e) {
        }
    }
}
