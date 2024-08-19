package io.github.captivecow;

public record RenderSize(int width, int height) {
    @Override
    public String toString() {
        if (width == -1 && height == -1){
            return "Random";
        }
        return width + "x" + height;
    }
}
