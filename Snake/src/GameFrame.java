import javax.swing.*;

public class GameFrame extends JFrame {
    GameFrame() {
        GamePanel panel = new GamePanel();
        this.add(panel);
        this.setTitle("I'm a pink snake");
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setResizable(false);
        this.pack();
        this.setLocationRelativeTo(null);
        this.setVisible(true);
    }
}