package com.corgibot.example;

import com.corgibot.engine.audio.Speaker;
import com.corgibot.engine.control.Mouse;
import com.corgibot.engine.game.GameConfig;
import com.corgibot.engine.game.Raster;
import com.corgibot.engine.game.Game;

import java.awt.*;

public class Demo {
    public static void main(String[] args) {
        Game game = new Game(new GameConfig(32, 13, Color.black));
        game.onFrame(Demo::onFrame);
        game.start();
    }

    private static void onFrame(Game game) {
        game.raster.drawBlock(Mouse.getPosition(),"Dan");
    }
}
