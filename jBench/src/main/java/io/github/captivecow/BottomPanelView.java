package io.github.captivecow;

import javax.swing.*;
import java.awt.*;

public class BottomPanelView {
    private final JPanel bottomPanel;
    private final GridBagLayout layout;
    private final GridBagConstraints constraints;
    private final JLabel displayResolutionLabel;
    private final JLabel imageAmountLabel;
    private final JComboBox<DisplayModeOption> displayResolutionOptions;
    private final JComboBox<Integer> imageAmountOptions;
    private final JLabel renderSizeLabel;
    private final JComboBox<RenderSize> renderSizeOptions;
    private final JButton updateRenderButton;

    public BottomPanelView(){
        bottomPanel = new JPanel();
        layout = new GridBagLayout();
        constraints = new GridBagConstraints();
        displayResolutionLabel = new JLabel("Display Modes");
        imageAmountLabel = new JLabel("Render Amount");
        renderSizeLabel = new JLabel("Render Image Size");
        displayResolutionOptions = new JComboBox<>();
        imageAmountOptions = new JComboBox<>();
        renderSizeOptions = new JComboBox<>();
        updateRenderButton = new JButton("Update");
    }

    public void initBottomPanelView(){
        populateDisplayModes();
        populateImageAmounts();
        populateRenderSizeOptions();
        bottomPanel.setLayout(layout);
        constraints.gridx = 0;
        constraints.gridy = 0;
        bottomPanel.add(displayResolutionLabel, constraints);
        constraints.gridx = 1;
        constraints.gridy = 0;
        bottomPanel.add(displayResolutionOptions, constraints);
        constraints.gridx = 2;
        constraints.gridy = 0;
        bottomPanel.add(imageAmountLabel, constraints);
        constraints.gridx = 3;
        constraints.gridy = 0;
        bottomPanel.add(imageAmountOptions, constraints);
        constraints.gridx = 4;
        constraints.gridy = 0;
        bottomPanel.add(renderSizeLabel, constraints);
        constraints.gridx = 5;
        constraints.gridy = 0;
        bottomPanel.add(renderSizeOptions, constraints);
        constraints.gridx = 6;
        constraints.gridy = 0;
        bottomPanel.add(updateRenderButton, constraints);

        bottomPanel.setBackground(new Color(230, 230, 230));
    }

    private void populateDisplayModes(){
        GraphicsDevice graphicsDevice = GraphicsEnvironment.getLocalGraphicsEnvironment().getDefaultScreenDevice();
        DisplayMode[] displayModes = graphicsDevice.getDisplayModes();

        DisplayModeOption defaultDisplayModeOption = new DisplayModeOption(800, 600);
        displayResolutionOptions.addItem(defaultDisplayModeOption);

        for (DisplayMode displayMode : displayModes) {
            DisplayModeOption displayModeOption = new DisplayModeOption(displayMode.getWidth(), displayMode.getHeight());
            displayResolutionOptions.addItem(displayModeOption);
        }
    }

    public void populateImageAmounts(){
        imageAmountOptions.addItem(10);
        imageAmountOptions.addItem(100);
        imageAmountOptions.addItem(1000);
        imageAmountOptions.addItem(5000);
        imageAmountOptions.addItem(10000);
    }

    public void populateRenderSizeOptions(){
        renderSizeOptions.addItem(new RenderSize(16, 16));
        renderSizeOptions.addItem(new RenderSize(32, 32));
        renderSizeOptions.addItem(new RenderSize(64, 64));
        renderSizeOptions.addItem(new RenderSize(16, 32));
        renderSizeOptions.addItem(new RenderSize(32, 64));
        renderSizeOptions.addItem(new RenderSize(-1, -1));
    }

    public JPanel getBottomPanel(){
        return bottomPanel;
    }
}
