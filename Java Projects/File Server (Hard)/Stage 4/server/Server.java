package server;

import java.io.*;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.HashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;

public class Server {
    private final String address;
    private final int port;
    private final int backlog;
    private final Path directory;
    private int fileID;
    private final Object dummyLock;
    private final HashMap<Integer, String> fileIdToNameMap;
    private final HashMap<String, Integer> nameToFieldIDMap;
    private final AtomicBoolean alive;

    public Server(String address, int port, int backlog, String directory) throws IOException, ClassNotFoundException {
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
        //Deserialize hashmaps, if possible
        if (Files.exists(this.directory.resolve("nameMap"))) {
            try (FileInputStream fos1 = new FileInputStream(this.directory + File.separator + "nameMap");
                 FileInputStream fos2 = new FileInputStream(this.directory + File.separator + "iDMap");
                 BufferedInputStream bos1 = new BufferedInputStream(fos1);
                 BufferedInputStream bos2 = new BufferedInputStream(fos2);
                 ObjectInputStream oos1 = new ObjectInputStream(bos1);
                 ObjectInputStream oos2 = new ObjectInputStream(bos2)) {
                nameToFieldIDMap = (HashMap<String, Integer>) oos1.readObject();
                fileIdToNameMap = (HashMap<Integer, String>) oos2.readObject();
            } catch (IOException | ClassNotFoundException e) {
                System.out.println("Error serializing hashmaps...");
                System.out.println(e.getMessage());
                System.out.println(Arrays.toString(e.getStackTrace()));
                throw e;
            }
        } else {
            //fileID's start from 1 and increase
            fileIdToNameMap = new HashMap<>();
            nameToFieldIDMap = new HashMap<>();
        }
        fileID = fileIdToNameMap.keySet().stream().max(Integer::compare).orElse(1);

        dummyLock = new Object();

        this.alive = new AtomicBoolean(true);
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
        int poolSize = Runtime.getRuntime().availableProcessors();
        ExecutorService executor = Executors.newFixedThreadPool(poolSize);
        while (this.alive.get()) {
            try {
                //Instance mutex around socket generation
                Socket socket;
                synchronized (dummyLock) {
                    socket = server.accept();
                }
                DataInputStream input = new DataInputStream(socket.getInputStream());
                DataOutputStream output = new DataOutputStream(socket.getOutputStream());

                //Submit to executor service:
                executor.submit(() -> {
                    try {
                        parseRequest(server, socket, input, output);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                });
            } catch (IOException e) {
                System.out.println("Error in run ");
                System.out.println(e.getMessage());
                System.out.println(Arrays.toString(e.getStackTrace()));
            }
        }
        executor.shutdown();
        while (true) {
            try {
                executor.awaitTermination(1, TimeUnit.SECONDS);
                break;
            } catch (InterruptedException ignored) {
            }
        }
    }

    //Note: parseRequest is responsible for closing all parameters!
    private void parseRequest(ServerSocket server, Socket socket, DataInputStream input, DataOutputStream output) throws IOException {
        String str = input.readUTF();
        synchronized (this) {
            if ("exit".equals(str)) {
                server.close();
                this.alive.set(false);
                //Must close all streams and sockets since try with resources block doesn't apply here:
                socket.close();
                input.close();
                output.close();
                serializeMaps();
                return;
            }
        }
        String[] parts = str.split(" ", 3);
        try (socket; input; output) {
            switch (parts[0]) {
                case "GET": {
                    String fileName = parts[2];
                    if ("BY_ID".equals(parts[1])) {
                        int id = Integer.parseInt(parts[2]);
                        //Must synchronize to prevent data race on hashmaps
                        synchronized (this) {
                            if (fileIdToNameMap.containsKey(id)) {
                                fileName = fileIdToNameMap.get(id);
                            } else {
                                output.writeUTF("404");
                                return;
                            }
                        }
                    }
                    Path file = this.directory.resolve(fileName);
                    if (Files.notExists(file)) {
                        output.writeUTF("404");
                    } else {
                        byte[] message = Files.readAllBytes(file);
                        output.writeUTF("200");
                        output.writeInt(message.length);
                        output.write(message);
                    }
                    break;
                }
                case "PUT": {
                    String fileName = parts[1];
                    Path file = this.directory.resolve(fileName);
                    int fileID;
                    //Must synchronize to prevent data race on hashmaps and ID generator variable
                    synchronized (this) {
                        if (Files.notExists(file) && !nameToFieldIDMap.containsKey(fileName)) {
                            nameToFieldIDMap.put(fileName, this.fileID);
                            fileIdToNameMap.put(this.fileID, fileName);
                            fileID = this.fileID;
                            ++this.fileID;
                        } else {
                            output.writeUTF("403");
                            return;
                        }
                    }
                    //Only at this point if the file didn't exist yet, so can just write it
                    byte[] message = new byte[input.readInt()];
                    try {
                        input.read(message);
                    } catch (IOException e) {
                        System.out.println(e.getMessage());
                        System.out.println(Arrays.toString(e.getStackTrace()));
                    }
                    Files.write(file, message);
                    output.writeUTF("200");
                    output.writeInt(fileID);
                    break;
                }
                case "DELETE": {
                    if ("BY_NAME".equals(parts[1])) {
                        if (deleteIfPresent(parts[2])) {
                            output.writeUTF("200");
                        } else {
                            output.writeUTF("404");
                        }
                    } else { // if ("BY_ID".equals(parts[1]))
                        if (deleteIfPresent(Integer.parseInt(parts[2]))) {
                            output.writeUTF("200");
                        } else {
                            output.writeUTF("404");
                        }
                    }
                    break;
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

    //Two delete methods must be synchronized to protect the HashMaps for reading and updating
    private synchronized boolean deleteIfPresent(int fileID) {
        if (fileIdToNameMap.containsKey(fileID)) {
            String fileName = fileIdToNameMap.get(fileID);
            try {
                Files.delete(this.directory.resolve(fileName));
                fileIdToNameMap.remove(fileID);
                nameToFieldIDMap.remove(fileName);
                return true;
            } catch (IOException e) {
                e.printStackTrace();
                return false;
            }
        } else {
            return false;
        }
    }

    private synchronized boolean deleteIfPresent(String fileName) {
        if (nameToFieldIDMap.containsKey(fileName)) {
            int id = nameToFieldIDMap.get(fileName);
            try {
                Files.delete(this.directory.resolve(fileName));
                nameToFieldIDMap.remove(fileName);
                fileIdToNameMap.remove(id);
                return true;
            } catch (IOException e) {
                e.printStackTrace();
                return false;
            }
        } else {
            return false;
        }
    }

    public void serializeMaps() {
        try (FileOutputStream fos1 = new FileOutputStream(this.directory + File.separator + "nameMap");
             FileOutputStream fos2 = new FileOutputStream(this.directory + File.separator + "iDMap");
             BufferedOutputStream bos1 = new BufferedOutputStream(fos1);
             BufferedOutputStream bos2 = new BufferedOutputStream(fos2);
             ObjectOutputStream oos1 = new ObjectOutputStream(bos1);
             ObjectOutputStream oos2 = new ObjectOutputStream(bos2)) {
            oos1.writeObject(nameToFieldIDMap);
            oos2.writeObject(fileIdToNameMap);
        } catch (IOException e) {
            System.out.println("Error serializing hashmaps...");
            System.out.println(e.getMessage());
            System.out.println(Arrays.toString(e.getStackTrace()));
        }
    }
}
