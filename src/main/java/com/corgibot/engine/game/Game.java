package com.corgibot.engine.game;

import com.corgibot.utils.time.Time;

import java.time.Duration;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.time.temporal.TemporalUnit;
import java.util.function.Consumer;

public class Game {
    public static GameConfig config;
    private boolean isRunning;
    private Instant lastDraw;
    private final Frame frame;
    private Consumer<Frame> frameHandler;

    public Game() {
        this(new GameConfig(16, 40, 100));
    }

    public Game(GameConfig config) {
        Game.config = config;
        this.frame = new Frame(config.getBlockSize(), config.getSize());
    }

    public void onFrame(Consumer<Frame> frameHandler) {
        this.frameHandler = frameHandler;
    }

    public void start() {
        isRunning = true;
        Duration frameDuration = Duration.of(config.getFrameDuration(), ChronoUnit.MILLIS);
        while (isRunning) {
            if (frameHandler != null) {
                Instant now = Instant.now();
                Duration sinceLastDraw = lastDraw == null ? frameDuration : Duration.between(lastDraw, now);
                long delay = sinceLastDraw.minus(frameDuration).toMillis();
                if (delay >= 0) {
                    frameHandler.accept(frame);
                    frame.draw();
                    lastDraw = Instant.now();

                    if (delay > 0) {
                        System.out.println("Frame delay of: " + delay + " ms");
                    }
                }
            }
        }
    }

    public void stop() {
        isRunning = false;
    }
}
