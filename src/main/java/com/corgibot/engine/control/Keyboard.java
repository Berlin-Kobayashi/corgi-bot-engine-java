package com.corgibot.engine.control;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.HashMap;
import java.util.Map;

// TODO use event queue for Keyboard events, and process them in onFrame to avoid race conditions
public class Keyboard {
    private static final Map<Integer, Runnable> keyMapping = new HashMap<>();

    public static void onKey(int key, Runnable keyHandler) {
        keyMapping.put(key, keyHandler);
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
