package com.corgibot.engine.game;

public class GameConfig {
    private int blockSize;
    private int size;
    private int frameDuration;

    public GameConfig(int blockSize, int size, int frameDuration) {
        this.blockSize = blockSize;
        this.size = size;
        this.frameDuration = frameDuration;
    }

    public int getBlockSize() {
        return blockSize;
    }

    public int getSize() {
        return size;
    }

    public int getFrameDuration() {
        return frameDuration;
    }
}
