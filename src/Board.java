import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;

import java.io.IOException;
import java.lang.reflect.Array;
import java.awt.image.ImageObserver;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.util.ArrayList;
import java.util.Map;
import javax.swing.*;

public class Board extends JPanel implements Runnable, Commons{


    private final int DELAY = 25;
    private final int NO_OF_PADDLES = 4;

    private Thread animator;
    private Paddle[] paddleArray;
    private Ball ball;

    private Bot[] bot_array_multi;
    private Bot[] botarray;
    private int myid;

    private boolean running;
    private int[] specialPaddle = new int[6];
    private boolean freeze;
    private boolean elongate = false;
    private boolean elongateOver = true;
    private boolean fastPaddleOver = true;
    private boolean fastPaddle = false;
    private boolean live = false;
    private boolean blackbox = false;
    private boolean dirChange = false;
    private boolean freezeOver = true;
    private int[] lives = new int[4];
    private boolean[] hold = new boolean[4];
    private int[] speed = new int[4];
    private long[] holdTimer = new long[4];

    private int playersOut = 0;
    private long waitTimer;
    private long waitTimerDiff;
    private boolean wait;
    private boolean winner;
    private int waitDelay = 1000 ;
    public static ArrayList<powerUp> powerUps;
    public static ArrayList<powerUp> powerCollect;
    private boolean single_player;
    private int difficult;
    private int number_of_players;
    private boolean is_host;
    private boolean power_packet_sent[];
    private boolean all_packets_sent;
    private boolean all_players_disconnect=false;
    private boolean[] disconnected_machines = new boolean[4];
    private int global_type;
    private int global_x;
    private int global_y;
    private long global_time_stamp;
    //public static ArrayList<powerUp> powerUps;


    private int SET_KEY_LISTENER_ON;
    AffineTransform at = new AffineTransform();


    //For Networking from this class

    DatagramSocket socket1;
    String[] ip_array;
    int[] port_array;
    String my_ip;

    public void setNetwork(DatagramSocket socket1,int number_of_players,int[] port_array,int myid,String my_ip,String[] ip_array) {
        this.socket1 = socket1;
        this.number_of_players = number_of_players;
        this.port_array = port_array;
        this.myid = myid;
        this.my_ip = my_ip;
        this.ip_array = ip_array;
        power_packet_sent = new boolean[number_of_players+1];
        for(int i=0;i<=number_of_players;i++){
            if(i==myid){
                power_packet_sent[i] = true;
            }
            else{
                power_packet_sent[i] = false;
            }
        }
        for(int i=0;i<4;i++){
            disconnected_machines[i] = false;
        }
        //send_power_packet();
    }



    public Board(int id,boolean single_player,int players){
        this.single_player = single_player;
        this.number_of_players = players;
        initBoard(id);
        for (int j = 0; j < 4; j++){
            hold[j] = true;
            holdTimer[j] = 0;
        }
        winner = false;
    }


