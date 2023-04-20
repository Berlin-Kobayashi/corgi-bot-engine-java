package com.corgibot.example;

import com.corgibot.engine.control.Keyboard;
import com.corgibot.engine.control.Mouse;
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

    private static int fieldWith = 20;

    private static Color gridColor = Color.black;
    private static Color highlightGridColor = Color.red;
    private static Color filledGridColor = Color.blue;
    private static Color backgroundColor = Color.white;

    private static Position highlightedField = new Position(7, 7);

    public static void main(String[] args) {
        grid = new char[15][15];

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

        Mouse.onClick(Scrabble::onClick);

        Keyboard.onAnyKey(Scrabble::onAnyKey);

        game.onFrame(Scrabble::onFrame);
        game.start();
    }

    private static void onFrame(Game game) {
        for (int i = 0; i < grid.length; i++) {
            for (int j = 0; j < grid[i].length; j++) {
                char character = grid[i][j];
                drawField(game, character, new Position(i * fieldWith, j * fieldWith), gridColor);
            }

            drawField(game, grid[highlightedField.x][highlightedField.y], new Position(highlightedField.x * fieldWith, highlightedField.y * fieldWith), highlightGridColor);
        }
    }

    private static void drawField(Game game, char character, Position position, Color color) {
        for (int i = 0; i < fieldWith + 1; i++) {
            for (int j = 0; j < fieldWith + 1; j++) {
                Position pixelPos = new Position(position.x + i, position.y + j);

                if (i == fieldWith || j == fieldWith || i == 0 || j == 0) {
                    game.raster.erase(pixelPos);
                    game.raster.drawBlock(pixelPos, color);
                } else {
                    game.raster.erase(pixelPos);
                }
            }
        }

        game.raster.drawBlock(position, gridColor, character, fieldWith);
    }

    private static void onClick(Position position) {
        highlightedField = new Position(position.x / fieldWith, (position.y - fieldWith / 2) / fieldWith);
//        grid[position.x / fieldWith][(position.y - fieldWith / 2) / fieldWith] = 'S';
    }

    private static void onAnyKey(KeyEvent keyEvent) {
        grid[highlightedField.x][highlightedField.y] = Character.toUpperCase(keyEvent.getKeyChar());
    }
}
