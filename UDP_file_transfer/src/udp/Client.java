package udp2;

import java.io.*;
import java.net.*;

public class Client2 {
    public static void main(String[] args) {
        String filePath = "img5.bmp"; // Path to your file
        String host = "127.0.0.1"; // Destination host
        int port = 12345; // Destination port
        int bufferSize = 125; // Adjust buffer size as needed

        try {
            DatagramSocket socket = new DatagramSocket();
            InetAddress address = InetAddress.getByName(host);

            File file = new File(filePath);
            FileInputStream fileInputStream = new FileInputStream(file);
            byte[] buffer = new byte[bufferSize];
            int bytesRead;

            // Send the file name first
            byte[] fileNameBytes = file.getName().getBytes();
            DatagramPacket fileNamePacket = new DatagramPacket(fileNameBytes, fileNameBytes.length, address, port);
            socket.send(fileNamePacket);

            // Send the file data
            while ((bytesRead = fileInputStream.read(buffer)) != -1) {
                DatagramPacket packet = new DatagramPacket(buffer, bytesRead, address, port);
                socket.send(packet);
                // Clear the buffer for the next packet
                buffer = new byte[bufferSize];
            }

            fileInputStream.close();
            socket.close();
            System.out.println("File sent successfully.");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
