/**
 * Created by tarun on 16/4/16.
 */

// This class sends out data packets to the received ip's



import java.awt.*;
import java.net.*;
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.event.*;
import java.io.*;

public class server extends gui{


    static boolean game_start = false;
    public ButtonHandler bHandler;
    public ButtonHandler1 bHandler1;
    static DatagramSocket socket1;
    private final int DELAY = 100;
    String[] ip_array = new String[4];
    int[] port_array = new int[4];


    int myid=0;
    String my_ip = "192.168.0.101";
    int my_port = 4456;
    //assign local ip here and port here

    int number_of_players=0;

    static Board board;

    static Paddle[] paddles;

    Container container1;

    public server (String title) throws IOException
    {

        super (title);
        bHandler = new ButtonHandler ();
        bHandler1 = new ButtonHandler1();
        sendButton.addActionListener (bHandler);
        connect.addActionListener(bHandler1);
        socket1 = new DatagramSocket (my_port);
        container1 = this;
    }


    private class ButtonHandler1 implements ActionListener
    {
        public void actionPerformed (ActionEvent event) //throws IOException
        {
            try
            {
                // here we send our ip and our port to the host
                //DatagramSocket socket = new DatagramSocket ();
                byte[] buf = new byte[256];
                String ip_address = txArea.getText ();
                String temp="0#ip=";//127.0.0.1
                String my_ip1 = temp.concat(my_ip);
                my_ip1 = my_ip1.concat("#port=").concat(String.valueOf(my_port)).concat("#time=").concat(String.valueOf(java.lang.System.currentTimeMillis())).concat("#");
                int port_send = Integer.parseInt(rxArea.getText());// here the port of the host has to be entered and read from the txArea
                buf = my_ip1.getBytes ();
                InetAddress address = InetAddress.getByName (ip_address);
                DatagramPacket packet = new DatagramPacket (buf, buf.length, address, port_send);
                socket1.send(packet);
            }
            catch (IOException e)
            {

            }
        }
    }

    private class ButtonHandler implements ActionListener
    {
        public void actionPerformed (ActionEvent event) //throws IOException
        {
            // draw the board and start the game and send start event to the other machine
            try {
                //DatagramSocket socket = new DatagramSocket();
                byte[] buf = new byte[256];
                String temp = "2#num=".concat(String.valueOf(number_of_players)).concat("#");
                for(int i=0;i<number_of_players;i++){
                    temp = temp.concat("ip".concat(String.valueOf(i+1))).concat("=").concat(ip_array[i]).
                            concat("#port".concat(String.valueOf(i+1))).concat("=").concat(String.valueOf(port_array[i])).concat("#");
                }
                temp = temp.concat("time=").concat(String.valueOf(java.lang.System.currentTimeMillis())).concat("#");
                buf = temp.getBytes();// here we want our ip-address instead
                for(int i=0;i<number_of_players;i++) {
                    InetAddress address = InetAddress.getByName(ip_array[i]);
                    DatagramPacket packet = new DatagramPacket(buf, buf.length, address, port_array[i]);
                    socket1.send(packet);
                }
            }
            catch (IOException e)
            {
            }
            container1.setVisible(false);
            board = new Board();
            JFrame new_frame= new JFrame();
            new_frame.add(board);
            new_frame.pack();
            new_frame.setSize(Commons.WIDTH, Commons.HEIGTH);
            new_frame.setLocationRelativeTo(null);
            new_frame.setResizable(false);
            new_frame.setVisible(true);
            paddles = board.getPaddleArray();
            game_start = true;
        }
    }



    // Receive Message
    public void run_receive () throws IOException

