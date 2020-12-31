package server;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.concurrent.atomic.AtomicBoolean;

public class Process extends Thread {
    private final Socket socket;
    private final SimpleStorage s;
    private final AtomicBoolean b;

    public Process(Socket socket, SimpleStorage s, AtomicBoolean b) {
        this.socket = socket;
        this.s = s;
        this.b = b;
    }

    @Override
    public void run() {
        try (DataInputStream dis = new DataInputStream(socket.getInputStream());
             DataOutputStream dos = new DataOutputStream(socket.getOutputStream())) {
            JsonObject jo = JsonParser.parseString(dis.readUTF()).getAsJsonObject();
            switch (jo.get("type").getAsString()) {
                case "set":
                    dos.writeUTF(s.set(jo.get("key"), jo.get("value")));
                    break;
                case "get":
                    dos.writeUTF(s.get(jo.get("key")));
                    break;
                case "delete":
                    dos.writeUTF(s.delete(jo.get("key")));
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
