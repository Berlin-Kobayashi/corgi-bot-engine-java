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
    //TODO move out everything static
    private static final Queue<Consumer<Graphics>> actions = new ArrayDeque<>();
    private static final Map<String, Image> graphics = new HashMap<>();
    Canvas canvas = null;
    public static final JFrame frame = new JFrame(getMainClassName());

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


    private static String getMainClassName() {
        StackTraceElement trace[] = Thread.currentThread().getStackTrace();
        if (trace.length > 0) {
            return trace[trace.length - 1].getClassName();
        }

        return "CorgiBot";
    }

    public void erase(Position position) {
        Position finalPosition = position.clone();
        actions.add(g -> g.clearRect(finalPosition.x * blockSize, finalPosition.y * blockSize, blockSize, blockSize));
    }

    public void drawBlock(Position position, Color color) {
        Position finalPosition = position.clone();

        actions.add(g -> {
            g.setColor(color);
            g.fillRect(finalPosition.x * blockSize, finalPosition.y * blockSize, blockSize, blockSize);
        });
    }

    public void drawImage(Position position, String imageName) {
        Position finalPosition = position.clone();

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
                g.drawImage(image, finalPosition.x * blockSize, finalPosition.y * blockSize, null);
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
            frame.setMinimumSize(new Dimension(blockSize * size + 30, blockSize * size + 60));
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
            g.setColor(Color.black);
            g.drawString(text, size * blockSize, 30);

            while (actions.size() > 0) {
                Consumer<Graphics> action = actions.remove();
                action.accept(g);
            }
        }
    }
}
