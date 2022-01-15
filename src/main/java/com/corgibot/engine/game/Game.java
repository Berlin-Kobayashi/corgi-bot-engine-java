package com.corgibot.engine.game;

import com.corgibot.utils.time.Time;

import java.util.function.Consumer;

public class Game {
    public static GameConfig config;
    private boolean isRunning;
    private final Frame frame;
    private Consumer<Frame> frameHandler;

    public Game() {
        this(new GameConfig(16, 40, 100));
    }

    public Game(GameConfig config) {
        Game.config = config;
        this.frame = new Frame(config.getBlockSize(), config.getSize());
    }

    public void onFrame(Consumer<Frame> frameHandler) {
        this.frameHandler = frameHandler;
    }

    public void start() {
        isRunning = true;

        while (isRunning) {
            if (frameHandler != null) {
                frameHandler.accept(frame);
                frame.draw();

                Time.sleep(config.getFrameDuration());
            }
        }
    }

    public void stop() {
        isRunning = false;
    }
}
