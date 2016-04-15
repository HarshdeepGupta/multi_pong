import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import javax.swing.JPanel;

public class Board extends JPanel implements Runnable {


    private final int DELAY = 25;
    private final int NO_OF_PADDLES = 4;

    private Thread animator;
    private Paddle[] paddleArray;
    private Ball ball;

    private int SET_KEY_LISTENER_ON;



    public Board(){
        initBoard();
    }

    private void initBoard() {
        setBackground(Color.BLACK);

        /*Paddle ID 1 is at the bottom edge
        Paddle ID 2 is at the top edge
        Paddle ID 3 is at the left edge
        Paddle ID 4 is at the right edge
        */
        //give the paddle ID here
        SET_KEY_LISTENER_ON = 3;

        addKeyListener(new TAdapter());
        setFocusable(true);
        setDoubleBuffered(true);
        paddleArray = new Paddle[NO_OF_PADDLES];
        for (int i = 0;i<NO_OF_PADDLES;i++){
            paddleArray[i] = new Paddle(i+1);
        }

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

        drawPaddles(g2d);
        g2d.drawImage(ball.getImage(),ball.getX(),ball.getY(),ball.getWidth(),ball.getHeight(),this);

    }

    private class TAdapter extends KeyAdapter {



        @Override
        public void keyReleased(KeyEvent e) {
            paddleArray[SET_KEY_LISTENER_ON-1].keyReleased(e);
        }

        @Override
        public void keyPressed(KeyEvent e) {
            paddleArray[SET_KEY_LISTENER_ON-1].keyPressed(e);
        }
    }




    @Override
    public void run() {

        long beforeTime, timeDiff, sleep;

        beforeTime = System.currentTimeMillis();

        while (true) {


            movePaddles();
            ball.moveBall();
            checkCollision();
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



    private void checkCollision(){
        Paddle paddle;
        for (int i = 0; i < NO_OF_PADDLES;i++){
            paddle = paddleArray[i];
            if(ball.getRect().intersects(paddle.getRect())){
                if(ball.ballVelocity.dot(paddle.normal) > 0){
//                int vel_X = ball.ballVelocity.X;
//                int vel_Y = ball.ballVelocity.Y;
//                vel_X = vel_X - 2*(ball.ballVelocity.dot(paddle.normal));
//                vel_Y = vel_Y - 2*(ball.ballVelocity.dot(paddle.normal));
//                ball.ballVelocity.X = vel_X;
//                ball.ballVelocity.Y = vel_Y;
                Vector2D vel = ball.ballVelocity;
                vel = vel.sub( paddle.normal.scalarMult(  2*(vel.dot(paddle.normal))));
                ball.ballVelocity = vel;
                }
            }

        }

    }



    private void drawPaddles(Graphics2D g2d){
        Paddle paddle;
        for(int i = 0; i < NO_OF_PADDLES;i++){
            paddle = paddleArray[i];
            g2d.drawImage(paddle.getImage(), paddle.getX(), paddle.getY(),
                    paddle.getWidth(), paddle.getHeight(), this);
        }

    }

    private void movePaddles() {
        for(int i = 0; i < NO_OF_PADDLES;i++){
            paddleArray[i].movePaddle();
        }

    }





















}
