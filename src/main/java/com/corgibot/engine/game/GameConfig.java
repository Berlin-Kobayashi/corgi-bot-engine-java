package com.corgibot.engine.game;

import java.awt.*;

public class GameConfig {
    private int blockSize;
    private int frameDuration;
    private Color backgroundColor;

    public GameConfig(int blockSize, int frameDuration, Color backgroundColor) {
        this.blockSize = blockSize;
        this.frameDuration = frameDuration;
        this.backgroundColor = backgroundColor;
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

    public Color getBackgroundColor() {
        return backgroundColor;
    }
}
