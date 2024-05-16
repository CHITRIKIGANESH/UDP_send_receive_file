package udp1;

import java.io.*;
import java.net.*;

public class Server {
    public static void main(String[] args) {
        int port = 12345; // Port to listen on
        int bufferSize = 125; // Adjust buffer size as needed

        try {
            DatagramSocket socket = new DatagramSocket(port);
            byte[] buffer = new byte[bufferSize];
            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();

            System.out.println("Server is running. Waiting for data...");

            while (true) {
                DatagramPacket packet = new DatagramPacket(buffer, buffer.length);
                socket.receive(packet);

                outputStream.write(packet.getData(), 0, packet.getLength());

                // Check if this is the last packet
                if (packet.getLength() < bufferSize) {
                    break;
                }
            }

            String receivedFilePath = "received_file.bmp"; // Path to save the received file
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
