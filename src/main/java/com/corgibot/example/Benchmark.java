package com.corgibot.example;

import com.corgibot.engine.game.Raster;
import com.corgibot.engine.game.Game;
import com.corgibot.engine.game.GameConfig;
import com.corgibot.engine.game.Position;
import com.corgibot.utils.random.Random;

import java.awt.*;

public class Benchmark {
    public static void main(String[] args) {
        Game game = new Game(new GameConfig(1, 13, Color.black));
        game.onFrame(Benchmark::onFrame);
        game.start();
    }

    private static void onFrame(Raster raster) {
        raster.drawHead(String.format("Pixels: %,d; Colors: %,d", Game.config.getHeight() * Game.config.getWidth(), 256 * 256 * 256));

        for (int i = 0; i < Game.config.getWidth(); i++) {
            for (int j = 0; j < Game.config.getHeight(); j++) {
                Position pos = new Position(i, j);
                raster.drawBlock(pos, Random.color());
            }
        }
    }
}
