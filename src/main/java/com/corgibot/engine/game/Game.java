package com.corgibot.engine.game;

import java.util.function.Consumer;

public class Game {
    private final GameConfig config;
    private final GameState state;

    public Game(GameConfig config) {
        this.config = config;
        this.state = new GameState();
    }

    public void onFrame(Consumer<GameState> frameHandler) {
        if (state.isRunning()) {
            // Todo pass Frame class
            frameHandler.accept(state);
            state.incrementFrameCounter();
        }
    }

    public void start() {
        state.setRunning();
    }

    public void stop() {
        state.setStopped();
    }
}
