package Managers_Handlers;

import Panels.GamePanel;
import Entities.Enemy;

import java.awt.*;
import java.util.Random;

public class EnemyManager {
    private final int nrOfEnemies;

    private final Enemy[] e;

    private boolean status = false;

    GamePanel gm;

    public EnemyManager(GamePanel gm, int nrOfEnemies) {
        this.gm = gm;
        this.nrOfEnemies = nrOfEnemies;
        e = new Enemy[nrOfEnemies];
        Random r = new Random();
        for (int i = 0; i < nrOfEnemies; i++) {
            e[i] = Factory.EnemyFactory(r.nextInt(1, 4), this.gm);
        }
    }

    public void Draw(Graphics2D g2) {
        for (int i = 0; i < nrOfEnemies; i++) {
            e[i].draw(g2);
        }
    }

    public void Update() {
        for (int i = 0; i < nrOfEnemies; i++) {
            e[i].update();
        }
    }

    public int getNrOfEnemies() {
        return nrOfEnemies;
    }

    public Enemy getE(int i) {
        return e[i];
    }

    public void ifGameOver() {
        for (int i = 0; i < getNrOfEnemies(); i++) {
            if (e[i].getY() + gm.getTileSize() - 10 >= gm.getP().getPlayerY()) {

                status = true;
                break;
            }
        }
    }

    public void setE(int i, Enemy x) {
        this.e[i] = x;
    }

    public void setStatus(boolean status) {
        this.status = status;
    }

    public boolean isStatus() {
        return status;
    }



}
