package Managers_Handlers;

import Panels.GamePanel;
import Entities.Missile;

import java.awt.*;
import java.util.ArrayList;
import java.util.Random;

public class MissileManager {
    private final GamePanel gm;

    private final ArrayList<Missile> missiles = new ArrayList<>();

    public void add(Missile x) {
            missiles.add(x);
    }

    public MissileManager(GamePanel gm) {
        this.gm = gm;
    }

    public void update() {
        //System.out.println(missiles.toArray().length);
        for (Missile missile : missiles) {
            missile.update();
        }
    }

    public void draw(Graphics2D g2) {
        int i=0;
        while (i< missiles.size()) {
            if(missiles.get(i).getY() >= 0) {
                missiles.get(i).draw(g2);
            }
            i++;
        }
    }

    public void collector() {
        for (int i=0; i<missiles.size(); i++) {
            if (missiles.get(i).getY() <= 0) {
               missiles.remove(i);
               i++;
            }
        }
    }

    public void fullClear() {
        missiles.clear();
    }

    public void checkIfHit(EnemyManager em) {
        Random r = new Random();
        for (Missile missile : missiles) {
            for (int j = 0; j < em.getNrOfEnemies(); j++)
                if ((missile.getY() > 0)
                        && (missile.getX() >= em.getE(j).getX() - 5 || (missile.getX() <= em.getE(j).getX() && em.getE(j).getX() - missile.getX() < 16))
                        && (missile.getX() <= em.getE(j).getX() + gm.getTileSize() - 16)
                        && (missile.getY() <= em.getE(j).getY() + gm.getTileSize() - 5)) {
                    em.setE(j, Factory.EnemyFactory(r.nextInt(1, 4), gm));
                    missile.setY(-1);
                    gm.setScore(gm.getScore() + 1);
                }
        }
    }
}
