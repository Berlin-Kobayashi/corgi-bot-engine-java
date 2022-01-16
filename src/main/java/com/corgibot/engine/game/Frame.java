package com.corgibot.engine.game;

import com.corgibot.engine.control.Keyboard;
import com.corgibot.engine.control.Mouse;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.net.URL;
import java.util.ArrayDeque;
import java.util.HashMap;
import java.util.Map;
import java.util.Queue;
import java.util.function.Consumer;

public class Frame {
    private static final int MARGIN_TOP = 20;
    private static final Color HEADER_COLOR = new Color(230, 159, 20);
    //TODO move out everything static
    private static final Queue<Consumer<Graphics>> actions = new ArrayDeque<>();
    private static final Map<String, Image> graphics = new HashMap<>();
    Canvas canvas = null;
    public static final JFrame frame = new JFrame(Game.config.getName());

    private final int blockSize;
    private final int size;
    private int counter;
    private String text = "";

    public Frame(int blockSize, int size) {
        this.blockSize = blockSize;
        this.size = size;
        this.counter = 0;

        initialize();
    }

    public void erase(Position position) {
        Position pixelPosition = getPixelPosition(position);
        actions.add(g -> g.clearRect(pixelPosition.x, pixelPosition.y, blockSize, blockSize));
    }

    public void drawBlock(Position position, Color color) {
        Position pixelPosition = getPixelPosition(position);

        actions.add(g -> {
            g.setColor(color);
            g.fillRect(pixelPosition.x, pixelPosition.y, blockSize, blockSize);
        });
    }

    public void drawImage(Position position, String imageName) {
        Position pixelPosition = getPixelPosition(position);

        actions.add(g -> {
            try {
                Image image;
                if (graphics.containsKey(imageName)) {
                    image = graphics.get(imageName);
                } else {
                    String path = System.getProperty("user.dir") + "/assets/graphics/" + imageName + ".png";
                    image = ImageIO.read(new URL("file://" + path));

                    graphics.put(imageName, image);
                }
                g.drawImage(image, pixelPosition.x, pixelPosition.y, null);
            } catch (IOException e) {
                // TODO draw placeholder
                e.printStackTrace();
            }
        });
    }

    public void drawText(String text) {
        // TODO draw text as positioned image too
        this.text = text;
    }

    private Position getPixelPosition(Position blockPosition) {
        return new Position(blockPosition.x * blockSize, blockPosition.y * blockSize + MARGIN_TOP);
    }

    void draw() {
        try {
            SwingUtilities.invokeAndWait(() -> canvas.paint(canvas.getGraphics()));
        } catch (InterruptedException | InvocationTargetException e) {
            e.printStackTrace();
        }
        counter++;
    }

    public int getCounter() {
        return counter;
    }

    private void initialize() {
        if (canvas == null) {
            Canvas canvas = new Canvas();
            frame.addKeyListener(new Keyboard.Listener());
            canvas.addMouseListener(new Mouse.Listener());
            frame.setMinimumSize(new Dimension(blockSize * size, blockSize * size + 48));
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.getContentPane().add(canvas);
            frame.setVisible(true);
            frame.pack();
            this.canvas = canvas;
        }
    }

    private class Canvas extends JPanel {
        @Override
        protected void paintComponent(Graphics g) {
            // TODO buffer next frame
            g.setColor(HEADER_COLOR);
            g.fillRect(0, 0, blockSize * size, MARGIN_TOP);
            g.setColor(Color.black);
            g.drawString(text, 0, (int) (MARGIN_TOP / 1.5));

            while (actions.size() > 0) {
                Consumer<Graphics> action = actions.remove();
                action.accept(g);
            }
        }
    }
}
