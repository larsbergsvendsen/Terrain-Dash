package com.terraindash.utils;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import javax.imageio.ImageIO;

/**
 * Standalone utility to generate the app launcher icon as a PNG file.
 * Uses AWT (not LibGDX) so it can run without a GL context.
 */
public class IconGenerator {

    public static void main(String[] args) throws Exception {
        int[] sizes = {48, 72, 96, 144, 192};
        String[] folders = {"mipmap-mdpi", "mipmap-hdpi", "mipmap-xhdpi", "mipmap-xxhdpi", "mipmap-xxxhdpi"};

        for (int i = 0; i < sizes.length; i++) {
            BufferedImage img = generateIcon(sizes[i]);
            File dir = new File("android/src/main/res/" + folders[i]);
            dir.mkdirs();
            ImageIO.write(img, "png", new File(dir, "ic_launcher.png"));
            System.out.println("Generated " + sizes[i] + "x" + sizes[i] + " icon in " + folders[i]);
        }
    }

    public static BufferedImage generateIcon(int size) {
        BufferedImage img = new BufferedImage(size, size, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g = img.createGraphics();
        g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        // Background: rounded rectangle with gradient
        GradientPaint bgGrad = new GradientPaint(0, 0, new Color(30, 60, 120),
            size, size, new Color(20, 40, 80));
        g.setPaint(bgGrad);
        g.fillRoundRect(0, 0, size, size, size / 6, size / 6);

        // Terrain line (green hills)
        int[] terrainX = new int[size + 2];
        int[] terrainY = new int[size + 2];
        int pts = size;
        for (int x = 0; x < pts; x++) {
            terrainX[x] = x;
            double h = Math.sin(x * 0.08) * size * 0.12
                + Math.sin(x * 0.15 + 1) * size * 0.06;
            terrainY[x] = (int) (size * 0.65 + h);
        }
        terrainX[pts] = size;
        terrainY[pts] = size;
        terrainX[pts + 1] = 0;
        terrainY[pts + 1] = size;

        GradientPaint terrainGrad = new GradientPaint(0, (int)(size * 0.5), new Color(70, 160, 50),
            0, size, new Color(40, 100, 30));
        g.setPaint(terrainGrad);
        g.fillPolygon(terrainX, terrainY, pts + 2);

        // Terrain edge
        g.setColor(new Color(90, 190, 60));
        g.setStroke(new BasicStroke(Math.max(1, size / 24f)));
        for (int x = 0; x < pts - 1; x++) {
            g.drawLine(terrainX[x], terrainY[x], terrainX[x + 1], terrainY[x + 1]);
        }

        // Vehicle body
        int vx = (int)(size * 0.35);
        int vy = (int)(size * 0.52);
        int vw = (int)(size * 0.35);
        int vh = (int)(size * 0.12);

        g.setColor(new Color(50, 140, 220));
        g.fillRoundRect(vx, vy, vw, vh, size / 16, size / 16);
        g.setColor(new Color(40, 120, 190));
        g.fillRoundRect(vx + vw / 6, vy - vh / 2, vw / 2, vh / 2 + 2, size / 20, size / 20);

        // Wheels
        int wheelR = (int)(size * 0.06);
        g.setColor(new Color(50, 50, 55));
        g.fillOval(vx + vw / 6 - wheelR, vy + vh - wheelR, wheelR * 2, wheelR * 2);
        g.fillOval(vx + vw * 5 / 6 - wheelR, vy + vh - wheelR, wheelR * 2, wheelR * 2);
        g.setColor(new Color(140, 140, 150));
        g.fillOval(vx + vw / 6 - wheelR / 2, vy + vh - wheelR / 2, wheelR, wheelR);
        g.fillOval(vx + vw * 5 / 6 - wheelR / 2, vy + vh - wheelR / 2, wheelR, wheelR);

        // Speed lines
        g.setColor(new Color(255, 255, 255, 80));
        g.setStroke(new BasicStroke(Math.max(1, size / 48f)));
        for (int i = 0; i < 3; i++) {
            int ly = vy + vh / 4 + i * vh / 3;
            int lx = vx - size / 8 - i * size / 16;
            g.drawLine(lx, ly, lx - size / 6, ly);
        }

        // "TD" text
        g.setColor(Color.WHITE);
        g.setFont(new Font("SansSerif", Font.BOLD, size / 4));
        FontMetrics fm = g.getFontMetrics();
        String text = "TD";
        int tx = (size - fm.stringWidth(text)) / 2;
        int ty = (int)(size * 0.3);
        g.drawString(text, tx, ty);

        g.dispose();
        return img;
    }
}
