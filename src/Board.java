import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.geom.AffineTransform;
import java.awt.geom.Ellipse2D;

import javax.swing.JPanel;

public class Board extends JPanel implements Runnable {


    private final int DELAY = 25;

    private Thread animator;
    private Paddle paddle;
    private Ball ball;



    public Board(){
        initBoard();
    }

    private void initBoard() {
        setBackground(Color.BLACK);

        addKeyListener(new TAdapter());
        setFocusable(true);
        setDoubleBuffered(true);
        paddle = new Paddle();
        ball = new Ball();
    }


    @Override
    public void addNotify(){
        super.addNotify();
        animator = new Thread(this);
        animator.start();
    }


    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);

        Graphics2D g2d = (Graphics2D) g;

        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                RenderingHints.VALUE_ANTIALIAS_ON);

        g2d.setRenderingHint(RenderingHints.KEY_RENDERING,
                RenderingHints.VALUE_RENDER_QUALITY);

        drawGameObjects(g2d);
        Toolkit.getDefaultToolkit().sync();
    }




    private void drawGameObjects(Graphics2D g2d) {


        g2d.drawImage(paddle.getImage(), paddle.getX(), paddle.getY(),
                paddle.getWidth(), paddle.getHeight(), this);
        g2d.drawImage(ball.getImage(),ball.getX(),ball.getY(),ball.getWidth(),ball.getHeight(),this);

    }

    private class TAdapter extends KeyAdapter {

        @Override
        public void keyReleased(KeyEvent e) {
            paddle.keyReleased(e);
        }

        @Override
        public void keyPressed(KeyEvent e) {
            paddle.keyPressed(e);
        }
    }




    @Override
    public void run() {

        long beforeTime, timeDiff, sleep;

        beforeTime = System.currentTimeMillis();

        while (true) {

            paddle.movePaddle();
            ball.moveBall();
            repaint();
//            System.out.println("Here");

            timeDiff = System.currentTimeMillis() - beforeTime;
            sleep = DELAY - timeDiff;

            if (sleep < 0) {
                sleep = 2;
            }

            try {
                Thread.sleep(sleep);
            } catch (InterruptedException e) {
                System.out.println("Interrupted: " + e.getMessage());
            }

            beforeTime = System.currentTimeMillis();
        }


    }
}
