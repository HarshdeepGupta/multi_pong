import javax.swing.*;
import java.awt.*;

/**
 * Created by hd on 14/4/16.
 */
public class Ball extends Sprite implements Commons {

    protected Vector2D ballVelocity;
    private int ballSpeed;

    private Color color1;
    private Color color2;
    protected int wall_hit;
    protected int last_hit_by;

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
        radius = 10;

        color1 = Color.WHITE;
        color2 = Color.RED;
        width = 2*radius;
        height = 2*radius;

        resetState();


    }

    public void moveBall() {

        int X = position.X;
        int Y = position.Y;
        X += ballVelocity.X;
        Y += ballVelocity.Y;
        position.X = X;
        position.Y = Y;
//        System.out.print(WIDTH);
//        System.out.print(" ");
//        System.out.println(HEIGHT);
//        System.out.print(X1);
//        System.out.print(" ");
//        System.out.println(Y1);
        //Detect the collision with the walls and update its velocity and wall_hit parameters
        //Ball hits the left wall
        if (X <= 0 && ballVelocity.X < 0) {
            ballVelocity.X *= -1 ;
            wall_hit = 4;
//            System.out.print("Here1");
        }

        //Ball hits the right wall
        else if (X > WIDTH - width && ballVelocity.X >0) {
            ballVelocity.X *= -1;
            wall_hit = 3;
//            System.out.print("Here2");
        }
        //Ball hits the top wall
        else if (Y <= 0 && ballVelocity.Y < 0) {
            ballVelocity.Y *= -1;
            wall_hit = 1;
//            System.out.print("Here3");
        }
        else if (Y > HEIGHT - height && ballVelocity.Y > 0) {

            ballVelocity.Y *= -1;
            wall_hit = 2;
//            System.out.print("Here4");
        }
        else{
            wall_hit = 0;
        }
        position.X = X;
        position.Y = Y;

    }

    private void resetState() {
        ballSpeed = 4;

        ballVelocity.X = (int)( 3 * Math.random()*ballSpeed - ballSpeed);
        ballVelocity.Y = (int)( 4 * Math.random()*ballSpeed - ballSpeed);

        position.X = INIT_BALL_X;
        position.Y = INIT_BALL_Y;
        last_hit_by = 0;
        wall_hit = 0;
    }
    public int getBallSpeed() {
        return ballSpeed;
    }

    public void setBallSpeed(int ballSpeed) {
        this.ballSpeed = ballSpeed;
    }


    public void draw(Graphics2D g2d) {

        g2d.setColor(color1);
        g2d.fillOval(position.X , position.Y , 2*radius, 2*radius);
        g2d.setStroke(new BasicStroke(3));
        g2d.setColor(color1.darker());
        g2d.drawOval(position.X , position.Y , 2*radius, 2*radius);

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
