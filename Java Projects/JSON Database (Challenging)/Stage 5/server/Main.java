package server;

import java.io.IOException;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketTimeoutException;
import java.util.concurrent.atomic.AtomicBoolean;

public class Main {
    private static SimpleStorage s;
    private static final String address = "127.0.0.1";
    private static final int port = 8080;

    public synchronized static void main(String[] args) throws IOException {
        s = new SimpleStorage();
        try (ServerSocket server = new ServerSocket(port, 50, InetAddress.getByName(address))) {
            AtomicBoolean loop = new AtomicBoolean(true);
            server.setSoTimeout(10);
            System.out.println("Server started!");
            while (loop.get()) {
                try {
                    Socket socket = server.accept();
                    Process p = new Process(socket, s, loop);
                    p.start();
                } catch (SocketTimeoutException e) {
                    continue; //By now loop should contain false and loop should terminate
                }
            }
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
    }
}