    {
        Thread receive_message = new Thread(new Runnable() {
            @Override
            public void run() {
                try
                {
                    DatagramPacket packet;
                    byte[] buf = new byte[256];
                    while (true)
                    {
                        packet = new DatagramPacket (buf, buf.length);
                        socket1.receive (packet);

                        String received = new String (packet.getData());
                        System.out.println("received".concat(received));

                        if(received.length()!=0) {
                            if (Integer.parseInt(Character.toString(received.charAt(0))) == 0) {//packet contains the ip
                                boolean contains=false;
                                String received_ip  = received.substring(received.indexOf("ip") + 3, received.indexOf("port") - 1);
                                for (int i=0;i<number_of_players;i++){
                                    if(ip_array[i].equals(received_ip)){
                                        contains = true;
                                    }
                                }
                                if(contains == false) {
                                    ip_array[number_of_players] = received.substring(received.indexOf("ip") + 3, received.indexOf("port") - 1);
                                    port_array[number_of_players] = Integer.parseInt(received.substring(received.indexOf("port") + 5, received.indexOf("time")-1));
                                    number_of_players += 1;
                                    try {
                                        //DatagramSocket socket = new DatagramSocket();
                                        buf = new byte[256];
                                        String temp = "0#ip=";
                                        String my_id = temp.concat(my_ip).concat("#port=").concat(String.valueOf(my_port)).concat("#time=").concat(String.valueOf(java.lang.System.currentTimeMillis()));
                                        buf = my_id.getBytes();// here we want our ip-address instead
                                        for(int i=0;i<number_of_players;i++) {
                                            InetAddress address = InetAddress.getByName(ip_array[i]);
                                            packet = new DatagramPacket(buf, buf.length, address, port_array[i]);
                                            socket1.send(packet);
                                            System.out.println("sent");
                                        }
                                    }

                                    catch (IOException e)
                                    {
                                    }
                                }



                            } else if (Integer.parseInt(Character.toString(received.charAt(0))) == 1) {
                                //packet contains game info and update the game state here
                                // Eg :- 1#ip=127.0.0.1#ps=3#pv=(x,y)#time=#
                                String ip;
                                int id = 0;
                                int paddlespeed = 0;
                                int paddlevelocity_x = 0;
                                int paddlevelocity_y = 0;
                                ip = received.substring(received.indexOf("ip") + 3, received.indexOf("ps") - 1);
                                for(int i=0;i<number_of_players;i++){
                                    if(ip.equals(ip_array[i])){
                                        id = i;
                                        break;
                                    }
                                }
                                // Eg :- 1#ip=127.0.0.1#ps=3#pv=(x,y)#time=#
                                paddlespeed = Integer.parseInt(Character.toString(received.charAt(received.indexOf("ps")+3)));
                                if(Character.toString(received.charAt(received.indexOf("pv")+4)).equals("-")){// x velocity is negative
                                    paddlevelocity_x = Integer.parseInt(Character.toString(received.charAt(received.indexOf("pv")+4)).
                                    concat(Character.toString(received.charAt(received.indexOf("pv")+5))));
                                    if(Character.toString(received.charAt(received.indexOf("pv")+7))=="-"){//y velocity also negative
                                        paddlevelocity_y = Integer.parseInt(Character.toString(received.charAt(received.indexOf("pv")+7))
                                                .concat(Character.toString(received.charAt(received.indexOf("pv")+8))));
                                    }
                                    else{// y is positive
                                        paddlevelocity_y = Integer.parseInt(Character.toString(received.charAt(received.indexOf("pv")+7)));
                                    }
                                }
                                else{// x is positive
                                    paddlevelocity_x = Integer.parseInt(Character.toString(received.charAt(received.indexOf("pv") + 4)));

                                    if(Character.toString(received.charAt(received.indexOf("pv")+6)).equals("-")){
                                        paddlevelocity_y = Integer.parseInt(Character.toString(received.charAt(received.indexOf("pv")+6))
                                                .concat(Character.toString(received.charAt(received.indexOf("pv")+7))));
                                    }
                                    else{
                                        paddlevelocity_y = Integer.parseInt(Character.toString(received.charAt(received.indexOf("pv")+6)));
                                    }
                                }

                                //paddlevelocity_y = Integer.parseInt(Character.toString(received.charAt(received.indexOf("pv")+6)));
                                paddles[id].setPaddleSpeed(paddlespeed);
                                paddles[id].setPaddleVelocity(paddlevelocity_x,paddlevelocity_y);
                                board.setPaddleArray(id,paddlespeed,paddlevelocity_x,paddlevelocity_y);

                            } else if (Integer.parseInt(Character.toString(received.charAt(0))) == 2) {
                                //Acknowledge to start the game
                                // Game Start
                                // Eg :- 2#num=3#ip1=127.0.0.1#port1=#ip2=#port2=#ip3=#port3=#
                                int number = Integer.parseInt(Character.toString(received.charAt(received.indexOf("num")+4)));
                                for(int i=0;i<number;i++){

                                    int port = Integer.parseInt(received.substring(received.indexOf("port".concat(String.valueOf(i+1))) + 6,
                                            received.substring(received.indexOf("port".concat(String.valueOf(i+1)))
                                                    ,received.length()).indexOf("#")+received.indexOf("port".concat(String.valueOf(i+1)))));
                                    System.out.println("port".concat(String.valueOf(port)));
                                    String ip = received.substring(received.indexOf("ip".concat(String.valueOf(i+1)))+4,
                                            received.indexOf("port".concat(String.valueOf(i+1)))-1);
                                    System.out.println("ip".concat(ip));
                                    if(ip.equals(my_ip)==false){
                                    try {
                                        //DatagramSocket socket = new DatagramSocket();
                                        buf = new byte[256];
                                        String temp = "0#ip=";
                                        String my_id = temp.concat(my_ip).concat("#port=").concat(String.valueOf(my_port)).concat("#time=").concat(String.valueOf(java.lang.System.currentTimeMillis()));
                                        buf = my_id.getBytes();// here we want our ip-address instead
                                        InetAddress address = InetAddress.getByName(ip);
                                        packet = new DatagramPacket(buf, buf.length, address,port);
                                        socket1.send(packet);
                                    }

                                    catch (IOException e)
                                    {

                                    }
                                    }
                                }
                                container1.setVisible(false);
                                board = new Board();
                                JFrame new_frame= new JFrame();
                                new_frame.setTitle("Multipong Intern");
                                new_frame.add(board);
                                new_frame.pack();
                                new_frame.setSize(Commons.WIDTH, Commons.HEIGTH);
                                new_frame.setLocationRelativeTo(null);
                                new_frame.setResizable(false);
                                new_frame.setVisible(true);
                                paddles = board.getPaddleArray();
                                game_start = true;
                            }

                        }

                    }
                }
                catch (IOException e)
                {
                }
            }
        });
        receive_message.start();
    }

