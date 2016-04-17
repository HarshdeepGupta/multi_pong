import java.awt.EventQueue;
import java.io.IOException;
import javax.swing.JFrame;


public class Application extends JFrame {




    public Application() {

        initUI();
    }

    private void initUI() {
        /*
        add(new Board());
        setResizable(false);
        pack();

        setSize(Commons.WIDTH, Commons.HEIGTH);
        setLocationRelativeTo(null);
        setResizable(false);
        setVisible(true);
        */

    }

    public static void main(String[] args) {
        /*
        EventQueue.invokeLater(new Runnable() {
            @Override
            public void run() {
                Application ex = new Application();
                ex.setVisible(true);
            }
        });
        */
        try
        {
            server f = new server ("Multipong Intern");
            f.pack ();
            f.show ();
            f.run_receive ();
            f.run_send();
        }
        catch (IOException e)
        {
            System.err.println("Couldn't get I/O for the connection to: 194.81.104.118.");
            System.exit(1);
        }
    }

}