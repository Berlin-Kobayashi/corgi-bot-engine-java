package com.corgibot.example;

import com.corgibot.engine.control.Mouse;
import com.corgibot.engine.game.Frame;
import com.corgibot.engine.game.Game;

import java.awt.*;

public class Demo {
    public static void main(String[] args) {
        Game game = new Game();
        game.onFrame(Demo::onFrame);
        game.start();
    }

    private static void onFrame(Frame frame) {
        frame.drawBlock(Mouse.getPosition(), Color.BLACK);
    }
}