    public void run_send () throws IOException{
        Thread send_message = new Thread(new Runnable() {
            public void run() {
                long beforeTime, timeDiff, sleep;

                beforeTime = System.currentTimeMillis();

                while (true) {
                    if(game_start==true) {
                        try {
                            //DatagramSocket socket = new DatagramSocket();
                            byte[] buf = new byte[256];
                            String temp = "1#ip=";
                            String my_id = temp.concat(my_ip).concat("#ps=").concat(String.valueOf(paddles[3].getPaddleSpeed())).concat("#pv=(")
                                    .concat(String.valueOf(paddles[3].getPaddleVelocity().X)).concat(",")
                                    .concat(String.valueOf(paddles[3].getPaddleVelocity().Y)).concat(")#time=")
                                    .concat(String.valueOf(java.lang.System.currentTimeMillis())).concat("#");
                            buf = my_id.getBytes();// here we want our ip-address instead
//                            System.out.println("sent_packet".concat(my_id));
                            for (int i = 0; i < number_of_players; i++) {
                                InetAddress address = InetAddress.getByName(ip_array[i]);
                                DatagramPacket packet = new DatagramPacket(buf, buf.length, address, port_array[i]);
                                socket1.send(packet);
                            }
                        } catch (IOException e) {

                        }
                    }
                    timeDiff = System.currentTimeMillis() - beforeTime;
                    sleep = DELAY - timeDiff;

                    if (sleep < 0) {
                        sleep = 2;
                    }

                    try {
                        Thread.sleep(sleep);
                    } catch (InterruptedException e) {
                        System.out.println("Interrupted: " + e.getMessage());
                    }

                    beforeTime = System.currentTimeMillis();
                }
            }
        });
        send_message.start();
    }
}

