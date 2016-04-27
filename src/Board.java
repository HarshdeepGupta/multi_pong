import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import javax.swing.JPanel;

public class Board extends JPanel implements Runnable {


    private final int DELAY = 25;
    private final int NO_OF_PADDLES = 4;
//    private static int HEIGHT = 575;
//    private static int WIDTH = 920;

    private Thread animator;
    private Paddle[] paddleArray;
    private Ball ball;
    private Bot bot;

    private boolean running;
    private int lives;


    private long waitTimer;
    private long waitTimerDiff;
    private boolean wait;
    private int waitDelay = 1000 ;

    //public static ArrayList<powerUp> powerUps;


    private int SET_KEY_LISTENER_ON;



    public Board(){
        initBoard();
    }

    private void initBoard() {
        setBackground(Color.cyan);
        setPreferredSize(new Dimension(WIDTH,HEIGHT));
        setFocusable(true);
        requestFocus();
        lives = 3;
        //powerUps = new ArrayList<powerUp>();

        /*Paddle ID 1 is at the  top edge
        Paddle ID 2 is at the right edge
        Paddle ID 3 is at the  left edge
        Paddle ID 4 is at the bottom edge
        */
        //give the paddle ID here
        SET_KEY_LISTENER_ON = 4;

        addKeyListener(new TAdapter());
        setFocusable(true);
        setDoubleBuffered(true);
        paddleArray = new Paddle[NO_OF_PADDLES];
        for (int i = 0;i<NO_OF_PADDLES;i++){
            paddleArray[i] = new Paddle(i+1,2);
        }


        ball = new Ball();

        //Initialize the bot and attach it to a paddle
        bot = new Bot(ball,1);

        bot.attach(paddleArray[0]);
//        bot1.attach(paddleArray[2]);
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

        Graphics2D g2d;
        g2d = (Graphics2D) g;

        g2d.setRenderingHint(RenderingHints.KEY_ALPHA_INTERPOLATION, RenderingHints.VALUE_ALPHA_INTERPOLATION_QUALITY);
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2d.setRenderingHint(RenderingHints.KEY_COLOR_RENDERING, RenderingHints.VALUE_COLOR_RENDER_QUALITY);
        g2d.setRenderingHint(RenderingHints.KEY_DITHERING, RenderingHints.VALUE_DITHER_ENABLE);
        g2d.setRenderingHint(RenderingHints.KEY_FRACTIONALMETRICS, RenderingHints.VALUE_FRACTIONALMETRICS_ON);
        g2d.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
        g2d.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
        g2d.setRenderingHint(RenderingHints.KEY_STROKE_CONTROL, RenderingHints.VALUE_STROKE_PURE);
        Toolkit.getDefaultToolkit().sync();

        g2d.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING,
                RenderingHints.VALUE_TEXT_ANTIALIAS_ON);

        drawGameObjects(g2d);

    }


    ///////////////Code for drawing game starts/////////////

    private void drawGameObjects(Graphics2D g2d) {

        drawPaddles(g2d);
        drawBall(g2d);
        //drawPowerUps(g2d);
        drawPlayerLives(g2d);

    }

    private void drawBall(Graphics2D g2d) {
        ball.draw(g2d);
    }


    private void drawPaddles(Graphics2D g2d){
        Paddle paddle;
        for(int i = 0; i < NO_OF_PADDLES;i++){
            paddle = paddleArray[i];
            paddle.draw(g2d);
        }

    }

    private void drawPlayerLives(Graphics2D g2d){
        for(int i = 0; i < lives; i++){
            g2d.setColor(Color.WHITE);
            g2d.fillOval(20 + (20 * i), 450, 10, 10);
            g2d.setStroke(new BasicStroke(3));
            g2d.setColor(Color.WHITE.darker());
            g2d.fillOval(20 + (20 * i), 450, 10, 10);
            g2d.setStroke(new BasicStroke(1));
        }

    }

