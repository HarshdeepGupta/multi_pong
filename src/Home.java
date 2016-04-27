/**
 * Created by anmol on 24/04/16.
 */
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


public class Home extends JFrame {


    private JButton single;
    private JButton multi;
    private JButton group;
    private JButton help;
    private JButton mute;
    private JButton exit;
    private JTextArea area;
    private JTextArea option;


    public Home() {

        initUI();
    }

    private void initUI() {


        single = new JButton("Single Player");
        single.setToolTipText("Play against Computer");
        single.setBounds(700, 350, 120, 25);
        multi = new JButton("Multi Player");
        multi.setToolTipText("Host a new Game");
        multi.setBounds(700, 380, 120, 25);
        group = new JButton("Connect to Host");
        group.setToolTipText("Play against your Friends");
        group.setBounds(700, 410, 140, 30);
        help = new JButton("Help");
        help.setToolTipText("About the Game");
        help.setBounds(100, 350, 120, 25);
        mute = new JButton("Mute");
        mute.setToolTipText("Turn off Sound");
        mute.setBounds(100, 380, 120, 25);
        exit = new JButton("Exit");
        exit.setToolTipText("Exit the Game");
        exit.setBounds(100, 410, 120, 25);

        single.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent event) {
                try
                {
                    server f = new server ("Multipong Intern", "      Not Required");
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
                    server f = new server ("Multipong Intern", "  Wait for your friends to connect");
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
        group.addActionListener(new ActionListener() {
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
        help.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent event) {
                dispose();
                //System.exit(0);
                Help help = new Help();
                help.setVisible(true);
            }
        });
//        mute.addActionListener(new ActionListener() {
//
//        });
        exit.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent event) {
                System.exit(0);
            }
        });


        area = new JTextArea("      Start a new Game");
        area.setEditable(false);
        area.setBounds(700, 310, 160, 20);
        option = new JTextArea("         Options");
        option.setEditable(false);
        option.setBounds(100, 310, 120, 20);

        add(single);
        add(multi);
        add(group);
        add(area);
        add(help);
        add(mute);
        add(exit);
        add(option);
        add(new Screen("back.jpg","name.png"));
        setResizable(false);


        createMenuBar();
        setSize(920, 575);

        setTitle("Multipong Intern");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
    }


    private void createMenuBar() {

        JMenuBar menubar = new JMenuBar();
        ImageIcon icon = new ImageIcon("exit.png");

        JMenu file = new JMenu("File");
        file.setToolTipText("File Options");
        file.setMnemonic(KeyEvent.VK_F);

        JMenuItem ex = new JMenuItem("Exit");
        ex.setMnemonic(KeyEvent.VK_E);
        ex.setToolTipText("Exit application");
        ex.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent event) {
                System.exit(0);
            }
        });

        file.add(ex);
        menubar.add(file);

        JMenuItem mute = new JMenuItem("Mute");
        mute.setMnemonic(KeyEvent.VK_E);
        mute.setToolTipText("Mute");
        mute.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent event) {
                //System.exit(0);
            }
        });

        JMenuItem min = new JMenuItem("Minimum");
        min.setMnemonic(KeyEvent.VK_E);
        min.setToolTipText("Set to Min");
        min.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent event) {
                //System.exit(0);
            }
        });

        JMenuItem mid = new JMenuItem("Medium");
        mid.setMnemonic(KeyEvent.VK_E);
        mid.setToolTipText("Set to Medium");
        mid.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent event) {
                //System.exit(0);
            }
        });

        JMenuItem max = new JMenuItem("Maximum");
        max.setMnemonic(KeyEvent.VK_E);
        max.setToolTipText("Set to Max");
        max.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent event) {
                //System.exit(0);
            }
        });

        JMenu sound = new JMenu("Sound");
        sound.setMnemonic(KeyEvent.VK_F);
        sound.setToolTipText("Adjust Sound Level");

        JMenu level = new JMenu("Level");
        level.setMnemonic(KeyEvent.VK_E);

        level.add(max);
        level.add(mid);
        level.add(min);
        level.add(mute);

        sound.add(level);
        menubar.add(sound);

        setJMenuBar(menubar);
    }

}