    private void initBoard(int id) {

        setBackground(Color.DARK_GRAY);
        myid = id;

//        setSize(new Dimension(WIDTH,HEIGHT));
        setFocusable(true);
        requestFocus();
        powerUps = new ArrayList<powerUp>();
        powerCollect = new ArrayList<powerUp>();

        //difficult=getDifficult();
        difficult = 2;
        freeze = false;

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

        switch (myid){
            case 0:
                g2d.rotate(0, getWidth()/2, getHeight()/2);
                break;
            case 1:
                g2d.rotate(Math.PI, getWidth()/2, getHeight()/2);
                break;
            case 2:
                g2d.rotate((Math.PI )/ 2, getWidth()/2, getHeight()/2);
                break;
            case 3:
                g2d.rotate((-1*Math.PI) / 2, getWidth()/2, getHeight()/2);
                break;
            default:

        }

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
        for (int j = 0; j < 4; j++) {
            if (j == 0){
                for(int i = 0; i < lives[j]; i++){
                    g2d.setColor(Color.GREEN);
                    g2d.fillOval(20 + (12 * i), paddleArray[j].getY()-30, 7, 7);
                    g2d.setStroke(new BasicStroke(3));
                    g2d.setColor(Color.GREEN.darker());
                    g2d.fillOval(20 + (12 * i), paddleArray[j].getY()-30, 7, 7);
                    g2d.setStroke(new BasicStroke(1));
                }
            }
            if (j == 1) {
                for(int i = 0; i < lives[j]; i++){
                    g2d.setColor(Color.RED);
                    g2d.fillOval(430 + (12 * i), 30, 7, 7);
                    g2d.setStroke(new BasicStroke(3));
                    g2d.setColor(Color.RED.darker());
                    g2d.fillOval(430 + (12 * i), 30, 7, 7);
                    g2d.setStroke(new BasicStroke(1));
                }
            }
            if (j == 2) {
                for(int i = 0; i < lives[3]; i++){
                    g2d.setColor(Color.YELLOW);
                    g2d.fillOval(480, 430 + (12 * i), 7, 7);
                    g2d.setStroke(new BasicStroke(3));
                    g2d.setColor(Color.YELLOW.darker());
                    g2d.fillOval(480, 430 + (12 * i), 7, 7);
                    g2d.setStroke(new BasicStroke(1));
                }
            }
            if (j == 3) {
                for(int i = 0; i < lives[2]; i++){
                    g2d.setColor(Color.MAGENTA);
                    g2d.fillOval(30, 20 + (12 * i), 7, 7);
                    g2d.setStroke(new BasicStroke(3));
                    g2d.setColor(Color.MAGENTA.darker());
                    g2d.fillOval(30, 20 + (12 * i), 7, 7);
                    g2d.setStroke(new BasicStroke(1));
                }
            }

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
        int transparency = (int) (255 * Math.exp(- 3.14 * (waitTimerDiff-d)/waitDelay));
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

            if (waitTimer == 0 && wait) {
                wait = false;

                waitTimer = System.currentTimeMillis();
            } else {

                waitTimerDiff = System.currentTimeMillis() - waitTimer;
                if (waitTimerDiff < waitDelay) {
                    drawText(3, 0);
                } else if (waitTimerDiff > waitDelay && waitTimerDiff < 2 * waitDelay) {
                    //System.out.println(89);
                    drawText(2, waitDelay);
                } else if (waitTimerDiff > 2 * waitDelay && waitTimerDiff < 3 * waitDelay) {
                    drawText(1, 2 * waitDelay);
                } else {
                    wait = true;
                    waitTimerDiff = 0;
                }
            }

            //movePaddles();
            //ball.moveBall();
            //checkCollision();

            //repaint();

            //remove losing playes
            if (wait) {
                beforeTime = System.currentTimeMillis();
                checkCollision();
                updatePowerUp();
                updatePowerCollect();
                ball.moveBall();
                movePaddles();

                Graphics g2d = this.getGraphics();
                g2d.setColor(Color.WHITE);
                g2d.setFont(new Font("Century Gothic", Font.PLAIN, 20));
                //losing players
                for (int i = 0; i < 4; i++) {
                    if (lives[i] == 0 && hold[i]) {
                        //drawOutPlayer(i);
                        playersOut++;
                        paddleArray[i].setHeight(0);
                        paddleArray[i].setWidth(0);
                        long holdTimerDiff;
                        long holdDelay = 1000;
                        System.out.println("HI, Player" + (i + 1) + " is out of the game");
                        if (holdTimer[i] == 0) {
                            hold[i] = false;
                            System.out.println(2);
                            holdTimer[i] = System.currentTimeMillis();
                        }
                        holdTimerDiff = System.currentTimeMillis() - holdTimer[i];
                        while (holdTimerDiff < holdDelay) {
                            holdTimerDiff = System.currentTimeMillis() - holdTimer[i];
                            g2d.drawString("Player" + (i + 1) + " is out of the game", 140, 250);
                        }
                        while (holdTimerDiff < 2 * holdDelay) {
                            holdTimerDiff = System.currentTimeMillis() - holdTimer[i];
                        }
                    }
                }

                //chance for power ups
                double rand = Math.random();
                double randx = 0.90 * getWidth() * Math.random();
                int x = ((int) randx);
                double randy = 0.90 * getHeight() * Math.random();
                int y = ((int) randy);

                if (is_host && number_of_players == 0) {
                    if (rand < .005 && !fastPaddle) {
                        fastPaddle = true;
                        long time = System.currentTimeMillis();
                        powerUps.add(new powerUp(4, x, y, time));
                        global_type = 4;
                        global_x = x;
                        global_y = y;
                        global_time_stamp = time;
                    } else if (rand < 0.001 && !dirChange) {
                        dirChange = true;
                        long time = System.currentTimeMillis();
                        powerUps.add(new powerUp(5, x, y, time));
                        global_type = 5;
                        global_x = x;
                        global_y = y;
                        global_time_stamp = time;
                    } else if (rand < 0.0015 && !elongate) {
                        elongate = true;
                        long time = System.currentTimeMillis();
                        powerUps.add(new powerUp(3, x, y, time));
                        global_type = 3;
                        global_x = x;
                        global_y = y;
                        global_time_stamp = time;
                    } else if (rand < 0.002 && !freeze) {
                        freeze = true;
                        long time = System.currentTimeMillis();
                        powerUps.add(new powerUp(2, x, y, time));
                        global_type = 2;
                        global_x = x;
                        global_y = y;
                        global_time_stamp = time;
                    } else if (rand < 0.0025 && !live) {
                        live = true;
                        long time = System.currentTimeMillis();
                        powerUps.add(new powerUp(1, x, y, time));
                        global_type = 1;
                        global_x = x;
                        global_y = y;
                        global_time_stamp = time;
                    }
                    else if (rand < 0.003 && !live) {
                        long time = System.currentTimeMillis();
                        blackbox = true;
                        powerUps.add(new powerUp(6, x, y, System.currentTimeMillis()));
                        global_type = 6;
                        global_x = x;
                        global_y = y;
                        global_time_stamp = time;
                    }


                    //update power ups
                    for (int i = 0; i < powerUps.size(); i++) {
                        boolean remove = powerUps.get(i).update();
                        if (remove) {
                            powerUps.remove(i);
                            i--;
                        }
                    }


                    if (single_player) {
                        for (int i = 0; i < 3; i++)
                            if (botarray[i].is_attached()) {
                                botarray[i].updateBot();
                            }
                    } else {
                        for (int i = 0; i < botarray.length; i++)
                            if (botarray[i].is_attached()) {
                                botarray[i].updateBot();
                            }
                    }

                    if (playersOut == 3 && !winner) {
                        //game over declare the remaining player as winner
                        long winnerTimerDiff;
                        long winnerDelay = 3000;
                        long winnerTimer = System.currentTimeMillis();
                        winnerTimerDiff = System.currentTimeMillis() - winnerTimer;
                        for (int k = 0; k < 4; k++) {
                            if (lives[k] > 0) {
                                g2d.setColor(Color.WHITE);
                                g2d.setFont(new Font("Century Gothic", Font.PLAIN, 20));
                                System.out.println("HI, Player" + (k + 1) + " is the winner");

//                            while (winnerTimerDiff < 1.5*winnerDelay) {
//                                winnerTimerDiff = System.currentTimeMillis() - winnerTimer;
//                                g.drawString(" ", 170, 250);
//                            }
                                while (winnerTimerDiff < winnerDelay) {
                                    winnerTimerDiff = System.currentTimeMillis() - winnerTimer;
                                    g2d.drawString("Player" + (k + 1) + " is the winner", 170, 250);
                                    winner = true;
                                }
                                g2d.drawString(" ", 170, 250);
                            }
                        }
                        rematch();
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
    }

    private void rematch(){
        long winnerTimerDiff;
        long winnerDelay = 1000;
        long winnerTimer = System.currentTimeMillis();
        winnerTimerDiff = System.currentTimeMillis() - winnerTimer;
        Graphics g = this.getGraphics();
        g.setColor(Color.WHITE);
        g.setFont(new Font("Century Gothic", Font.PLAIN, 20));
        while (winnerTimerDiff < 5*winnerDelay) {
            winnerTimerDiff = System.currentTimeMillis() - winnerTimer;
            g.drawString("Rematch starts in 5 sec", 180, 250);
        }

        winner = false;
        playersOut = 0;
        for (int j = 0; j < 4; j++){
            hold[j] = true;
            lives[j] = 3;
            if (paddleArray[j].getMove_x()) paddleArray[j].setWidth(60);
            if (paddleArray[j].getMove_y()) paddleArray[j].setWidth(5);
            if (paddleArray[j].getMove_y()) paddleArray[j].setHeight(60);
            if (paddleArray[j].getMove_x()) paddleArray[j].setHeight(5);
            if (j == 0) {
                paddleArray[j].setX(INIT_PADDLE1_X);
                paddleArray[j].setY(INIT_PADDLE1_Y);
            }
            if (j == 1) {
                paddleArray[j].setX(INIT_PADDLE2_X);
                paddleArray[j].setY(INIT_PADDLE2_Y);
            }
            if (j == 2) {
                paddleArray[j].setX(INIT_PADDLE3_X);
                paddleArray[j].setY(INIT_PADDLE3_Y);
            }
            if (j == 3) {
                paddleArray[j].setX(INIT_PADDLE4_X);
                paddleArray[j].setY(INIT_PADDLE4_Y);
            }
        }
        ball.setX(250);
        ball.setY(250);
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

//            //Collision detection for circular paddle
//            else if (paddle.getType() == 2){
//                //Collision detection for circular paddle
//                //Get the centers of the ball and the circular paddle
//                Vector2D c1 = ball.getCenter();
//                Vector2D c2 = paddle.getCenter();
//                int r1 = ball.getRadius();
//                int r2 = ball.getRadius();
//                //Compute the distance between the centers
//                double dist = c1.dist(c2);
////                System.out.println(Math.abs( dist - (r1+r2)) );
//                //if collision happens, then get the components of velocity along the normal and the tangent
//                if(Math.abs( dist - (r1+r2)) <= 2){
//                    System.out.println("Collsion detected");
//                    //for this, get the normal of the paddle at the point of collision
//                    //the normal is along the line joining the centers of the two circles
//
//                }
//            }


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
        int pr  = ball.getRadius();
        for (int i = 0; i < powerUps.size(); i++){
            powerUp p = powerUps.get(i);
            double x = p.getx();
            double y = p.gety();
            double r = p.getr();
            double dx = px - x;
            double dy = py - y;
            double dist = Math.sqrt(dx * dx + dy * dy);

            int id = ball.last_hit_by;
            //System.out.println(ball.last_hit_by);

            //collected power-up
            if (dist < pr + r){
                //System.out.println("Last Hit by  ---- " + ball.last_hit_by);
                if (id > 0) {
                    int type = p.gettype();
                    if (type == 1) {
                        //System.out.println("Here11");
                        live = false;
                        specialPaddle[0] = id;
                        if (lives[id - 1] < 6) lives[id - 1]++;
                    }
                    if (type == 2) {
                        //System.out.println("Here12");
                        long powerEffect = System.currentTimeMillis();
                        powerCollect.add(new powerUp(2, p.getX(), p.getY(), powerEffect));
                        specialPaddle[1] = id;
                        freeze = false;
                        freezeOver = false;
                        speed[id - 1] = 4;
                    }
                    if (type == 3) {
                        //System.out.println("Here13");
                        elongate = false;
                        elongateOver = false;
                        specialPaddle[2] = id;
                        long powerEffect = System.currentTimeMillis();
                        powerCollect.add(new powerUp(3, p.getX(), p.getY(), powerEffect));
                    }
                    if (type == 4) {

                        //System.out.println("Here14");
                        long powerEffect = System.currentTimeMillis();
                        powerCollect.add(new powerUp(4, p.getX(), p.getY(), powerEffect));
                        fastPaddle = false;
                        fastPaddleOver = false;
                        specialPaddle[3] = id;
                    }
                    if (type == 5) {
                        //System.out.println("Here15");
                        long powerEffect = System.currentTimeMillis();
                        //change ball direction here
                        double number = 3 * Math.random();
                        int choice = ((int) number);
                        switch (choice){
                            case 0:
                                ball.setBallVelocity(ball.ballVelocity.X * -1,ball.ballVelocity.Y * 1);
                                break;
                            case 1:
                                ball.setBallVelocity(ball.ballVelocity.X * 1,ball.ballVelocity.Y * -1);
                                break;
                            case 2:
                                ball.setBallVelocity(ball.ballVelocity.X * -1,ball.ballVelocity.Y * -1);
                                break;
                            default:
                                ball.setBallVelocity(ball.ballVelocity.X * -1,ball.ballVelocity.Y * -1);
                                break;

                        }
                        dirChange = false;
                        specialPaddle[4] = id;
                    }
                    if (type == 6) {
                        //System.out.println("Here16");
                        long powerEffect = System.currentTimeMillis();
                        //change ball position here
                        ball.setX(p.getY());
                        ball.setY(p.getX());
                        blackbox = false;
                        specialPaddle[5] = id;
                    }
                    System.out.println("Power Up " +p.gettype()+ " to " +id);
                    System.out.println("Last Hit by  ---- " + ball.last_hit_by);
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
            powerDiff = System.currentTimeMillis() - powerTime;
            if(powerDiff > powerDelay) {
                if(powerUps.get(i).gettype() == 1) live = false;
                if(powerUps.get(i).gettype() == 2) freeze = false;
                if(powerUps.get(i).gettype() == 3) elongate = false;
                if(powerUps.get(i).gettype() == 4) fastPaddle = false;
                if(powerUps.get(i).gettype() == 5) dirChange = false;
                if(powerUps.get(i).gettype() == 6) blackbox = false;
                powerUps.remove(i);
                i--;
            }
        }
    }

    private void updatePowerCollect(){
        long powerTime, powerDiff, powerDelay;
        for (int i = 0; i < powerCollect.size(); i++){
            powerUp power = powerCollect.get(i);
            powerTime = powerCollect.get(i).getstarttime();
            powerDiff = System.currentTimeMillis() - powerTime;
            //code for freezing paddle i
            if (power.gettype() == 2){
                Color original = paddleArray[specialPaddle[1]-1].getPaddleColor();
                if (!freezeOver) {
                    paddleArray[specialPaddle[1]-1].setPaddleSpeed(0);
                    paddleArray[specialPaddle[1]-1].setPaddleColor(new Color(0, 245, 255, 240));
                }
                powerDelay = 5000;
                if(powerDiff > powerDelay) {
                    paddleArray[specialPaddle[1]-1].setPaddleSpeed(4);
                    paddleArray[specialPaddle[1]-1].setPaddleColor(original);
                    freezeOver = true;
                    //System.out.println(78374);
                    powerCollect.remove(i);
                    i--;
                }
            }
            if (power.gettype() == 3){
                if (!elongateOver) {
                    if (paddleArray[specialPaddle[2]-1].getMove_x()) paddleArray[specialPaddle[2]-1].setWidth(120);
                    if (paddleArray[specialPaddle[2]-1].getMove_y()) paddleArray[specialPaddle[2]-1].setHeight(120);
                }
                powerDelay = 10000;
                if(powerDiff > powerDelay) {
                    if (paddleArray[specialPaddle[2]-1].getMove_x()) paddleArray[specialPaddle[2]-1].setWidth(60);
                    if (paddleArray[specialPaddle[2]-1].getMove_y()) paddleArray[specialPaddle[2]-1].setHeight(60);
                    elongateOver = true;
                    //System.out.println(65774);
                    powerCollect.remove(i);
                    i--;
                }
            }
            if (power.gettype() == 4){
                Color original = paddleArray[specialPaddle[3]-1].getPaddleColor();
                powerDelay = 10000;
                if (!fastPaddleOver) {
                    paddleArray[specialPaddle[3]-1].setPaddleSpeed(8);
                    paddleArray[specialPaddle[3]-1].setPaddleColor(new Color(255, 69, 0, 240));
                }
                if(powerDiff > powerDelay) {
                    //TODO Error Here
                    paddleArray[specialPaddle[3]-1].setPaddleSpeed(4);
                    paddleArray[specialPaddle[3]-1].setPaddleColor(original);
                    fastPaddleOver = true;
                    //System.out.println(65774);
                    powerCollect.remove(i);
                    i--;
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

    public void setIs_host(boolean is_host){
        this.is_host  = is_host;
    }

    public void add_powerup(int type,int x,int y,long time_stamp){
        powerUps.add(new powerUp(type,x,y,time_stamp));
    }

    public void send_power_packet(){
        final Thread listen_to_ack = new Thread(new Runnable() {
            @Override
            public void run() {
                System.out.println(all_packets_sent);
        try{
            //System.out.println("time".concat(String.valueOf(System.currentTimeMillis())));
            //if(this.wall_hit!=0){

            while(all_packets_sent==false && number_of_players!=0 && all_players_disconnect==false){
                byte[] buf = new byte[256];
                String temp = "8#my_ip=";
                String my_id = temp.concat(my_ip).concat("#my_id=").concat(String.valueOf(myid))
                        .concat("#type=").concat(String.valueOf(global_type))
                        .concat("#x_position=").concat(String.valueOf(global_x))
                        .concat("#y_position=").concat(String.valueOf(global_y))
                        .concat("#time_stamp=").concat(String.valueOf(global_time_stamp))
                        .concat("#time=")
                        .concat(String.valueOf(java.lang.System.currentTimeMillis())).concat("#");
                buf = my_id.getBytes();// here we want our ip-address instead

                for (int i = 0; i <= number_of_players; i++) {
                    if (i != myid) {
                        System.out.println("sent".concat(my_id));
                        InetAddress address = InetAddress.getByName(ip_array[i]);
                        DatagramPacket packet = new DatagramPacket(buf, buf.length, address, port_array[i]);
                        socket1.send(packet);
                    }
                }
                try {
                    Thread.sleep(50);
                } catch (InterruptedException e) {
                    System.out.println("Interrupted: " + e.getMessage());
                }
                //}
            }
            all_packets_sent = false;
            System.out.println("getting here");
        }
        catch (IOException e) {

        }
            }
    });
        listen_to_ack.start();
    }

    public void setPower_packet_sent(boolean x,int id){
        power_packet_sent[id] = x;
        boolean temp = true;
        for(int i=0;i<number_of_players;i++){
            temp = temp && power_packet_sent[i];
        }
        if(temp==true){
            System.out.println("all_packets_sent".concat(String.valueOf(all_packets_sent)));
            all_packets_sent = true;
            System.out.println("all_packets_sent".concat(String.valueOf(all_packets_sent)));
            for(int i=0;i<=number_of_players;i++){
                if(i==myid || disconnected_machines[i]==true){
                    power_packet_sent[i] = true;
                }
                else{
                    power_packet_sent[i] = false;
                }
            }
        }
        else{
            all_packets_sent = false;
        }
    }


    public void disconnect(int id){
        for(int i=0;i<=number_of_players;i++){
            if(i==myid || i==id){
                disconnected_machines[i] = true;
            }
        }
        boolean temp  = true;
        for(int i=0;i<=number_of_players;i++){
            temp  = temp && disconnected_machines[i];
        }
        if(temp){
            all_players_disconnect = true;
        }
        else{
            all_players_disconnect = false;
        }
    }



}
