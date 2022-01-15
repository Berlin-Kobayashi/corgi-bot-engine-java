package com.corgibot.engine.game;

public class GameConfig {
    private int blockSize;
    private int frameSize;
    private int frameDuration;
    private String name;

    public GameConfig(int blockSize, int frameSize, int frameDuration, String name) {
        this.blockSize = blockSize;
        this.frameSize = frameSize;
        this.frameDuration = frameDuration;
        this.name = name;
    }

    public int getBlockSize() {
        return blockSize;
    }

    public int getFrameSize() {
        return frameSize;
    }

    public int getFrameDuration() {
        return frameDuration;
    }

    public String getName() {
        return name;
    }
}
