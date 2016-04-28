import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import javax.swing.*;

/**
 * Created by anmol on 24/04/16.
 */

public class Animate extends JPanel
        implements ActionListener {

    //private final int B_WIDTH = 350;
    //private final int B_HEIGHT = 350;
    public Timer timer;

    private final int INITIAL_X = 310;
    private final int INITIAL_Y = -40;
    private final int DELAY = 25;
    private int x, y;

    private boolean st;
    private Image screen;
    private String image1;
    private String image2;
    private Image text;
    private JButton cont;

    public Animate(String file1, String file2) {

        image1 = file1;
        image2 = file2;
        st = true;
        initBoard();

    }

    private void loadImage() {

        ImageIcon ii = new ImageIcon(image1);
        screen = ii.getImage();
        ImageIcon ii2 = new ImageIcon(image2);
        text = ii2.getImage();
        st = true;
    }

    private void initBoard() {

        //setBackground(Color.BLACK);
        //setPreferredSize(new Dimension(B_WIDTH, B_HEIGHT));
        setDoubleBuffered(true);

        loadImage();

        cont = new JButton("Continue");
        cont.setToolTipText("That's my Spot");
        cont.setBounds(380, 350, 120, 25);
        cont.setVisible(false);
        x = INITIAL_X;
        y = INITIAL_Y;
        add(cont);

        timer = new Timer(DELAY, this);
        timer.start();
        st = true;

        cont.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent event) {
                //this.();
                Home home = new Home();
                home.setVisible(true);
            }
        });
    }


    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);

        drawStar(g);
    }

    private void drawStar(Graphics g) {

        g.drawImage(screen, 0, 0, null);
        g.drawImage(text, x, y, this);
        Toolkit.getDefaultToolkit().sync();
    }

    @Override
    public void actionPerformed(ActionEvent e) {

        y += 8;
        if (y >= 450){
            timer.stop();
            st = false;
            //setVisible(false);
            cont.setVisible(true);
//            Home home = new Home();
//            home.setVisible(true);
        }
        repaint();
    }

    public boolean state (){
        return st;
    }

}