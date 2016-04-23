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
        /*
        add(new Board());
        setResizable(false);
        pack();

        setSize(Commons.WIDTH, Commons.HEIGTH);
        setLocationRelativeTo(null);
        setResizable(false);
        setVisible(true);
        */

        JButton single = new JButton("Single Player");
        single.setToolTipText("Play against Computer");
        single.setBounds(700, 400, 120, 25);
        JButton multi = new JButton("Multi Player");
        multi.setToolTipText("Play against your Friends");
        multi.setBounds(700, 430, 120, 25);

        single.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent event) {
                try
                {
                    server f = new server ("Multipong Intern");
                    dispose();
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
        });
        multi.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent event) {
                try
                {
                    server f = new server ("Multipong Intern");
                    dispose();
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
        });

        JTextArea area = new JTextArea(" Start a new Game");
        area.setEditable(false);
        area.setBounds(700, 360, 160, 25);

        add(single);
        add(multi);
        add(area);
        add(new Screen());

        createMenuBar();
        //playSound();
        setSize(920, 575);

        setTitle("Application");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
    }

    public void playSound() {
        try {
            AudioInputStream audioInputStream = AudioSystem.getAudioInputStream(new File("/Users/anmol/IdeaProjects/ex/back.wav").getAbsoluteFile());
            Clip clip = AudioSystem.getClip();
            clip.open(audioInputStream);
            clip.start();
        } catch(Exception ex) {
            System.out.println("Error with playing sound.");
            ex.printStackTrace();
        }
    }

    private void createMenuBar() {

        JMenuBar menubar = new JMenuBar();
        ImageIcon icon = new ImageIcon("exit.png");

        JMenu file = new JMenu("File");
        file.setMnemonic(KeyEvent.VK_F);

        JMenuItem eMenuItem = new JMenuItem("Exit");
        eMenuItem.setMnemonic(KeyEvent.VK_E);
        eMenuItem.setToolTipText("Exit application");
        eMenuItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent event) {
                System.exit(0);
            }
        });

        file.add(eMenuItem);
        menubar.add(file);

        setJMenuBar(menubar);
    }

    public static void main(String[] args) {
        EventQueue.invokeLater(new Runnable() {
            @Override
            public void run() {
                Application ex = new Application();
                ex.setVisible(true);
            }
        });
       /* try
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
        }*/
        System.out.println("1");

    }

}