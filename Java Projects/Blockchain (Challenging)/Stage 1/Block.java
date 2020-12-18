package blockchain;

import java.util.Date;

public class Block {
    private static int numCreated = 0;
    private static Block previous = null;

    //Included in hash
    private final String previousHash;
    private final int id;

    //Excluded from hash
    private final long timeStamp;
    private final String thisHash;

    public Block() {
        ++numCreated;
        this.id = numCreated;
        if (previous != null)
            this.previousHash = previous.thisHash;
        else
            this.previousHash = "0";
        this.timeStamp = new Date().getTime();
        thisHash = StringUtil.applySha256(id + previousHash);
        previous = this;
    }

    @Override
    public String toString() {
        return "Block: "
                + "\nId: " + id
                + "\nTimestamp: " + timeStamp
                + "\nHash of the previous block:\n"
                + previousHash
                + "\nHash of the block:\n"
                + thisHash;
    }
}
