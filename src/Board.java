import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.geom.AffineTransform;
import javax.swing.JPanel;

public class Board extends JPanel implements Runnable {


    private final int DELAY = 25;
    private final int NO_OF_PADDLES = 4;

    private Thread animator;
    private Paddle[] paddleArray;
    private Ball ball;
    private Bot bot;
    private int myid;
    private int SET_KEY_LISTENER_ON;
    AffineTransform at = new AffineTransform();


    public Board(int id){
        initBoard(id);
    }

    private void initBoard(int id) {
        setBackground(Color.BLACK);
        myid = id;
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
            paddleArray[i] = new Paddle(i+1);
        }

        ball = new Ball();

        //Initialize the bot and attach it to a paddle
        bot = new Bot(ball,1);
        //bot.attach(paddleArray[0]);
        switch (myid){
            case 0:
                at.rotate(0);
                break;
            case 1:
                at.rotate(Math.PI);
                break;
            case 2:
                at.rotate(Math.PI/2);
                break;
            case 3:
                at.rotate((3*Math.PI)/2);
                break;
            default:

        }
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


        g2d.setTransform(at);
        drawGameObjects(g2d);
        Toolkit.getDefaultToolkit().sync();



    }


    ///////////////Code for drawing game starts/////////////

    private void drawGameObjects(Graphics2D g2d) {

        drawPaddles(g2d);
        drawBall(g2d);

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

        long beforeTime, timeDiff, sleep;

        beforeTime = System.currentTimeMillis();

        while (true) {


            movePaddles();
            ball.moveBall();
            checkCollision();
            if(bot.is_attached()) {
                bot.updateBot();
            }
            repaint();

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
//                    int vel_X = ball.ballVelocity.X;
//                    int vel_Y = ball.ballVelocity.Y;
//                    vel_X = vel_X - 2*(ball.ballVelocity.dot(paddle.normal));
//                    vel_Y = vel_Y - 2*(ball.ballVelocity.dot(paddle.normal));
//                    ball.ballVelocity.X = vel_X;
//                    ball.ballVelocity.Y = vel_Y;
                    Vector2D vel = ball.ballVelocity;
                    vel = vel.sub( paddle.normal.scalarMult(2*(vel.dot(paddle.normal))));
                    ball.ballVelocity = vel;
                    ball.ballVelocity.add(paddle.paddleVelocity);
                    //also update the last_hit_by
                    ball.last_hit_by = paddle.paddleID;
                    System.out.print(ball.last_hit_by);

                }
            }

        }

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

    void setSET_KEY_LISTENER_ON(int set_key_listener_on){
        SET_KEY_LISTENER_ON = set_key_listener_on+1;
    }

    Ball getball(){
        return ball;
    }

    Bot getBot(){
        return bot;
    }

    public void setMyid(int id){
        myid = id;
    }

    public int getMyid(){
        return myid;
    }

    ///////////////Code for updating game state ends////////////////

}
