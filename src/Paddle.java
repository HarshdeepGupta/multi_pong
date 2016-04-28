import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;

/**
 * Created by hd on 14/4/16.
 */

public class Paddle extends Sprite implements Commons {

    protected Vector2D paddleVelocity;
    protected Vector2D normal;
    protected int paddleID;
    protected int score;

    private int radius;

    //move_x = true means paddle can move only in x direction
    protected boolean move_x, move_y,isBot;

    /*Paddle ID 1 is at the bottom edge
    Paddle ID 2 is at the left edge
    Paddle ID 3 is at the top edge
    Paddle ID 4 is at the right edge
    */
    protected int paddleSpeed;
    private int type;

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public Paddle(int paddleID, int type){


        paddleVelocity = new Vector2D();
        position = new Vector2D();
        normal = new Vector2D();
        score = 0;

        this.paddleID = paddleID;
        move_x = false;
        move_y = false;
        this.type = type;
        radius=30;
        resetState();
        isBot = false;
        paddleSpeed = 4;


    }
    public void draw(Graphics2D g2d){
        if(type == 1){
            g2d.drawRect(position.X,position.Y,width,height);
            g2d.setColor(Color.green.brighter());
            g2d.fillRect(position.X,position.Y,width,height);

        }
        if(type == 2){

            g2d.setColor(Color.cyan);
            g2d.setStroke(new BasicStroke(3));
            g2d.drawOval(this.getX(),this.getY()-radius ,2*radius,2*radius);
            g2d.setStroke(new BasicStroke(1));
        }


    }

    public void movePaddle(){

        if(paddleID==1||paddleID==2){
            int X = position.X;
            X += paddleVelocity.X;
            if (X <= 0) {
                X = 0;
            }

            if (X >= WIDTH - width ) {
                X = WIDTH - width ;
            }
            position.X = X;
        }
        if(paddleID==3||paddleID==4){
            int Y = position.Y;
            Y += paddleVelocity.Y;
            if (Y <= 0) {
                Y = 0;
            }

            if (Y >= HEIGHT-width) {
                Y = HEIGHT-width;
            }
            position.Y = Y;
        }



    }

    public void keyPressed(KeyEvent e) {

        int key = e.getKeyCode();
        if(paddleID==1||paddleID==2){
            if (key == KeyEvent.VK_LEFT) {
                paddleVelocity.X = -paddleSpeed;
            }

            if (key == KeyEvent.VK_RIGHT) {
                paddleVelocity.X = paddleSpeed;
            }

        }
        if(paddleID==3||paddleID==4){
            if (key == KeyEvent.VK_LEFT) {
                paddleVelocity.Y = -paddleSpeed;
            }

            if (key == KeyEvent.VK_RIGHT) {
                paddleVelocity.Y = paddleSpeed;
            }

        }


    }

    public void keyReleased(KeyEvent e) {

        int key = e.getKeyCode();
        if (key == KeyEvent.VK_LEFT||key == KeyEvent.VK_RIGHT) {
            paddleVelocity.X = 0;
            paddleVelocity.Y = 0;
        }
    }


    private void resetState() {
        if(paddleID ==1){
            //Initialize the position on the board
            position.X = INIT_PADDLE1_X;
            position.Y = INIT_PADDLE1_Y;
            //Initialize the normal vector of the paddle
            normal.X = 0;//0
            normal.Y = 1;//1
            move_x = true;
            width = 2*radius;
            height = 5;

        }
        else if(paddleID == 2){
            //Initialize the position on the board
            position.X = INIT_PADDLE2_X;
            position.Y = INIT_PADDLE2_Y;
            //Initialize the normal vector of the paddle
            normal.X = 0;
            normal.Y = -1;
            move_x = true;
            width = 2*radius;
            height = 5;
        }

        else if(paddleID ==3){
            //Initialize the position on the board
            position.X = INIT_PADDLE3_X;
            position.Y = INIT_PADDLE3_Y;
            //Initialize the normal vector of the paddle
            normal.X = 1;
            normal.Y = 0;
            move_y = true;
            width = 5;
            height = 2*radius;
        }

        else if(paddleID ==4){
            //Initialize the position on the board
            position.X = INIT_PADDLE4_X;
            position.Y = INIT_PADDLE4_Y;
            //Initialize the normal vector of the paddle
            normal.X = -1;
            normal.Y = 0;
            move_y = true;
            width = 5;
            height = 2*radius;
        }
        else{
            System.out.println("Error in assigning paddleID");
        }

    }

    public Vector2D getPaddleVelocity(){
        return paddleVelocity;
    }

    public int getPaddleSpeed(){
        return paddleSpeed;
    }

    public void setPaddleSpeed(int speed){
        paddleSpeed = speed;
    }

    public void setPaddleVelocity(int x,int y){
        paddleVelocity.X = x;
        paddleVelocity.Y = y;
    }


    public Vector2D getCenter() {
        return new Vector2D(position.X + radius, position.Y + radius);
    }

    public void setBot(boolean bot){
        this.isBot = bot;
    }
    public boolean getIsBot() {
        return isBot;
    }

}
