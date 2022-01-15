package com.corgibot.example;

import com.corgibot.engine.game.Frame;
import com.corgibot.engine.game.Game;

import java.awt.*;

public class Test {
    public static void main(String[] args) {
        Game game = new Game();
        game.onFrame(Test::onFrame);
        game.start();
    }

    private static void onFrame(Frame frame) {
        if(frame.getCounter() < frame.getSize()){
            frame.drawBlock(frame.getCounter(), 0, "Item");
            frame.drawBlock(0, frame.getCounter(), Color.BLACK);
        }

        frame.drawText("Hi");
    }
}
