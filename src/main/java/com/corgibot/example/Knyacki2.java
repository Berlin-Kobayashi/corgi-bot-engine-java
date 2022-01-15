package com.corgibot.example;

import com.corgibot.engine.audio.Speaker;
import com.corgibot.engine.control.Keyboard;
import com.corgibot.engine.game.Frame;
import com.corgibot.engine.game.Game;
import com.corgibot.engine.game.Position;
import com.corgibot.utils.math.Math;

import java.awt.event.KeyEvent;
import java.util.ArrayDeque;
import java.util.Queue;

public class Knyacki2 {
    private static Position position;
    private static Richtung richtung;
    private static Queue<Position> schwanz;
    private static int endeZähler;
    private static int letzerEndeZählScore;
    private static int score;

    private static boolean istGameOver = true;
    private static final Game game = new Game();
    private static final Feld[][] felder = new Feld[Game.config.getSize()][Game.config.getSize()];

    private static boolean turnLeftPressed;
    private static boolean turnRightPressed;
    private static boolean newGamePressed;

    private enum Feld {
        KOPF, KÖRPER, BAUEN, WAND
    }

    private enum Richtung {
        RECHTS, LINKS, OBEN, UNTEN
    }

    public static void main(String[] args) {
        Keyboard.onKey(KeyEvent.VK_LEFT, Knyacki2::linksDrehen);
        Keyboard.onKey(KeyEvent.VK_RIGHT, Knyacki2::rechtsDrehen);
        Keyboard.onKey(KeyEvent.VK_SPACE, Knyacki2::onNewGameKey);

        game.onFrame(Knyacki2::onFrame);
        game.start();
    }

    private static void onNewGameKey() {
        newGamePressed = true;
    }

    private static void onFrame(Frame frame) {
        if (!istGameOver) {
            bewegen(frame);
            bauen(frame);
            score++;

            if (score % 50 == 0) {
                Speaker.play("Bauen");
                int außenWandDicke = (score / 50) + 1;
                for (int spaltenZähler = 0; spaltenZähler < Game.config.getSize(); spaltenZähler++) {
                    for (int reihenZähler = 0; reihenZähler < Game.config.getSize(); reihenZähler++) {
                        if (spaltenZähler < außenWandDicke || reihenZähler < außenWandDicke || spaltenZähler > Game.config.getSize() - außenWandDicke - 1 || reihenZähler > Game.config.getSize() - außenWandDicke - 1) {
                            if (felder[spaltenZähler][reihenZähler] == Feld.KOPF) {
                                Speaker.play("GameOver", 6);
                                istGameOver = true;
                            }
                            felder[spaltenZähler][reihenZähler] = Feld.WAND;
                            frame.drawImage(new Position(spaltenZähler, reihenZähler), "Wand");
                        }
                    }
                }
            }
        } else {
            if (newGamePressed) {
                neuesSpiel(frame);
            }
        }

        frame.drawText(String.valueOf(score));
    }

    private static void linksDrehen() {
        switch (richtung) {
            case OBEN:
                richtung = Richtung.LINKS;
                break;
            case RECHTS:
                richtung = Richtung.OBEN;
                break;
            case UNTEN:
                richtung = Richtung.RECHTS;
                break;
            case LINKS:
                richtung = Richtung.UNTEN;
                break;
        }
    }

    private static void rechtsDrehen() {
        switch (richtung) {
            case OBEN:
                richtung = Richtung.RECHTS;
                break;
            case RECHTS:
                richtung = Richtung.UNTEN;
                break;
            case UNTEN:
                richtung = Richtung.LINKS;
                break;
            case LINKS:
                richtung = Richtung.OBEN;
                break;
        }
    }

    private static void neuesSpiel(Frame frame) {
            Speaker.play("NewGame");
            for (int spaltenZähler = 0; spaltenZähler < Game.config.getSize(); spaltenZähler++) {
                for (int reihenZähler = 0; reihenZähler < Game.config.getSize(); reihenZähler++) {
                    if (spaltenZähler == 0 || reihenZähler == 0 || spaltenZähler == Game.config.getSize() - 1 || reihenZähler == Game.config.getSize() - 1) {
                        frame.drawImage(new Position(spaltenZähler, reihenZähler), "Wand");
                        felder[spaltenZähler][reihenZähler] = Feld.WAND;
                    } else {
                        frame.drawBlock(new Position(spaltenZähler, reihenZähler), null);
                        felder[spaltenZähler][reihenZähler] = null;
                    }
                }
            }

            schwanz = new ArrayDeque<>();
            endeZähler = 0;
            letzerEndeZählScore = 0;
            richtung = Richtung.OBEN;
            position = new Position(Game.config.getSize() / 2, Game.config.getSize() / 2);
            score = 0;
            istGameOver = false;
            newGamePressed = false;
    }

    private static void bewegen(Frame frame) {
        int neuePosX = position.x;
        int neuePosY = position.y;

        switch (richtung) {
            case OBEN:
                neuePosY--;
                break;
            case RECHTS:
                neuePosX++;
                break;
            case UNTEN:
                neuePosY++;
                break;
            case LINKS:
                neuePosX--;
                break;
        }

        if ((neuePosX < 0 || neuePosX >= Game.config.getSize() || neuePosY < 0 || neuePosY >= Game.config.getSize()) ||
                felder[neuePosX][neuePosY] == Feld.KÖRPER || felder[neuePosX][neuePosY] == Feld.WAND) {
            if (score - letzerEndeZählScore > 10) {
                endeZähler++;
                switch (endeZähler) {
                    case 1:
                        Speaker.play("Ichi", 6);
                        break;
                    case 2:
                        Speaker.play("Ni", 6);
                        break;
                    case 3:
                        Speaker.play("San", 6);
                        break;
                    case 4:
                        Speaker.play("GameOver", 6);
                        istGameOver = true;

                }
                letzerEndeZählScore = score;
            }

            return;
        } else {
            letzerEndeZählScore = 0;
            endeZähler = 0;
        }

        if (felder[neuePosX][neuePosY] == Feld.BAUEN) {
            for (int zähler = 0; zähler < 10 && schwanz.size() > 0; zähler++) {
                Position schwanzSpitze = schwanz.poll();
                if (felder[schwanzSpitze.x][schwanzSpitze.y] == Feld.KÖRPER) {
                    frame.drawBlock(schwanzSpitze, null);
                }
                felder[schwanzSpitze.x][schwanzSpitze.y] = null;
            }

            Speaker.play("KnyackiChan");
        }

        if ((felder[neuePosX][neuePosY] == null || felder[neuePosX][neuePosY] == Feld.BAUEN)) {
            frame.drawImage(position, "Körper");
            felder[position.x][position.y] = Feld.KÖRPER;
            schwanz.add(new Position(position.x, position.y));

            position.x = neuePosX;
            position.y = neuePosY;
            frame.drawImage(position, "Kopf");
            felder[position.x][position.y] = Feld.KOPF;
        }
    }

    private static void bauen(Frame frame) {
        int x = Math.random(0, Game.config.getSize() - 1);
        int y = Math.random(0, Game.config.getSize() - 1);
        if (felder[x][y] == null) {
            frame.drawImage(new Position(x, y), "Item");
            felder[x][y] = Feld.BAUEN;
        }
    }
}
