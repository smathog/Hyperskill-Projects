package server;

public class Main {
    public static void main(String[] args) throws InterruptedException {
        Thread thread = new Thread(() -> {
            Server server = new Server("127.0.0.1", 23456, 50);
            server.startServer();
        });
        thread.start();
        thread.join();
    }
}