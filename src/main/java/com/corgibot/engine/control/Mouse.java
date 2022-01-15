package com.corgibot.engine.control;

import com.corgibot.engine.game.Frame;
import com.corgibot.engine.game.Game;
import com.corgibot.engine.game.Position;

import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.function.Consumer;

public class Mouse {

    private static Consumer<Position> clickHandler;

    public static void onClick(Consumer<Position> clickHandler) {
        Mouse.clickHandler = clickHandler;
    }

    public static Position getPosition() {
        try {
            PointerInfo a = MouseInfo.getPointerInfo();
            Point b = a.getLocation();

            Component[] components = Frame.frame.getComponents();

            Component canvas = components[0];

            return getBlockPosition(b.x - canvas.getLocationOnScreen().x, b.y - canvas.getLocationOnScreen().y);

        } catch (Exception e) {
            return getBlockPosition(0, 0);
        }
    }

    private static Position getBlockPosition(int pixelX, int pixelY) {
        Position position = new Position(pixelX / Game.config.getBlockSize(), pixelY / Game.config.getBlockSize());
        if (position.x >= Game.config.getSize()) {
            position.x = Game.config.getSize() - 1;
        }

        if (position.x < 0) {
            position.x = 0;
        }

        if (position.y >= Game.config.getSize()) {
            position.y = Game.config.getSize() - 1;
        }

        if (position.y < 0) {
            position.y = 0;
        }

        return position;
    }

    public static class Listener implements MouseListener {
        @Override
        public void mouseClicked(MouseEvent e) {
        }

        @Override
        public void mousePressed(MouseEvent e) {
            if (clickHandler != null) {
                clickHandler.accept(getBlockPosition(e.getX(), e.getY()));
            }
        }

        @Override
        public void mouseReleased(MouseEvent e) {

        }

        @Override
        public void mouseEntered(MouseEvent e) {

        }

        @Override
        public void mouseExited(MouseEvent e) {

        }
    }
}
