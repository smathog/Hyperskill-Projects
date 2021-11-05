package client;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.InetAddress;
import java.net.Socket;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.Scanner;

public class Client {
    private final String address;
    private final int port;
    private final Path myDir;

    public Client(String address, int port, String directory) {
        this.address = address;
        this.port = port;
        this.myDir = Paths.get(directory);
        //Set up directory, if it does not yet exist:
        try {
            if (Files.notExists(this.myDir)) {
                Files.createDirectories(this.myDir);
            }
        } catch (IOException e) {
            System.out.println("Error in Client constructor");
            System.out.println(e.getMessage());
            System.out.println(Arrays.toString(e.getStackTrace()));
        }
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
            switch (choice) {
                case "1": {
                    getFile(scanner, input, output);
                    break;
                }
                case "2": {
                    sendFile(scanner, input, output);
                    break;
                }
                case "3": {
                    deleteFile(scanner, input, output);
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

    private void getFile(Scanner scanner, DataInputStream input, DataOutputStream output) throws IOException {
        System.out.print("Do you want to get the file by name or by id (1 - name, 2 - id): ");
        int choice = Integer.parseInt(scanner.nextLine());
        if (choice == 1) {
            System.out.print("Enter name of the file: ");
            String name = scanner.nextLine();
            output.writeUTF(String.format("%s %s %s", "GET", "BY_NAME", name));
        } else if (choice == 2) {
            System.out.print("Enter id: ");
            String id = scanner.nextLine();
            output.writeUTF(String.format("%s %s %s", "GET", "BY_ID", id));
        }
        System.out.println("The request was sent.");
        String response = input.readUTF();
        if ("200".equals(response)) {
            int length = input.readInt();
            byte[] content = new byte[length];
            input.read(content);
            System.out.print("The file was downloaded! Specify a name for it: ");
            String name = scanner.nextLine();
            Files.write(this.myDir.resolve(name), content);
            System.out.println("File saved on the hard drive!");
        } else if ("404".equals(response)) {
            System.out.println("The response says that this file is not found!");
        }
    }

    private void sendFile(Scanner scanner, DataInputStream input, DataOutputStream output) throws IOException {
        System.out.print("Enter the name of the file: ");
        String name = scanner.nextLine();
        System.out.print("Enter the name of the file to be saved on server: ");
        String serverName = scanner.nextLine();
        if (serverName.isEmpty()) {
            serverName = name;
        }
        byte[] content = null;
        try {
            content = Files.readAllBytes(myDir.resolve(name));
        } catch (IOException e) {
            System.out.println("Error reading client file!");
            System.out.println(e.getMessage());
            System.out.println(Arrays.toString(e.getStackTrace()));
            System.out.println(Files.exists(myDir.resolve(name)));
        }
        output.writeUTF(String.format("%s %s", "PUT", serverName));
        output.writeInt(content.length);
        output.write(content);
        System.out.println("The request was sent.");
        String response = input.readUTF();
        if ("200".equals(response)) {
            int fileID = input.readInt();
            System.out.printf("Response says that file is saved! ID = %d%n", fileID);
        } else if ("403".equals(response)) {
            System.out.println("The response says that creating the file was forbidden!");
        }
    }

    private void deleteFile(Scanner scanner, DataInputStream input, DataOutputStream output) throws IOException {
        System.out.print("Do you want to delete the file by name or by id (1 - name, 2 - id): ");
        int choice = Integer.parseInt(scanner.nextLine());
        if (choice == 1) {
            System.out.print("Enter the name of the file: ");
            String name = scanner.nextLine();
            output.writeUTF(String.format("%s %s %s", "DELETE", "BY_NAME", name));
        } else if (choice == 2) {
            System.out.print("Enter id: ");
            String id = scanner.nextLine();
            output.writeUTF(String.format("%s %s %s", "DELETE", "BY_ID", id));
        }
        System.out.println("The request was sent.");
        String response = input.readUTF();
        if ("200".equals(response)) {
            System.out.println("The response says that the file was successfully deleted!");
        } else if ("404".equals(response)) {
            System.out.println("The response says that the file was not found!");
        }
    }
}
