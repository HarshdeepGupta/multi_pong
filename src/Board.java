import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;

import java.lang.reflect.Array;
import java.awt.image.ImageObserver;
import java.util.ArrayList;
import javax.swing.JPanel;

public class Board extends JPanel implements Runnable{


    private final int DELAY = 25;
    private final int NO_OF_PADDLES = 4;

    private Thread animator;
    private Paddle[] paddleArray;
    private Ball ball;

    private Bot[] bot_array_multi;
    private Bot[] botarray;
    private int myid;

    private boolean running;
    private int[] lives = new int[4];


    private long waitTimer;
    private long waitTimerDiff;
    private boolean wait;
    private int waitDelay = 1000 ;
    public static ArrayList<powerUp> powerUps;
    public static ArrayList<powerUp> powerCollect;
    private boolean single_player;
    private int difficult;
    private int number_of_players;
    //public static ArrayList<powerUp> powerUps;


    private int SET_KEY_LISTENER_ON;
    AffineTransform at = new AffineTransform();


    public Board(int id,boolean single_player,int players){
        this.single_player = single_player;
        this.number_of_players = players;
        initBoard(id);
    }


    private void initBoard(int id) {
        setBackground(Color.DARK_GRAY);
        myid = id;

//        setSize(new Dimension(WIDTH,HEIGHT));
        setFocusable(true);
        requestFocus();
        powerUps = new ArrayList<powerUp>();
        powerCollect = new ArrayList<powerUp>();

        difficult=1;

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
            paddleArray[i] = new Paddle(i+1,1);
        }


        ball = new Ball();

        //Initialize the bot and attach it to a paddle

        //bot = new Bot(ball,1);
        //bot.attach(paddleArray[0]);
        switch (myid){
            case 0:
                at.rotate(0);
                break;
            case 1:
                at.rotate(0);
                break;
            case 2:
                at.rotate(0);
                break;
            case 3:
                at.rotate(0);
                break;
            default:

        }

        if(single_player){
            botarray = new Bot[3];
            for (int i = 0;i<3;i++){
                botarray[i] = new Bot(ball,difficult);
                botarray[i].attach(paddleArray[i+1]);
            }
        }
        else{
            botarray = new Bot[4-1-number_of_players];
            for (int i=0;i<botarray.length;i++){
                botarray[i] = new Bot(ball,difficult);
                botarray[i].attach(paddleArray[number_of_players+i+1]);
            }
        }

        for(int i=0;i<4;i++){
            lives[i]=3;
        }

        bot_array_multi = new Bot[3];// To store dynamically created bots on disconnection
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
        g2d.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BICUBIC);
        g2d.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
        g2d.setRenderingHint(RenderingHints.KEY_STROKE_CONTROL, RenderingHints.VALUE_STROKE_PURE);
        Toolkit.getDefaultToolkit().sync();

        g2d.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING,
                RenderingHints.VALUE_TEXT_ANTIALIAS_ON);

