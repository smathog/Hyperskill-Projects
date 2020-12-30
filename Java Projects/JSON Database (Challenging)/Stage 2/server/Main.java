package server;

import java.io.IOException;
import java.net.InetAddress;
import java.net.ServerSocket;

public class Main {
    private static SimpleStorage s = new SimpleStorage();
    private static final String address = "127.0.0.1";
    private static final int port = 8080;

    public static void main(String[] args) {
        try (ServerSocket server = new ServerSocket(port, 50, InetAddress.getByName(address))) {
            System.out.println("Server started!");
            Process p = new Process(server.accept());
            p.start();
        } catch (IOException e) {
            System.out.println(e.getCause());
        }
    }

    private static void handleRequest(String[] inputs) {
        String command = inputs[0];
        switch (command) {
            case "set":
                s.set(Integer.parseInt(inputs[1]), inputs[2]);
                break;
            case "get":
                s.get(Integer.parseInt(inputs[1]));
                break;
            case "delete":
                s.delete(Integer.parseInt(inputs[1]));
                break;
            default:
                break;
        }
    }
}
