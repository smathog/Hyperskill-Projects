package server;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;

public class Process extends Thread {
    private Socket socket;

    public Process(Socket socket) {
        this.socket = socket;
    }

    @Override
    public void run() {
        try (DataInputStream dis = new DataInputStream(socket.getInputStream());
             DataOutputStream dos = new DataOutputStream(socket.getOutputStream())) {
            String input = dis.readUTF();
            System.out.println("Received: " + input);
            String message = input.replace("Give me a ", "A ") + " was sent!";
            dos.writeUTF(message);
            System.out.println("Sent: " + message);
            socket.close();
        } catch (IOException e) {
            System.out.println("Error in run");
            System.out.println(e.getMessage());
        }
    }
}
