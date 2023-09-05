package Entities;

import Panels.GamePanel;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class Missile {
    private final int x, speed;
    private int y;
    private BufferedImage img;
    private final GamePanel gm;

    public Missile(GamePanel gm) {
        this.gm = gm;
        this.x = gm.getP().getPlayerX() + 7;
        this.y = gm.getP().getPlayerY() - 10;
        speed = 5;
        try {
            img = ImageIO.read(new File("Resources/missile1.png"));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void update() {
        y -= speed;
    }

    public void draw(Graphics2D g2) {
        g2.drawImage(img, x, y, gm.getTileSize(), gm.getTileSize(), null);
    }

    public int getX() {
        return x;
    }


    public int getY() {
        return y;
    }

    public void setY(int y) {
        this.y = y;
    }
}
