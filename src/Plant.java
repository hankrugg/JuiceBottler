/***
 * @author Nate Williams, modified by Hank Rugg
 * @date Feb. 23, 2024
 * Running this file will output results of one plant running with workers created
 * each with their own thread.
 */

import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;

public class Plant implements Runnable {
    // How long do we want to run the juice processing
    public final long PROCESSING_TIME = 5 * 1000;
    public final int ORANGES_PER_BOTTLE = 3;
    private final Thread thread;
    private final int NUM_WORKERS = 9;
    private final Worker[] workers = new Worker[NUM_WORKERS];
    private volatile Queue<Orange> fetchedOranges = new ConcurrentLinkedQueue<Orange>();
    private volatile Queue<Orange> peeledOranges = new ConcurrentLinkedQueue<Orange>();
    private volatile Queue<Orange> squeezedOranges = new ConcurrentLinkedQueue<Orange>();
    private volatile Queue<Orange> bottledOranges = new ConcurrentLinkedQueue<Orange>();
    private volatile Queue<Orange> processedOranges = new ConcurrentLinkedQueue<Orange>();

    /**
     * Constructor for Plant
     * @param threadNum any number to represent the thread that has been created
     */
    Plant(int threadNum) {
        thread = new Thread(this, "Plant[" + threadNum + "]");
    }

    /**
     * delay to make the plant wait for threads to do stuff
     */
    private void delay() {
        long sleepTime = Math.max(1, PROCESSING_TIME);
        try {
            Thread.sleep(sleepTime);
        } catch (InterruptedException e) {
            System.err.println("Plant malfunction");
        }
    }

    /**
     * Main method that runs the plant
     * @param args
     */
    public static void main(String[] args) {
        // Startup a single plant
        final Plant p = new Plant(0);
        p.startPlant();

        // Give the plants time to do work
        p.delay();

        // Stop the plant, and wait for it to shut down
        p.stopPlant();


        System.out.println("Size of fetched queue :" + p.fetchedOranges.size());
        System.out.println("Size of peeled queue :" + p.peeledOranges.size());
        System.out.println("Size of squeezed queue :" + p.squeezedOranges.size());
        System.out.println("Size of bottled queue :" + p.bottledOranges.size());
        System.out.println("Size of processed queue :" + p.processedOranges.size());

        // Summarize the results
        System.out.println("Total provided/processed = " + p.getProvidedOranges() + "/" + p.getProcessedOranges());
        System.out.println("Created " + p.getBottles() + " bottles of orange juice" +
                ", wasted " + p.getWaste() + " oranges");
    }

    /**
     * Makes the workers for the plant and puts them in the workers list
     */
    public void makeWorkers() {
        // anecdotally-- best results using 1 fetcher, 1 peeler, 3 squeezers, 2 bottlers, and 2 processors
        workers[0] = new Worker("fetcher", fetchedOranges, peeledOranges, squeezedOranges, bottledOranges, processedOranges);
        workers[1] = new Worker("peeler", fetchedOranges, peeledOranges, squeezedOranges, bottledOranges, processedOranges);
        workers[2] = new Worker("squeezer", fetchedOranges, peeledOranges, squeezedOranges, bottledOranges, processedOranges);
        workers[3] = new Worker("squeezer", fetchedOranges, peeledOranges, squeezedOranges, bottledOranges, processedOranges);
        workers[4] = new Worker("squeezer", fetchedOranges, peeledOranges, squeezedOranges, bottledOranges, processedOranges);
        workers[5] = new Worker("bottler", fetchedOranges, peeledOranges, squeezedOranges, bottledOranges, processedOranges);
        workers[6] = new Worker("bottler", fetchedOranges, peeledOranges, squeezedOranges, bottledOranges, processedOranges);
        workers[7] = new Worker("processor", fetchedOranges, peeledOranges, squeezedOranges, bottledOranges, processedOranges);
        workers[8] = new Worker("processor", fetchedOranges, peeledOranges, squeezedOranges, bottledOranges, processedOranges);
    }

    /**
     * Starts the plant
     */
    public void startPlant() {
        makeWorkers();
        thread.start();
    }

    /**
     * Stops the plant and all workers in it
     */
    public void stopPlant() {
        try {
            for (Worker worker : workers) {
                worker.stopWorker();
            }
            thread.join();
        } catch (InterruptedException e) {
            System.err.println(thread.getName() + " stop malfunction");
        }
    }

    /**
     * Overrides run method, Called when thread starts
     */
    public void run() {
        System.out.println(Thread.currentThread().getName() + " Processing oranges");
        for (Worker worker : workers) {
            worker.startWorker();
        }
    }

    /**
     * Gets the amount of oranges provided for each worker
     * @return The amount of oranges provided for this plant
     */
    public int getProvidedOranges() {
        int count = 0;
        for (Worker worker : workers) {
            count += worker.getOrangesFetched();
        }
        return count;
    }

    /**
     * Gets amount of oranges processed
     * @return amount of oranges processed
     */
    public int getProcessedOranges() {
        return processedOranges.size();
    }

    /**
     * Gets amount of bottles produced
     * @return The amount of bottles produced
     */
    public int getBottles() {
        return getProcessedOranges() / ORANGES_PER_BOTTLE;
    }

    /**
     * Gets the amount of oranges wasted
     * @return Number of oranges wasted
     */
    public int getWaste() {
        return getProvidedOranges() - processedOranges.size();
    }
}