package io.github.captivecow;

import javax.swing.*;

public class JBenchController {

    private final JBenchView jBenchView;
    private final JBenchModel jBenchModel;

    public JBenchController(){
        jBenchView = new JBenchView(this);
        jBenchModel = new JBenchModel(jBenchView);
    }

    public void start(){
        SwingUtilities.invokeLater(jBenchView);
    }

    public void update(){
        System.out.println("Update button was pressed. Current State:");
        RenderState currentState = jBenchView.getRenderState();
        System.out.println(currentState);
        jBenchModel.updateViewRenderState(currentState);
    }
}
