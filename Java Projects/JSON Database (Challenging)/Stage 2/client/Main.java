package client;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.InetAddress;
import java.net.Socket;

public class Main {
    private static final String address = "127.0.0.1";
    private static final int port = 8080;

    public static void main(String[] args) {
        System.out.println("Client started!");
        try (Socket socket = new Socket(InetAddress.getByName(address), port)) {
            try (DataInputStream dis = new DataInputStream(socket.getInputStream());
                 DataOutputStream dos = new DataOutputStream(socket.getOutputStream())) {
                String message = "Give me a record # 12";
                dos.writeUTF(message);
                System.out.println("Sent: " + message);
                message = dis.readUTF();
                System.out.println("Received: " + message);
            } catch (IOException e) {
                System.out.println("Error in client streams");
                System.out.println(e.getMessage());
            }
        } catch (IOException e) {
            System.out.println("Error in client socket creation");
            System.out.println(e.getMessage());
        }
    }
}
