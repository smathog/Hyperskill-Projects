package blockchain;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        ArrayList<Block> blockList = (ArrayList<Block>) deserialize("blockchain.db");
        if (blockList == null)
            blockList = new ArrayList<>();
        Scanner scanner = new Scanner(System.in);
        System.out.print("Enter how many zeros the hash must start with: ");
        int num = Integer.parseInt(scanner.nextLine());
        for (int i = 0; i < 5; ++i) {
            Block newBlock;
            if (blockList.isEmpty())
                newBlock = new Block(num);
            else
                newBlock = new Block(blockList.get(blockList.size() - 1), num);
            System.out.println(newBlock);
            System.out.println();
            blockList.add(newBlock);
            serialize(blockList, "blockchain.db");
        }
    }

    private static void serialize(Object o, String fileName) {
        try (FileOutputStream fos = new FileOutputStream(fileName);
             BufferedOutputStream bos = new BufferedOutputStream(fos);
             ObjectOutputStream oos  = new ObjectOutputStream(bos)) {
            oos.writeObject(o);
        } catch (IOException e) {
            System.out.println("Error serializing Object to " + fileName + "!");
        }
    }

    private static Object deserialize(String fileName) {
        try (FileInputStream fis = new FileInputStream(fileName);
             BufferedInputStream bis = new BufferedInputStream(fis);
             ObjectInputStream ois = new ObjectInputStream(bis)) {
            return ois.readObject();
        } catch (IOException | ClassNotFoundException e) {
            System.out.println("Error deserializing from " + fileName);
            return null;
        }
    }
}
