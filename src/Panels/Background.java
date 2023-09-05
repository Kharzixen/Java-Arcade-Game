package Panels;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class Background {
    private BufferedImage img;
    private final GamePanel gp;

    public Background(GamePanel gp) {
        this.gp = gp;
        try {
            img = ImageIO.read(new File("Resources/spacebg.png"));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void draw(Graphics2D g2) {
        int x, y = 0;

        for (int i = 0; i < gp.getMaxScreenRow(); i++) {
            x = 0;
            for (int j = 0; j < gp.getMaxScreenCol(); j++) {
                g2.drawImage(img, x, y, gp.getTileSize(), gp.getTileSize(), null);
                x = x + gp.getTileSize();
            }
            y = y + gp.getTileSize();
        }

        //Get the current size of this component
        Dimension d = gp.getSize();

        //draw in black
        g2.setColor(Color.RED);
        //draw a centered horizontal line
        g2.drawLine(0, gp.getP().getPlayerY() - 4, d.width, gp.getP().getPlayerY());
    }
}
