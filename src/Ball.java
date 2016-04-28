import javax.swing.*;
import java.awt.*;

/**
 * Created by hd on 14/4/16.
 */
public class Ball extends Sprite implements Commons {

    protected Vector2D ballVelocity;
    private int ballSpeed;
     int last_hit_by;
    private Color color1;
    private Color color2;
    protected int wall_hit;
    protected int wall_hit_temp;
    protected Vector2D center;
    private int radius;

    public int getRadius() {
        return radius;
    }

    public void setRadius(int radius) {
        this.radius = radius;
    }



    public Ball() {

        ballVelocity = new Vector2D();
        position = new Vector2D();

        color1 = Color.WHITE;
        color2 = Color.RED;
//        ImageIcon ii = new ImageIcon("ball.jpeg");
//        image = ii.getImage();

//        width = image.getWidth(null);
//        height = image.getHeight(null);

        width = 10;
        height = 10;
        resetState();


    }

    public void moveBall() {

        int X = position.X;
        int Y = position.Y;

        X += ballVelocity.X;
        Y += ballVelocity.Y;
        //Detect the collision with the walls and update its velocity and wall_hit parameters
        //Ball hits the left wall
        if (X <= width && ballVelocity.X < 0) {
            ballVelocity.X *= -1 ;
            wall_hit = 4;
            try {
                Thread.sleep(50);
            } catch (InterruptedException e) {
                System.out.println("Interrupted: " + e.getMessage());
            }
        }

        //Ball hits the right ball
        if (X >= WIDTH - width && ballVelocity.X >0) {
            ballVelocity.X *= -1;
            wall_hit = 3;
            try {
                Thread.sleep(50);
            } catch (InterruptedException e) {
                System.out.println("Interrupted: " + e.getMessage());
            }
        }
        //Ball hits the top wall
        if (Y <= height && ballVelocity.Y < 0) {
            ballVelocity.Y *= -1;
            wall_hit = 2;
            try {
                Thread.sleep(50);
            } catch (InterruptedException e) {
                System.out.println("Interrupted: " + e.getMessage());
            }
        }
        //Ball hits the bottom wall
        if (Y >= HEIGHT  - height && ballVelocity.Y > 0) {
            ballVelocity.Y *= -1;
            wall_hit = 1;
            try {
                Thread.sleep(50);
            } catch (InterruptedException e) {
                System.out.println("Interrupted: " + e.getMessage());
            }
        }
        //Ball hits no wall

        else{
            wall_hit = 0;
        }



        position.X = X;
        position.Y = Y;
    }

    private void resetState() {
        ballSpeed = 4;

        ballVelocity.X = ballSpeed;
        ballVelocity.Y = ballSpeed;

        position.X = INIT_BALL_X;
        position.Y = INIT_BALL_Y;
        last_hit_by = 0;
    }
    public int getBallSpeed() {
        return ballSpeed;
    }

    public void setBallSpeed(int ballSpeed) {
        this.ballSpeed = ballSpeed;
    }


    public void draw(Graphics2D g2d) {

        g2d.setColor(color1);
        g2d.fillOval(position.X - 10, position.Y - 10, 20, 20);
        g2d.setStroke(new BasicStroke(3));
        g2d.setColor(color1.darker());
        g2d.drawOval(position.X - 10, position.Y - 10, 20, 20);
        g2d.setStroke(new BasicStroke(1));

        //g2d.drawImage(this.getImage(),this.getX(),this.getY(),this.getWidth(),this.getHeight(),null);
    }

    public Vector2D getCenter(){
        return new Vector2D(position.X + radius, position.Y + radius);

    }

    Rectangle getFutureRect() {
        return new Rectangle(position.X +ballVelocity.X, position.Y + ballVelocity.Y,
                this.getWidth(), this.getHeight());
    }

    public void setBallVelocity(int ball_x,int ball_y){
        ballVelocity.X = ball_x;
        ballVelocity.Y = ball_y;
    }

    public Vector2D getBallVelocity(){
        return ballVelocity;
    }

}
