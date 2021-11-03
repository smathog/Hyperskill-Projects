package client;

public class Main {
    public static void main(String[] args) throws InterruptedException {
        Thread thread = new Thread(() -> {
            Client client = new Client("127.0.0.1", 23456);
            client.connectToServer();
        });
        thread.start();
        thread.join();
    }
}
