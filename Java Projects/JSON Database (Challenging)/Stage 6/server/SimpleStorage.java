package server;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

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
    private JsonObject jsonObject;
    private final ReentrantReadWriteLock lock;
    private static final Gson gson = new Gson();
    private static final String ERROR = "ERROR";
    private static final String OK = "OK";
    private static final String NSKERROR = "No such key";
    private static final String dbName = "C:\\Users\\Scott\\Desktop\\JSON Database\\JSON Database\\task\\src\\server\\data\\db.json";

    public SimpleStorage() throws IOException {
        lock = new ReentrantReadWriteLock();
        readFromFile();
    }

    public String set(JsonElement key, JsonElement s) {
        lock.writeLock().lock();
        JsonResponse jr = null;
        if (key.isJsonArray()) {
            JsonObject currentLevel = jsonObject;
            for (int i = 0; i < key.getAsJsonArray().size(); ++i) {
                if (!currentLevel.has(key.getAsJsonArray().get(i).getAsString())) {
                    if (i == key.getAsJsonArray().size() - 1)
                        currentLevel.add(key.getAsJsonArray().get(i).getAsString(), s);
                    else {
                        JsonObject nextLevel = new JsonObject();
                        currentLevel.add(key.getAsJsonArray().get(i).getAsString(), nextLevel);
                        currentLevel = nextLevel;
                    }
                } else {
                    if (i == key.getAsJsonArray().size() - 1) {
                        currentLevel.remove(key.getAsJsonArray().get(i).getAsString());
                        currentLevel.add(key.getAsJsonArray().get(i).getAsString(), s);
                    } else {
                        currentLevel = currentLevel.getAsJsonObject(key.getAsJsonArray().get(i).getAsString());
                    }
                }
            }
        } else
            jsonObject.add(key.getAsString(), s);
        jr = new JsonResponse(OK, null, null);
        writeToFile();
        lock.writeLock().unlock();
        return gson.toJson(jr);
    }

    public String get(JsonElement key) {
        lock.readLock().lock();
        JsonResponse jr = null;
        if (key.isJsonArray()) {
            JsonObject currentLevel = jsonObject;
            for (int i = 0; i < key.getAsJsonArray().size(); ++i) {//currentLevel jsonObject MUST have field matching key[i], or just return error
                if (!currentLevel.has(key.getAsJsonArray().get(i).getAsString())) {
                    jr = new JsonResponse(ERROR, NSKERROR, null);
                    break;
                } else if (i == key.getAsJsonArray().size() - 1) { //terminal condition!
                    jr = new JsonResponse(OK, null, currentLevel.get(key.getAsJsonArray().get(i).getAsString()));
                } else { //update loop iteration
                    currentLevel = currentLevel.get(key.getAsJsonArray().get(i).getAsString()).getAsJsonObject();
                }
            }
        } else { //must be single value string
            if (!jsonObject.has(key.getAsString()) || "".equals(jsonObject.get(key.getAsString()).getAsString()))
                jr = new JsonResponse(ERROR, NSKERROR, null);
            else
                jr = new JsonResponse(OK, null, jsonObject.get(key.getAsString()));
        }
        lock.readLock().unlock();
        return gson.toJson(jr);
    }

    public String delete(JsonElement key) {
        lock.writeLock().lock();
        JsonResponse jr = null;
        if (key.isJsonArray()) {
            JsonObject currentLevel = jsonObject;
            for (int i = 0; i < key.getAsJsonArray().size(); ++i) {//currentLevel jsonObject MUST have field matching key[i], or just return error
                if (!currentLevel.has(key.getAsJsonArray().get(i).getAsString())) {
                    jr = new JsonResponse(ERROR, NSKERROR, null);
                    break;
                } else if (i == key.getAsJsonArray().size() - 1) { //terminal condition! Delete and set response
                    currentLevel.remove(key.getAsJsonArray().get(i).getAsString());
                    jr = new JsonResponse(OK, null,null);
                } else { //update loop iteration
                    currentLevel = currentLevel.get(key.getAsJsonArray().get(i).getAsString()).getAsJsonObject();
                }
            }
        } else { //must be single value string
            if (!jsonObject.has(key.getAsString()) || "".equals(jsonObject.get(key.getAsString()).getAsString()))
                jr = new JsonResponse(ERROR, NSKERROR, null);
            else {
                jsonObject.remove(key.getAsString());
                jr = new JsonResponse(OK, null, null);
            }
        }
        writeToFile();
        lock.writeLock().unlock();
        return gson.toJson(jr);
    }

    private void writeToFile() {
        lock.writeLock().lock();
        try (FileOutputStream fos = new FileOutputStream(dbName);
             OutputStreamWriter isr = new OutputStreamWriter(fos, StandardCharsets.UTF_8)) {
            gson.toJson(jsonObject, isr);
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
                JsonElement je = JsonParser.parseReader(reader);
                if (je.isJsonNull())
                    jsonObject = new JsonObject();
                else
                    jsonObject = je.getAsJsonObject();
            } catch (IOException e) {
                System.out.println("Error reading from db.json");
                System.out.println(e.getMessage());
            } finally {
                lock.readLock().unlock();
            }
        } else {
            new File(dbName).createNewFile();
            jsonObject = new JsonObject();
        }
    }

}
