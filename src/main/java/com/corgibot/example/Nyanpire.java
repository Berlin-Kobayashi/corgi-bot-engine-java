package com.corgibot.example;

import com.corgibot.engine.audio.Speaker;
import com.corgibot.engine.control.Keyboard;
import com.corgibot.engine.game.Frame;
import com.corgibot.engine.game.Game;
import com.corgibot.engine.game.GameConfig;
import com.corgibot.engine.game.Position;
import com.corgibot.utils.random.Random;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;

public class Nyanpire {
    private static final int MAX_YOKAI = 3;
    private static final Game game = new Game(new GameConfig(50, 13, Color.darkGray));

    private static final Player player = new Player(getMidPosition(), "Nyanpire/Player");
    private static final List<Yokai> yokais = new ArrayList<>();
    private static final Map<Yokai, AimedProjectile> aimedProjectiles = new HashMap<>();

    static class Player {
        private final Position position;
        private final String image;

        public Player(Position position, String image) {
            this.position = position;
            this.image = image;
        }
    }

    static class Yokai {
        private final Position position;
        private final Position nextPosition;
        private final String image;

        private Yokai(Position position, String image) {
            this.position = new Position(position.x, position.y);
            this.nextPosition = new Position(position.x, position.y);
            this.image = image;
        }

        private void moveTowardsPlayer() {
            if (player.position.x < position.x - 1) {
                nextPosition.x--;
            } else {
                if (player.position.x > position.x + 1) {
                    nextPosition.x++;
                }
            }

            if (player.position.y < position.y - 1) {
                nextPosition.y--;
            } else {
                if (player.position.y > position.y + 1) {
                    nextPosition.y++;
                }
            }
        }
    }

    static class AimedProjectile {
        private final Position position;
        private final Position nextPosition;
        private final String image;
        private final Yokai target;

        private AimedProjectile(Position position, String image, Yokai target) {
            this.position = new Position(position.x, position.y);
            this.nextPosition = new Position(position.x, position.y);
            this.image = image;
            this.target = target;
        }

        private void moveTowardsTarget() {
            if (target.position.x < position.x) {
                nextPosition.x--;
            } else {
                if (target.position.x > position.x) {
                    nextPosition.x++;
                }
            }

            if (target.position.y < position.y) {
                nextPosition.y--;
            } else {
                if (target.position.y > position.y) {
                    nextPosition.y++;
                }
            }
        }
    }

    public static void main(String[] args) {
        Speaker.loop("Nyanpire/Music");
        Keyboard.onKey(KeyEvent.VK_LEFT, Nyanpire::moveWorldRight);
        Keyboard.onKey(KeyEvent.VK_RIGHT, Nyanpire::moveWorldLeft);
        Keyboard.onKey(KeyEvent.VK_UP, Nyanpire::moveWorldDown);
        Keyboard.onKey(KeyEvent.VK_DOWN, Nyanpire::moveWorldUp);

        game.onFrame(Nyanpire::onFrame);

        game.start();
    }

    private static void moveWorldLeft() {
        aimedProjectiles.values().forEach(aimedProjectile -> aimedProjectile.nextPosition.x--);
        yokais.forEach(yokai -> yokai.nextPosition.x--);
    }

    private static void moveWorldRight() {

        aimedProjectiles.values().forEach(aimedProjectile -> aimedProjectile.nextPosition.x++);

        yokais.forEach(yokai -> yokai.nextPosition.x++);
    }

    private static void moveWorldUp() {
        aimedProjectiles.values().forEach(aimedProjectile -> aimedProjectile.nextPosition.y--);
        yokais.forEach(yokai -> yokai.nextPosition.y--);
    }

    private static void moveWorldDown() {
        aimedProjectiles.values().forEach(aimedProjectile -> aimedProjectile.nextPosition.y++);
        yokais.forEach(yokai -> yokai.nextPosition.y++);
    }

    private static void onFrame(Frame frame) {
        frame.drawImage(player.position, player.image);
        updateCharacterPosition(frame);
        updateProjectiles(frame);
        if (frame.getCounter() != 0) {
            if (frame.getCounter() % 60 == 0) {
                moveYokais();
            }
            if (frame.getCounter() % 30 == 0) {
                fireWeapons();
                moveProjectiles();
            }
            if (frame.getCounter() % (180 / ((frame.getCounter() / 180) + 1)) == 0) {
                spawnYokai();
            }
        }
    }

    private static void updateProjectiles(Frame frame) {
        List<Yokai> finishedProjectiles = new ArrayList<>();
        aimedProjectiles.values().forEach(aimedProjectile -> {
                    frame.erase(aimedProjectile.position);
                    if (aimedProjectile.nextPosition.equals(aimedProjectile.target.position)) {
                        Speaker.play("Nyanpire/Death");
                        frame.erase(aimedProjectile.nextPosition);
                        yokais.remove(aimedProjectile.target);
                        finishedProjectiles.add(aimedProjectile.target);
                    } else {
                        frame.drawImage(aimedProjectile.nextPosition, aimedProjectile.image);
                        aimedProjectile.position.x = aimedProjectile.nextPosition.x;
                        aimedProjectile.position.y = aimedProjectile.nextPosition.y;
                    }
                }
        );

        finishedProjectiles.forEach(aimedProjectiles::remove);
    }

    private static void moveProjectiles() {
        aimedProjectiles.values().forEach(AimedProjectile::moveTowardsTarget);
    }

    private static void fireWeapons() {
        Speaker.play("Nyanpire/Flame");
        yokais.forEach(yokai -> {
            if (!aimedProjectiles.containsKey(yokai)) {
                aimedProjectiles.put(yokai, new AimedProjectile(player.position, "Nyanpire/Flame", yokai));
            }
        });
    }

    private static void moveYokais() {
        yokais.forEach(Yokai::moveTowardsPlayer);
    }

    private static void spawnYokai() {
        if (yokais.size() < MAX_YOKAI) {
            yokais.add(new Yokai(randomOuterPosition(), "Nyanpire/Yokai"));
        }
    }

    private static Position randomOuterPosition() {
        if (Random.coinToss()) {
            if (Random.coinToss()) {
                return new Position(Random.number(0, Game.config.getWidth() - 1), 0);
            } else {
                return new Position(Random.number(0, Game.config.getWidth() - 1), Game.config.getHeight() - 1);
            }
        } else {
            if (Random.coinToss()) {
                return new Position(0, Random.number(0, Game.config.getHeight() - 1));
            } else {
                return new Position(Game.config.getWidth() - 1, Random.number(0, Game.config.getHeight() - 1));
            }
        }
    }

    private static void updateCharacterPosition(Frame frame) {
        var positionsTaken = new HashSet<Position>();
        yokais.forEach(yokai -> positionsTaken.add(yokai.position));

        yokais.forEach(yokai -> {
            if (player.position.equals(yokai.nextPosition)) {
                Speaker.play("Knyacki/GameOver");
            } else {
                if (!positionsTaken.contains(yokai.nextPosition)) {
                    frame.erase(yokai.position);
                    frame.drawImage(yokai.nextPosition, yokai.image);
                    yokai.position.x = yokai.nextPosition.x;
                    yokai.position.y = yokai.nextPosition.y;
                    positionsTaken.add(yokai.position);
                } else {
                    yokai.nextPosition.x = yokai.position.x;
                    yokai.nextPosition.y = yokai.position.y;
                }
            }
        });
    }

    private static Position getMidPosition() {
        return new Position(Game.config.getWidth() / 2, Game.config.getHeight() / 2);
    }
}
