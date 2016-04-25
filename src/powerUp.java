import javax.swing.*;
import java.awt.*;

import static javax.swing.SpringLayout.HEIGHT;

/**
 * Created by hd on 23/4/16.
 */
public class powerUp extends Sprite implements Commons{

    //Fields
    private double x;
    private double y;
    private int r;
    private Color color1;

    private int type;
    // Types of PowerUp
    // 1 -----  +1 life
    // 2 -----  freeze
    // 3 -----  fast region activated
    // 4 -----  slow region activated

    //Constructor
    public powerUp(int type, double x, double y) {

        this.x = x;
        this.y = y;
        this.type = type;
        position = new Vector2D();

        if (type == 1) {
            color1 = Color.WHITE;
            r = 7;
        }
        if (type == 2) {
            color1 = Color.YELLOW;
            r = 5;
        }
        if (type == 3) {
            color1 = Color.YELLOW;
            r = 5;
        }
        if (type == 1) {
            color1 = Color.YELLOW;
            r = 5;
        }


    }

    //Functions
    public double getx() {return x;}
    public double gety() {return y;}
    public double getr() {return r;}

    public int gettype() {return type;}

    public boolean update(){

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
