package udp;

import java.io.*;
import java.net.*;

public class Client {
    public static void main(String args[]) throws Exception {
        DatagramSocket clientSocket = new DatagramSocket();
        InetAddress IPAddress = InetAddress.getByName("localhost");
        int serverPort = 9876;

        // Read the file into a byte array
        File file = new File("example.txt");
        FileInputStream fis = new FileInputStream(file);
        byte[] sendData = new byte[(int) file.length()];
        fis.read(sendData);
        fis.close();

        // Send the byte array over UDP
        DatagramPacket sendPacket = new DatagramPacket(sendData, sendData.length, IPAddress, serverPort);
        clientSocket.send(sendPacket);

        // Receive acknowledgment from the server
        byte[] receiveData = new byte[1024];
        DatagramPacket receivePacket = new DatagramPacket(receiveData, receiveData.length);
        clientSocket.receive(receivePacket);
        
        // Convert acknowledgment data to String and print it
        String ackMessage = new String(receivePacket.getData(), 0, receivePacket.getLength());
        System.out.println("ACK from server: " + ackMessage);

        clientSocket.close();
    }
}
