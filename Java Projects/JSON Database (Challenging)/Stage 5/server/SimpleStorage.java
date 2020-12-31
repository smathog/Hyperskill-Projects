package server;

import com.google.gson.Gson;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.Reader;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.HashSet;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.locks.ReentrantReadWriteLock;


public class SimpleStorage {
    private HashMap<String, String> storage;
    private final ReentrantReadWriteLock lock;
    private static final Gson gson = new Gson();
    private static final String ERROR = "ERROR";
    private static final String OK = "OK";
    private static final String NSKERROR = "No such key";
    private static final String dbName = "C:\\Users\\Scott\\Desktop\\JSON Database\\JSON Database\\task\\src\\server\\data\\db.json";

    public SimpleStorage() throws IOException {
        storage = new HashMap<>();
        lock = new ReentrantReadWriteLock();
        readFromFile();
    }

    public String set(String key, String s) {
        lock.writeLock().lock();
        JsonResponse jr;
        storage.put(key, s);
        jr = new JsonResponse(OK, null, null);
        writeToFile();
        lock.writeLock().unlock();
        return gson.toJson(jr);
    }

    public String get(String key) {
        lock.readLock().lock();
        JsonResponse jr;
        if (!storage.containsKey(key) || "".equals(storage.get(key)))
            jr = new JsonResponse(ERROR, NSKERROR, null);
        else
            jr = new JsonResponse(OK, null, storage.get(key));
        lock.readLock().unlock();
        return gson.toJson(jr);
    }

    public String delete(String key) {
        lock.writeLock().lock();
        JsonResponse jr;
        if (storage.containsKey(key) && !"".equals(storage.get(key))) {
            storage.put(key, "");
            jr = new JsonResponse(OK, null, null);
            writeToFile();
        } else
            jr = new JsonResponse(ERROR, NSKERROR, null);
        lock.writeLock().unlock();
        return gson.toJson(jr);
    }

    private void writeToFile() {
        lock.writeLock().lock();
        try (FileOutputStream fos = new FileOutputStream(dbName);
             OutputStreamWriter isr = new OutputStreamWriter(fos, StandardCharsets.UTF_8)) {
            gson.toJson(storage, isr);
        } catch (IOException e) {
            System.out.println("Error writing to db.json");
            System.out.println(e.getMessage());
        } finally {
            lock.writeLock().unlock();
        }
    }

    private void readFromFile() throws IOException {
        if (new File(dbName).exists()) {
            lock.readLock().lock();
            Path path = new File(dbName).toPath();
            try (Reader reader = Files.newBufferedReader(path, StandardCharsets.UTF_8)) {
                storage = gson.fromJson(reader, HashMap.class);
            } catch (IOException e) {
                System.out.println("Error reading from db.json");
                System.out.println(e.getMessage());
            } finally {
                lock.readLock().unlock();
            }
        } else
            new File(dbName).createNewFile();
    }

}
