package com.corgibot.engine.game;

import com.corgibot.engine.control.Keyboard;
import com.corgibot.engine.control.Mouse;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.geom.Rectangle2D;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayDeque;
import java.util.HashMap;
import java.util.Map;
import java.util.Queue;
import java.util.function.Consumer;

public class Raster {
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
    private String text = "";
    private int fps = 0;
    private Font headerFont;

    public Raster(int blockSize, int width, int height, Color backgroundColor) {
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();

        this.blockSize = blockSize;
        this.width = width;
        this.height = height;
        this.marginLeft = (int) (screenSize.getWidth() - width * blockSize) / 2;

        enableFullScreen();

        initializeFrame();
        frame.setBackground(backgroundColor);
        this.canvasContent = frame.createVolatileImage(screenSize.width, screenSize.height);
        this.graphics = this.canvasContent.getGraphics();
        this.headerFont = graphics.getFont();
    }

    private void enableFullScreen() {
        GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
        GraphicsDevice device;
        device = ge.getDefaultScreenDevice();

        if (device.isFullScreenSupported()) {
            device.setFullScreenWindow(frame);
        }
    }

    private void initializeFrame() {
        frame.addKeyListener(new Keyboard.Listener());
        frame.addMouseListener(new Mouse.Listener());
        frame.setMinimumSize(Toolkit.getDefaultToolkit().getScreenSize());
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);
        frame.pack();
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

    public void drawBlock(Position position, Color color, String text, int size) {
        if (!text.isEmpty()) {

            Position pixelPosition = getPixelPosition(position);
            int pixelSize = size * blockSize;

            actions.add(g -> {
                g.setColor(color);
                g.setFont(new Font("Arial", Font.BOLD, pixelSize));
                FontMetrics fm = g.getFontMetrics();
                Rectangle2D rect = fm.getStringBounds(text, g);
                g.drawString(String.valueOf(text), (int) (pixelPosition.x + pixelSize / 2 - rect.getWidth() / 2),
                        (int) (pixelPosition.y + pixelSize / 2 + rect.getHeight() / 2) - size / 2 + 2 * blockSize);
            });
        }
    }

    public void drawBlock(Position position, String imageName) {
        Position pixelPosition = getPixelPosition(position);

        actions.add(g -> {
            try {
                Image image;
                if (images.containsKey(imageName)) {
                    image = images.get(imageName);
                } else {
                    String path = System.getProperty("user.dir") + "/assets/graphics/" + imageName + ".png";
                    image = ImageIO.read(new URL("file://" + path));
                    image = image.getScaledInstance(blockSize, blockSize, Image.SCALE_DEFAULT);
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

    void drawFPS(int fps) {
        this.fps = fps;
    }

    private Position getPixelPosition(Position blockPosition) {
        return new Position(blockPosition.x * blockSize + marginLeft, blockPosition.y * blockSize + HEADER_HEIGHT);
    }

    public void draw() {
        graphics.setColor(HEADER_COLOR);
        graphics.fillRect(marginLeft, 0, blockSize * width, HEADER_HEIGHT);
        graphics.setColor(Color.black);
        graphics.setFont(headerFont);
        graphics.drawString(text, marginLeft, (int) (HEADER_HEIGHT / 1.5));
        graphics.drawString("FPS: " + fps, blockSize * width - 50, (int) (HEADER_HEIGHT / 1.5));

        while (actions.size() > 0) {
            Consumer<Graphics> action = actions.remove();
            action.accept(graphics);
        }

        frame.getGraphics().drawImage(canvasContent, 0, 0, null);
    }
}
