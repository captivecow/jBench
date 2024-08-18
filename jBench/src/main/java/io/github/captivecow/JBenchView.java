package io.github.captivecow;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferStrategy;

public class JBenchView implements Runnable {

    private JFrame frame;
    private Canvas canvas;
    private BufferStrategy bufferStrategy;
    private GridBagLayout layout;
    private GridBagConstraints constraints;
    private BottomPanelView bottomPanel;
    private JBenchController controller;

    public JBenchView(JBenchController controller){
        frame = new JFrame("jBench");
        canvas = new Canvas();
        layout = new GridBagLayout();
        constraints = new GridBagConstraints();
        bottomPanel = new BottomPanelView();
        this.controller = controller;
    }

    public void createAndShowGui(){
        canvas.setPreferredSize(new Dimension(800, 600));
        canvas.setIgnoreRepaint(true);
        frame.setLayout(layout);

        constraints.gridx = 0;
        constraints.gridy = 0;
        frame.add(canvas, constraints);

        bottomPanel.initBottomPanelView();
        bottomPanel.getUpdateRenderButton().addActionListener(e -> controller.update());
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

    public RenderState getRenderState(){
        DisplayModeOption displayModeOption = bottomPanel.getCurrentDisplayOption();
        RenderSize renderSize = bottomPanel.getCurrentRenderSize();
        int imageAmount = bottomPanel.getImageAmount();
        return new RenderState(displayModeOption, renderSize, imageAmount);
    }

    public void modelNotify(){
        System.out.println("Notify from model");
    }
}
