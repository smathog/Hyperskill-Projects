package server;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Arrays;

public class Server {
    private String address;
    private int port;
    private int backlog;

    public Server(String address, int port, int backlog) {
        this.address = address;
        this.port = port;
        this.backlog = backlog;
    }

    public void startServer() {
        System.out.println("Server started!");
        try (ServerSocket server = new ServerSocket(port, backlog, InetAddress.getByName(address));
             Socket socket = server.accept();
             DataInputStream input = new DataInputStream(socket.getInputStream());
             DataOutputStream output = new DataOutputStream(socket.getOutputStream())){
            String message = input.readUTF();
            System.out.printf("Received: %s%n", message);
            String outMessage = "All files were sent!";
            output.writeUTF(outMessage);
            System.out.printf("Sent: %s%n", outMessage);
        } catch (IOException e) {
            System.out.println(Arrays.toString(e.getStackTrace()));
        }
    }
}
