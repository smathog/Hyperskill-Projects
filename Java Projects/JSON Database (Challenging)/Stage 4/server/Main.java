package server;

import java.io.IOException;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.util.concurrent.atomic.AtomicBoolean;

public class Main {
    private static SimpleStorage s = new SimpleStorage();
    private static final String address = "127.0.0.1";
    private static final int port = 8080;

    public synchronized static void main(String[] args) {
        try (ServerSocket server = new ServerSocket(port, 50, InetAddress.getByName(address))) {
            AtomicBoolean loop = new AtomicBoolean(true);
            System.out.println("Server started!");
            while (loop.get()) {
                Process p = new Process(server.accept(), s, loop);
                p.start();
                try {
                    p.join();
                } catch (InterruptedException e) {
                    System.out.println(e.getMessage());
                }
            }
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
    }
}
