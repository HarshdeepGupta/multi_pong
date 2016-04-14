import javax.swing.*;

/**
 * Created by hd on 14/4/16.
 */
public class Ball extends Sprite implements Commons {

    private Vector2D ballVelocity;
    private int ballSpeed;

    public Ball() {

        ballVelocity = new Vector2D();
        position = new Vector2D();

        ballSpeed = 3;

        ballVelocity.X = ballSpeed;
        ballVelocity.Y = ballSpeed;

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

        position.X = INIT_BALL_X;
        position.Y = INIT_BALL_Y;
    }
    public int getBallSpeed() {
        return ballSpeed;
    }

    public void setBallSpeed(int ballSpeed) {
        this.ballSpeed = ballSpeed;
    }


}
