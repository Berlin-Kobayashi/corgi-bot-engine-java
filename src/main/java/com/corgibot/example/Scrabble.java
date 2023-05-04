package com.corgibot.example;

import com.corgibot.engine.control.Keyboard;
import com.corgibot.engine.game.Game;
import com.corgibot.engine.game.GameConfig;
import com.corgibot.engine.game.Position;
import com.corgibot.example.scrabble.Board;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.util.*;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

import static java.awt.Color.*;

// TODO Display Score
// TODO Add letter swap
// TODO restrict letter placement to only valid fields

public class Scrabble {
    record Placement(char letter, int x, int y) {
        public int getScoreWithBonus() {
            int letterMultiplier = 1;

            switch (Board.bonusFields[x][y]) {
                case LETTER_DOUBLE_BONUS -> letterMultiplier = 2;
                case LETTER_TRIPLE_BONUS -> letterMultiplier = 3;
            }

            return getScoreWithoutBonus() * letterMultiplier;
        }

        public int getScoreWithoutBonus() {
            return letterScores.get(letter);
        }
    }

    private static List<Character> bench;

    private static final List<Placement> turn = new LinkedList<>();

    private static final List<Placement> placements = new LinkedList<>();

    private static final int FIELD_SIZE = 20;

    private static final int BOARD_SIZE = 15;
    private static final int BENCH_SIZE = 7;
    private static final int BINGO_BONUS = 50;

    private static final Position SCORE_POSITION = new Position(BOARD_SIZE * FIELD_SIZE + 2 * FIELD_SIZE, 0);

    private static int score = 0;

    private static final Color GRID_COLOR = black;
    private static final Color HIGHLIGHT_GRID_COLOR = red;
    private static final Color FILLED_GRID_COLOR = blue;
    private static final Color BACKGROUND_COLOR = white;

    private static final Color LETTER_DOUBLE_BONUS_COLOR = cyan;
    private static final Color LETTER_TRIPLE_BONUS_COLOR = blue;


    private static final Color WORD_DOUBLE_BONUS_COLOR = yellow;
    private static final Color WORD_TRIPLE_BONUS_COLOR = red;

    private static final Position highlightedField = new Position(7, 7);

    private static final Map<Character, Integer> letterScores = Map.ofEntries(
            Map.entry('A', 1),
            Map.entry('B', 3),
            Map.entry('C', 4),
            Map.entry('D', 1),
            Map.entry('E', 1),
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
        initializeBag();

        bench = new LinkedList<>();
        resetBench();

        Game game = new Game(new GameConfig(2, 1, BACKGROUND_COLOR));

        Keyboard.onKey(KeyEvent.VK_UP, Scrabble::up);
        Keyboard.onKey(KeyEvent.VK_DOWN, Scrabble::down);
        Keyboard.onKey(KeyEvent.VK_LEFT, Scrabble::left);
        Keyboard.onKey(KeyEvent.VK_RIGHT, Scrabble::right);
        Keyboard.onKey(KeyEvent.VK_BACK_SPACE, Scrabble::delete);
        Keyboard.onKey(KeyEvent.VK_ENTER, Scrabble::endTurn);
        Keyboard.onAnyKey(Scrabble::onAnyKey);

        game.onFrame(Scrabble::onFrame);

        drawBoard(game);

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
        drawBoard(game);
        drawTurn(game);
        drawPlacements(game);
        drawBench(game);
        drawScore(game);

        drawEmptyField(game, new Position(highlightedField.x * FIELD_SIZE, highlightedField.y * FIELD_SIZE), HIGHLIGHT_GRID_COLOR, BACKGROUND_COLOR);
    }

    private static void drawBoard(Game game) {
        for (int i = 0; i < BOARD_SIZE; i++) {
            for (int j = 0; j < BOARD_SIZE; j++) {
                Color fieldBackgroundColor = BACKGROUND_COLOR;
                switch (Board.bonusFields[i][j]) {
                    case LETTER_DOUBLE_BONUS -> fieldBackgroundColor = LETTER_DOUBLE_BONUS_COLOR;
                    case LETTER_TRIPLE_BONUS -> fieldBackgroundColor = LETTER_TRIPLE_BONUS_COLOR;
                    case WORD_DOUBLE_BONUS -> fieldBackgroundColor = WORD_DOUBLE_BONUS_COLOR;
                    case WORD_TRIPLE_BONUS -> fieldBackgroundColor = WORD_TRIPLE_BONUS_COLOR;
                }

                drawField(game, (char) 0, new Position(i * FIELD_SIZE, j * FIELD_SIZE), GRID_COLOR, fieldBackgroundColor);
            }
        }
    }

    private static void drawTurn(Game game) {
        for (Placement placement : turn) {
            drawField(game, placement.letter, new Position(placement.x * FIELD_SIZE, placement.y * FIELD_SIZE), FILLED_GRID_COLOR, BACKGROUND_COLOR);
        }
    }

    private static void drawPlacements(Game game) {
        for (Placement placement : placements) {
            drawField(game, placement.letter, new Position(placement.x * FIELD_SIZE, placement.y * FIELD_SIZE), GRID_COLOR, BACKGROUND_COLOR);
        }
    }

