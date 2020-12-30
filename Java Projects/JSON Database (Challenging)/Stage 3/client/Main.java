package client;

import com.beust.jcommander.*;

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
        Args argv = new Args();
        JCommander.newBuilder()
                .addObject(argv)
                .build()
                .parse(args);
        String message = argv.getType() + " " + argv.getIndex() + (argv.hasMessage() ? " " + argv.getMessage() : "");
        try (Socket socket = new Socket(InetAddress.getByName(address), port)) {
            try (DataInputStream dis = new DataInputStream(socket.getInputStream());
                 DataOutputStream dos = new DataOutputStream(socket.getOutputStream())) {
                dos.writeUTF(message);
                System.out.println("Sent: " + message);
                System.out.println("Received: " + dis.readUTF());
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
