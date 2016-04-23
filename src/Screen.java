/**
 * Created by anmol on 23/04/16.
 */
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Image;
import javax.swing.ImageIcon;
import javax.swing.JPanel;

public class Screen extends JPanel {

    private Image screen;

    public Screen() {

        initBoard();
    }

    private void initBoard() {

        loadImage();

        int w = screen.getWidth(this);
        int h =  screen.getHeight(this);
        setPreferredSize(new Dimension(w, h));
    }

    private void loadImage() {

        ImageIcon ii = new ImageIcon("back.jpg");
        screen = ii.getImage();
    }

    @Override
    public void paintComponent(Graphics g) {

        g.drawImage(screen, 0, 0, null);
    }
}


