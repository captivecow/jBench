package io.github.captivecow;

public record DisplayModeOption(int width, int height) {

    @Override
    public String toString() {
        return width + "x" + height;
    }
}
