package io.github.captivecow;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferStrategy;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;
import java.text.DecimalFormat;
import java.util.*;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.TimeUnit;

public class JBenchView implements Runnable {

    private Map<RenderingHints.Key, Object> HINTS;
    private DecimalFormat df = new DecimalFormat("#.####");


    private final JFrame frame;
    private final Canvas canvas;
    private BufferStrategy bufferStrategy;
    private final GridBagLayout layout;
    private final GridBagConstraints constraints;
    private final BottomPanelView bottomPanel;
    private final ScheduledExecutorService fpsScheduler =
            Executors.newScheduledThreadPool(1);
    private RenderState state;
    private final Runnable beingRendering;
    private Sprite sprite;
    private ArrayList<Sprite> spriteList;
    private Map<Integer, BufferedImage> imageMap;
    private long lastTime;
    private double accumulation = 0;
    private double lastDelta = 1;

    public JBenchView(){
        frame = new JFrame("jBench");
        canvas = new Canvas();
        layout = new GridBagLayout();
        constraints = new GridBagConstraints();
        bottomPanel = new BottomPanelView();
        spriteList = new ArrayList<>();
        imageMap = new HashMap<>();
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

        lastTime = System.nanoTime();
        fpsScheduler.scheduleAtFixedRate(beingRendering, 0, 16, TimeUnit.MILLISECONDS);
    }

    public void initRenderState(){
        HINTS = new HashMap<>();
        HINTS.put(RenderingHints.KEY_ALPHA_INTERPOLATION, RenderingHints.VALUE_ALPHA_INTERPOLATION_SPEED);
        HINTS.put(RenderingHints.KEY_COLOR_RENDERING, RenderingHints.VALUE_COLOR_RENDER_SPEED);
        HINTS.put(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_SPEED);
        HINTS.put(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);


        state = new RenderState(new DisplayModeOption(800, 600), new RenderSize(16, 16), 10);

        for (int i = 0; i<state.renderAmount(); i++){
            int randomX = ThreadLocalRandom.current().nextInt(0, 800 + 1);
            int randomY = ThreadLocalRandom.current().nextInt(0, 600 + 1);

            Sprite newSprite = new Sprite(
                    state.renderSize().width(),
                    state.renderSize().height(),
                    randomX,
                    randomY);
            spriteList.add(newSprite);
        }
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
        BufferedImage compatibleImage = graphicsConfiguration.createCompatibleImage(
                rawImage.getWidth(),
                rawImage.getHeight(), Transparency.BITMASK
        );

        Graphics2D graphics2D = (Graphics2D) compatibleImage.getGraphics();
        graphics2D.drawImage(rawImage, 0, 0, null);
        graphics2D.dispose();

        imageMap.put(1, compatibleImage);
    }


    public void render(){
        /*
            Correct way to render with buffer strategy
            https://docs.oracle.com/javase%2F8%2Fdocs%2Fapi%2F%2F/java/awt/image/BufferStrategy.html#:~:text=The%20BufferStrategy%20class%20represents%20the,buffer%20strategy%20can%20be%20implemented.
         */
        do {
            do {

                long time = System.nanoTime();
                double currentDelta = (double) (time - lastTime)/1000000000.0;
                lastTime = time;
                accumulation += currentDelta;

                Graphics2D g2d = (Graphics2D) bufferStrategy.getDrawGraphics();
                g2d.clearRect(0, 0, 800, 600);
                g2d.setRenderingHints(HINTS);

                if(accumulation > 1.0){
                    lastDelta = currentDelta;
                    accumulation = 0;
                }

                for (Sprite sprite: spriteList){
                    g2d.drawImage(imageMap.get(sprite.getSpriteId()),
                            (int) sprite.getX(), (int) sprite.getY(),
                            sprite.getWidth(), sprite.getHeight(), null);
                }
                g2d.drawString("FPS: " + df.format(Math.round(1/lastDelta)), 0, 10);

                g2d.dispose();
            } while (bufferStrategy.contentsRestored());
            // Display the buffer
            bufferStrategy.show();
            // Repeat the rendering if the drawing buffer was lost
        } while (bufferStrategy.contentsLost());
    }
}
