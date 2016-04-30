/**
 * Created by tarun on 16/4/16.
 */

// This class sends out data packets to the received ip's



import java.awt.*;
import java.net.*;
import java.awt.*;
import java.awt.event.*;
import javax.print.DocFlavor;
import javax.swing.*;
import javax.swing.event.*;
import java.io.*;
import java.util.ArrayList;

public class server extends gui implements Commons{


    static boolean game_start = false;
    public ButtonHandler bHandler;
    public ButtonHandler1 bHandler1;
    public ButtonHandler1 bHandler2;
    static DatagramSocket socket1;
    private final int DELAY = 100;
    String[] ip_array = new String[4];
    int[] port_array = new int[4];
    long[] last_connected = new long[4];// To determine disconnection
    Ball ball;


    int myid=0;// global player_id in the game
    //String my_ip = InetAddress.getLocalHost().getHostAddress().toString();

    String my_ip = "192.168.0.103";
    int my_port = 27015;

    int number_of_players=0;

    //Drawables and board paddles to be controlled
    static Board board;
    static Paddle[] paddles;


    Container container1;
    //acknowledgement for networking variables
    boolean host_connection = false;// To make sure that client is connected to host
    boolean[] start_connection = new boolean[4];// To make sure that game start command from host reaches everyone
    boolean[] id_exchange_connection = new boolean[4];// To make sure that all players exchange id's at game start
    boolean all_players_ready = false;
    Bot[] bot_array_multi;

    //Variables to be set when the player is in a single player game
    boolean single_player = false;
    boolean is_host = false;
    int current_host = 0;
    long time_array_score[] = new long[4];
    ArrayList<powerUp> powerUps;
    long last_power_up = 0;
    //ArrayList<powerUp> powerCollect;

    JFrame jf;

    public server (String title, String info) throws IOException
    {

        super (title);
        System.out.println(InetAddress.getLocalHost().getHostAddress());
        bHandler = new ButtonHandler ();
        bHandler1 = new ButtonHandler1();
        bHandler2 = new ButtonHandler1();
        startSingle.addActionListener (bHandler);
        startMultiple.addActionListener(bHandler1);
        startGroup.addActionListener(bHandler2);
        socket1 = new DatagramSocket (my_port);
        my_port = socket1.getLocalPort();
        System.out.println(my_port);
        myipArea.setText(info);
        hostipArea.setText(info);
        jf = this;
        container1 = this;
        for (int i=0;i<4;i++){
            ip_array[i] = "";
        }
        for (int i=0;i<4;i++){
            port_array[i] = 0;
        }
        for (int i=0;i<4;i++){
            last_connected[i]=0;
        }
        for (int i=0;i<4;i++){
            id_exchange_connection[i]=false;
        }
        for (int i=0;i<4;i++){
            start_connection[i]=false;
        }
        for(int i=0;i<4;i++){
            time_array_score[i] = 0;
        }
        bot_array_multi = new Bot[3];
        initUI();
    }


    //To decide difficulty level of bot
    int difficult;
    public server (String title) throws IOException
    {

        super (title);
        System.out.println(InetAddress.getLocalHost().getHostAddress());
        bHandler = new ButtonHandler ();
        bHandler1 = new ButtonHandler1();
        bHandler2 = new ButtonHandler1();
        startSingle.addActionListener (bHandler);
        startMultiple.addActionListener(bHandler1);
        startGroup.addActionListener(bHandler2);
        socket1 = new DatagramSocket ();
        my_port = socket1.getLocalPort();
        my_ip = myipArea.getText();
        container1 = this;
        for (int i=0;i<4;i++){
            ip_array[i] = "";
        }
        for (int i=0;i<4;i++){
            port_array[i] = 0;
        }
        for (int i=0;i<4;i++){
            last_connected[i]=0;
        }
        for (int i=0;i<4;i++){
            id_exchange_connection[i]=false;
        }
        for (int i=0;i<4;i++){
            start_connection[i]=false;
        }
        for(int i=0;i<4;i++){
            time_array_score[i] = 0;
        }
        bot_array_multi = new Bot[3];
        jf = this;
        initUI();
    }

