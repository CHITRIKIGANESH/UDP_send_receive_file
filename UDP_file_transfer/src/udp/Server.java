import java.io.*;
import java.net.*;

public class Server2 {
    public static void main(String[] args) {
        int port = 12345; // Port to listen on
        int bufferSize = 256; // Adjust buffer size as needed

        try {
            DatagramSocket socket = new DatagramSocket(port);
            byte[] buffer = new byte[bufferSize];
            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            int packetCounter = 0;

            System.out.println("Server is running. Waiting for data...");

            // Receive the file name first
            DatagramPacket fileNamePacket = new DatagramPacket(buffer, buffer.length);
            socket.receive(fileNamePacket);
            packetCounter++;
            String fileName = new String(fileNamePacket.getData(), 0, fileNamePacket.getLength());

            // Send acknowledgment for the file name
            String ackMessage = "File name received";
            byte[] ackBytes = ackMessage.getBytes();
            DatagramPacket ackPacket = new DatagramPacket(ackBytes, ackBytes.length, fileNamePacket.getAddress(), fileNamePacket.getPort());
            socket.send(ackPacket);

            // Receive the file data
            while (true) {
                DatagramPacket packet = new DatagramPacket(buffer, buffer.length);
                socket.receive(packet);
                packetCounter++;

                outputStream.write(packet.getData(), 0, packet.getLength());

                // Send acknowledgment for every 5 packets
                if (packetCounter % 5 == 0) {
                    ackMessage = "Received " + packetCounter + " packets";
                    ackBytes = ackMessage.getBytes();
                    ackPacket = new DatagramPacket(ackBytes, ackBytes.length, packet.getAddress(), packet.getPort());
                    socket.send(ackPacket);
                    System.out.println("Acknowledgment sent for batch of 5 packets");
                }

                // Check if this is the last packet
                if (packet.getLength() < bufferSize) {
                    break;
                }
            }

            // If the last batch is less than 5 packets, still need to send an acknowledgment
            if (packetCounter % 5 != 0) {
                ackMessage = "Received final " + (packetCounter % 5) + " packets";
                ackBytes = ackMessage.getBytes();
                ackPacket = new DatagramPacket(ackBytes, ackBytes.length, ackPacket.getAddress(), ackPacket.getPort());
                socket.send(ackPacket);
                System.out.println("Acknowledgment sent for final batch of packets");
            }

            String receivedFilePath = fileName; // Use the received file name
            FileOutputStream fileOutputStream = new FileOutputStream(receivedFilePath);
            outputStream.writeTo(fileOutputStream);

            System.out.println("File received and saved as: " + receivedFilePath);

            fileOutputStream.close();
            outputStream.close();
            socket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
