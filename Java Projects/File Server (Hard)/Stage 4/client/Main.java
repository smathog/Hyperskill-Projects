package client;

import java.io.File;

public class Main {
    public static void main(String[] args) throws InterruptedException {
        Thread thread = new Thread(() -> {
            String dirPath = System.getProperty("user.dir") +
                    File.separator + "src" + File.separator
                    + "client" + File.separator + "data" + File.separator;
            Client client = new Client("127.0.0.1", 23456, dirPath);
            client.connectToServer();
        });
        thread.start();
        thread.join();
    }
}
