package io.github.captivecow;

public record RenderState(DisplayModeOption displayModeOption, RenderSize renderSize, int renderAmount) {

    @Override
    public String toString() {
        return "Display Mode: " + displayModeOption.toString() +
                "\nRender Size: " + renderSize.toString() +
                "\nImage Amount: " + renderAmount;
    }
}
