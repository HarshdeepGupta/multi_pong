import java.awt.*;
import java.awt.event.*;
import java.io.IOException;
import javax.swing.*;
import javax.swing.event.*;

/**
 * Created by tarun on 16/4/16.
 */

public class gui extends JFrame{
    public JButton sendButton;
    public JButton connect;
    public JButton back;
    public JTextArea txArea, rxArea, ip_ad, port, choice;


    public gui (String title)
    {
        super (title);
        initUI();

    }

    private void initUI() {

        txArea = new JTextArea (6, 40);
        txArea.setBounds(50, 380, 210, 40);
        rxArea = new JTextArea (6, 40);
        rxArea.setBounds(300, 380, 210, 40);
        sendButton = new JButton("Start");
        sendButton.setToolTipText("Start a new game");
        connect = new JButton("Connect");
        connect.setToolTipText("Connect to an Existing Game");
        sendButton.setBounds(85, 450, 120, 25);
        connect.setBounds(335, 450, 120, 25);
        back = new JButton("Back");
        back.setToolTipText("Go Back to Home Screen");
        back.setBounds(210, 520, 120, 25);

        back.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent event) {
                dispose();
                //System.exit(0);
                Home ex = new Home();
                ex.setVisible(true);
            }
        });

        port = new JTextArea("     Enter the Host Port Number");
        port.setEditable(false);
        port.setBounds(50, 350, 210, 20);
        ip_ad = new JTextArea("      Enter the Host IP Address");
        ip_ad.setEditable(false);
        ip_ad.setBounds(300, 350, 210, 20);

        choice = new JTextArea(" Choose the House you dare to Defend");
        choice.setEditable(false);
        choice.setBounds(50, 220, 250, 20);
        String[] choices = { " House Stark", " House Lannister", " House Targaryen", " House Martell", " House Baratheon", " House Tyrell", " House Arryn", " House Greyjoy", " House Tully"};

        final JComboBox<String> cb = new JComboBox<String>(choices);

        cb.setVisible(true);
        cb.setBounds(50, 250, 200, 25);

        add(sendButton);
        add(connect);
        add(txArea);
        add(rxArea);
        add(port);
        add(ip_ad);
        add(choice);
        add(back);
        add(cb);
        add(new Screen("defend.jpg"));


        setSize(920, 575);

        createMenuBar();
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

        JMenuItem exit = new JMenuItem("Exit");
        exit.setMnemonic(KeyEvent.VK_E);
        exit.setToolTipText("Exit application");
        exit.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent event) {
                System.exit(0);
            }
        });
        JMenuItem home = new JMenuItem("Home");
        home.setMnemonic(KeyEvent.VK_E);
        home.setToolTipText("Return to Home Screen");
        home.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent event) {
                dispose();
                //System.exit(0);
                Home ex = new Home();
                ex.setVisible(true);
            }
        });

        file.add(home);
        file.add(exit);
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
