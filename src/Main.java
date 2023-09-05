import Panels.GamePanel;

import javax.swing.*;

public class Main {
    public static void main(String[] args) {
        JFrame window = new JFrame("Space Battles(v0.1)");
        window.setResizable(false);
        window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        GamePanel p1 = new GamePanel();
        window.add(p1);
        window.pack();  //window to be sized to fit the size of the game panel
        window.setVisible(true);
    }
}
