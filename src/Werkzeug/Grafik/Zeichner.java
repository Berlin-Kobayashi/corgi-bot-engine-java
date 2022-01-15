package Werkzeug.Grafik;

import Werkzeug.Steuerung.Maus;
import Werkzeug.Steuerung.Tastatur;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

public class Zeichner {
    public static final int PIXEL_BREITE = 40;
    public static final int PIXEL_SIZE = 16;

    static Canvas canvas = null;
    public static final JFrame frame = new JFrame(getMainClassName());

    private static final Bild[][] blocks = new Bild[PIXEL_BREITE][PIXEL_BREITE];
    private static String text = "";

    private static final Map<Color, BufferedImage> pixels = new HashMap<>();
    private static final Map<String, BufferedImage> graphics = new HashMap<>();

    private static class Bild {
        public BufferedImage bild;

        public Bild(String bildName) throws IOException {
            if (graphics.containsKey(bildName)) {
                this.bild = graphics.get(bildName);
            } else {
                String path = System.getProperty("user.dir") + "/assets/graphics/" + bildName + ".png";
                BufferedImage image = ImageIO.read(new URL("file://" + path));

                this.bild = image;
                graphics.put(bildName, image);
            }
        }

        public Bild(Color farbe) {
            if (pixels.containsKey(farbe)) {
                this.bild = pixels.get(farbe);
            } else {
                this.bild = new BufferedImage(PIXEL_SIZE, PIXEL_SIZE, BufferedImage.TYPE_INT_RGB);

                var graphics = bild.getGraphics();
                graphics.setColor(farbe);
                graphics.fillRect(0, 0, PIXEL_SIZE, PIXEL_SIZE);

                pixels.put(farbe, this.bild);
            }
        }
    }

    private static class Canvas extends JPanel {
        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);

            g.setColor(Farbe.SCHWARZ);
            g.drawString(text, PIXEL_BREITE * PIXEL_SIZE, 30);

            for (int i = 0; i < PIXEL_BREITE; i++) {
                Bild[] line = blocks[i];
                for (int j = 0; j < PIXEL_BREITE; j++) {
                    Bild pixel = line[j];
                    if (pixel != null) {
                        g.drawImage(pixel.bild, i * PIXEL_SIZE, j * PIXEL_SIZE, null);
                    }
                }
            }
        }
    }

    private static String getMainClassName() {
        StackTraceElement trace[] = Thread.currentThread().getStackTrace();
        if (trace.length > 0) {
            return trace[trace.length - 1].getClassName();
        }

        return "Zeichner";
    }

    public static void pixelSkizzieren(int x, int y, Color color) {
        var pixel = new Bild(color);
        blocks[x][y] = pixel;
    }

    public static void bildSkizzieren(int x, int y, String bildName) {
        try {
            blocks[x][y] = new Bild(bildName);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void textSkizzieren(String text) {
        Zeichner.text = text;
    }

    public static void zeichnen() {
        SwingUtilities.invokeLater(Zeichner::draw);
    }

    private static void draw() {
        initialize();

        frame.getContentPane().repaint();
    }

    private static void initialize() {
        if (Zeichner.canvas == null) {
            Canvas canvas = new Canvas();
            frame.addKeyListener(new Tastatur.Listener());
            canvas.addMouseListener(new Maus.Listener());
            frame.setMinimumSize(new Dimension(PIXEL_SIZE * PIXEL_BREITE + 30, PIXEL_SIZE * PIXEL_BREITE + 60));
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.getContentPane().add(canvas);
            frame.setVisible(true);
            frame.pack();
            Zeichner.canvas = canvas;
        }
    }
}
