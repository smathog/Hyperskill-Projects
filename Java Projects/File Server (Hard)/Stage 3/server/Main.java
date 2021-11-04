package server;

import java.io.File;

public class Main {
    public static void main(String[] args) throws InterruptedException {
        Thread thread = new Thread(() -> {
            String dirPath = System.getProperty("user.dir") + File.separator + "src" + File.separator
                    + "server" + File.separator + "data" + File.separator;
            Server server = new Server("127.0.0.1", 23456, 50, dirPath);
            server.startServer();
        });
        thread.start();
        thread.join();
    }
}