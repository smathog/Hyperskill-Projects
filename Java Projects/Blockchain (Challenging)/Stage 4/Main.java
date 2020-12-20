package blockchain;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.Random;

public class Main {
    private static final int numThreads = 10;
    private static final int numBlocks = 5;

    public static void main(String[] args) {
        ExecutorService miners = Executors.newFixedThreadPool(numThreads);
        ExecutorService minerLaunch = Executors.newSingleThreadExecutor();
        ExecutorService messageGenerator = Executors.newFixedThreadPool(numThreads);
/*
        BlockChain chain = BlockChain.deserialize("blockchain.db");
        if (chain == null)
            chain = new BlockChain("blockchain.db");
*/

        BlockChain chain = new BlockChain("blockchain.db");
        int currentLength = chain.getNumEntries();
        final var chainCopy = chain;
        final var random = new Random();
        minerLaunch.submit(() -> {
            for (int i = 0; i < numThreads * numBlocks; ++i) {
                miners.submit(() -> {
                    if (chainCopy.getNumEntries() < currentLength + 5) {
                        Block lastBlock = chainCopy.lastBlock();
                        Block newBlock = new Block(lastBlock, chainCopy.getCurrentN(), chainCopy.getMessages());
                        int minerNumber = (int) ((Thread.currentThread().getId() % numThreads) + 1);
                        if (chainCopy.getNumEntries() < currentLength + 5)
                            chainCopy.addBlock(newBlock, minerNumber);
                    }
                });
            }
            miners.shutdown();
        });
        minerLaunch.shutdown();
        boolean isShutdown = false;
        do {
            final var shutDownCopy = isShutdown;
            messageGenerator.submit(() -> {
                if (!shutDownCopy)
                    chainCopy.submitMessage("Message from messenger " + ((Thread.currentThread().getId() % numThreads) + 1) + " " + random.nextInt());
            });
            try {
                isShutdown = miners.awaitTermination(2, TimeUnit.MILLISECONDS) && minerLaunch.awaitTermination(2, TimeUnit.MILLISECONDS);
            } catch (InterruptedException e) {
                isShutdown = true;
            }
        } while (!isShutdown);
        messageGenerator.shutdownNow();
    }
}
