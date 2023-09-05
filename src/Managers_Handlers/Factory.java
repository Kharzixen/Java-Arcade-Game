package Managers_Handlers;

import Entities.Enemy;
import Entities.Enemy1;
import Entities.Enemy2;
import Entities.Enemy3;
import Panels.GamePanel;

public class Factory {
    public static Enemy EnemyFactory(int i, GamePanel gm) {
        return switch (i) {
            case 2 -> new Enemy2(gm);
            case 3 -> new Enemy3(gm);
            default -> new Enemy1(gm);
        };
    }
}
