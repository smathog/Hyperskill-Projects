package client;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.InetAddress;
import java.net.Socket;
import java.util.Arrays;
import java.util.Scanner;

public class Client {
    private String address;
    private int port;

    public Client(String address, int port) {
        this.address = address;
        this.port = port;
    }

    public void connectToServer() {
        while (true) {
            try (Socket socket = new Socket(InetAddress.getByName(address), port)) {
                communicateWithServer(socket);
                break;
            } catch (IOException e) {
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException ex) {
                    ex.printStackTrace();
                    break;
                }

            }
        }
    }

    private void communicateWithServer(Socket socket) {
        try (DataInputStream input = new DataInputStream(socket.getInputStream());
             DataOutputStream output = new DataOutputStream(socket.getOutputStream())) {
            Scanner scanner = new Scanner(System.in);
            System.out.print("Enter action (1 - get a file, 2 - create a file, 3 - delete a file): ");
            String choice = scanner.nextLine();
            if ("exit".equals(choice)) {
                output.writeUTF(choice);
                System.out.println("The request was sent.");
                return;
            }
            System.out.print("Enter filename: ");
            String name = scanner.nextLine();
            switch (choice) {
                case "1": {
                    output.writeUTF(String.format("%s %s", "GET", name));
                    System.out.print("The request was sent.");
                    String[] response = input.readUTF().split(" ", 2);
                    if ("200".equals(response[0])) {
                        System.out.printf("The content of the file is: %s%n", response[1]);
                    } else if ("404".equals(response[0])) {
                        System.out.println("The response says that the file was not found!");
                    }
                    break;
                }
                case "2": {
                    System.out.print("Enter file content: ");
                    String content = scanner.nextLine();
                    output.writeUTF(String.format("%s %s %s", "PUT", name, content));
                    System.out.println("The request was sent.");
                    String response = input.readUTF();
                    if ("200".equals(response)) {
                        System.out.println("The response says that the file was created!");
                    } else if ("403".equals(response)) {
                        System.out.println("The response says that creating the file was forbidden!");
                    }
                    break;
                }
                case "3": {
                    output.writeUTF(String.format("%s %s", "DELETE", name));
                    System.out.println("The request was sent.");
                    String response = input.readUTF();
                    if ("200".equals(response)) {
                        System.out.println("The response says that the file was successfully deleted!");
                    } else if ("404".equals(response)) {
                        System.out.println("The response says that the file was not found!");
                    }
                    break;
                }
                default:
                    break;
            }
        } catch (IOException e) {
            System.out.println(e.getMessage());
            System.out.println(Arrays.toString(e.getStackTrace()));
        }
    }
}
