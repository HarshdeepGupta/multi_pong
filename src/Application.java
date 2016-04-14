import java.awt.EventQueue;
import javax.swing.JFrame;


public class Application extends JFrame {




    public Application() {

        initUI();
    }

    private void initUI() {

        add(new Board());
        setResizable(false);
        pack();

        setSize(Commons.WIDTH, Commons.HEIGTH);
        setLocationRelativeTo(null);
        setResizable(false);
        setVisible(true);
    }

    public static void main(String[] args) {

        EventQueue.invokeLater(new Runnable() {
            @Override
            public void run() {
                Application ex = new Application();
                ex.setVisible(true);
            }
        });
    }
}