import java.awt.*;

/**
 * Created by hd on 14/4/16.
 */
public class Sprite {

    protected Vector2D position;
    protected int height;
    protected int width;

    public Image getImage() {
        return image;
    }

    protected Image image;


    public int getY() {
        return position.Y;
    }

    public void setY(int y) {
        position.Y = y;
    }

    public int getX() {
        return position.X;
    }

    public void setX(int x) {
        position.X = x;
    }

    public int getHeight() {
        return height;
    }

    public void setHeight(int height) {
        this.height = height;
    }

    public int getWidth() {
        return width;
    }

    public void setWidth(int width) {
        this.width = width;
    }

    Rectangle getRect() {
        return new Rectangle(position.X, position.Y,
                image.getWidth(null), image.getHeight(null));
    }

    Rectangle getSquare() {
        return new Rectangle(position.X, position.Y, 10, 10);
    }


}