    private void initUI() {

//        setSize(500, 500);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
    }


    //TODO Add Acknowledgment for this packet
    private class ButtonHandler1 implements ActionListener {
        public void actionPerformed(ActionEvent event) //throws IOException
        {
            int counter = 0;

            while (host_connection == false && counter <50) {
                try {
                    // here we send our ip and our port to the host
                    //DatagramSocket socket = new DatagramSocket ();
                    byte[] buf = new byte[256];
                    String ip_address = hostipArea.getText();
                    String temp = "0#ip=";//127.0.0.1
                    String my_ip1 = temp.concat(my_ip);
                    my_ip1 = my_ip1.concat("#port=").concat(String.valueOf(my_port)).concat("#time=").concat(String.valueOf(java.lang.System.currentTimeMillis())).concat("#");
                    //int port_send = Integer.parseInt(rxArea.getText());// here the port of the host has to be entered and read from the txArea
                    buf = my_ip1.getBytes();
                    int port_send = 12345;
                    InetAddress address = InetAddress.getByName(ip_address);
                    DatagramPacket packet = new DatagramPacket(buf, buf.length, address, port_send);
                    socket1.send(packet);
                    try {
                        Thread.sleep(100);
                    } catch (InterruptedException e) {
                        System.out.println("Interrupted: " + e.getMessage());
                    }
                } catch (IOException e) {
                    /*
                    try {
                        // here we send our ip and our port to the host
                        //DatagramSocket socket = new DatagramSocket ();
                        byte[] buf = new byte[256];
                        String ip_address = txArea.getText(); // here the ip_address of the host has to be entered and read from the txArea
                        String temp = "0#ip=";//127.0.0.1
                        String my_ip1 = temp.concat(my_ip);
                        my_ip1 = my_ip1.concat("#port=").concat(String.valueOf(my_port)).concat("#time=").concat(String.valueOf(java.lang.System.currentTimeMillis())).concat("#");
                        int port_send = Integer.parseInt(rxArea.getText());// here the port of the host has to be entered and read from the txArea
                        buf = my_ip1.getBytes();
                        InetAddress address = InetAddress.getByName(ip_address);
                        DatagramPacket packet = new DatagramPacket(buf, buf.length, address, port_send);
                        socket1.send(packet);
                    } catch (IOException exception) {

                    }
                    */
                }
                counter++;
            }
            if(counter==50){// COULD NOT CONNECT TO HOST
                System.out.println("COULD NOT CONNECT TO HOST");
            }
        }
    }

