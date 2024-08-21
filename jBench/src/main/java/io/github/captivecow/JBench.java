package io.github.captivecow;

import javax.swing.*;

public class JBench
{
    public static void main( String[] args ) {
        JBenchView jBenchView = new JBenchView();
        jBenchView.loadImage();
        jBenchView.initRenderState();
        SwingUtilities.invokeLater(jBenchView);
    }
}
