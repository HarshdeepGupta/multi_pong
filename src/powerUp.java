import javax.swing.*;
import java.awt.*;

/**
 * Created by hd on 23/4/16.
 */
public class powerUp extends Sprite {

    public powerUp() {

        position = new Vector2D();
        ImageIcon ii = new ImageIcon("powerUp.java");
        image =  ii.getImage();
        width = image.getWidth(null);
        height = image.getHeight(null);
    }


    public void draw(Graphics2D g2d) {
        g2d.drawImage(this.getImage(),this.getX(),this.getY(),this.getWidth(),this.getHeight(),null);
    }

}
