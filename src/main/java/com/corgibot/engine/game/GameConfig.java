package com.corgibot.engine.game;

public class GameConfig {
    private String name;
    private int blockSize;
    private int size;
    private int frameDuration;

    public GameConfig(String name, int blockSize, int size, int frameDuration) {
        this.name = name;
        this.blockSize = blockSize;
        this.size = size;
        this.frameDuration = frameDuration;
    }

    public String getName() {
        return name;
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
