import java.io.*;
import java.net.*;

public class Client2 {
    public static void main(String[] args) {
        String filePath = "img2.png"; // Path to your file
        String host = "localhost"; // Destination host
        int port = 12345; // Destination port
        int bufferSize = 256; // Adjust buffer size as needed

        try {
            DatagramSocket socket = new DatagramSocket();
            InetAddress address = InetAddress.getByName(host);

            File file = new File(filePath);
            FileInputStream fileInputStream = new FileInputStream(file);
            byte[] buffer = new byte[bufferSize];
            int bytesRead;
            int packetCounter = 0;

            // Send the file name first
            byte[] fileNameBytes = file.getName().getBytes();
            DatagramPacket fileNamePacket = new DatagramPacket(fileNameBytes, fileNameBytes.length, address, port);
            socket.send(fileNamePacket);

            // Receive acknowledgment for the file name
            byte[] ackBuffer = new byte[bufferSize];
            DatagramPacket ackPacket = new DatagramPacket(ackBuffer, ackBuffer.length);
            socket.receive(ackPacket);

            long totalStartTime = System.currentTimeMillis(); // Start measuring total time
            System.out.println(totalStartTime);

            // Send the file data
            while ((bytesRead = fileInputStream.read(buffer)) != -1) {
                DatagramPacket packet = new DatagramPacket(buffer, bytesRead, address, port);
                socket.send(packet);
                packetCounter++;

                // Send acknowledgment for every 5 packets
                if (packetCounter % 5 == 0) {
                    socket.receive(ackPacket);
                    String ackMessage = new String(ackPacket.getData(), 0, ackPacket.getLength());
                    System.out.println("Acknowledgment received for batch of 5 packets: " + ackMessage);
                }

                // Clear the buffer for the next packet
                buffer = new byte[bufferSize];
            }

            // If the last batch is less than 5 packets, still need an acknowledgment
            if (packetCounter % 5 != 0) {
                socket.receive(ackPacket);
                String ackMessage = new String(ackPacket.getData(), 0, ackPacket.getLength());
                System.out.println("Acknowledgment received for final batch of packets: " + ackMessage);
            }

            long totalEndTime = System.currentTimeMillis(); // End measuring total time
            System.out.println("Total time taken for file transfer: " + (totalEndTime - totalStartTime) + " ms");

            fileInputStream.close();
            socket.close();
            System.out.println("File sent successfully.");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
