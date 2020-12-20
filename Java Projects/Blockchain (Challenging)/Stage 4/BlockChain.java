package blockchain;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.ArrayList;

public class BlockChain implements Serializable {
    public final static long serialVersionUID = 6L;

    private transient Object lock = new Object();

    private final ArrayList<Block> blockList;
    private ArrayList<String> messages;
    private ArrayList<String> mailbox;
    private final String fileName;
    private int currentN;

    public BlockChain(String fileName) {
        this.fileName = fileName;
        blockList = new ArrayList<>();
        messages = null;
        mailbox = new ArrayList<>();
        currentN = 0;
    }

    public void submitMessage(String message) {
        synchronized (lock) {
            mailbox.add(message);
        }
    }

    public void resetMessages() {
        synchronized (lock) {
            messages = mailbox;
            mailbox = new ArrayList<>();
        }
    }

    public synchronized ArrayList<String> getMessages() {
        return messages;
    }

    public synchronized int getNumEntries() {
        return blockList.size();
    }

    public int getCurrentN() {
        return  currentN;
    }

    public Block lastBlock() {
        return blockList.isEmpty() ? null : blockList.get(blockList.size() - 1);
    }

    public synchronized void addBlock(Block block, int minerNumber) {
        //Validate block
        if (!blockList.isEmpty()) {
            if (!block.getPreviousHash().equals(blockList.get(blockList.size() - 1).getHash()))
                return;
        } else
            if (!"0".equals(block.getPreviousHash()))
                return;
        if (!block.getHash().equals(StringUtil.applySha256(block.getContents())))
                return;
        if (!block.getHash().startsWith("0".repeat(currentN)))
            return;

        //Block valid, add it in
        blockList.add(block);
        System.out.println("Block: ");
        System.out.println("Created by miner # " + minerNumber);
        System.out.println(block);
        int timeToGenerate = block.getSecondsToGenerate();
        if (timeToGenerate > 60) {
            --currentN;
            System.out.println("N was decreased by 1");
        } else if (timeToGenerate < 10) {
            ++currentN;
            System.out.println("N was increased to " + currentN);
        } else {
            System.out.println("N stays the same");
        }
        System.out.println();
        resetMessages();
        serialize(fileName);
    }

    private void serialize(String fileName) {
        try (FileOutputStream fos = new FileOutputStream(fileName);
             BufferedOutputStream bos = new BufferedOutputStream(fos);
             ObjectOutputStream oos  = new ObjectOutputStream(bos)) {
            oos.writeObject(this);
        } catch (IOException e) {
            System.out.println("Error serializing Object to " + fileName + "!");
            System.out.println(e.getMessage());
        }
    }

    public static BlockChain deserialize(String fileName) {
        try (FileInputStream fis = new FileInputStream(fileName);
             BufferedInputStream bis = new BufferedInputStream(fis);
             ObjectInputStream ois = new ObjectInputStream(bis)) {
            var temp = (BlockChain) ois.readObject();
            temp.lock = new Object();
            return temp;
        } catch (IOException | ClassNotFoundException e) {
            return null;
        }
    }
}
