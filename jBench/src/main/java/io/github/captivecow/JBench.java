package io.github.captivecow;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferStrategy;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;
import java.util.Objects;

public class JBench
{
    public static void main( String[] args ) throws IOException {

        GraphicsDevice graphicsDevice = GraphicsEnvironment.getLocalGraphicsEnvironment().getDefaultScreenDevice();
        DisplayMode[] displayModes = graphicsDevice.getDisplayModes();
        GraphicsConfiguration[] graphicsConfigurations = graphicsDevice.getConfigurations();

        System.out.println("=== Graphics device ===");
        System.out.println("is full screen supported: " + graphicsDevice.isFullScreenSupported());
        System.out.println("is display change supported: " + graphicsDevice.isDisplayChangeSupported());

        System.out.println("Display modes:");
        for (DisplayMode displayMode : displayModes) {
            System.out.println("Width: " + displayMode.getWidth() +
                    " Height: " + displayMode.getHeight() +
                    " Bit depth: " + displayMode.getBitDepth() +
                    " Monitor refresh rate: " + displayMode.getRefreshRate());
        }
        System.out.println("Graphics Configurations:");
        for (GraphicsConfiguration graphicsConfiguration : graphicsConfigurations) {
            System.out.println("is accelerated: " + graphicsConfiguration.getImageCapabilities().isAccelerated());
            System.out.println("is volatile: " + graphicsConfiguration.getImageCapabilities().isTrueVolatile());
        }

        InputStream spriteImageFile = Objects.requireNonNull(JBench.class.getResourceAsStream("/sprite.png"));
        BufferedImage spriteImage = ImageIO.read(spriteImageFile);

        GraphicsConfiguration graphicsConfiguration = graphicsDevice.getDefaultConfiguration();
        BufferedImage compatibleSpriteImage = graphicsConfiguration.createCompatibleImage(spriteImage.getWidth(),
                spriteImage.getHeight(), Transparency.BITMASK);

        Graphics2D graphics2D = (Graphics2D) compatibleSpriteImage.getGraphics();
        graphics2D.drawImage(spriteImage, 0, 0, null);
        graphics2D.dispose();

        JFrame frame = new JFrame("jBench");
        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        frame.setIgnoreRepaint(true);
        frame.setResizable(false);

        Canvas canvas = new Canvas();
        canvas.setPreferredSize(new Dimension(800, 600));
        canvas.setIgnoreRepaint(true);

        frame.add(canvas, BorderLayout.CENTER);
        frame.pack();
        frame.setVisible(true);

        canvas.createBufferStrategy(2);
        BufferStrategy bufferStrategy = canvas.getBufferStrategy();

        int amountOfSprites = 13;

        Graphics2D g2d = (Graphics2D) bufferStrategy.getDrawGraphics();
        for(int i = 0; i<amountOfSprites; i++){
            g2d.drawImage(compatibleSpriteImage, i*64, 0, 64, 64, null);
        }
        g2d.dispose();
        bufferStrategy.show();
    }
}
