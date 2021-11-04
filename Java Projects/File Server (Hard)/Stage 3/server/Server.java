package server;

import java.io.*;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Arrays;

public class Server {
    private String address;
    private int port;
    private int backlog;
    private Path directory;

    public Server(String address, int port, int backlog, String directory) {
        this.address = address;
        this.port = port;
        this.backlog = backlog;
        //Set up directory, if it does not yet exist:
        this.directory = Path.of(directory);
        try {
            if (Files.notExists(this.directory)) {
                Files.createDirectories(this.directory);
            }
        } catch (IOException e) {
            System.out.println("Error in Server constructor");
            System.out.println(e.getMessage());
            System.out.println(Arrays.toString(e.getStackTrace()));
        }
    }

    public void startServer() {
        System.out.println("Server started!");
        try (ServerSocket server = new ServerSocket(port, backlog, InetAddress.getByName(address))) {
            run(server);
        } catch (IOException e) {
            System.out.println("Error in startServer");
            System.out.println(e.getMessage());
            System.out.println(Arrays.toString(e.getStackTrace()));
        }
    }

    private void run(ServerSocket server) {
        while (true) {
        try (Socket socket = server.accept();
             DataInputStream input = new DataInputStream(socket.getInputStream());
             DataOutputStream output = new DataOutputStream(socket.getOutputStream())){
                // Wait for message from client:
                String message = input.readUTF();

                //If message is exit, close down:
                if ("exit".equals(message)) {
                    break;
                }

                //Otherwise, parse the request and return the response
                output.writeUTF(parseRequest(message));
            } catch (IOException e) {
                System.out.println("Error in run ");
                System.out.println(e.getMessage());
                System.out.println(Arrays.toString(e.getStackTrace()));
            }
        }
    }

    private String parseRequest(String request) throws IOException {
        String[] parts = request.split(" ", 3);
        Path file = this.directory.resolve(parts[1]);
        try {
            switch (parts[0]) {
                case "GET": {
                    if (Files.notExists(file)) {
                        return "404";
                    } else {
                        String content = Files.readString(file);
                        return String.format("200 %s", content);
                    }
                }
                case "PUT": {
                    if (Files.notExists(file)) {
                        try (BufferedWriter writer = Files.newBufferedWriter(file)) {
                            writer.write(parts[2]);
                            return "200";
                        }
                    } else {
                        return "403";
                    }
                }
                case "DELETE": {
                    return Files.deleteIfExists(file) ? "200" : "404";
                }
                default:
                    throw new IllegalArgumentException(String.format("Unknown command: %s%n", parts[0]));
            }
        } catch (IOException e) {
            System.out.println("Error in parseRequest");
            System.out.println(e.getMessage());
            System.out.println(Arrays.toString(e.getStackTrace()));
            throw e;
        }
    }
}
