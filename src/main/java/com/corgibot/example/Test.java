package com.corgibot.example;

import com.corgibot.engine.audio.Speaker;
import com.corgibot.engine.control.Keyboard;
import com.corgibot.engine.control.Mouse;
import com.corgibot.engine.game.Frame;
import com.corgibot.engine.game.Game;
import com.corgibot.engine.game.Position;

import java.awt.*;
import java.awt.event.KeyEvent;

public class Test {
    private static Position clickedPosition;
    private static boolean spacePressed = false;
    private static int spaceCounter = 0;

    public static void main(String[] args) {
        Game game = new Game();
        Keyboard.onKey(KeyEvent.VK_SPACE, Test::onSpace);
        Mouse.onClick(Test::onClick);
        game.onFrame(Test::onFrame);
        game.start();
    }

    private static void onClick(Position position) {
        clickedPosition = position;
    }

    private static void onSpace() {
        spacePressed = true;
    }

    private static void onFrame(Frame frame) {
        if (spaceCounter < frame.getSize()) {
            if (spacePressed) {
                frame.drawImage(new Position(spaceCounter, 0), "Item");
                frame.drawBlock(new Position(0, spaceCounter), Color.BLACK);
                spacePressed = false;
                spaceCounter++;

                Speaker.play("Bauen");
            }
        }
        if (clickedPosition != null) {
            frame.drawImage(clickedPosition, "Wand");
        }

        frame.drawImage(Mouse.getPosition(), "Körper");

        frame.drawText(String.valueOf(spaceCounter));
    }
}
