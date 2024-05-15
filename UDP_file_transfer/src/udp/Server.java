package udp;

import java.io.*;
import java.net.*;

public class Server {
    public static void main(String args[]) throws Exception {
        try (DatagramSocket serverSocket = new DatagramSocket(9876)) {
			byte[] receiveData = new byte[10244];

			while (true) {
			    DatagramPacket receivePacket = new DatagramPacket(receiveData, receiveData.length);
			    serverSocket.receive(receivePacket);

			    // Reassemble the byte array into a file
			    ByteArrayInputStream byteStream = new ByteArrayInputStream(receivePacket.getData());
			    BufferedReader reader = new BufferedReader(new InputStreamReader(byteStream));
			    StringBuilder fileContent = new StringBuilder();
			    String line;
			    while ((line = reader.readLine()) != null) {
			        fileContent.append(line).append("\n");
			    }
			    reader.close();

			    // Store the received file content into a new text file
			    String fileName = "received_file.txt";
			    BufferedWriter writer = new BufferedWriter(new FileWriter(fileName));
			    writer.write(fileContent.toString());
			    writer.close();

			    
			    // Send acknowledgment back to the client
			    InetAddress clientIPAddress = receivePacket.getAddress();
			    int clientPort = receivePacket.getPort();
			    String ackMessage = "File received successfully!";
			    byte[] ackData = ackMessage.getBytes();
			    DatagramPacket ackPacket = new DatagramPacket(ackData, ackData.length, clientIPAddress, clientPort);
			    serverSocket.send(ackPacket);
			}
		}
    }
}