    //TODO Add Acknowledgement for this packet
    private class ButtonHandler implements ActionListener
    {
        public void actionPerformed (ActionEvent event) //throws IOException
        {
            // draw the board and start the game and send start event to the other machine
            while(all_players_ready==false && number_of_players!=0) {
                try {
                    //DatagramSocket socket = new DatagramSocket();
                    byte[] buf = new byte[256];
                    String temp = "2#num=".concat(String.valueOf(number_of_players)).concat("#");
                    for (int i = 1; i <= number_of_players; i++) {
                        temp = temp.concat("id").concat(String.valueOf(i)).concat("=").concat(String.valueOf(i)).concat("#ip".concat(String.valueOf(i))).concat("=").concat(ip_array[i]).
                                concat("#port".concat(String.valueOf(i))).concat("=").concat(String.valueOf(port_array[i])).concat("#");
                    }
                    temp = temp.concat("time=").concat(String.valueOf(java.lang.System.currentTimeMillis())).concat("#");
                    buf = temp.getBytes();// here we want our ip-address instead
                    System.out.println("start".concat(temp));

                    for (int i = 1; i <= number_of_players; i++) {
                        InetAddress address = InetAddress.getByName(ip_array[i]);
                        DatagramPacket packet = new DatagramPacket(buf, buf.length, address, port_array[i]);

                        socket1.send(packet);

                    }
                    try {
                        Thread.sleep(100);
                    } catch (InterruptedException e) {
                        System.out.println("Interrupted: " + e.getMessage());
                    }
                } catch (IOException e) {
                }
            }
            int x_velocity = (int) (3 * Math.random() * 4 - 4);
            int y_velocity = (int) (4 * Math.random() * 4 - 4);
            try {
                //DatagramSocket socket = new DatagramSocket();
                byte[] buf = new byte[256];
                String temp = "5#num=".concat(String.valueOf(number_of_players)).concat("#");
                temp = temp.concat("time=").concat(String.valueOf(java.lang.System.currentTimeMillis())).concat("#")
                        .concat("x_velocity=").concat(String.valueOf(x_velocity)).concat("#y_velocity=")
                        .concat(String.valueOf(y_velocity)).concat("#");
                buf = temp.getBytes();// here we want our ip-address instead

                for (int i = 1; i <= number_of_players; i++) {
                    System.out.println(temp);
                    InetAddress address = InetAddress.getByName(ip_array[i]);
                    DatagramPacket packet = new DatagramPacket(buf, buf.length, address, port_array[i]);
                    socket1.send(packet);
                }
            } catch (IOException e) {

            }
            if(number_of_players==0){
                single_player=true;
                is_host=true;
                my_ip = "127.0.0.1";
            }
            ip_array[myid] = my_ip;
            port_array[myid] = my_port;
            if(myid==0){
                is_host = true;
                current_host = 0;
            }
            container1.setVisible(false);

            board = new Board(myid,single_player,number_of_players);
            String difficulty = (String) cb.getSelectedItem();
            if (difficulty == "  1  ") difficult = 1;
            else if (difficulty == "  2  ") difficult = 2;
            else if (difficulty == "  3  ") difficult = 3;
            System.out.println("Here ------- "+difficult);
            board.setDifficult(difficult);

            jf.setVisible(false);
            JFrame new_frame= new JFrame();
            new_frame.add(board);
            new_frame.getContentPane().setPreferredSize(new Dimension(Commons.WIDTH,
                    Commons.HEIGHT));
            new_frame.pack();
//            new_frame.setSize(Commons.WIDTH,Commons.HEIGHT);
            new_frame.setLocationRelativeTo(null);
            new_frame.setResizable(false);
            new_frame.setVisible(true);
            paddles = board.getPaddleArray();
            board.setSET_KEY_LISTENER_ON(myid);
            /*
            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                System.out.println("Interrupted: " + e.getMessage());
            }
            */
            board.setMyid(myid);
            board.setNumber_of_players(number_of_players);
            board.setIs_host(is_host);
            ball = board.getball();
            ball.setInitialVelocity(x_velocity,y_velocity);
            board.setNetwork(socket1,number_of_players,port_array,myid,my_ip,ip_array);
            ball.setNetwork(socket1,number_of_players,port_array,myid,my_ip,ip_array);
            game_start = true;
        }
    }



    // Receive Message
    public void run_receive () throws IOException

