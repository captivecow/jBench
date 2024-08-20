package io.github.captivecow;

public class Sprite {

    private int width;
    private int height;
    private float x;
    private float y;

    public Sprite(int width, int height, float x, float y){
        this.width = width;
        this.height = height;
        this.x = x;
        this.y = y;
    }


    public int getHeight() {
        return height;
    }

    public void setHeight(int height) {
        this.height = height;
    }

    public int getWidth() {
        return width;
    }

    public void setWidth(int width) {
        this.width = width;
    }

    public int getSpriteId() {
        return 1;
    }

    public float getX() {
        return x;
    }

    public void setX(float x) {
        this.x = x;
    }

    public void setY(float y) {
        this.y = y;
    }

    public float getY() {
        return y;
    }
}
