package com.corgibot.engine.game;

public class GameConfig {
    private int blockSize;
    private int size;
    private int frameDuration;
    private String name;

    public GameConfig(int blockSize, int size, int frameDuration, String name) {
        this.blockSize = blockSize;
        this.size = size;
        this.frameDuration = frameDuration;
        this.name = name;
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

    public String getName() {
        return name;
    }
}
