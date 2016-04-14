import javax.swing.*;
import java.awt.event.KeyEvent;

/**
 * Created by hd on 14/4/16.
 */
public class Paddle extends Sprite implements Commons {

    private Vector2D paddleVelocity;
    private int paddleSpeed;
    public Paddle(){


        paddleVelocity = new Vector2D();
        position = new Vector2D();

        ImageIcon ii = new ImageIcon("paddle.png");
        image =  ii.getImage();
        width = image.getWidth(null);
        height = image.getHeight(null);
        paddleSpeed = 3;
        resetState();
    }

    public void movePaddle(){

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

    public void keyPressed(KeyEvent e) {

        int key = e.getKeyCode();

        if (key == KeyEvent.VK_LEFT) {
            paddleVelocity.X = -paddleSpeed;
        }

        if (key == KeyEvent.VK_RIGHT) {
            paddleVelocity.X = paddleSpeed;
        }
    }

    public void keyReleased(KeyEvent e) {

        int key = e.getKeyCode();

        if (key == KeyEvent.VK_LEFT||key == KeyEvent.VK_RIGHT) {
            paddleVelocity.X = 0;
        }
    }


    private void resetState() {
        position.X = INIT_PADDLE_X;
        position.Y = INIT_PADDLE_Y;
    }

}
