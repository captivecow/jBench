package io.github.captivecow;

public class RenderState {
    private final DisplayModeOption displayModeOption;
    private final RenderSize renderSize;
    private final int renderAmount;

    public RenderState(DisplayModeOption displayModeOption, RenderSize renderSize, int renderAmount){
        this.displayModeOption = displayModeOption;
        this.renderSize = renderSize;
        this.renderAmount = renderAmount;
    }

    public DisplayModeOption getDisplayModeOption() {
        return displayModeOption;
    }

    public int getRenderAmount() {
        return renderAmount;
    }

    public RenderSize getRenderSize() {
        return renderSize;
    }

    @Override
    public String toString() {
        return "Display Mode: " + displayModeOption.toString() +
                "\nRender Size: " + renderSize.toString() +
                "\nImage Amount: " + renderAmount;
    }
}
