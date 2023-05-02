package com.corgibot.example;

import com.corgibot.engine.control.Keyboard;
import com.corgibot.engine.game.Game;
import com.corgibot.engine.game.GameConfig;
import com.corgibot.engine.game.Position;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ThreadLocalRandom;

// TODO Display Letter Scores
// TODO Add MouseHandler
// TODO Add BonusFields
// TODO Display Score
// TODO Stop when bag empty

// TODO Add Bag
// TODO Add Bank
// TODO Add Multiplayer

public class Scrabble {

    private static char[][] grid;

    private static List<Character> bench;

    private static final int FIELD_SIZE = 20;

    private static final int GRID_SIZE = 15;
    private static final int BENCH_SIZE = 7;

    private static Color gridColor = Color.black;
    private static Color highlightGridColor = Color.red;
    private static Color filledGridColor = Color.blue;
    private static Color backgroundColor = Color.white;

    private static Position highlightedField = new Position(7, 7);

    private static final Map<Character, Integer> letterScores = Map.ofEntries(
            Map.entry('A', 1),
            Map.entry('B', 3),
            Map.entry('C', 4),
            Map.entry('D', 1),
            Map.entry('E', 5),
            Map.entry('F', 4),
            Map.entry('G', 2),
            Map.entry('H', 2),
            Map.entry('I', 1),
            Map.entry('J', 6),
            Map.entry('K', 4),
            Map.entry('L', 2),
            Map.entry('M', 3),
            Map.entry('N', 1),
            Map.entry('O', 2),
            Map.entry('P', 4),
            Map.entry('Q', 10),
            Map.entry('R', 1),
            Map.entry('S', 1),
            Map.entry('T', 1),
            Map.entry('U', 1),
            Map.entry('V', 6),
            Map.entry('W', 3),
            Map.entry('X', 8),
            Map.entry('Y', 10),
            Map.entry('Z', 3),
            Map.entry('Ä', 6),
            Map.entry('Ö', 8),
            Map.entry('Ü', 6),
            Map.entry(' ', 0)
    );

    private static final Map<Character, Integer> letterAmounts = Map.ofEntries(
            Map.entry('A', 5),
            Map.entry('B', 2),
            Map.entry('C', 2),
            Map.entry('D', 4),
            Map.entry('E', 15),
            Map.entry('F', 2),
            Map.entry('G', 3),
            Map.entry('H', 4),
            Map.entry('I', 6),
            Map.entry('J', 1),
            Map.entry('K', 2),
            Map.entry('L', 3),
            Map.entry('M', 4),
            Map.entry('N', 9),
            Map.entry('O', 3),
            Map.entry('P', 1),
            Map.entry('Q', 1),
            Map.entry('R', 6),
            Map.entry('S', 7),
            Map.entry('T', 6),
            Map.entry('U', 6),
            Map.entry('V', 1),
            Map.entry('W', 1),
            Map.entry('X', 1),
            Map.entry('Y', 1),
            Map.entry('Z', 1),
            Map.entry('Ä', 1),
            Map.entry('Ö', 1),
            Map.entry('Ü', 1),
            Map.entry(' ', 2)
    );

    private static final List<Character> bag = new LinkedList<>();

    public static void main(String[] args) {
        grid = new char[GRID_SIZE][GRID_SIZE];

        initializeBag();

        bench = new LinkedList<>();
        resetBench();

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

    private static void resetBench() {
        for (int i = 0; i < BENCH_SIZE; i++) {
            bench.add(pullLetterFromBag());
        }
    }

    private static void initializeBag() {
        for (Map.Entry<Character, Integer> entry : letterAmounts.entrySet()) {
            addLetterToBag(entry.getKey(), entry.getValue());
        }
    }

    private static void addLetterToBag(char letter, int amount) {
        for (int i = 0; i < amount; i++) {
            bag.add(letter);
        }
    }

    private static char pullLetterFromBag() {
        return bag.remove(ThreadLocalRandom.current().nextInt(0, bag.size()));
    }

    private static void onFrame(Game game) {
        drawGrid(game);
        drawBench(game);
    }

    private static void drawGrid(Game game) {
        for (int i = 0; i < grid.length; i++) {
            for (int j = 0; j < grid[i].length; j++) {
                char character = grid[i][j];
                drawField(game, character, new Position(i * FIELD_SIZE, j * FIELD_SIZE), gridColor);
            }

            drawField(game, grid[highlightedField.x][highlightedField.y], new Position(highlightedField.x * FIELD_SIZE, highlightedField.y * FIELD_SIZE), highlightGridColor);
        }
    }

    private static void drawBench(Game game) {
        int i = 0;
        for (char letter : bench) {
            drawField(game, letter, new Position((i + 4) * FIELD_SIZE, (GRID_SIZE + 2) * FIELD_SIZE), gridColor);
            i++;
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

        if (letterScores.containsKey(character)) {
            game.raster.drawBlock(position, gridColor, letterScores.get(character).toString().charAt(0), 5);
        }
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
        char pressed = Character.toUpperCase(keyEvent.getKeyChar());

        if (bench.contains(pressed)) {
            grid[highlightedField.x][highlightedField.y] = Character.toUpperCase(pressed);
            bench.remove(bench.indexOf(pressed));
            bench.add(pullLetterFromBag());
        }
    }
}
