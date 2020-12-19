package blockchain;

import java.io.Serializable;
import java.util.Date;

public class Block implements Serializable {
    private static int numCreated = 0;
    private static Block previous = null;
    private static final long serialVersionUID = 4L;

    //Included in hash
    private String previousHash;
    private int id;
    private int magicNumber;

    //Excluded from hash
    private long timeStamp;
    private String thisHash;
    private int secondsToGenerate;

    public Block(Block block, int numZeroes) {
        long start = System.currentTimeMillis();
        Block.numCreated = block == null ? 0 : block.id;
        Block.previous = block;
        initBlock(numZeroes);
        secondsToGenerate = (int) (System.currentTimeMillis() - start) / 1000;
    }

    public String getHash() {
        return thisHash;
    }

    public String getPreviousHash() {
        return previousHash;
    }

    public String getContents() {
        return id + previousHash + magicNumber;
    }

    public int getSecondsToGenerate() {
        return secondsToGenerate;
    }

    private void initBlock(int numZeroes) {
        ++numCreated;
        this.id = numCreated;
        if (previous != null)
            this.previousHash = previous.thisHash;
        else
            this.previousHash = "0";
        this.timeStamp = new Date().getTime();
        magicNumber = 0;
        String thisHash;
        do {
            ++magicNumber;
            thisHash = StringUtil.applySha256(id + previousHash + magicNumber);
        } while (!thisHash.startsWith("0".repeat(numZeroes)));
        this.thisHash = thisHash;
        previous = this;
    }

    @Override
    public String toString() {
        return "Id: " + id
                + "\nTimestamp: " + timeStamp
                + "\nMagic number: " + magicNumber
                + "\nHash of the previous block:\n"
                + previousHash
                + "\nHash of the block:\n"
                + thisHash
                + "\nBlock was generating for " + secondsToGenerate + " seconds";
    }
}
