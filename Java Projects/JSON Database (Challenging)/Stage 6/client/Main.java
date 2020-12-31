package client;

import com.beust.jcommander.*;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.JsonPrimitive;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.Reader;
import java.net.InetAddress;
import java.net.Socket;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Arrays;

public class Main {
    private static final String address = "127.0.0.1";
    private static final int port = 8080;
    private static final String folderName = "C:\\Users\\Scott\\Desktop\\JSON Database\\JSON Database\\task\\src\\client\\data\\";

    public static void main(String[] args) {
        System.out.println("Client started!");
        Args argv = new Args();
        JCommander.newBuilder()
                .addObject(argv)
                .build()
                .parse(args);
        JsonObject jo = new JsonObject();
        jo.addProperty("type", argv.getType());
        if (argv.hasInFile()) {
            Path path = new File(folderName + argv.getInFile()).toPath();
            try (Reader reader = Files.newBufferedReader(path, StandardCharsets.UTF_8)) {
                jo = JsonParser.parseReader(reader).getAsJsonObject();
            } catch (IOException e) {
                System.out.println("Error reading in file for client");
                System.out.println(e.getMessage());
                return;
            }
        } else {
            if (argv.hasIndex()) {
                if (argv.getIndex().matches("\\[.*\\]")) {
                    JsonArray ja = new JsonArray();
                    Arrays.stream(argv.getIndex().substring(1, argv.getIndex().length() - 1).split(","))
                            .map(JsonPrimitive::new)
                            .forEach(ja::add);
                    jo.add("key", ja);
                } else
                    jo.addProperty("key", argv.getIndex());
            }
            if (argv.hasMessage())
                jo.addProperty("value", argv.getMessage());
        }
        String message = jo.toString();
        try (Socket socket = new Socket(InetAddress.getByName(address), port)) {
            try (DataInputStream dis = new DataInputStream(socket.getInputStream());
                 DataOutputStream dos = new DataOutputStream(socket.getOutputStream())) {
                dos.writeUTF(message);
                System.out.println("Sent: " + message);
                System.out.println("Received: " + dis.readUTF());
            } catch (IOException e) {
                System.out.println("Error in client streams");
                System.out.println(e.getMessage());
            }
        } catch (IOException e) {
            System.out.println("Error in client socket creation");
            System.out.println(e.getMessage());
        }
    }
}
