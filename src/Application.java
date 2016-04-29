import java.awt.EventQueue;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.io.File;
import java.io.IOException;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.swing.*;


public class Application extends JFrame {



    public Application() {


        initUI();
    }

    private void initUI() {

        setResizable(false);
        setSize(920, 575);

        setTitle("Multipong Intern");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
    }




    public static void main(String[] args) {
        /*EventQueue.invokeLater(new Runnable() {
            @Override
            public void run() {
                Application ex = new Application();
                ex.setVisible(true);
            }
        });*/
        try
        {
            server f = new server ("Multipong");
            f.pack ();
            f.show ();
            f.run_receive ();
            f.run_send();
        }
        catch (IOException e)
        {
            System.err.println("Couldn't get I/O for the connection .");
            System.exit(1);
        }

        System.out.println(Math.abs(-2.3));

    }


}