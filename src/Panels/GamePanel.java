//Game Panel , the only object added to the frame , the frame fits the panel in size ;
//GameLoop -> update and draw -> limited to 60fps
//Auxiliary (private) functions
//Manager objects of enemies , missiles
//Key Handler -> keyboard inputs

package Panels;
import Managers_Handlers.EnemyManager;
import Managers_Handlers.KeyHandler;
import Managers_Handlers.MissileManager;
import Entities.Player;

import javax.imageio.ImageIO;
import javax.sound.sampled.*;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class GamePanel extends JPanel implements Runnable {

    //menu -> to draw the menu page
    private boolean menu = true;
    //setDif -> to draw the select difficulty page
    private boolean setDiff = false;
    //size of the panel :
    private final int originalTileSize = 16; //16x16 -> characters
    private final int scale = 3;
    private final int tileSize = originalTileSize * scale;
    private final int maxScreenCol = 16;
    private final int maxScreenRow = 12;
    private final int screenWidth = tileSize * maxScreenCol;       //768
    private final int screenHeight = tileSize * maxScreenRow;     //576

    //thread of the game loop
    private Thread gameThread;

    //instantiating the KeyHandler , Managers and the Player object
    private final KeyHandler keyH = new KeyHandler(this);
    private final Background b = new Background(this);
    private final MissileManager mm = new MissileManager(this);
    private EnemyManager em = new EnemyManager(this, 3);
    private final Player p = new Player(this, keyH, mm);

    //score-highScore counters
    private int score = 0;
    private int highScore = 0;

    //background music -> Master of Puppets (8bit version) by Metallica
    private Clip clip;

    //Constructor of the game Panel -> setting up the game loop
    public GamePanel() {
        this.setPreferredSize(new Dimension(screenWidth, screenHeight));
        this.setBackground(Color.BLACK);
        this.addKeyListener(keyH);
        this.setFocusable(true);
        this.setLayout(null);
        startGameThread();

        //background music ;
        try {
            AudioInputStream audioStream = AudioSystem.getAudioInputStream(new File("Resources/gameSound.wav").getAbsoluteFile());
            clip = AudioSystem.getClip();
            clip.open(audioStream);
        } catch (UnsupportedAudioFileException | IOException | LineUnavailableException e) {
            e.printStackTrace();
        }

        FloatControl gainControl = (FloatControl) clip.getControl(FloatControl.Type.MASTER_GAIN);
        gainControl.setValue(20f * (float) Math.log10(0.005));
        clip.start();
        clip.loop(15);
    }

    public void startGameThread() {
        //starting the loop at another thread
        gameThread = new Thread(this);
        gameThread.start();
    }

    //loop-thread function:
    public void run() {
        int FPS = 60;
        //the number of iterations per sec of the cycle is limited to ~ 60 .
        double drawInterval = (double) 1000000000 / FPS;
        double nextDrawTime = System.nanoTime() + drawInterval;
        int count = 0;

        while (gameThread != null) {

            //the enemy wave moves forward at every 1.5 sec -> in every 90. iteration
            if (!keyH.pauseGame && !em.isStatus() && !menu) {
                count++;
            }

            //UPDATE : updates the status of all entities
            if (!keyH.pauseGame && !em.isStatus()) {
                update(count);
                if (count >= 90) {
                    count = 0;
                }
            }

            repaint();

            //gameLoop waits the remaining time .
            try {
                double remainingTime = nextDrawTime - System.nanoTime();
                remainingTime = remainingTime / 1000000;

                if (remainingTime < 0) {
                    remainingTime = 0;
                }

                Thread.sleep((long) remainingTime);

                nextDrawTime += drawInterval;

            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            //if the wave hits the line , and ESC key is pressed , the game restarts .
            if (em.isStatus() && keyH.pauseGame) {
                restartGame();
            }

        }
    }

    //UPDATE:
    public void update(int count) {
        //in game-mode , updates every entity at the panel .
        if (!menu) {
            p.update();
            mm.update();
            //at every 90. iteration the wave goes forward
            if (count >= 90) {
                em.Update();
            }
            mm.checkIfHit(em);
            mm.collector();
        } else {
            //non-game-mode:
            //setDiff menu -> when SPACE is pressed in main menu
            if (keyH.diff != -1 && setDiff) {
                //sets up the number of enemies by diff
                em = new EnemyManager(this, 12 * keyH.diff);
                menu = false;
            }

            //if SPACE is pressed -> difficulty menu
            if (keyH.firePressed) {
                setDiff = true;
            }
        }
    }

    //REDRAW
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D) g;
        //game-mode:drawing all entities
        if (!menu) {
            b.draw(g2);
            p.draw(g2);
            em.Draw(g2);
            mm.draw(g2);
            //GamePaused statistics
            if (keyH.pauseGame) {
                paintGamePaused(g2);
            }
            paintScore(g2);
            em.ifGameOver();
            //GameOver statistics
            if (em.isStatus()) {
                paintGameOver(g2);
            }
        } else {
            //non-game-mode:
            if (!setDiff) {
                b.draw(g2);
                p.draw(g2);
                paintMenu(g2);
            } else {
                b.draw(g2);
                p.draw(g2);
                paintDiffMenu(g2);
            }
        }
        g2.dispose();
    }

    private void paintGamePaused(Graphics2D g2) {
        try {
            BufferedImage img;
            img = ImageIO.read(new File("src/Resources/pause.png"));
            g2.drawImage(img, 0, 0, this.getTileSize(), this.getTileSize(), null);
            g2.setFont(new Font("Courier New", Font.BOLD, 23));
            g2.setColor(Color.WHITE);
            g2.drawString("GAME PAUSED", screenWidth / 2 - 90, screenHeight / 2 - 30);
            g2.drawString("High score:" + highScore, screenWidth / 2 - 100, screenHeight / 2 + 10);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void paintScore(Graphics g2) {
        g2.setFont(new Font("Courier New", Font.BOLD, 23));
        g2.setColor(Color.WHITE);
        g2.drawString("Score: " + score, 10, 550);
    }

    private void paintGameOver(Graphics2D g2) {
        g2.drawString("GAME OVER", screenWidth / 2 - 90, screenHeight / 2 - 30);
        g2.drawString("Press ESC to restart", screenWidth / 2 - 168, screenHeight / 2 + 20);
        if (score > highScore) {
            highScore = score;
        }
        g2.drawString("Current score: " + score, screenWidth / 2 - 168, screenHeight / 2 + 70);
        g2.drawString("High score: " + highScore, screenWidth / 2 - 168, screenHeight / 2 + 120);
    }

    private void paintMenu(Graphics2D g2) {
        g2.setFont(new Font("Courier New", Font.BOLD, 45));
        g2.setColor(Color.RED);

        g2.drawString("Space Battles!", screenWidth / 2 - 215, screenHeight / 4);

        g2.setFont(new Font("Courier New", Font.BOLD, 25));
        g2.setColor(Color.WHITE);

        g2.drawString("Press SPACE to start the game !", screenWidth / 2 - 250, screenHeight / 2);

        g2.setFont(new Font("Courier New", Font.BOLD, 20));
        g2.setColor(Color.WHITE);

        g2.drawString("Created By: mmim2055", 15, screenHeight - 35);
    }

    private void paintDiffMenu(Graphics2D g2) {
        g2.setFont(new Font("Courier New", Font.BOLD, 30));
        g2.setColor(Color.WHITE);

        g2.drawString("Set Difficulty :", screenWidth / 2 - 250, screenHeight / 4);

        g2.setFont(new Font("Courier New", Font.BOLD, 30));
        g2.setColor(Color.WHITE);

        g2.drawString("1) - Easy", screenWidth / 2 - 100, screenHeight / 4 + 70);

        g2.drawString("2) - Medium", screenWidth / 2 - 100, screenHeight / 4 + 140);

        g2.drawString("3) - Hard", screenWidth / 2 - 100, screenHeight / 4 + 210);
    }

    //at GameOver -> restarts the game
    private void restartGame() {
        this.score = 0;
        setDiff = true;
        menu = true;
        keyH.diff = -1;
        mm.fullClear();
        em.setStatus(false);
        p.playerRestart();
        keyH.pauseGame = false;
    }

    //GETTERS AND SETTERS:

    public boolean isSetDiff() {
        return setDiff;
    }

    public int getScreenWidth() {
        return screenWidth;
    }

    public int getScreenHeight() {
        return screenHeight;
    }

    public int getTileSize() {
        return tileSize;
    }

    public int getMaxScreenCol() {
        return maxScreenCol;
    }

    public int getMaxScreenRow() {
        return maxScreenRow;
    }

    public Player getP() {
        return p;
    }

    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }

    public boolean isMenu() {
        return menu;
    }
}
