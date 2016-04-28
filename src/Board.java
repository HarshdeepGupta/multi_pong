import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.Map;
import javax.swing.JPanel;

public class Board extends JPanel implements Runnable {


    private final int DELAY = 25;
    private final int NO_OF_PADDLES = 4;
//    private static int HEIGHT = 575;
//    private static int WIDTH = 920;

    private Thread animator;
    private Paddle[] paddleArray;
    private Ball ball;
    private Bot[] bot_array_multi;
    private Bot[] botarray;
    private int myid;
    private boolean running;
    private int[] specialPaddle = new int[5];
    private boolean freeze;
    private boolean elongate = false;
    private boolean fastPaddleOver = true;
    private boolean fastPaddle = false;
    private boolean live = false;
    private boolean dirChange = false;
    private boolean freezeOver = true;
    private int[] lives = new int[5];
    private int[] speed = new int[5];


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
        setBackground(Color.cyan);
        myid = id;
        setPreferredSize(new Dimension(WIDTH,HEIGHT));
        setFocusable(true);
        requestFocus();
        powerUps = new ArrayList<powerUp>();
        powerCollect = new ArrayList<powerUp>();

        difficult=1;
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

        Graphics2D g2d = (Graphics2D) g;

        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                RenderingHints.VALUE_ANTIALIAS_ON);

        g2d.setRenderingHint(RenderingHints.KEY_RENDERING,
                RenderingHints.VALUE_RENDER_QUALITY);

        g2d.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING,
                RenderingHints.VALUE_TEXT_ANTIALIAS_ON);

        g2d.setTransform(at);



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
                    g2d.setColor(Color.WHITE);
                    g2d.fillOval(20 + (20 * i), 450, 7, 7);
                    g2d.setStroke(new BasicStroke(3));
                    g2d.setColor(Color.WHITE.darker());
                    g2d.fillOval(20 + (20 * i), 450, 7, 7);
                    g2d.setStroke(new BasicStroke(1));
                }
            }
            if (j == 1) {
                for(int i = 0; i < lives[j]; i++){
                    g2d.setColor(Color.WHITE);
                    g2d.fillOval(400 + (20 * i), 50, 7, 7);
                    g2d.setStroke(new BasicStroke(3));
                    g2d.setColor(Color.WHITE.darker());
                    g2d.fillOval(400 + (20 * i), 50, 7, 7);
                    g2d.setStroke(new BasicStroke(1));
                }
            }
            if (j == 2) {
                for(int i = 0; i < lives[j]; i++){
                    g2d.setColor(Color.WHITE);
                    g2d.fillOval(450, 400 + (20 * i), 7, 7);
                    g2d.setStroke(new BasicStroke(3));
                    g2d.setColor(Color.WHITE.darker());
                    g2d.fillOval(450, 400 + (20 * i), 7, 7);
                    g2d.setStroke(new BasicStroke(1));
                }
            }
            if (j == 3) {
                for(int i = 0; i < lives[j]; i++){
                    g2d.setColor(Color.WHITE);
                    g2d.fillOval(50, 20 + (20 * i), 7, 7);
                    g2d.setStroke(new BasicStroke(3));
                    g2d.setColor(Color.WHITE.darker());
                    g2d.fillOval(50, 20 + (20 * i), 7, 7);
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

            //movePaddles();
            //ball.moveBall();
            //checkCollision();

            //repaint();

            if (wait){
                beforeTime = System.currentTimeMillis();
                movePaddles();
                ball.moveBall();
                checkCollision();
                updatePowerUp();
                updatePowerCollect();

                //chance for power ups
                double rand = Math.random();
                double randx = 500*Math.random();
                int x = ((int) randx);
                System.out.println(x);
                double randy = 500*Math.random();
                int y = ((int) randy);
                if (rand < .05 && !fastPaddle) {
                    System.out.println("Here1");
                    fastPaddle = true;
                    powerUps.add(new powerUp(4, x, y, System.currentTimeMillis()));
                }
                else if (rand < 0.01 && !dirChange) {
                    System.out.println("Here4");
                    dirChange = true;
                    powerUps.add(new powerUp(5, x, y, System.currentTimeMillis()));
                }
                else if (rand < 0.02 && !elongate) {
                    System.out.println("Here2");
                    elongate = true;
                    powerUps.add(new powerUp(3, x, y, System.currentTimeMillis()));
                }
                else if (rand < 0.03 && !freeze) {
                    System.out.println("Here3");
                    freeze = true;
                    powerUps.add(new powerUp(2, x, y, System.currentTimeMillis()));
                }
                else if (rand < 0.04 && !live) {
                    System.out.println("Here4");
                    live = true;
                    powerUps.add(new powerUp(1, x, y, System.currentTimeMillis()));
                }

                //update power ups
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
               //update power ups
                for(int i = 0; i < powerUps.size(); i++){
                    boolean remove = powerUps.get(i).update();
                    if (remove){
                        powerUps.remove(i);
                        i--;
                    }
                }

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

            /*for (int i = 0; i < 4; i++) {
                if (lives[i] == 0) {
                    //drawOutPlayer(i);
                    wait = true;
                    waitTimer = 0;
                    waitTimerDiff = 0;
                    running = true;


                    Graphics g2d = this.getGraphics();
                    g2d.setFont(new Font("Century Gothic", Font.PLAIN, 20));

                    //long beforeTime, timeDiff, sleep;

                    while (running) {

                        if (waitTimer == 0 && wait){
                            wait = false;
                            System.out.println(2);
                            waitTimer = System.currentTimeMillis();
                        }
                        else {

                            waitTimerDiff = System.currentTimeMillis() - waitTimer;
                            if (waitTimerDiff < waitDelay){
                                g2d.drawString("HI, you are out of the game", 200, 250);
                            }
                            else {
                                wait = true;
                                waitTimerDiff = 0;
                            }
                        }

                    }
                }
            }*/

        }
    }



    private void checkCollision(){
        Paddle paddle;
        for (int i = 0; i < NO_OF_PADDLES;i++){
            paddle = paddleArray[i];
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
                    System.out.print(ball.last_hit_by);

                }
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
            if (dist < pr + r){
                int type = p.gettype();
                if (type == 1){
                    System.out.println("Here9");
                    lives[ball.last_hit_by-1]++;
                    live = false;
                    specialPaddle[0] = ball.last_hit_by;
                }
                if (type == 2){
                    System.out.println("Here10");
                    long powerEffect = System.currentTimeMillis();
                    powerCollect.add(new powerUp(2, p.getX(), p.getY(), powerEffect));
                    specialPaddle[1] = ball.last_hit_by;
                    freeze = false;
                    freezeOver = false;
                    speed[ball.last_hit_by-1] = paddleArray[ball.last_hit_by-1].getPaddleSpeed();
                }
                if (type == 3){
                    System.out.println("Here11");
                    elongate = false;
                    specialPaddle[2] = ball.last_hit_by;
                    lives[ball.last_hit_by-1]++;
                }
                if (type == 4){
                    System.out.println("Here12");
                    long powerEffect = System.currentTimeMillis();
                    powerCollect.add(new powerUp(4, p.getX(), p.getY(), powerEffect));
                    fastPaddle = false;
                    fastPaddleOver = false;
                    specialPaddle[3] = ball.last_hit_by;
                }
                if (type == 5){
                    System.out.println("Here15");
                    long powerEffect = System.currentTimeMillis();
                    //change ball direction here
                    dirChange = false;
                    specialPaddle[4] = ball.last_hit_by;
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
                powerUps.remove(i);
                i--;
            }
        }
    }

    private void updatePowerCollect(){
        long powerTime, powerDiff, powerDelay;
        powerDelay = 5000;
        for (int i = 0; i < powerCollect.size(); i++){
            powerUp power = powerCollect.get(i);
            powerTime = powerCollect.get(i).getstarttime();
            //powerDiff = 0
            powerDiff = System.currentTimeMillis() - powerTime;
            //code for freezing paddle i
            if (power.gettype() == 2){
                if (!freezeOver) paddleArray[specialPaddle[1]-1].setPaddleSpeed(0);
                if(powerDiff > powerDelay) {
                    paddleArray[specialPaddle[1]-1].setPaddleSpeed(4);
                    freezeOver = true;
                    System.out.println(78374);
                    powerCollect.remove(i);
                    i--;
                }
            }
            if (power.gettype() == 4){
                if (!fastPaddleOver) paddleArray[specialPaddle[3]-1].setPaddleSpeed(8);
                if(powerDiff > powerDelay) {
                    paddleArray[specialPaddle[3]-1].setPaddleSpeed(4);
                    fastPaddleOver = true;
                    System.out.println(65774);
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

    ///////////////Code for updating game state ends////////////////

}
