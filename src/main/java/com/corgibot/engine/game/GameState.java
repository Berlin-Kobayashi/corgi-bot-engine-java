package com.corgibot.engine.game;

public class GameState {
    private boolean isRunning;
    private int frameCounter;
    private final FrameState frameState;

    public GameState() {
        this.isRunning = false;
        this.frameCounter = 0;
        this.frameState = new FrameState();
    }

    public boolean isRunning() {
        return isRunning;
    }

    void setRunning() {
        isRunning = true;
    }

    void setStopped() {
        isRunning = false;
    }

    public int getFrameCounter() {
        return frameCounter;
    }

    void incrementFrameCounter() {
        this.frameCounter++;
    }

    public FrameState getFrame() {
        return frameState;
    }
}
