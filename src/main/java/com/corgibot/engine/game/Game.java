package com.corgibot.engine.game;

import java.awt.*;
import java.time.Duration;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

public class Game {
    public static GameConfig config;
    private boolean isRunning;
    private Instant lastDraw;
    public final Raster raster;
    private final List<FrameHandler> frameHandlers = new ArrayList<>();
    private int frameCounter = 0;

    private static class FrameHandler {
        private final int frequency;
        private final Consumer<Game> handler;

        public FrameHandler(int frequency, Consumer<Game> handler) {
            this.frequency = frequency;
            this.handler = handler;
        }
    }

    // TODO configure asset folder and load all assets on start
    public Game() {
        this(new GameConfig(16, 13, Color.white));
    }

    public Game(GameConfig config) {
        Game.config = config;
        this.raster = new Raster(config.getBlockSize(), config.getWidth(), config.getHeight(), config.getBackgroundColor());
    }

    public void onFrame(Consumer<Game> frameHandler) {
        this.frameHandlers.add(new FrameHandler(1, frameHandler));
    }

    public void onFrame(int frequency, Consumer<Game> frameHandler) {
        this.frameHandlers.add(new FrameHandler(frequency, frameHandler));
    }

    public void start() {
        isRunning = true;
        Duration frameDuration = Duration.of(config.getFrameDuration(), ChronoUnit.MILLIS);
        while (isRunning) {
            Duration sinceLastDraw = lastDraw == null ? frameDuration : Duration.between(lastDraw, Instant.now());
            if (sinceLastDraw.compareTo(frameDuration) >= 0) {
                frameHandlers.forEach(frameHandler -> {
                    if (frameHandler != null && frameCounter % frameHandler.frequency == 0) {
                        frameHandler.handler.accept(this);
                    }
                });

                raster.draw();

                sinceLastDraw = lastDraw == null ? frameDuration : Duration.between(lastDraw, Instant.now());
                long delay = sinceLastDraw.minus(frameDuration).toMillis();
                if (delay > 0) {
                    System.err.println("Frame delay of: " + delay + " ms");
                }

                lastDraw = Instant.now();

                frameCounter++;
            }
        }
    }

    public void stop() {
        isRunning = false;
    }
}
