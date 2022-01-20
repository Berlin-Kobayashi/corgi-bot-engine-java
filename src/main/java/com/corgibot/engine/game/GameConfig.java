package com.corgibot.engine.game;

import java.awt.*;

public class GameConfig {
    private int blockSize;
    private int frameDuration;

    public GameConfig(int blockSize, int frameDuration) {
        this.blockSize = blockSize;
        this.frameDuration = frameDuration;
    }

    public int getBlockSize() {
        return blockSize;
    }

    public int getWidth() {
        return (int) (Toolkit.getDefaultToolkit().getScreenSize().getWidth()) / blockSize;
    }

    public int getHeight() {
        return (int) (Toolkit.getDefaultToolkit().getScreenSize().getHeight() - 42) / blockSize;
    }

    public int getFrameDuration() {
        return frameDuration;
    }
}
