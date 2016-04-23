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
    public JTextArea txArea, rxArea;
    public Container container;
    public gui (String title)
    {
        super (title);
        container = getContentPane();
        container.setLayout( new FlowLayout() );
        txArea = new JTextArea (6, 40);
        rxArea = new JTextArea (6, 40);
        sendButton = new JButton("Start");
        sendButton.setToolTipText("Start a new game");
        connect = new JButton("Connect");
        connect.setToolTipText("Connect to an Existing Game");
        container.add (rxArea);
        container.add (txArea);
        container.add (sendButton);
        container.add (connect);
        }
}
