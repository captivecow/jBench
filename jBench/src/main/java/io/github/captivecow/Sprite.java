package io.github.captivecow;

import java.util.concurrent.ThreadLocalRandom;

public class Sprite {

    private int width;
    private int height;
    private float x;
    private float y;
    private float veloX;
    private float veloY;

    public Sprite(int width, int height, float x, float y){
        this.width = width;
        this.height = height;
        this.x = x;
        this.y = y;
        veloX = ThreadLocalRandom.current().nextBoolean() ? 1: -1;
        veloY = ThreadLocalRandom.current().nextBoolean() ? 1: -1;
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

    public float getVeloX() {
        return veloX;
    }

    public void setVeloX(float veloX) {
        this.veloX = veloX;
    }

    public float getVeloY() {
        return veloY;
    }

    public void setVeloY(float veloY) {
        this.veloY = veloY;
    }
}
