package com.corgibot.example;

import com.corgibot.engine.game.Frame;
import com.corgibot.engine.game.Game;
import com.corgibot.engine.game.GameConfig;
import com.corgibot.engine.game.Position;
import com.corgibot.utils.random.Random;

public class Benchmark {
    public static void main(String[] args) {
        Game game = new Game(new GameConfig("Benchmark", 1, 640, 13));
        game.onFrame(Benchmark::onFrame);
        game.start();
    }

    private static void onFrame(Frame frame) {
        frame.drawHead(String.format("Pixels: %,d; Colors: %,d", Game.config.getSize() * Game.config.getSize(), 256 * 256 * 256));

        for (int i = 0; i < Game.config.getSize(); i++) {
            for (int j = 0; j < Game.config.getSize(); j++) {
                Position pos = new Position(i, j);
                frame.drawBlock(pos, Random.color());
            }
        }
    }
}
