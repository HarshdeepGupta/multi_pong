import java.awt.*;
import java.awt.event.*;
import java.io.File;
import java.io.IOException;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.swing.*;
import javax.swing.event.*;

/**
 * Created by tarun on 16/4/16.
 */

public class gui extends JFrame{
    public JButton startSingle;
    public JButton startMultiple;
    public JButton startGroup;
    public JTextArea myipArea, hostipArea, ip_ad, port, choice;
    public AudioInputStream audioInputStream;
    public Clip clip;
    public boolean sound = false;
    public String music = "Mute";
    String[] choices = { "  1  ", "  2  ", "  3  "};
    public final JComboBox<String> cb = new JComboBox<String>(choices);
    private JTextArea single;
    private JTextArea multi;
    private JTextArea group;
    //private JTextArea area;
    private JTextArea option;


    public gui (String title)
    {
        super (title);
        initUI();
        try {
            audioInputStream = AudioSystem.getAudioInputStream(new File("tune.wav").getAbsoluteFile());
            clip = AudioSystem.getClip();
            clip.open(audioInputStream);
        } catch(Exception ex) {
            System.out.println("Error with playing sound.");
            ex.printStackTrace();
        }
    }

    private void initUI() {

        int BUTTON_HEIGHT = 20;
        int BUTTON_WIDTH = 150;

        option = new JTextArea("    Game Options");
        option.setEditable(false);
        option.setBounds(100, 310, 120, 20);

        single = new JTextArea("     Single Player");
        single.setToolTipText("Play against Computer");
        single.setBounds(100, 350, BUTTON_WIDTH, BUTTON_HEIGHT);
        multi = new JTextArea("     Multi Player");
        multi.setToolTipText("Host a new Game");
        multi.setBounds(350, 350, BUTTON_WIDTH, BUTTON_HEIGHT);
        group = new JTextArea("   Connect to Host");
        group.setToolTipText("Play against your Friends");
        group.setBounds(600, 350, BUTTON_WIDTH, BUTTON_HEIGHT);


        /*area = new JTextArea("Start a new Game");
        area.setEditable(false);
        area.setBounds(700, 310, 160, 20);*/


        add(single);
        add(multi);
        add(group);
        add(option);

//        add(new Screen("back.jpg","name.png"));
        setResizable(false);

        myipArea = new JTextArea (6, 40);
        myipArea.setBounds(350, 410, 210, 40);
        hostipArea = new JTextArea (6, 40);
        hostipArea.setBounds(600, 410, 210, 40);
        startSingle = new JButton("Start");
        startSingle.setToolTipText("Start a new game");
        startMultiple = new JButton("Start");
        startMultiple.setToolTipText("Connect to an Existing Game");
        startSingle.setBounds(100, 480, 120, 25);
        startMultiple.setBounds(350, 480, 120, 25);
        startGroup = new JButton("Start");
        startGroup.setToolTipText("Go Back to Home Screen");
        startGroup.setBounds(600, 480, 120, 25);

        choice = new JTextArea("  Choose the Difficulty Level");
        choice.setEditable(false);
        choice.setBounds(100, 380, 200, 20);

        cb.setVisible(true);
        cb.setBounds(100, 420, 200, 25);

        startGroup.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent event) {
                dispose();
                //System.exit(0);
                Home ex = new Home();
                ex.setVisible(true);
            }
        });

        port = new JTextArea("     Enter your IP Address");
        port.setEditable(false);
        port.setBounds(350, 380, 210, 20);
        ip_ad = new JTextArea("     Enter the Host IP Address");
        ip_ad.setEditable(false);
        ip_ad.setBounds(600, 380, 210, 20);

        /*choice = new JTextArea(" Choose the House you dare to Defend");
        choice.setEditable(false);
        choice.setBounds(50, 220, 250, 20);
        String[] choices = { " House Stark", " House Lannister", " House Targaryen", " House Martell", " House Baratheon", " House Tyrell", " House Arryn", " House Greyjoy", " House Tully"};

        final JComboBox<String> cb = new JComboBox<String>(choices);

        cb.setVisible(true);
        cb.setBounds(50, 250, 200, 25);*/

        add(startSingle);
        add(startGroup);
        add(startMultiple);
        add(myipArea);
        add(port);
        add(ip_ad);
        add(choice);
        add(hostipArea);
        add(cb);
        //change the background here
        add(new Screen("defend.jpg"));
        setResizable(false);


        setSize(920, 575);

        createMenuBar();
        setTitle("Multipong Intern");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
    }

    public void playSound(int x) {
        if (x == 0) {
            clip.start();
            sound = true;
        }
        else {
            clip.stop();
            sound = false;
        }
    }

    private void createMenuBar() {

        JMenuBar menubar = new JMenuBar();
        ImageIcon icon = new ImageIcon("exit.png");

        JMenu file = new JMenu("File");
        file.setToolTipText("File Options");
        file.setMnemonic(KeyEvent.VK_F);

        JMenuItem exit = new JMenuItem("Exit");
        exit.setMnemonic(KeyEvent.VK_E);
        exit.setToolTipText("Exit application");
        exit.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent event) {
                System.exit(0);
            }
        });

        JMenuItem help = new JMenuItem("Help");
        exit.setMnemonic(KeyEvent.VK_E);
        exit.setToolTipText("About the Game");
        exit.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent event) {
                dispose();
                Help help = new Help();
                help.setVisible(true);
            }
        });

        JMenuItem mute = new JMenuItem(music);
        mute.setMnemonic(KeyEvent.VK_E);
        mute.setToolTipText("Mute");
        mute.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent event) {
                if (sound){
                    playSound(1);
                }
                else {
                    playSound(0);
                }
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

        file.add(exit);
        file.add(help);
        menubar.add(file);

        level.add(max);
        level.add(mid);
        level.add(min);
        level.add(mute);

        sound.add(level);
        menubar.add(sound);

        setJMenuBar(menubar);
    }
}
