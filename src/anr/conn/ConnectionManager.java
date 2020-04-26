package anr.conn;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.swing.JOptionPane;

import anr.panels.GeneralPanel;
import anr.panels.TerminalPanel;
import anr.ui.GUIManager;

public class ConnectionManager {

    private static ConnectionManager instance = null;

    // UDP connection
    //private static final int SOCKET_TIMEOUT = 3000; // in milliseconds
    private static InetAddress ip;
    private static int port = 5007;
    private static int portOut = 8051;
    private static DatagramSocket socket;
    private StringBuilder consoleOutput = new StringBuilder();
    private SimpleDateFormat simpleDateFormat = new SimpleDateFormat("H:m:s");
    
    private ConnectionManager() 
    { 
        
    } 
  
    public static ConnectionManager getInstance() 
    { 
        if (instance == null) 
        instance = new ConnectionManager(); 
  
        return instance; 
    }

    /**
     * Sets up the UDP connection to the Unity client.
     */
    public void setupUdpConnection() {
        Thread receiveThread = null;

        try {
            // setup socket
            socket = new DatagramSocket(port, ip);
            ip = InetAddress.getByName("127.0.0.1"); // localhost
            // socket.setSoTimeout(SOCKET_TIMEOUT); // set the timeout in milliseconds

            receiveThread = new Thread(new Runnable() {
                @Override
                public void run() {
                    // setup receive udp packet
                    byte[] receiveData = new byte[1024];
                    DatagramPacket receivePacket = new DatagramPacket(receiveData, receiveData.length);

                    while (true) {
                        // receive data from Unity Client
                        try {
                            socket.receive(receivePacket);
                            String modifiedSentence = new String(receivePacket.getData(), receivePacket.getOffset(),
                                    receivePacket.getLength());

                            // console output
                            consoleOutput.insert(0,
                                    simpleDateFormat.format(new Date()) + " - From Unity: " + modifiedSentence + "\n");
                            TerminalPanel.getInstance().setTerminalText(consoleOutput.toString());

                            // evaluate the message from Unity
                            evaluateReceivedData(modifiedSentence);
                        } catch (IOException ioe) {
                            System.err.println(ioe.getMessage());
                        }
                    }
                }
            });
            receiveThread.start();

            sendData("connection", "start");

            while (true) {
                // keep connection
            }
        } catch (Exception e) {
            System.err.println(e.getMessage());
        } finally {
            System.out.println("Stopping Java Client ...");
            if (socket != null) {
                socket.close();
            }
        }
    }

    /**
     * Sends data to the Unity client.
     * 
     * @param topic      message topic
     * @param message    message content
     */
    public void sendData(String topic, String message) {
        String sendString = topic + ":" + message;
        byte[] sendData = new byte[1024];
        sendData = sendString.getBytes();
        DatagramPacket sendPacket = new DatagramPacket(sendData, sendData.length, ip, portOut);
        try {
            socket.send(sendPacket);
        } catch (IOException e) {
            System.err.println(e.getMessage());
        }

        // console output
        consoleOutput.insert(0, simpleDateFormat.format(new Date()) + " - To Unity: " + sendString + "\n");
        TerminalPanel.getInstance().setTerminalText(consoleOutput.toString());
    }

    /**
     * Evaluates the received data and returns a response if necessary.
     * 
     * @param _data  data from Unity client
     */
    public void evaluateReceivedData(String _data) {
        if (_data != null) {
            switch (_data) {
            case "condition_finished":
                GeneralPanel.getInstance().setDriveStatus("stop");
                sendData("startStop", "stop");

                // show alert
                GUIManager.getInstance().showWarningMessage("Finish", "The participant finished this condition.", JOptionPane.INFORMATION_MESSAGE);
                break;
            case "unity_app_started":
                GeneralPanel.getInstance().setConnectionStatus("connected");
                sendData("connection", "start");
                break;
            case "unity_app_connected":
            GeneralPanel.getInstance().setConnectionStatus("connected");
                break;
            case "unity_app_closed":
                GeneralPanel.getInstance().setConnectionStatus("disconnected");
                GeneralPanel.getInstance().setDriveStatus("stop");
                sendData("connection", "disconnected");

                // show alert
                GUIManager.getInstance().showWarningMessage("Connection", "The Unity Application disconnected.", JOptionPane.ERROR_MESSAGE);
                break;
            default:
                break;
            }
        }
    }
}