import javax.swing.*;
import java.awt.*;

import static javax.swing.SpringLayout.HEIGHT;

/**
 * Created by hd on 23/4/16.
 */
public class powerUp extends Sprite implements Commons{

    //Fields
    private int x;
    private int y;
    private int r;
    private Color color1;
    private long startTime;

    private int type;
    // Types of PowerUp
    // 1 -----  +1 life
    // 2 -----  freeze
    // 3 -----  longer paddle
    // 4 -----  faster paddle
    // 5 -----  ball direction change
    // 6 -----  black hole

    //Constructor
    public powerUp(int type, int x, int y, long start) {

        this.x = x;
        this.y = y;
        this.type = type;
        position = new Vector2D();
        setX(x);
        setY(y);
        r = 0;
        startTime = start;
        if (type == 1) {
            color1 = Color.GREEN;
            r = 5;
        }
        if (type == 2) {
            color1 = Color.BLUE;
            r = 5;
        }
        if (type == 3) {
            color1 = Color.RED;
            r = 5;
        }
        if (type == 4) {
            color1 = Color.PINK;
            r = 5;
        }
        if (type == 5) {
            color1 = Color.WHITE;
            r = 5;
        }
        if (type == 6) {
            color1 = Color.ORANGE;
            r = 5;
        }

    }

    //Functions
    public double getx() {return x;}
    public double gety() {return y;}
    public double getr() {return r;}
    public long getstarttime() {return startTime;}

    public int gettype() {return type;}

    public boolean update(){

        //y += 2;
        if (y > HEIGHT + r){
            return true;
        }
        return false;
    }

    public void draw(Graphics2D g) {

        g.setColor(color1);
        g.fillRect((int) (x-r), (int) (y-r), 2 * r, 2 * r);
        g.setStroke(new BasicStroke(3));
        g.setColor(color1.darker());
        g.drawRect((int) (x-r), (int) (y-r), 2 * r, 2 * r);
        g.setStroke(new BasicStroke(1));
    }
}
