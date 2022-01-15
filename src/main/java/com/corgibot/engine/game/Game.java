package com.corgibot.engine.game;

import com.corgibot.engine.time.Time;

import java.util.function.Consumer;

public class Game {
    private final GameConfig config;
    private final GameState state;
    private final Frame frame;
    private Consumer<Frame> frameHandler;

    public Game() {
        this(new GameConfig(16, 40, 100, "Corgi Bot"));
    }

    public Game(GameConfig config) {
        this.config = config;
        this.state = new GameState();
        this.frame = new Frame(config.getBlockSize(), config.getFrameSize());
    }

    public void onFrame(Consumer<Frame> frameHandler) {
        this.frameHandler = frameHandler;
    }

    public void start() {
        state.setRunning();

        while (state.isRunning()) {
            if (frameHandler != null) {
                frameHandler.accept(frame);
                frame.draw();

                state.incrementFrameCounter();
                Time.sleep(config.getFrameDuration());
            }
        }
    }

    public void stop() {
        state.setStopped();
    }
}
