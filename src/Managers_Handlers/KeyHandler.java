package Managers_Handlers;

import Panels.GamePanel;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

public class KeyHandler implements KeyListener {
    public boolean leftPressed, rightPressed, firePressed, pauseGame = false;
    public int diff = -1;
    private final GamePanel gm;

    public KeyHandler(GamePanel gm) {
        this.gm = gm;
    }

    @Override
    public void keyTyped(KeyEvent e) {
    }

    @Override
    public void keyPressed(KeyEvent e) {
        int code = e.getKeyCode();

        if (code == KeyEvent.VK_A) {
            leftPressed = true;
        }

        if (code == KeyEvent.VK_D) {
            rightPressed = true;
        }

        if (code == KeyEvent.VK_SPACE) {
            firePressed = true;
        }

        if (code == KeyEvent.VK_ESCAPE  && !gm.isMenu()) {
            pauseGame = !pauseGame;
        }

        if (code == KeyEvent.VK_1 && gm.isSetDiff()) {
            diff = 1;
        }

        if (code == KeyEvent.VK_2 && gm.isSetDiff()) {
            diff = 2;
        }

        if (code == KeyEvent.VK_3 && gm.isSetDiff()) {
            diff = 3;
        }

    }

    @Override
    public void keyReleased(KeyEvent e) {
        int code = e.getKeyCode();

        if (code == KeyEvent.VK_A) {
            leftPressed = false;
        }

        if (code == KeyEvent.VK_D) {
            rightPressed = false;
        }

        if (code == KeyEvent.VK_SPACE) {
            firePressed = false;
        }
    }
}
