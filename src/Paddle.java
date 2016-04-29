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
    protected Color paddleColor;

    //move_x = true means paddle can move only in x direction
    protected boolean move_x, move_y,isBot;

    /*Paddle ID 1 is at the bottom edge
    Paddle ID 2 is at the left edge
    Paddle ID 3 is at the top edge
    Paddle ID 4 is at the right edge
    */
    protected int paddleSpeed;
    public Paddle(int paddleID){


        paddleVelocity = new Vector2D();
        position = new Vector2D();
        normal = new Vector2D();
        score = 0;
        this.paddleID = paddleID;
        move_x = false;
        move_y = false;
        resetState();
        isBot = false;
        paddleSpeed = 4;


    }
    public void draw(Graphics2D g2d){
        g2d.drawRect(position.X,position.Y,width,height);
        g2d.setColor(paddleColor.brighter());
        g2d.fillRect(position.X,position.Y,width,height);
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
            width = PADDLE_WIDTH;
            height = PADDLE_HEIGHT;
            paddleColor = Color.green;

        }
        else if(paddleID == 2){
            //Initialize the position on the board
            position.X = INIT_PADDLE2_X;
            position.Y = INIT_PADDLE2_Y;
            //Initialize the normal vector of the paddle
            normal.X = 0;
            normal.Y = -1;
            move_x = true;
            width = PADDLE_WIDTH;
            height = PADDLE_HEIGHT;
            paddleColor = Color.red;
        }

        else if(paddleID ==3){
            //Initialize the position on the board
            position.X = INIT_PADDLE3_X;
            position.Y = INIT_PADDLE3_Y;
            //Initialize the normal vector of the paddle
            normal.X = 1;
            normal.Y = 0;
            move_y = true;
            width = PADDLE_HEIGHT;
            height = PADDLE_WIDTH;
            paddleColor = Color.yellow;
        }

        else if(paddleID ==4){
            //Initialize the position on the board
            position.X = INIT_PADDLE4_X;
            position.Y = INIT_PADDLE4_Y;
            //Initialize the normal vector of the paddle
            normal.X = -1;
            normal.Y = 0;
            move_y = true;
            width = PADDLE_HEIGHT;
            height = PADDLE_WIDTH;
            paddleColor = Color.MAGENTA;
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

    public void setPaddleColor(Color color){
        paddleColor = color;
    }

    public Color getPaddleColor(){
        if (paddleID == 1) return Color.green;
        else if (paddleID == 2) return Color.red;
        else if (paddleID == 3) return Color.yellow;
        else return Color.MAGENTA;
    }

    public void setPaddleVelocity(int x,int y){
        paddleVelocity.X = x;
        paddleVelocity.Y = y;
    }

    public void setBot(boolean bot){
        this.isBot = bot;
    }
    public boolean getIsBot() {
        return isBot;
    }
    public boolean getMove_x() {
        return move_x;
    }
    public boolean getMove_y() {
        return move_y;
    }

}
