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

    public AudioInputStream audioInputStream;
    public Clip clip;


    public Application() {

        try {
            audioInputStream = AudioSystem.getAudioInputStream(new File("tune.wav").getAbsoluteFile());
            clip = AudioSystem.getClip();
            clip.open(audioInputStream);
        } catch(Exception ex) {
            System.out.println("Error with playing sound.");
            ex.printStackTrace();
        }
        initUI();
    }

    private void initUI() {


        //add(new Screen("back.jpg","name.png"));
        Animate ex = new Animate("back.jpg","name.png");
        setResizable(false);
        add(ex);
        //dispose();

        createMenuBar();
        playSound(0);
        setSize(920, 575);

        setTitle("Multipong Intern");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
    }

    public void playSound(int x) {
        if (x == 0)
            clip.start();
        else
            clip.stop();
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



        file.add(exit);
        menubar.add(file);

        level.add(max);
        level.add(mid);
        level.add(min);
        level.add(mute);

        sound.add(level);
        menubar.add(sound);

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

        System.out.println("1");

    }

}