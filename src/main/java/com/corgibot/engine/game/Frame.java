package com.corgibot.engine.game;

import com.corgibot.engine.control.Keyboard;
import com.corgibot.engine.control.Mouse;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayDeque;
import java.util.HashMap;
import java.util.Map;
import java.util.Queue;
import java.util.function.Consumer;

public class Frame {
    static final int HEADER_HEIGHT = 20;
    private static final Color HEADER_COLOR = new Color(230, 159, 20);
    //TODO move out everything static
    private static final Queue<Consumer<Graphics>> actions = new ArrayDeque<>();
    private static final Map<String, Image> images = new HashMap<>();
    public static final JFrame frame = new JFrame("CorgiBot");
    private final Image canvasContent;
    private final Graphics graphics;
    private final int blockSize;
    private final int width;
    private final int height;
    private final int marginLeft;
    private int counter;
    private String text = "";
    private boolean initialized = false;

    public Frame(int blockSize, int width, int height) {
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();

        this.blockSize = blockSize;
        this.width = width;
        this.height = height;
        this.marginLeft = (int) (screenSize.getWidth() - width * blockSize) / 2;
        this.counter = 0;

        this.canvasContent = new BufferedImage(screenSize.width, screenSize.height, BufferedImage.TYPE_INT_RGB);
        this.graphics = this.canvasContent.getGraphics();

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
                if (images.containsKey(imageName)) {
                    image = images.get(imageName);
                } else {
                    String path = System.getProperty("user.dir") + "/assets/graphics/" + imageName + ".png";
                    image = ImageIO.read(new URL("file://" + path));

                    images.put(imageName, image);
                }
                g.drawImage(image, pixelPosition.x, pixelPosition.y, null);
            } catch (IOException e) {
                // TODO draw placeholder
                e.printStackTrace();
            }
        });
    }

    public void drawHead(String text) {
        this.text = text;
    }

    private Position getPixelPosition(Position blockPosition) {
        return new Position(blockPosition.x * blockSize + marginLeft, blockPosition.y * blockSize + HEADER_HEIGHT);
    }

    void draw() {
        draw(frame.getGraphics());
        counter++;
    }

    private void draw(Graphics g) {
        graphics.setColor(HEADER_COLOR);
        graphics.fillRect(marginLeft, 0, blockSize * width, HEADER_HEIGHT);
        graphics.setColor(Color.black);
        graphics.drawString(text, marginLeft, (int) (HEADER_HEIGHT / 1.5));

        while (actions.size() > 0) {
            Consumer<Graphics> action = actions.remove();
            action.accept(graphics);
        }

        g.drawImage(canvasContent, 0, 0, null);
    }

    public int getCounter() {
        return counter;
    }

    private void initialize() {
        if (!initialized) {
            GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
            GraphicsDevice device;
            device = ge.getDefaultScreenDevice();

            if (device.isFullScreenSupported()) {
                device.setFullScreenWindow(frame);
            }

            initialized = true;
            frame.addKeyListener(new Keyboard.Listener());
            frame.addMouseListener(new Mouse.Listener());
            frame.setMinimumSize(Toolkit.getDefaultToolkit().getScreenSize());
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.setVisible(true);
            frame.pack();
        }
    }
}
