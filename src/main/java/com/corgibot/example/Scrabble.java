package com.corgibot.example;

import com.corgibot.engine.game.Game;
import com.corgibot.engine.game.GameConfig;
import com.corgibot.engine.game.Position;

import java.awt.*;

public class Scrabble {

    private static char[][] grid;

    private static int fieldWith = 20;

    private static Color gridColor = Color.black;
    private static Color backgroundColor = Color.white;

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
        game.onFrame(Scrabble::onFrame);
        game.start();
    }

    private static void onFrame(Game game) {
        for (int i = 0; i < grid.length; i++) {
            for (int j = 0; j < grid[i].length; j++) {
                char character = grid[i][j];
                drawField(game, character, new Position(i * fieldWith, j * fieldWith));
            }
        }
    }

    private static void drawField(Game game, char character, Position position) {
        for (int i = 0; i < fieldWith; i++) {
            for (int j = 0; j < fieldWith; j++) {
                if (i == fieldWith - 1 || j == fieldWith - 1) {
                    Position pixelPos = new Position(position.x + i, position.y + j);
                    game.raster.drawBlock(pixelPos, gridColor);
                }
            }
        }

        game.raster.drawBlock(position, gridColor, character, fieldWith);
    }
}
