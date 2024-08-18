package io.github.captivecow;

import javax.swing.*;
import java.awt.*;

public class BottomPanelView {

    JPanel bottomPanel;
    GridBagLayout layout;
    GridBagConstraints constraints;
    JLabel displayResolutionLabel;
    JComboBox<DisplayModeOption> displayResolutionOptions;

    public BottomPanelView(){
        bottomPanel = new JPanel();
        layout = new GridBagLayout();
        constraints = new GridBagConstraints();
        displayResolutionLabel = new JLabel("Display Modes");
        displayResolutionOptions = new JComboBox<>();
    }

    public void initBottomPanelView(){
        populateDisplayModes();
        bottomPanel.setLayout(layout);
        constraints.gridx = 0;
        constraints.gridy = 0;
        bottomPanel.add(displayResolutionLabel, constraints);
        constraints.gridx = 1;
        constraints.gridy = 0;
        bottomPanel.add(displayResolutionOptions, constraints);
    }

    private void populateDisplayModes(){
        GraphicsDevice graphicsDevice = GraphicsEnvironment.getLocalGraphicsEnvironment().getDefaultScreenDevice();
        DisplayMode[] displayModes = graphicsDevice.getDisplayModes();

        for (DisplayMode displayMode : displayModes) {
            DisplayModeOption displayModeOption = new DisplayModeOption(displayMode.getWidth(), displayMode.getHeight());
            displayResolutionOptions.addItem(displayModeOption);
        }
    }

    public JPanel getBottomPanel(){
        return bottomPanel;
    }
}
