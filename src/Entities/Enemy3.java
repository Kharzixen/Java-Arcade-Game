package Entities;

import Panels.GamePanel;

import javax.imageio.ImageIO;
import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.util.Random;

public class Enemy3 extends Enemy {
    private final GamePanel gm;

    public Enemy3(GamePanel gm) {
        try {
            img = ImageIO.read(new File("Resources/enemy3.png"));
        } catch (IOException e) {
            e.printStackTrace();
        }
        Random r = new Random();
        System.out.println(gm.getTileSize());
        x = r.nextInt(0, 768 - gm.getTileSize());
        y = 0;
        speed = 35;

        this.gm = gm;
    }

    @Override
    public void update() {
        y = y + speed;
    }

    @Override
    public void draw(Graphics2D g2) {
        g2.drawImage(img, x, y, gm.getTileSize(), gm.getTileSize(), null);
    }
}