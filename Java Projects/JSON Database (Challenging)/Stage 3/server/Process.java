package server;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.concurrent.atomic.AtomicBoolean;

public class Process extends Thread {
    private Socket socket;
    private SimpleStorage s;
    private AtomicBoolean b;

    public Process(Socket socket, SimpleStorage s, AtomicBoolean b) {
        this.socket = socket;
        this.s = s;
        this.b = b;
    }

    @Override
    public void run() {
        try (DataInputStream dis = new DataInputStream(socket.getInputStream());
             DataOutputStream dos = new DataOutputStream(socket.getOutputStream())) {
            String[] inputs = dis.readUTF().split(" ", 3);
            String command = inputs[0];
            switch (command) {
                case "set":
                    dos.writeUTF(s.set(Integer.parseInt(inputs[1]), inputs[2]));
                case "get":
                    dos.writeUTF(s.get(Integer.parseInt(inputs[1])));
                    break;
                case "delete":
                    dos.writeUTF(s.delete(Integer.parseInt(inputs[1])));
                    break;
                case "exit":
                    b.set(false);
                    dos.writeUTF("OK");
            }
            socket.close();
        } catch (IOException e) {
            System.out.println("Error in run");
            System.out.println(e.getMessage());
        }
    }
}
