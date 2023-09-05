package Entities;

import Panels.GamePanel;
import Managers_Handlers.KeyHandler;
import Managers_Handlers.MissileManager;

import javax.imageio.ImageIO;
import javax.sound.sampled.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;


public class Player {
    private final GamePanel gm;
    private int playerX;
    private int playerY;
    private final int playerSpeed;
    private final KeyHandler keyH;
    private Clip clip;
    private BufferedImage playerShip;
    private final MissileManager mm;

    public Player(GamePanel gm, KeyHandler keyH, MissileManager mm) {
        this.gm = gm;
        this.keyH = keyH;
        this.mm = mm;
        playerX = gm.getScreenWidth() / 2 - gm.getTileSize();
        playerY = gm.getScreenHeight() - gm.getTileSize() - 80;
        playerSpeed = 5;
        try {
            playerShip = ImageIO.read(new File("Resources/player_ship16x16.png"));
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public void update() {
        if (keyH.rightPressed) {
            playerX += playerSpeed;
        }
        if (keyH.leftPressed) {
            playerX -= playerSpeed;
        }

        if (keyH.firePressed) {
            mm.add(new Missile(gm));
            keyH.firePressed = false;
            try {
                AudioInputStream audioStream = AudioSystem.getAudioInputStream(new File("Resources/laser.wav").getAbsoluteFile());
                clip = AudioSystem.getClip();
                clip.open(audioStream);
                FloatControl gainControl = (FloatControl) clip.getControl(FloatControl.Type.MASTER_GAIN);
                gainControl.setValue(20f * (float) Math.log10(0.005));
            } catch (UnsupportedAudioFileException | IOException | LineUnavailableException e) {
                e.printStackTrace();
            }
            clip.start();
        }

    }

    public void draw(Graphics2D g2) {
        g2.drawImage(playerShip, playerX, playerY, gm.getTileSize(), gm.getTileSize(), null);
    }

    public int getPlayerX() {
        return playerX;
    }

    public int getPlayerY() {
        return playerY;
    }

    public void playerRestart(){

        playerX = gm.getScreenWidth() / 2 - gm.getTileSize();
        playerY = gm.getScreenHeight() - gm.getTileSize() - 80;
    }

}