//        g2d.rotate(-Math.PI / 2, getWidth()/2, getHeight()/2);



        drawGameObjects(g2d);

        Toolkit.getDefaultToolkit().sync();

    }


    ///////////////Code for drawing game starts/////////////

    private void drawGameObjects(Graphics2D g2d) {

        drawPaddles(g2d);
        drawBall(g2d);
        drawPowerUps(g2d);
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
        for(int i = 0; i < lives[0]; i++){
            g2d.setColor(Color.WHITE);
            g2d.fillOval(20 + (20 * i), 450, 10, 10);
            g2d.setStroke(new BasicStroke(3));
            g2d.setColor(Color.WHITE.darker());
            g2d.fillOval(20 + (20 * i), 450, 10, 10);
            g2d.setStroke(new BasicStroke(1));
        }

    }

    private void drawPowerUps(Graphics2D g2d){
        for(int i = 0; i < powerUps.size(); i++){
            powerUps.get(i).draw(g2d);
        }
    }

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

            //movePaddles();
            //ball.moveBall();
            //checkCollision();

            //repaint();

            if (wait){
                beforeTime = System.currentTimeMillis();
                checkCollision();
                updatePowerUp();
                updatePowerCollect();
                ball.moveBall();
                movePaddles();


                //chance for power ups
                double rand = Math.random();
                if (rand < .001) {

                    powerUps.add(new powerUp(4, Math.abs(ball.getX() - 20), Math.abs(ball.getY() - 30), System.currentTimeMillis()));
                }
                else if (rand < 0.002) {

                    powerUps.add(new powerUp(3, Math.abs(ball.getX() - 30), Math.abs(ball.getY() - 20), System.currentTimeMillis()));
                }
                else if (rand < 0.003) {

                    powerUps.add(new powerUp(2, Math.abs(ball.getX() - 20), Math.abs(ball.getY() - 20), System.currentTimeMillis()));
                }
                else if (rand < 0.004) {

                    powerUps.add(new powerUp(1, Math.abs(ball.getX() - 30), Math.abs(ball.getY() - 30), System.currentTimeMillis()));
                }


                //update power ups
                for(int i = 0; i < powerUps.size(); i++){
                    boolean remove = powerUps.get(i).update();
                    if (remove){
                        powerUps.remove(i);
                        i--;
                    }
                }


                if(single_player) {
                    for(int i=0;i<3;i++)
                        if (botarray[i].is_attached()) {
                            botarray[i].updateBot();
                        }
                }
                else{
                    for(int i=0;i<botarray.length;i++)
                        if (botarray[i].is_attached()) {
                            botarray[i].updateBot();
                        }
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

            }

        }
    }



    private void checkCollision(){

        //check collision between paddle and wall
        Paddle paddle;
        for (int i = 0; i < NO_OF_PADDLES;i++){
            paddle = paddleArray[i];
            /*
            if(ball.getSquare().intersects(paddle.getRect())){
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
                   // System.out.print(ball.last_hit_by);
                   */
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
//                        System.out.print(ball.last_hit_by);

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


            //detect collision of ball with board walls
            if( ball.wall_hit == 1 ){

            }
            if(ball.wall_hit == 2) {

            }
            if(ball.wall_hit == 3){

            }
            if(ball.wall_hit == 4){

            }
        }

        //power-up ball collision
        int px = ball.position.X;
        int py = ball.position.Y;
        //System
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
            if (dist < pr + r && ball.last_hit_by>0){
                int type = p.gettype();
                if (type == 1){
                    lives[ball.last_hit_by-1]++;
                }
                if (type == 2){
                    long powerEffect = System.currentTimeMillis();
                    powerCollect.add(new powerUp(2, p.getX(), p.getY(), powerEffect));
                    //code for freezing paddle i
                }
                if (type == 3){
                    lives[ball.last_hit_by-1]++;
                }
                if (type == 4){

                    lives[ball.last_hit_by-1]++;
                }

                powerUps.remove(i);
                i--;
            }

        }

    }

    private void updatePowerUp(){
        long powerTime, powerDiff, powerDelay;
        powerDelay = 10000;
        for (int i = 0; i < powerUps.size(); i++){
            powerTime = powerUps.get(i).getstarttime();
            //powerDiff = 0
            powerDiff = System.currentTimeMillis() - powerTime;
            if(powerDiff > powerDelay) {
                powerUps.remove(i);
                i--;
            }
        }
    }

    private void updatePowerCollect(){
        long powerTime, powerDiff, powerDelay;
        powerDelay = 5000;
        for (int i = 0; i < powerCollect.size(); i++){
            powerTime = powerCollect.get(i).getstarttime();
            //powerDiff = 0
            powerDiff = System.currentTimeMillis() - powerTime;
            if(powerDiff > powerDelay) {
                powerCollect.remove(i);
                i--;
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

        paddleArray[id].setPaddleSpeed(speed);
        paddleArray[id].setPaddleVelocity(velocity_x,velocity_y);
    }

    void setSET_KEY_LISTENER_ON(int set_key_listener_on){
        SET_KEY_LISTENER_ON = set_key_listener_on+1;
    }

    Ball getball(){
        return ball;
    }

    public void setMyid(int id){
        myid = id;
    }

    public int getMyid(){
        return myid;
    }

    public void setDifficult(int level){
        difficult = level;
    }

    public int getDifficult(){
        return difficult;
    }

    public void setNumber_of_players(int players){
        this.number_of_players = players;
    }

    public int getNumber_of_players(){
        return number_of_players;
    }

    public void add_bot(int id){
        Bot[] new_array  = new Bot[botarray.length+1];
        for (int i=0;i<botarray.length;i++){
            new_array[i] = botarray[i];
        }
        new_array[botarray.length] = new Bot(ball,difficult);
        new_array[botarray.length].attach(paddleArray[id]);
        botarray = new_array;

    }

    public void reduce_lives(int id){
        lives[id] = lives[id]-1;
    }

    public ArrayList<powerUp> getPowerUps(){
        return powerUps;
    }

    ///////////////Code for updating game state ends////////////////

}
