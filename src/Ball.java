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

        if (X < 0 && ballVelocity.X < 0) {
            ballVelocity.X *= -1 ;
        }

        if (X > WIDTH - width && ballVelocity.X >0) {
            ballVelocity.X *= -1;
        }

        if (Y < 0 && ballVelocity.Y < 0) {
            ballVelocity.Y *= -1;
        }

        if (Y > HEIGHT - height && ballVelocity.Y > 0) {
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
        g2d.setColor(color1);
        g2d.fillOval(position.X-10,position.Y-10,20,20);
        g2d.setStroke(new BasicStroke(3));
        g2d.setColor(color1.darker());
        g2d.drawOval(position.X-10,position.Y-10,20,20);
        g2d.setStroke(new BasicStroke(1));

        //g2d.drawImage(this.getImage(),this.getX(),this.getY(),this.getWidth(),this.getHeight(),null);
    }
}
