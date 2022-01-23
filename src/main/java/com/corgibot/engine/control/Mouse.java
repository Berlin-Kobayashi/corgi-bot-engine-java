package com.corgibot.engine.control;

import com.corgibot.engine.game.Raster;
import com.corgibot.engine.game.Game;
import com.corgibot.engine.game.Position;

import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.function.Consumer;

// TODO use event queue for Mouse events, and process them in onFrame to avoid race conditions
// TODO support right and middle click
public class Mouse {

    private static Consumer<Position> clickHandler;

    public static void onClick(Consumer<Position> clickHandler) {
        Mouse.clickHandler = clickHandler;
    }

    public static Position getPosition() {
        try {
            PointerInfo a = MouseInfo.getPointerInfo();
            Point b = a.getLocation();

            return getBlockPosition(b.x - Raster.frame.getLocationOnScreen().x, b.y - Raster.frame.getLocationOnScreen().y);

        } catch (Exception e) {
            return getBlockPosition(0, 0);
        }
    }

    private static Position getBlockPosition(int pixelX, int pixelY) {
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        int marginLeft = (int) (screenSize.getWidth() - Game.config.getWidth() * Game.config.getBlockSize()) / 2;

        System.out.println(marginLeft);

        Position position = new Position( (pixelX-marginLeft) / Game.config.getBlockSize(), pixelY / Game.config.getBlockSize());
        if (position.x >= Game.config.getWidth()) {
            position.x = Game.config.getWidth() - 1;
        }

        if (position.x < 0) {
            position.x = 0;
        }

        if (position.y >= Game.config.getHeight()) {
            position.y = Game.config.getHeight() - 1;
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
