package blockchain;

import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class Main {
    public static void main(String[] args) {
        ExecutorService miners = Executors.newFixedThreadPool(20);
        /*
        BlockChain chain = BlockChain.deserialize("blockchain.db");
        if (chain == null)
            chain = new BlockChain("blockchain.db");

         */
        BlockChain chain = new BlockChain("blockchain.db");
        int currentLength = chain.getNumEntries();
        final var chainCopy = chain;
        for (int i = 0; i < 20 * 5; ++i) {
            miners.submit(() -> {
                if (chainCopy.getNumEntries() < currentLength + 5) {
                    Block lastBlock = chainCopy.lastBlock();
                    Block newBlock = new Block(lastBlock, chainCopy.getCurrentN());
                    int minerNumber = (int) ((Thread.currentThread().getId() % 20) + 1);
                    if (chainCopy.getNumEntries() < currentLength + 5)
                        chainCopy.addBlock(newBlock, minerNumber);
                }
            });
        }
        miners.shutdown();
        boolean isShutdown = false;
        do {
            try {
                isShutdown = miners.awaitTermination(1, TimeUnit.SECONDS);
            } catch (InterruptedException e) {
                isShutdown = true;
            }
        } while (!isShutdown);
    }
}
