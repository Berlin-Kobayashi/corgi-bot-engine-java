package com.corgibot.engine.game;

import java.awt.*;

public class GameConfig {
    private String name;
    private int blockSize;
    private int size;
    private int frameDuration;

    public GameConfig( int blockSize, int frameDuration) {
        this.blockSize = blockSize;
        this.frameDuration = frameDuration;
    }

    public int getBlockSize() {
        return blockSize;
    }

    public int getSize() {
        return (int) Toolkit.getDefaultToolkit().getScreenSize().getHeight() / blockSize;
    }

    public int getFrameDuration() {
        return frameDuration;
    }
}