/*    private void drawPowerUps(Graphics2D g2d){
        for(int i = 0; i < powerUps.size(); i++){
            powerUps.get(i).draw(g2d);
        }
    }*/

    private void drawText(int k, int d){
        Graphics g2d = this.getGraphics();
        g2d.setFont(new Font("Century Gothic", Font.PLAIN, 20));
        String s = " - FACE OFF BEGINS IN FEW" + " SEC - ";
        int len = (int) g2d.getFontMetrics().getStringBounds(s, g2d).getWidth();
        int transparency = (int) (255 * Math.sin(3.14 * (waitTimerDiff-d)/waitDelay));
        if (transparency > 255) transparency = 255;
        if (transparency < 5 ) transparency = 0;
        g2d.setColor(new Color(255, 255, 255, transparency));
        g2d.drawString(s, 250-len/2, 250);

    }
    ///////////////Code for drawing game ends/////////////

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



    ///////////////Code for updating game state begins////////////////
    @Override
    public void run() {

        wait = true;
        waitTimer = 0;
        waitTimerDiff = 0;
        running = true;


        long beforeTime, timeDiff, sleep;

        while (running) {

            if (waitTimer == 0 && wait){
                wait = false;
                System.out.println(2);
                waitTimer = System.currentTimeMillis();
            }
            else {

                waitTimerDiff = System.currentTimeMillis() - waitTimer;
                if (waitTimerDiff < waitDelay){
                    drawText(3,0);
                }
                else if (waitTimerDiff > waitDelay && waitTimerDiff < 2 * waitDelay){
                    //System.out.println(89);
                    drawText(2,waitDelay);
                }
                else if (waitTimerDiff > 2 * waitDelay && waitTimerDiff < 3 * waitDelay){
                    drawText(1,2*waitDelay);
                }

                else {
                    wait = true;
                    waitTimerDiff = 0;
                }
            }

            if (wait){
                beforeTime = System.currentTimeMillis();
                movePaddles();
                ball.moveBall();
                checkCollision();

                /*//chance for power ups
                double rand = Math.random();
                if (rand < .001) {
                    System.out.println("Here1");
                    powerUps.add(new powerUp(1, Math.abs(ball.getX()), Math.abs(ball.getY())));
                }
                else if (rand < 0.002) {
                    System.out.println("Here2");
                    powerUps.add(new powerUp(2, Math.abs(ball.getX()), Math.abs(ball.getY())));
                }
                else if (rand < 0.003) {
                    System.out.println("Here3");
                    powerUps.add(new powerUp(3, Math.abs(ball.getX()), Math.abs(ball.getY())));
                }
                else if (rand < 0.004) {
                    System.out.println("Here4");
                    powerUps.add(new powerUp(4, Math.abs(ball.getX()-300), Math.abs(ball.getY()-300)));
                }
*/
                bot.updateBot();

/*                //update power ups
                for(int i = 0; i < powerUps.size(); i++){
                    boolean remove = powerUps.get(i).update();
                    if (remove){
                        powerUps.remove(i);
                        i--;
                    }
                }*/

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

            }

        }
    }



    private void checkCollision(){
        Paddle paddle;
        for (int i = 0; i < NO_OF_PADDLES;i++){
            paddle = paddleArray[i];

            if(paddle.getType() == 1){
                //Collision detection for rectangular paddle
                if(ball.getRect().intersects(paddle.getRect())){
                    if(ball.ballVelocity.dot(paddle.normal) > 0){

                        Vector2D vel = ball.ballVelocity;
                        vel = vel.sub( paddle.normal.scalarMult(  2*(vel.dot(paddle.normal))));
                        ball.ballVelocity = vel;
                        ball.ballVelocity.add(paddle.paddleVelocity);
                        //also update the last_hit_by
                        ball.last_hit_by = paddle.paddleID;
                        System.out.print(ball.last_hit_by);

                    }
                }
            }
            //Collision detection for circular paddle
            else if (paddle.getType() == 2){
                //Collision detection for circular paddle
                //Get the centers of the ball and the circular paddle
                Vector2D c1 = ball.getCenter();
                Vector2D c2 = paddle.getCenter();
                int r1 = ball.getRadius();
                int r2 = ball.getRadius();
                //Compute the distance between the centers
                double dist = c1.dist(c2);
//                System.out.println(Math.abs( dist - (r1+r2)) );
                //if collision happens, then get the components of velocity along the normal and the tangent
                if(Math.abs( dist - (r1+r2)) <= 2){
                    System.out.println("Collsion detected");
                    //for this, get the normal of the paddle at the point of collision
                    //the normal is along the line joining the centers of the two circles

                }



            }


        }

        /*//power-up ball collision
        int px = ball.getX();
        int py = ball.getY();
        int pr  = 10;
        for (int i = 0; i < powerUps.size(); i++){
            powerUp p = powerUps.get(i);
            double x = p.getx();
            double y = p.gety();
            double r = p.getr();
            double dx = px - x;
            double dy = py - y;
            double dist = Math.sqrt(dx * dx + dy * dy);

            //collected power-up
            if (dist < px + r){
                int type = p.gettype();
                if (type == 1){
                    lives++;
                    lives--;
                }
                if (type == 2){
                    lives++;
                    lives--;
                }
                if (type == 3){
                    lives++;
                    lives--;
                }
                if (type == 4){
                    lives++;
                }
            }
            powerUps.remove(i);
            i--;

        }*/

    }



    private void movePaddles() {
        for(int i = 0; i < NO_OF_PADDLES;i++){
            paddleArray[i].movePaddle();
        }

    }

    Paddle[] getPaddleArray(){
        return paddleArray;
    }

    void setPaddleArray(int id,int speed,int velocity_x,int velocity_y){
        System.out.println(id);
        paddleArray[id].setPaddleSpeed(speed);
        paddleArray[id].setPaddleVelocity(velocity_x,velocity_y);
    }

    ///////////////Code for updating game state ends////////////////

}
