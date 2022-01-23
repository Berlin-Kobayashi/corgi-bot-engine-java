package com.corgibot.example;

import com.corgibot.engine.audio.Speaker;
import com.corgibot.engine.control.Keyboard;
import com.corgibot.engine.game.Game;
import com.corgibot.engine.game.GameConfig;
import com.corgibot.engine.game.Position;
import com.corgibot.engine.game.Raster;
import com.corgibot.utils.random.Random;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.util.ArrayDeque;
import java.util.Queue;

public class KnyackiSnake {
    private static Position position;
    private static Direction direction;
    private static Queue<Position> body;
    private static int endCounter;
    private static int length = 1;

    private static boolean isGameOver = true;
    private static Field[][] world;

    private static boolean newGamePressed;

    private enum Field {
        HEAD, BODY, ITEM, WALL
    }

    private enum Direction {
        RIGHT, LEFT, UP, DOWN
    }

    public static void main(String[] args) {
        Game game = new Game(new GameConfig(1, 2, Color.black));

        world = new Field[Game.config.getWidth()][Game.config.getHeight()];

        Speaker.loop("Knyacki/Music");

        Keyboard.onKey(KeyEvent.VK_LEFT, KnyackiSnake::turnLeft);
        Keyboard.onKey(KeyEvent.VK_RIGHT, KnyackiSnake::turnRight);
        Keyboard.onKey(KeyEvent.VK_SPACE, KnyackiSnake::onNewGameKey);

        game.onFrame(KnyackiSnake::newGame);
        game.onFrame(KnyackiSnake::move);
        game.onFrame(KnyackiSnake::placeItem);
        game.onFrame(KnyackiSnake::incrementScore);

        game.start();
    }

    private static void incrementScore(Game game) {
        game.raster.drawHead("Score:" + length);
    }

    private static void onNewGameKey() {
        if (isGameOver) {
            newGamePressed = true;
        }
    }

    private static void turnLeft() {
        switch (direction) {
            case UP:
                direction = Direction.LEFT;
                break;
            case RIGHT:
                direction = Direction.UP;
                break;
            case DOWN:
                direction = Direction.RIGHT;
                break;
            case LEFT:
                direction = Direction.DOWN;
                break;
        }
    }

    private static void turnRight() {
        switch (direction) {
            case UP:
                direction = Direction.RIGHT;
                break;
            case RIGHT:
                direction = Direction.DOWN;
                break;
            case DOWN:
                direction = Direction.LEFT;
                break;
            case LEFT:
                direction = Direction.UP;
                break;
        }
    }

    private static void newGame(Game game) {
        if (!isGameOver || !newGamePressed) {
            return;
        }

        Speaker.play("Knyacki/NewGame");

        resetWorld(game.raster);

        body = new ArrayDeque<>();
        endCounter = 0;
        length = 1;
        direction = Direction.UP;
        position = new Position(Game.config.getWidth() / 2, Game.config.getHeight() / 2);
        isGameOver = false;
        newGamePressed = false;
    }

    private static void move(Game game) {
        if (isGameOver) {
            return;
        }

        int newPosX = position.x;
        int newPosY = position.y;

        switch (direction) {
            case UP:
                newPosY--;
                break;
            case RIGHT:
                newPosX++;
                break;
            case DOWN:
                newPosY++;
                break;
            case LEFT:
                newPosX--;
                break;
        }

        if ((newPosX < 0 || newPosX >= Game.config.getWidth() || newPosY < 0 || newPosY >= Game.config.getHeight()) ||
                world[newPosX][newPosY] == Field.BODY || world[newPosX][newPosY] == Field.WALL) {
            if (game.getFrameCounter() % 10 == 0) {
                endCounter++;
                switch (endCounter) {
                    case 1:
                        Speaker.play("Knyacki/Ichi", 6);
                        break;
                    case 2:
                        Speaker.play("Knyacki/Ni", 6);
                        break;
                    case 3:
                        Speaker.play("Knyacki/San", 6);
                        break;
                    case 4:
                        Speaker.play("Knyacki/GameOver", 6);
                        isGameOver = true;

                }
            }

            return;
        } else {
            endCounter = 0;
        }

        while (body.size() >= length && body.size() > 0) {
            Position tail = body.poll();
            if (world[tail.x][tail.y] == Field.BODY) {
                game.raster.erase(tail);
            }
            world[tail.x][tail.y] = null;
        }

        if (world[newPosX][newPosY] == Field.ITEM) {
            length++;
            Speaker.play("Knyacki/KnyackiChan");
        }

        if ((world[newPosX][newPosY] == null || world[newPosX][newPosY] == Field.ITEM)) {
            game.raster.erase(position);
            game.raster.drawBlock(position, Color.GREEN);
            world[position.x][position.y] = Field.BODY;
            body.add(new Position(position.x, position.y));

            position.x = newPosX;
            position.y = newPosY;
            game.raster.drawBlock(position, Color.YELLOW);
            world[position.x][position.y] = Field.HEAD;
        }
    }

    private static void placeItem(Game game) {
        if (isGameOver) {
            return;
        }

        int x = Random.number(0, Game.config.getWidth() - 1);
        int y = Random.number(0, Game.config.getHeight() - 1);
        if (world[x][y] == null) {
            game.raster.drawBlock(new Position(x, y), Color.WHITE);
            world[x][y] = Field.ITEM;
        }
    }

    private static void resetWorld(Raster raster) {
        for (int columnCounter = 0; columnCounter < Game.config.getWidth(); columnCounter++) {
            for (int rowCounter = 0; rowCounter < Game.config.getHeight(); rowCounter++) {
                raster.erase(new Position(columnCounter, rowCounter));
                world[columnCounter][rowCounter] = null;
            }
        }
    }
}
