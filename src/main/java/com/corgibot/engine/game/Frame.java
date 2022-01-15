package com.corgibot.engine.game;

import com.corgibot.engine.control.Keyboard;
import com.corgibot.engine.control.Mouse;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

public class Frame {
    //TODO move out everything static
    private static final Map<Color, BufferedImage> pixels = new HashMap<>();
    private static final Map<String, BufferedImage> graphics = new HashMap<>();
    Canvas canvas = null;
    public static final JFrame frame = new JFrame(getMainClassName());

    private final Block[][] blocks;
    private final int blockSize;
    private final int size;
    private int counter;
    private String text = "";

    public Frame(int blockSize, int size) {
        blocks = new Block[size][size];
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

    public void drawBlock(int x, int y, Color color) {
        Block block = new Block(color);
        blocks[x][y] = block;
    }

    public void drawBlock(int x, int y, String imageName) {
        try {
            blocks[x][y] = new Block(imageName);
        } catch (IOException e) {
            // TODO draw placeholder
            e.printStackTrace();
        }
    }

    public void drawText(String text) {
        this.text = text;
    }

    public void draw() {
        try {
            SwingUtilities.invokeAndWait(() -> frame.getContentPane().repaint());
        } catch (InterruptedException | InvocationTargetException e) {
            e.printStackTrace();
        }
        counter++;
    }

    public int getCounter() {
        return counter;
    }

    public int getSize() {
        return size;
    }

    public int getBlockSize() {
        return blockSize;
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

    private class Block {
        public BufferedImage image;

        public Block(String imageName) throws IOException {
            if (graphics.containsKey(imageName)) {
                this.image = graphics.get(imageName);
            } else {
                String path = System.getProperty("user.dir") + "/assets/graphics/" + imageName + ".png";
                BufferedImage image = ImageIO.read(new URL("file://" + path));

                this.image = image;
                graphics.put(imageName, image);
            }
        }

        public Block(Color colour) {
            if (pixels.containsKey(colour)) {
                this.image = pixels.get(colour);
            } else {
                this.image = new BufferedImage(blockSize, blockSize, BufferedImage.TYPE_INT_RGB);

                var graphics = image.getGraphics();
                graphics.setColor(colour);
                graphics.fillRect(0, 0, blockSize, blockSize);

                pixels.put(colour, this.image);
            }
        }
    }

    private class Canvas extends JPanel {
        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);

            g.setColor(Color.black);
            g.drawString(text, size * blockSize, 30);

            for (int i = 0; i < size; i++) {
                Block[] line = blocks[i];
                for (int j = 0; j < size; j++) {
                    Block block = line[j];
                    if (block != null) {
                        g.drawImage(block.image, i * blockSize, j * blockSize, null);
                    }
                }
            }
        }
    }
}