    {
        final Thread receive_message = new Thread(new Runnable() {
            @Override
            public void run() {
                try
                {
                    DatagramPacket packet;
                    byte[] buf;
                    while (true)
                    {
                        buf = new byte[256];
                        packet = new DatagramPacket (buf, buf.length);
                        socket1.receive (packet);
                        String received = new String (packet.getData(),packet.getOffset(),packet.getLength());
                        System.out.println("received".concat(received));

                        if(received.length()!=0) {
                            //System.out.println("receiving".concat(received));
                            if (Integer.parseInt(Character.toString(received.charAt(0))) == 0) {//packet contains the ip
                                boolean contains=false;
                                String received_ip  = received.substring(received.indexOf("ip") + 3, received.indexOf("port") - 1);

                                for (int i=0;i<=number_of_players;i++){
                                    //TODO ERROR HERE
                                    if(ip_array[i].equals(received_ip)){
                                        contains = true;
                                    }
                                }
                                if(contains == false) {// host has sent id to be set in the client's machine and ip of host saved
                                    if (received.indexOf("your_id") != -1) {
                                        myid = Integer.parseInt(received.substring(received.indexOf("your_id") + 8, received.indexOf("my_id") - 1));
                                        ip_array[myid] = my_ip;
                                        port_array[myid] = my_port;
                                        int received_id =Integer.parseInt(received.substring(received.indexOf("my_id") + 6, received.indexOf("time") - 1));
                                        if(ip_array[received_id].equals("")) {
                                            ip_array[received_id] = received.substring(received.indexOf("ip") + 3, received.indexOf("port") - 1);
                                            port_array[received_id] = Integer.parseInt(received.substring(received.indexOf("port") + 5, received.indexOf("your_id") - 1));
                                            number_of_players += 1;
                                            long time_stamp = Long.parseLong(received.substring(received.indexOf("time")+5
                                                    ,received.indexOf("time")+received.substring(received.indexOf("time"),received.length()).indexOf("#")));
                                            last_connected[received_id] = time_stamp;
                                            if (received_id == 0) {// This packet is received from host and the host has received client ip
                                                host_connection = true;
                                            }
                                        }
                                        for (int i=0;i<10;i++) {
                                            try {

                                                buf = new byte[256];
                                                String temp = "3#type=0".concat("#ip=").concat(my_ip);
                                                String my_id = temp.concat("#time=").concat(String.valueOf(java.lang.System.currentTimeMillis())).concat("#");
                                                buf = my_id.getBytes();// here we want our ip-address instead
                                                //for (int i = 0; i < number_of_players; i++) {
                                                InetAddress address = InetAddress.getByName(received.substring(received.indexOf("ip") + 3, received.indexOf("port") - 1));
                                                packet = new DatagramPacket(buf, buf.length, address, Integer.parseInt(received.substring(received.indexOf("port") + 5, received.indexOf("your_id") - 1)));
                                                socket1.send(packet);
                                                System.out.println("sent".concat(my_id));
                                            } catch (IOException e) {

                                            }
                                        }

                                    }
                                    else if(received.indexOf("your_id") == -1) {
                                        number_of_players += 1;
                                        ip_array[number_of_players] = received.substring(received.indexOf("ip") + 3, received.indexOf("port") - 1);
                                        port_array[number_of_players] = Integer.parseInt(received.substring(received.indexOf("port") + 5, received.indexOf("time") - 1));

                                        long time_stamp = Long.parseLong(received.substring(received.indexOf("time")+5
                                                ,received.indexOf("time")+received.substring(received.indexOf("time"),received.length()).indexOf("#")));
                                        last_connected[number_of_players] = time_stamp;

                                        send_id_ack(number_of_players);
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

                                long time_stamp = Long.parseLong(received.substring(received.indexOf("time")+5
                                        ,received.indexOf("time")+received.substring(received.indexOf("time"),received.length()).indexOf("#")));
                                ip = received.substring(received.indexOf("ip") + 3, received.indexOf("ps") - 1);
                                for(int i=0;i<= number_of_players;i++){
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
                                if(received.indexOf("ball_speed")!=-1){
                                    //#ball_speed=4#ball_velocity_x=9#ball_velocity_y=-14#
                                    int ball_speed = Integer.parseInt(received.substring(received.indexOf("ball_speed")+11
                                            ,received.indexOf("ball_speed")+received.substring(received.indexOf("ball_speed"),received.length()).indexOf("#")));
                                    int ball_velocity_x = Integer.parseInt(received.substring(received.indexOf("ball_velocity_x")+16
                                            ,received.indexOf("ball_velocity_x")+received.substring(received.indexOf("ball_velocity_x"),received.length()).indexOf("#")));
                                    int ball_velocity_y = Integer.parseInt(received.substring(received.indexOf("ball_velocity_y")+16
                                            ,received.indexOf("ball_velocity_y")+received.substring(received.indexOf("ball_velocity_y"),received.length()).indexOf("#")));
                                    ball.setBallSpeed(ball_speed);
                                    ball.setBallVelocity(ball_velocity_x,ball_velocity_y);
                                }

                                //paddlevelocity_y = Integer.parseInt(Character.toString(received.charAt(received.indexOf("pv")+6)));

                                //paddles[id].setPaddleSpeed(paddlespeed);
                                //paddles[id].setPaddleVelocity(paddlevelocity_x,paddlevelocity_y);
                                last_connected[id] = time_stamp;
                                board.setPaddleArray(id,paddlespeed,paddlevelocity_x,paddlevelocity_y);

                            } else if (Integer.parseInt(Character.toString(received.charAt(0))) == 2) {
                                //Acknowledge to start the game
                                // Game Start
                                // Eg :- 2#num=3#ip1=127.0.0.1#port1=#ip2=#port2=#ip3=#port3=#
                                int number = Integer.parseInt(Character.toString(received.charAt(received.indexOf("num")+4)));
                                long time_stamp = Long.parseLong(received.substring(received.indexOf("time")+5
                                        ,received.indexOf("time")+received.substring(received.indexOf("time"),received.length()).indexOf("#")));
                                // THIS MAY CHANGE
                                last_connected[0] = time_stamp;
                                for(int i=1;i<=number;i++){

                                    int port = Integer.parseInt(received.substring(received.indexOf("port".concat(String.valueOf(i))) + 6,
                                            received.substring(received.indexOf("port".concat(String.valueOf(i)))
                                                    ,received.length()).indexOf("#")+received.indexOf("port".concat(String.valueOf(i)))));
                                    //System.out.println("port".concat(String.valueOf(port)));
                                    String ip = received.substring(received.indexOf("ip".concat(String.valueOf(i)))+4,
                                            received.indexOf("port".concat(String.valueOf(i)))-1);
                                    int player_id = i;
                                    //System.out.println("ip".concat(ip));
                                    if(ip.equals(my_ip)==false){
                                        try {//TODO Add Acknowledgement for these packets
                                            buf = new byte[256];
                                            String temp = "0#ip=".concat(my_ip).concat("#port=").concat(String.valueOf(my_port));
                                            temp = temp.concat("#your_id=").concat(String.valueOf(player_id)).concat("#my_id=").concat(String.valueOf(myid));
                                            String my_id = temp.concat("#time=").concat(String.valueOf(java.lang.System.currentTimeMillis())).concat("#");
                                            buf = my_id.getBytes();// here we want our ip-address instead
                                            InetAddress address = InetAddress.getByName(ip);
                                            packet = new DatagramPacket(buf, buf.length, address,port);
                                            socket1.send(packet);
                                        }

                                        catch (IOException e)
                                        {

                                        }

                                    }
                                    for(int pa=0;pa<10;pa++) {
                                        try {
                                            buf = new byte[256];
                                            String temp = "3#type=2".concat("#ip=").concat(my_ip);
                                            String my_id = temp.concat("#time=").concat(String.valueOf(java.lang.System.currentTimeMillis())).concat("#");
                                            buf = my_id.getBytes();// here we want our ip-address instead
                                            InetAddress address = InetAddress.getByName(ip_array[0]);
                                            packet = new DatagramPacket(buf, buf.length, address, port_array[0]);
                                            socket1.send(packet);
                                            System.out.println(my_id);
                                        } catch (IOException e) {

                                        }
                                    }
                                }

                            }

                            else if (Integer.parseInt(Character.toString(received.charAt(0))) == 3){
                                // This new type of packet sends ack on receiving packets of type 0 and type 2
                                // type 0 for the global id distribution packet
                                // type 2 for the game start packet
                                // And the packets are resent until these ack are received
                                // Eg 3#type=0#ip=ip#time=time#
                                System.out.println(received);
                                String received_ip = received.substring(received.indexOf("ip")+3
                                        ,received.indexOf("time")-1);
                                long time_stamp = Long.parseLong(received.substring(received.indexOf("time")+5
                                        ,received.indexOf("time")+received.substring(received.indexOf("time"),received.length()).indexOf("#")));
                                int type = Integer.parseInt(Character.toString(received.charAt(received.indexOf("type")+5)));
                                int id = 0;
                                for (int i=0;i<4;i++)
                                {
                                    if(received_ip.equals(ip_array[i])){
                                        id=i;
                                        break;
                                    }
                                }
                                System.out.println(id);
                                last_connected[id] = time_stamp;
                                if(type==0){
                                    id_exchange_connection[id]=true;
                                }
                                else if(type==2){
                                    start_connection[id]=true;
                                }
                                else if(type==3){
                                    board.setPower_packet_sent(true,id);
                                }
                                //boolean all_players_ready = true;
                                boolean temp=true;
                                for(int i=1;i<=number_of_players;i++){
                                    temp = temp && start_connection[i];
                                }
                                if(temp==true){
                                    all_players_ready = true;
                                }

                            }
                            else if(Integer.parseInt(Character.toString(received.charAt(0))) == 4){
                                //This packet holds the ip_address of the player that has disconnected from the game
                                //This packet is sent by the machine which detects that a certain player has disconnected
                                //The paddle associated with the ip_address of disconnected machine is replaced by a bot
                                //Eg:- 4#from=ip_address#disconnect=#time=#
                                String disconnected = received.substring(received.indexOf("disconnect")+11,received.indexOf("time")-1);
                                int id=0;
                                for (int i=0;i<4;i++)
                                {
                                    if(disconnected.equals(ip_array[i])){
                                        id=i;
                                        System.out.println("disconnected_machine".concat(String.valueOf(id)));
                                        break;
                                    }
                                }

                                if(paddles[id].getIsBot()==false) {
                                    board.add_bot(id);
                                    board.disconnect(id);
                                }

                            }
                            else if(Integer.parseInt(Character.toString(received.charAt(0))) == 5){
                                long time_stamp = Long.parseLong(received.substring(received.indexOf("time")+5
                                        ,received.indexOf("time")+received.substring(received.indexOf("time"),received.length()).indexOf("#")));
                                int x_velocity = Integer.parseInt(received.substring(received.indexOf("x_velocity")+11
                                        ,received.indexOf("x_velocity")+received.substring(received.indexOf("x_velocity"),received.length()).indexOf("#")));
                                int y_velocity = Integer.parseInt(received.substring(received.indexOf("y_velocity")+11
                                        ,received.indexOf("y_velocity")+received.substring(received.indexOf("y_velocity"),received.length()).indexOf("#")));
                                last_connected[0] = time_stamp;
                                container1.setVisible(false);
                                single_player=false;
                                current_host = 0;
                                is_host = false;
                                board = new Board(myid,single_player,number_of_players);
                                String difficulty = (String) cb.getSelectedItem();
                                if (difficulty == "  1  ") difficult = 1;
                                else if (difficulty == "  2  ") difficult = 2;
                                else if (difficulty == "  3  ") difficult = 3;
                                System.out.println("Here ------- "+difficult);
                                board.setDifficult(difficult);
                                jf.setVisible(false);
                                JFrame new_frame= new JFrame();
                                new_frame.setTitle("Multipong");
                                new_frame.getContentPane().setPreferredSize(new Dimension(Commons.WIDTH,
                                        Commons.HEIGHT));
                                new_frame.add(board);
                                new_frame.pack();
//                                new_frame.setSize(Commons.WIDTH,Commons.HEIGHT);
                                new_frame.setLocationRelativeTo(null);
                                new_frame.setResizable(false);
                                new_frame.setVisible(true);
                                paddles = board.getPaddleArray();
                                board.setSET_KEY_LISTENER_ON(myid);
                                board.setIs_host(is_host);
                                board.setMyid(myid);
                                board.setNumber_of_players(number_of_players);
                                ball = board.getball();
                                ball.setInitialVelocity(x_velocity,y_velocity);
                                board.setNetwork(socket1,number_of_players,port_array,myid,my_ip,ip_array);
                                ball.setNetwork(socket1,number_of_players,port_array,myid,my_ip,ip_array);
                                game_start = true;
                            }
                            else if(Integer.parseInt(Character.toString(received.charAt(0))) == 6){
                                long time_stamp = Long.parseLong(received.substring(received.indexOf("time")+5
                                        ,received.indexOf("time")+received.substring(received.indexOf("time"),received.length()).indexOf("#")));
                                int received_player_id = Integer.parseInt(received.substring(received.indexOf("my_id")+6,received.indexOf("score_id")-1));
                                String received_ip = received.substring(received.indexOf("my_ip")+6,received.indexOf("my_id")-1);
                                int score_id = Integer.parseInt(received.substring(received.indexOf("score_id")+9,received.indexOf("time")-1));
                                if (score_id == 3) score_id = 4;
                                if (score_id == 4) score_id = 3;
                                last_connected[received_player_id] = time_stamp;
                                if(java.lang.System.currentTimeMillis()-time_array_score[score_id]>=1000){
                                    board.reduce_lives(score_id);
                                    time_array_score[score_id] = java.lang.System.currentTimeMillis();
                                }


                            }
                            else if(Integer.parseInt(Character.toString(received.charAt(0))) == 7){
                                String disconnected = received.substring(received.indexOf("from")+5,received.indexOf("new_host")-1);
                                int id=0;
                                for (int i=0;i<4;i++)
                                {
                                    if(disconnected.equals(ip_array[i])){
                                        id=i;
                                        System.out.println("new_host".concat(String.valueOf(id)));
                                        break;
                                    }
                                }
                                current_host = id;
                                if(current_host==myid){
                                    is_host =true;
                                    board.setIs_host(true);
                                }

                            }
                            else if(Integer.parseInt(Character.toString(received.charAt(0))) == 8){
                                int type = Integer.parseInt(received.substring(received.indexOf("type")+5
                                        ,received.indexOf("type")+received.substring(received.indexOf("type"),received.length()).indexOf("#")));
                                int x = Integer.parseInt(received.substring(received.indexOf("x_position")+11
                                        ,received.indexOf("x_position")+received.substring(received.indexOf("x_position"),received.length()).indexOf("#")));
                                int y=Integer.parseInt(received.substring(received.indexOf("y_position")+11
                                        ,received.indexOf("y_position")+received.substring(received.indexOf("y_position"),received.length()).indexOf("#")));
                                long time_stamp = Long.parseLong(received.substring(received.indexOf("time_stamp")+11
                                        ,received.indexOf("time_stamp")+received.substring(received.indexOf("time_stamp"),received.length()).indexOf("#")));
                                if(time_stamp-last_power_up>=100){
                                    board.add_powerup(type,x,y,time_stamp);
                                }
                                last_power_up = time_stamp;
                                for(int pa=0;pa<10;pa++) {
                                    try {
                                        buf = new byte[256];
                                        String temp = "3#type=3".concat("#ip=").concat(my_ip);
                                        String my_id = temp.concat("#time=").concat(String.valueOf(java.lang.System.currentTimeMillis())).concat("#");
                                        buf = my_id.getBytes();// here we want our ip-address instead
                                        InetAddress address = InetAddress.getByName(ip_array[current_host]);
                                        packet = new DatagramPacket(buf, buf.length, address, port_array[current_host]);
                                        socket1.send(packet);
                                        System.out.println(my_id);
                                    } catch (IOException e) {

                                    }
                                }


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
                    if(game_start==true && number_of_players!=0) {
                        try {
                            //DatagramSocket socket = new DatagramSocket();
                            byte[] buf = new byte[256];
                            String temp = "1#ip=";
                            String my_id = temp.concat(my_ip).concat("#ps=").concat(String.valueOf(paddles[myid].getPaddleSpeed())).concat("#pv=(")
                                    .concat(String.valueOf(paddles[myid].getPaddleVelocity().X)).concat(",")
                                    .concat(String.valueOf(paddles[myid].getPaddleVelocity().Y)).concat(")#time=")
                                    .concat(String.valueOf(java.lang.System.currentTimeMillis())).concat("#");
                            if(is_host==true) {//To check if we have to send ball speed and velocity

                                my_id = my_id.concat("ball_speed=")
                                        .concat(String.valueOf(ball.getBallSpeed()))
                                        .concat("#ball_velocity_x=").concat(String.valueOf(ball.getBallVelocity().X))
                                        .concat("#ball_velocity_y=").concat(String.valueOf(ball.getBallVelocity().Y)).concat("#");
                            }
                            buf = my_id.getBytes();// here we want our ip-address instead
                            System.out.println("sent".concat(my_id));
                            for (int i = 0; i <= number_of_players; i++) {
                                if(i!=myid){
                                    InetAddress address = InetAddress.getByName(ip_array[i]);
                                    DatagramPacket packet = new DatagramPacket(buf, buf.length, address, port_array[i]);
                                    socket1.send(packet);
                                }
                            }
                        } catch (IOException e) {
                            System.out.println("Empty ip address1");
                        }
                        try{
                            //Logic to detect which machine has disconnected
                            String disconnect = "";
                            int id_disconnect = myid;
                            for(int i=0;i<=number_of_players;i++){
                                if(i!=myid && ((java.lang.System.currentTimeMillis())-last_connected[i]>=5000)){
                                    disconnect = ip_array[i];
                                    id_disconnect = i;
                                    break;
                                }
                            }
                            if(id_disconnect==current_host && id_disconnect!=myid){
                                current_host = myid;
                                is_host = true;
                                board.setIs_host(true);
                                try{
                                    byte[] buf = new byte[256];
                                    String temp = "7#from=".concat(my_ip).concat("#new_host=").concat("#");
                                    String my_id = temp.concat("time=")
                                            .concat(String.valueOf(java.lang.System.currentTimeMillis())).concat("#");
                                    buf = my_id.getBytes();// here we want our ip-address instead

                                    for (int i = 0; i <= number_of_players; i++) {
                                        if (i!=id_disconnect && i!=myid) {
                                            System.out.println("sent".concat(my_id));
                                            InetAddress address = InetAddress.getByName(ip_array[i]);
                                            DatagramPacket packet = new DatagramPacket(buf, buf.length, address, port_array[i]);
                                            socket1.send(packet);
                                        }
                                    }
                                }
                                catch (IOException e) {
                                    System.out.println("Empty ip address2");
                                }
                            }

                            if(!disconnect.equals("")) {
                                byte[] buf = new byte[256];
                                String temp = "4#from=".concat(my_ip).concat("#disconnect=").concat(disconnect);
                                String my_id = temp.concat("#time=")
                                        .concat(String.valueOf(java.lang.System.currentTimeMillis())).concat("#");
                                buf = my_id.getBytes();// here we want our ip-address instead
                                if(paddles[id_disconnect].getIsBot()==false) {
                                    board.add_bot(id_disconnect);
                                    board.disconnect(id_disconnect);
                                }
                                System.out.println("sent".concat(my_id));
                                for (int i = 0; i <= number_of_players; i++) {
                                    if (i!=id_disconnect && i!=myid) {

                                        InetAddress address = InetAddress.getByName(ip_array[i]);
                                        DatagramPacket packet = new DatagramPacket(buf, buf.length, address, port_array[i]);
                                        socket1.send(packet);
                                    }
                                }

                            }
                        }
                        catch (IOException e) {
                            System.out.println("Empty ip address3");
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

    private void  send_id_ack(int number) {

        final int num = number;
        final Thread listen_to_ack = new Thread(new Runnable() {
            @Override
            public void run() {
                long beforeTime, timeDiff, sleep;

                beforeTime = System.currentTimeMillis();
                while(id_exchange_connection[num]==false){

                    try {//TODO Add Acknowledgement for this packet
                        // DONE
                        //DatagramSocket socket = new DatagramSocket();
                        byte[] buf = new byte[256];
                        DatagramPacket packet;
                        String temp = "0#ip=".concat(my_ip).concat("#port=").concat(String.valueOf(my_port));
                        temp = temp.concat("#your_id=").concat(String.valueOf(num)).concat("#my_id=").concat(String.valueOf(myid));
                        String my_id = temp.concat("#time=").concat(String.valueOf(java.lang.System.currentTimeMillis())).concat("#");
                        buf = my_id.getBytes();// here we want our ip-address instead

                        InetAddress address = InetAddress.getByName(ip_array[num]);
                        packet = new DatagramPacket(buf, buf.length, address, port_array[num]);
                        socket1.send(packet);
                        System.out.println("sent".concat(my_id));
                        try {
                            Thread.sleep(100);
                        } catch (InterruptedException e) {
                            System.out.println("Interrupted: " + e.getMessage());
                        }
                        //}
                    } catch (IOException e) {

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
        listen_to_ack.start();
    }

}