    private static void drawBench(Game game) {
        for (int i = 0; i < BENCH_SIZE; i++) {
            if (bench.size() > i) {
                drawField(game, bench.get(i), new Position((i + 4) * FIELD_SIZE, (BOARD_SIZE + 2) * FIELD_SIZE), GRID_COLOR, BACKGROUND_COLOR);
            } else {
                drawField(game, (char) 0, new Position((i + 4) * FIELD_SIZE, (BOARD_SIZE + 2) * FIELD_SIZE), GRID_COLOR, BACKGROUND_COLOR);
            }
        }
    }

    private static void drawScore(Game game) {
        int i = 0;
        for (char digit : String.valueOf(score).toCharArray()) {
            drawField(game, digit, new Position(SCORE_POSITION.x + i * FIELD_SIZE, SCORE_POSITION.y), GRID_COLOR, BACKGROUND_COLOR);

            i++;
        }
    }

    private static void drawField(Game game, char character, Position position, Color color, Color backgroundColor) {
        drawEmptyField(game, position, color, backgroundColor);

        if (character != 0) {
            game.raster.drawBlock(position, GRID_COLOR, String.valueOf(character), FIELD_SIZE);
        }

        if (letterScores.containsKey(character)) {
            game.raster.drawBlock(position, GRID_COLOR, letterScores.get(character).toString(), 5);
        }
    }

    private static void drawEmptyField(Game game, Position position, Color color, Color backgroundColor) {
        for (int i = 0; i < FIELD_SIZE + 1; i++) {
            for (int j = 0; j < FIELD_SIZE + 1; j++) {
                Position pixelPos = new Position(position.x + i, position.y + j);

                game.raster.erase(pixelPos);
                if (i == FIELD_SIZE || j == FIELD_SIZE || i == 0 || j == 0) {
                    game.raster.drawBlock(pixelPos, color);
                } else {
                    game.raster.drawBlock(pixelPos, backgroundColor);
                }
            }
        }
    }

    private static void up() {
        if (highlightedField.y > 0) {
            highlightedField.y -= 1;
        }
    }

    private static void down() {
        if (highlightedField.y < BOARD_SIZE - 1) {
            highlightedField.y += 1;
        }
    }

    private static void left() {
        if (highlightedField.x > 0) {
            highlightedField.x -= 1;
        }
    }

    private static void right() {
        if (highlightedField.x < BOARD_SIZE - 1) {
            highlightedField.x += 1;
        }
    }

    private static void delete() {
        var currentField = turn.stream().filter(placement -> placement.x == highlightedField.x && placement.y == highlightedField.y).findFirst();
        if (currentField.isPresent()) {
            bench.add(currentField.get().letter);
            turn.remove(currentField.get());
        }
    }

    private static void onAnyKey(KeyEvent keyEvent) {
        char pressed = Character.toUpperCase(keyEvent.getKeyChar());

        if (bench.contains(pressed)) {
            if (placements.stream().noneMatch(placement -> placement.x == highlightedField.x && placement.y == highlightedField.y)) {
                bench.remove(bench.indexOf(pressed));
                var currentField = turn.stream().filter(placement -> placement.x == highlightedField.x && placement.y == highlightedField.y).findFirst();
                if (currentField.isPresent()) {
                    bench.add(currentField.get().letter);
                    turn.remove(currentField.get());
                }
                turn.add(new Placement(pressed, highlightedField.x, highlightedField.y));
            }
        }
    }

    private static void endTurn() {
        scoreTurn();

        placements.addAll(turn);

        turn.clear();

        for (int i = bench.size(); i < BENCH_SIZE; i++) {
            if (bag.size() > 0) {
                bench.add(pullLetterFromBag());
            }
        }
    }

    private static void scoreTurn() {
        int wordMultiplier = 1;
        int wordScore = 0;
        for (Placement turnLetter : turn) {
            wordScore += turnLetter.getScoreWithBonus();

            switch (Board.bonusFields[turnLetter.x][turnLetter.y]) {
                case WORD_DOUBLE_BONUS -> wordMultiplier *= 2;
                case WORD_TRIPLE_BONUS -> wordMultiplier *= 3;
            }
        }

        score += wordScore * wordMultiplier + getNeighboringWordsScore();

        if (turn.size() == BENCH_SIZE) {
            score += BINGO_BONUS;
        }
    }

    private static int getNeighboringWordsScore() {
        int neighboringWordsScore = 0;

        for (Placement turnLetter : turn) {
            for (Placement placedLetter : placements) {
                if ((placedLetter.x == turnLetter.x + 1 && placedLetter.y == turnLetter.y) || (placedLetter.x == turnLetter.x - 1 && placedLetter.y == turnLetter.y)) {
                    neighboringWordsScore += turnLetter.getScoreWithBonus();
                    neighboringWordsScore += placedLetter.getScoreWithoutBonus();

                    // TODO iterate over column
                } else {
                    if ((placedLetter.y == turnLetter.y + 1 && placedLetter.x == turnLetter.x) || (placedLetter.y == turnLetter.y - 1 && placedLetter.x == turnLetter.x)) {
                        neighboringWordsScore += turnLetter.getScoreWithBonus();
                        neighboringWordsScore += placedLetter.getScoreWithoutBonus();

                        // TODO iterate over row
                    }
                }
            }
        }

        return neighboringWordsScore;
    }
}
