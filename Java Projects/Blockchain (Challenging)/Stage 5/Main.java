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
                        Thread.currentThread().setPriority(Thread.MAX_PRIORITY);
                        Block lastBlock = chainCopy.lastBlock();
                        Block newBlock = new Block(lastBlock, chainCopy.getCurrentN(), chainCopy.getMessages());
                        int minerNumber = (int) ((Thread.currentThread().getId() % numThreads) + 1);
                        if (chainCopy.getNumEntries() < currentLength + 5) {
                            //System.out.println("miner " + minerNumber + " awaiting submission!");
                            boolean result = chainCopy.addBlock(newBlock, minerNumber);
                            //System.out.println("miner " + minerNumber + " submitted: " + (result ? "ACCEPTED" : "REJECTED"));
                        }
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
                Thread.currentThread().setPriority(Thread.MIN_PRIORITY);
                if (!shutDownCopy)
                    chainCopy.submitMessage("Message from messenger " + ((Thread.currentThread().getId() % numThreads) + 1) + " " + random.nextInt());
            });
            try {
                isShutdown = miners.awaitTermination((long) Math.pow(5, chain.getCurrentN()), TimeUnit.MILLISECONDS) && minerLaunch.awaitTermination((long) Math.pow(10, chain.getCurrentN()), TimeUnit.MILLISECONDS);
            } catch (InterruptedException e) {
                isShutdown = true;
            }
        } while (!isShutdown);
        messageGenerator.shutdownNow();
    }
}
