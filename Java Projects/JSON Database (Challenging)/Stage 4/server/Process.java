package server;

import com.google.gson.Gson;

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
            Gson gson = new Gson();
            JsonMessage message = gson.fromJson(dis.readUTF(), JsonMessage.class);
            switch (message.getType()) {
                case "set":
                    dos.writeUTF(s.set(message.getKey(), message.getValue()));
                case "get":
                    dos.writeUTF(s.get(message.getKey()));
                    break;
                case "delete":
                    dos.writeUTF(s.delete(message.getKey()));
                    break;
                case "exit":
                    b.set(false);
                    dos.writeUTF(new Gson().toJson(new JsonResponse("OK", null, null)));
            }
            socket.close();
        } catch (IOException e) {
            System.out.println("Error in run");
            System.out.println(e.getMessage());
        }
    }
}
