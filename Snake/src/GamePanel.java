import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Random;

public class GamePanel extends JPanel implements ActionListener {
    static final int width = 600;
    static final int height = 600;
    static final int width2 = 800;
    static final int unit_size = 25;
    static final int units= (width*height)/(unit_size*unit_size);
    static final int delay = 100;
    final int x[] = new int[units];
    final int y[] = new int[units];
    int body_parts = 2;
    int apples_eaten;
    int appleX;
    int appleY;
    char direction = 'R';
    boolean running = false;
    Timer timer;
    Random random = new Random();
    JButton replay;
    JButton close;
    private BufferedImage img;
    {
        try {
            img = ImageIO.read(new File("img_1.png"));
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }



    GamePanel() {
        random = new Random();
        this.setPreferredSize(new Dimension(width2, height));
        this.setBackground(Color.BLACK);
        this.setFocusable(true);
        this.addKeyListener(new MyKeyAdapter());
        startgame();
//replay buttons
        replay = new JButton();
        replay.setBounds(250, 230, 125, 60);
        replay.addActionListener(this);
        replay.setText("Replay");
        replay.setFont(new Font("Ink Free", Font.BOLD, 25));
        replay.setBackground(Color.BLACK);
        replay.setForeground(Color.WHITE);
//close button
        close = new JButton();
        close.setFont(new Font("Ink Free", Font.BOLD, 25));
        close.setBounds(425, 230, 125, 60);
        close.addActionListener(this);
        close.setText("Close");
        close.setBackground(Color.BLACK);
        close.setForeground(Color.WHITE);

    }
    public void startgame(){
        newApple();
        running = true;
        timer = new Timer(delay,this);
        timer.start();


    }
    public void paintComponent(Graphics g){
        super.paintComponent(g);
        draw(g);

    }
    public void draw(Graphics g){
        if(running) {
//vẽ đường kẻ
            g.setColor(Color.darkGray);
        for(int i=0; i <= height / unit_size; i++) {
            g.drawLine(i * unit_size, 0, i * unit_size, height);
        }
        for(int i=0; i <= width / unit_size; i++) {
            g.drawLine(0, i * unit_size, width, i * unit_size);
        }
//chèn tranh con rắn
        g.drawImage(img,550,300, 300,300,null);
// vẽ táo
        g.setColor(Color.red);
        g.fillOval(appleX, appleY, unit_size, unit_size);
//vẽ thân con rắn
        for(int i = 0; i < body_parts; i++){
            if(i == 0){
                g.setColor(Color.PINK);
            }
            else {
                g.setColor(new Color(233, 30, 99));
            }
            g.fillRect(x[i], y[i], unit_size, unit_size);
        }

        g.setColor(Color.PINK);
        //score
        g.setFont( new Font("Ink Free",Font.BOLD, 40));
        FontMetrics metrics = getFontMetrics(g.getFont());
        g.drawString("Score: "+apples_eaten, (width - metrics.stringWidth("Score: "+apples_eaten))/2+400, g.getFont().getSize());
        } else {
            gameOver(g);
        }

    }
    public void newApple(){
        appleX = random.nextInt((int)(width/unit_size))*unit_size;
        appleY = random.nextInt((int)(height/unit_size))*unit_size;
    }
    public void move(){
        for(int i = body_parts;i>0;i--) {
            x[i] = x[i-1];
            y[i] = y[i-1];
        }

        switch (direction) {
            case 'U':
                y[0] = y[0] - unit_size;
                break;
            case 'D':
                y[0] = y[0] + unit_size;
                break;
            case 'L':
                x[0] = x[0] - unit_size;
                break;
            case 'R':
                x[0] = x[0] + unit_size;
                break;
        }
    }
    public void checkApple(){
        if((x[0] == appleX) && (y[0] == appleY)) {
            body_parts++;
            apples_eaten++;
            newApple();
        }
    }
    public void checkCollisions(){
        for(int i = body_parts;i>0;i--) {
            if((x[0] == x[i])&& (y[0] == y[i])) {
                running = false;
            }
        }
        if(x[0] < -10) {
            running = false;
        }
        if(x[0] > width - unit_size) {
            running = false;
        }
        if(y[0] < -10) {
            running = false;
        }
        if(y[0] > height - unit_size) {
            running = false;
        }
        if(!running) {
            timer.stop();
        }

    }
    public void resetGame() {
        for (int i = 0; i < body_parts; i++) {
            x[i] = 0;
            y[i] = 0;
        }
        direction = 'R';
        apples_eaten = 0;
        body_parts = 2;
        running = true;
        remove(close);
        remove(replay);
        timer.start();
    }
    public void gameOver(Graphics g){
        g.setColor(Color.PINK);
        g.setFont( new Font("Ink Free", Font.PLAIN, 40));
        FontMetrics metrics1 = getFontMetrics(g.getFont());
        g.drawString("Score: "+apples_eaten, (width - metrics1.stringWidth("Score: "+apples_eaten))/2 +100, g.getFont().getSize()+300);

        g.setColor(Color.PINK);
        g.setFont( new Font("Ink Free", Font.PLAIN, 75));
        FontMetrics metrics2 = getFontMetrics(g.getFont());
        g.drawString("Game Over", (width - metrics2.stringWidth("Game Over"))/2 + 100, height/2-100);
        add(replay);
        add(close);
    }


    @Override
    public void actionPerformed(ActionEvent e) {
        if(running) {
            move();
            checkApple();
            checkCollisions();
        }
        repaint();
        if(e.getSource() == replay){
            resetGame();
        }
        if(e.getSource() == close){
            System.exit(0);
        }
    }
    public class MyKeyAdapter extends KeyAdapter {
        @Override
        public void keyPressed(KeyEvent e) {
            switch(e.getKeyCode()){
                case KeyEvent.VK_LEFT:
                    if(direction != 'R') direction = 'L';
                    break;

                case KeyEvent.VK_RIGHT:
                    if(direction != 'L') direction = 'R';
                    break;

                case KeyEvent.VK_UP:
                    if(direction != 'D') direction = 'U';
                    break;

                case KeyEvent.VK_DOWN:
                    if(direction != 'U') direction = 'D';
                    break;
            }
        }




    }
}
