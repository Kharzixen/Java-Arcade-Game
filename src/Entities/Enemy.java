package Entities;

import java.awt.*;
import java.awt.image.BufferedImage;

public abstract class Enemy {
    protected int x, y, speed;
    protected BufferedImage img;

    public abstract void update();

    public abstract void draw(Graphics2D g2);

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }
}
