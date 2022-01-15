package com.corgibot.engine.game;

public class GameConfig {
    private int blockSize;
    private int screenSize;
    private int frameRate;
    private String name;
    private String assetRoot;

    public GameConfig(int blockSize, int screenSize, int frameRate, String name, String assetRoot) {
        this.blockSize = blockSize;
        this.screenSize = screenSize;
        this.frameRate = frameRate;
        this.name = name;
        this.assetRoot = assetRoot;
    }

    public int getBlockSize() {
        return blockSize;
    }

    public int getScreenSize() {
        return screenSize;
    }

    public int getFrameRate() {
        return frameRate;
    }

    public String getName() {
        return name;
    }

    public String getAssetRoot() {
        return assetRoot;
    }
}
