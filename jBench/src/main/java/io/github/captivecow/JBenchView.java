package io.github.captivecow;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferStrategy;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;
import java.util.Objects;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class JBenchView implements Runnable {

    private final JFrame frame;
    private final Canvas canvas;
    private BufferStrategy bufferStrategy;
    private final GridBagLayout layout;
    private final GridBagConstraints constraints;
    private final BottomPanelView bottomPanel;
    private BufferedImage sprite;
    private final ScheduledExecutorService fpsScheduler =
            Executors.newScheduledThreadPool(1);
    private RenderState state;
    private final Runnable beingRendering;

    public JBenchView(){
        frame = new JFrame("jBench");
        canvas = new Canvas();
        layout = new GridBagLayout();
        constraints = new GridBagConstraints();
        bottomPanel = new BottomPanelView();
        beingRendering = this::render;
    }

    public void createAndShowGui(){

        /*
            Mixing Canvas (heavyweight) with other lightweight components
            https://www.oracle.com/technical-resources/articles/java/mixing-components.html#:~:text=Historically%2C%20in%20the%20Java%20language,you%20might%20need%20to%20know.
         */
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

        fpsScheduler.scheduleAtFixedRate(beingRendering, 0, 1, TimeUnit.SECONDS);
    }

    @Override
    public void run() {
        createAndShowGui();
    }

    public void updateRenderState(){
        state = getRenderState();
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

        /*
            Load image with BITMASK transparency
            https://docs.oracle.com/en/java/javase/17/troubleshoot/java-2d.html#GUID-D5849E59-9EB6-46FA-BA47-EA5C52DFA077
         */
        sprite = graphicsConfiguration.createCompatibleImage(
                rawImage.getWidth(),
                rawImage.getHeight(), Transparency.BITMASK
        );

        Graphics2D graphics2D = (Graphics2D) sprite.getGraphics();
        graphics2D.drawImage(rawImage, 0, 0, null);
        graphics2D.dispose();
    }


    public void render(){
        System.out.println(SwingUtilities.isEventDispatchThread());
        System.out.println("Hello world");
    }
}
