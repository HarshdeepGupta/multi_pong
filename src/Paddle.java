import javax.swing.*;
import java.awt.event.KeyEvent;

/**
 * Created by hd on 14/4/16.
 */

public class Paddle extends Sprite implements Commons {

    protected Vector2D paddleVelocity;
    Vector2D normal;
    private int paddleID;
    /*Paddle ID 1 is at the bottom edge, new :- top edge
    Paddle ID 2 is at the top edge, new :- right edge
    Paddle ID 3 is at the left edge, new :- left edge
    Paddle ID 4 is at the right edge, new :- bottom edge
    */
    private int paddleSpeed;
    public Paddle(int paddleID){


        paddleVelocity = new Vector2D();
        position = new Vector2D();
        normal = new Vector2D();

        this.paddleID = paddleID;
        resetState();


        width = image.getWidth(null);
        height = image.getHeight(null);
        paddleSpeed = 4;

    }

    public void movePaddle(){

        if(paddleID==1||paddleID==4){
            int X = position.X;
            X += paddleVelocity.X;
            if (X <= 0) {
                X = 0;
            }

            if (X >= WIDTH - width) {
                X = WIDTH - width;
            }
            position.X = X;
        }
        if(paddleID==3||paddleID==2){
            int Y = position.Y;
            Y += paddleVelocity.Y;
            if (Y <= 0) {
                Y = 0;
            }

            if (Y >= HEIGTH-height) {
                Y = HEIGTH-height;
            }
            position.Y = Y;
        }



    }

    public void keyPressed(KeyEvent e) {

        int key = e.getKeyCode();
        if(paddleID==1||paddleID==4){
            if (key == KeyEvent.VK_LEFT) {
                paddleVelocity.X = -paddleSpeed;
            }

            if (key == KeyEvent.VK_RIGHT) {
                paddleVelocity.X = paddleSpeed;
            }

        }
        if(paddleID==3||paddleID==2){
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
            normal.Y = -1;//1
            //Set the image
            ImageIcon ii = new ImageIcon("paddle_horizontal.png");
            image =  ii.getImage();
        }
        else if(paddleID ==2){
            //Initialize the position on the board
            position.X = INIT_PADDLE2_X;
            position.Y = INIT_PADDLE2_Y;
            //Initialize the normal vector of the paddle
            normal.X = 1;//0
            normal.Y = 0;//-1
            //Set the image
            ImageIcon ii = new ImageIcon("paddle_vertical.png");
            image =  ii.getImage();
        }

        else if(paddleID ==3){
            //Initialize the position on the board
            position.X = INIT_PADDLE3_X;
            position.Y = INIT_PADDLE3_Y;
            //Initialize the normal vector of the paddle
            normal.X = -1;
            normal.Y = 0;
            //Set the image
            ImageIcon ii = new ImageIcon("paddle_vertical.png");
            image =  ii.getImage();
        }

        else if(paddleID ==4){
            //Initialize the position on the board
            position.X = INIT_PADDLE4_X;
            position.Y = INIT_PADDLE4_Y;
            //Initialize the normal vector of the paddle
            normal.X = 0;
            normal.Y = 1;
            //Set the image
            ImageIcon ii = new ImageIcon("paddle_horizontal.png");
            image =  ii.getImage();
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

}
