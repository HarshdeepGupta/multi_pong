import java.net.DatagramSocket;

/**
 * Created by hd on 14/4/16.
 */

public interface Commons {

    public static final int WIDTH = 500;
    public static final int HEIGHT = 500;

    public static final int PADDLE_HEIGHT = 5;

    public static final int INIT_PADDLE1_X = 250;
    public static final int INIT_PADDLE1_Y = 500-PADDLE_HEIGHT;

    public static final int INIT_PADDLE2_X = 250;
    public static final int INIT_PADDLE2_Y = 0;

    public static final int INIT_PADDLE3_X = 500-PADDLE_HEIGHT;
    public static final int INIT_PADDLE3_Y = 250;

    public static final int INIT_PADDLE4_X = 0;
    public static final int INIT_PADDLE4_Y = 250;


    public static final int DELAY = 1000;
    public static final int PERIOD = 10;






    public static final int INIT_BALL_X = 250;
    public static final int INIT_BALL_Y = 250;
}
