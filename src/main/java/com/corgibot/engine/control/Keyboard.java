package com.corgibot.engine.control;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Consumer;

// TODO use event queue for Keyboard events, and process them in onFrame to avoid race conditions
public class Keyboard {
    private static final Map<Integer, Runnable> keyMapping = new HashMap<>();

    private static Consumer<KeyEvent> onAnyKey;

    public static void onKey(int key, Runnable keyHandler) {
        keyMapping.put(key, keyHandler);
    }

    public static void onAnyKey(Consumer<KeyEvent> keyHandler) {
        onAnyKey = keyHandler;
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

            if (onAnyKey != null) {
                onAnyKey.accept(e);
            }
        }

        @Override
        public void keyReleased(KeyEvent e) {
        }
    }
}
