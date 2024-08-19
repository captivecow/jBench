package io.github.captivecow;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferStrategy;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;
import java.util.Objects;

public class JBenchView implements Runnable {

    private JFrame frame;
    private Canvas canvas;
    private BufferStrategy bufferStrategy;
    private GridBagLayout layout;
    private GridBagConstraints constraints;
    private BottomPanelView bottomPanel;
    private BufferedImage sprite;

    public JBenchView(){
        frame = new JFrame("jBench");
        canvas = new Canvas();
        layout = new GridBagLayout();
        constraints = new GridBagConstraints();
        bottomPanel = new BottomPanelView();
    }

    public void createAndShowGui(){
        canvas.setPreferredSize(new Dimension(800, 600));
        canvas.setIgnoreRepaint(true);
        frame.setLayout(layout);

        constraints.gridx = 0;
        constraints.gridy = 0;
        frame.add(canvas, constraints);

        bottomPanel.initBottomPanelView();
        bottomPanel.getUpdateRenderButton().addActionListener(e -> updateRenderState());
        constraints.gridx = 0;
        constraints.gridy = 1;
        constraints.fill = GridBagConstraints.BOTH;
        frame.add(bottomPanel.getBottomPanel(), constraints);

        frame.setIgnoreRepaint(true);
        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        frame.setResizable(false);

        frame.pack();
        frame.setVisible(true);

        // Draw components that are not being drawn initially because of ignoring the repaint
        bottomPanel.getBottomPanel().repaint();

        canvas.createBufferStrategy(2);
        bufferStrategy = canvas.getBufferStrategy();
    }

    @Override
    public void run() {
        createAndShowGui();
    }

    public void updateRenderState(){
        RenderState newRenderState = getRenderState();
        System.out.println(newRenderState.toString());
    }

    public RenderState getRenderState(){
        DisplayModeOption displayModeOption = bottomPanel.getCurrentDisplayOption();
        RenderSize renderSize = bottomPanel.getCurrentRenderSize();
        int imageAmount = bottomPanel.getImageAmount();
        return new RenderState(displayModeOption, renderSize, imageAmount);
    }

    public void loadImage() {
        GraphicsDevice graphicsDevice = GraphicsEnvironment.getLocalGraphicsEnvironment().getDefaultScreenDevice();
        GraphicsConfiguration graphicsConfiguration = graphicsDevice.getDefaultConfiguration();
        InputStream rawImageFile = Objects.requireNonNull(JBench.class.getResourceAsStream("/sprite.png"));

        BufferedImage rawImage;
        try {
            rawImage = ImageIO.read(rawImageFile);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        sprite = graphicsConfiguration.createCompatibleImage(
                rawImage.getWidth(),
                rawImage.getHeight(), Transparency.BITMASK
        );

        Graphics2D graphics2D = (Graphics2D) sprite.getGraphics();
        graphics2D.drawImage(rawImage, 0, 0, null);
        graphics2D.dispose();
    }
}
