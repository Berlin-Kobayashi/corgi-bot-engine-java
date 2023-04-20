package com.corgibot.example;

import com.corgibot.engine.control.Keyboard;
import com.corgibot.engine.game.Game;
import com.corgibot.engine.game.GameConfig;
import com.corgibot.engine.game.Position;

import java.awt.*;
import java.awt.event.KeyEvent;

// TODO Add MouseHandler
// TODO Add BonusFields
// TODO Add Scoring

// TODO Add Bag
// TODO Add Bank
// TODO Add Multiplayer

public class Scrabble {

    private static char[][] grid;

    private static final int FIELD_SIZE = 20;

    private static final int GRID_SIZE = 15;

    private static Color gridColor = Color.black;
    private static Color highlightGridColor = Color.red;
    private static Color filledGridColor = Color.blue;
    private static Color backgroundColor = Color.white;

    private static Position highlightedField = new Position(7, 7);

    public static void main(String[] args) {
        grid = new char[GRID_SIZE][GRID_SIZE];

        // TODO Remove Demo data
        grid[7][7] = 'S';
        grid[7][8] = 'C';
        grid[7][9] = 'R';
        grid[7][10] = 'A';
        grid[7][11] = 'B';
        grid[7][12] = 'B';
        grid[7][13] = 'L';
        grid[7][14] = 'E';

        Game game = new Game(new GameConfig(2, 1, backgroundColor));


        Keyboard.onKey(KeyEvent.VK_UP, Scrabble::up);
        Keyboard.onKey(KeyEvent.VK_DOWN, Scrabble::down);
        Keyboard.onKey(KeyEvent.VK_LEFT, Scrabble::left);
        Keyboard.onKey(KeyEvent.VK_RIGHT, Scrabble::right);
        Keyboard.onKey(KeyEvent.VK_BACK_SPACE, Scrabble::delete);
        Keyboard.onAnyKey(Scrabble::onAnyKey);

        game.onFrame(Scrabble::onFrame);
        game.start();
    }

    private static void onFrame(Game game) {
        for (int i = 0; i < grid.length; i++) {
            for (int j = 0; j < grid[i].length; j++) {
                char character = grid[i][j];
                drawField(game, character, new Position(i * FIELD_SIZE, j * FIELD_SIZE), gridColor);
            }

            drawField(game, grid[highlightedField.x][highlightedField.y], new Position(highlightedField.x * FIELD_SIZE, highlightedField.y * FIELD_SIZE), highlightGridColor);
        }
    }

    private static void drawField(Game game, char character, Position position, Color color) {
        for (int i = 0; i < FIELD_SIZE + 1; i++) {
            for (int j = 0; j < FIELD_SIZE + 1; j++) {
                Position pixelPos = new Position(position.x + i, position.y + j);

                if (i == FIELD_SIZE || j == FIELD_SIZE || i == 0 || j == 0) {
                    game.raster.erase(pixelPos);
                    game.raster.drawBlock(pixelPos, color);
                } else {
                    game.raster.erase(pixelPos);
                }
            }
        }

        game.raster.drawBlock(position, gridColor, character, FIELD_SIZE);
    }

    private static void onClick(Position position) {
        highlightedField = new Position(position.x / FIELD_SIZE, (position.y - FIELD_SIZE / 2) / FIELD_SIZE);
//        grid[position.x / fieldWith][(position.y - fieldWith / 2) / fieldWith] = 'S';
    }

    private static void up() {
        if (highlightedField.y > 0) {
            highlightedField.y -= 1;
        }
    }

    private static void down() {
        if (highlightedField.y < GRID_SIZE - 1) {
            highlightedField.y += 1;
        }
    }

    private static void left() {
        if (highlightedField.x > 0) {
            highlightedField.x -= 1;
        }
    }

    private static void right() {
        if (highlightedField.x < GRID_SIZE - 1) {
            highlightedField.x += 1;
        }
    }

    private static void delete() {
        grid[highlightedField.x][highlightedField.y] = 0;

    }

    private static void onAnyKey(KeyEvent keyEvent) {
        if ((keyEvent.getKeyChar() >= 'a' && keyEvent.getKeyChar() <= 'z') || (keyEvent.getKeyChar() >= 'A' && keyEvent.getKeyChar() <= 'Z')) {
            grid[highlightedField.x][highlightedField.y] = Character.toUpperCase(keyEvent.getKeyChar());
        }
    }
}
