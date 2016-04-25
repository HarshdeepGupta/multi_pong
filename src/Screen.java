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
    private String image1;
    private String image2;
    private Image text;

    public Screen(String file) {

        image1 = file;
        initBoard();
    }

    public Screen(String file1, String file2) {

        image1 = file1;
        image2 = file2;
        initBoard();

    }

    private void initBoard() {

        loadImage();

        int w = screen.getWidth(this);
        int h =  screen.getHeight(this);
        setPreferredSize(new Dimension(w, h));
    }

    private void loadImage() {

        ImageIcon ii = new ImageIcon(image1);
        screen = ii.getImage();
        ImageIcon ii2 = new ImageIcon(image2);
        text = ii2.getImage();
    }

    @Override
    public void paintComponent(Graphics g) {

        g.drawImage(screen, 0, 0, null);
        g.drawImage(text, 310, 450, null);
    }
}


