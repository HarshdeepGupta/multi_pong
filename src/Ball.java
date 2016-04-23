import javax.swing.*;
import java.awt.*;

/**
 * Created by hd on 14/4/16.
 */
public class Ball extends Sprite implements Commons {

    protected Vector2D ballVelocity;
    private int ballSpeed;
     int last_hit_by;

    public Ball() {

        ballVelocity = new Vector2D();
        position = new Vector2D();



        ImageIcon ii = new ImageIcon("ball.jpeg");
        image = ii.getImage();

        width = image.getWidth(null);
        height = image.getHeight(null);

        resetState();
    }

    public void moveBall() {

        int X = position.X;
        int Y = position.Y;

        X += ballVelocity.X;
        Y += ballVelocity.Y;

        if (X < 0 && ballVelocity.X < 0) {
            ballVelocity.X *= -1 ;
        }

        if (X > WIDTH - width && ballVelocity.X >0) {
            ballVelocity.X *= -1;
        }

        if (Y < 0 && ballVelocity.Y < 0) {
            ballVelocity.Y *= -1;
        }

        if (Y > HEIGTH - height && ballVelocity.Y > 0) {
            ballVelocity.Y *= -1;
        }

        position.X = X;
        position.Y = Y;
    }

    private void resetState() {
        ballSpeed = 6;

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
        g2d.drawImage(this.getImage(),this.getX(),this.getY(),this.getWidth(),this.getHeight(),null);
    }
}
