/***
 * @author : Hank Rugg
 * @date Feb. 23, 2024
 *
 * Multithreaded class that does work based on its given title.
 * Used in Plant.java
 */

import java.util.Queue;

public class Worker implements Runnable {

    private final String title;
    private final Thread thread;
    private volatile Queue<Orange> fetchedOranges;
    public volatile Queue<Orange> peeledOranges;
    public volatile Queue<Orange> squeezedOranges;
    public volatile Queue<Orange> bottledOranges;
    public volatile Queue<Orange> processedOranges;
    private volatile boolean running;
    private volatile int orangesFetched = 0;


    /**
     * Constructor for Worker
     *
     * @param title title of worker. Valid options are: fetcher, peeler, squeezer, bottler, processor
     * @param fetched Queue of fetched oranges
     * @param squeezed Queue of squeezed oranges
     * @param peeled Queue of peeled oranges
     * @param bottled Queue of bottled oranges
     * @param processed Queue of processed oranges
     */
    Worker(String title, Queue<Orange> fetched, Queue<Orange> squeezed, Queue<Orange> peeled, Queue<Orange> bottled, Queue<Orange> processed) {
        this.title = title;
        thread = new Thread(this, title);
        fetchedOranges = fetched;
        peeledOranges = peeled;
        squeezedOranges = squeezed;
        bottledOranges = bottled;
        processedOranges = processed;
        running = true;
    }

    /**
     * Getter for oranges fetched
     * @return orangesFetched
     */
    public int getOrangesFetched() {
        return orangesFetched;
    }

    /**
     * Stops the worker by joining thread
     */
    public synchronized void stopWorker() {
        running = false;
        try {
            thread.join();
        } catch (InterruptedException e) {
            System.err.println(thread.getName() + " stop malfunction");
        }
    }

    /**
     * Creates a new orange
     * @return Orange that was fetched
     */
    public Orange startFetching() {
        System.out.println("Orange Fetched!");
        return new Orange();
    }

    /**
     * Starts the thread
     */
    public void startWorker() {
        thread.start();
    }

    /**
     * Runs the process of whatever state the orange is in
     * @param o Orange polled from a queue
     * @return
     */
    public Orange runProcess(Orange o) {
        o.runProcess();
        System.out.println("Orange " + o.getState() + "!");
        return o;
    }

    /**
     * Overridden method and called when the thread starts
     */
    public void run() {
        System.out.println(Thread.currentThread().getName() + " started");
        while (running) {
            doTask();
        }
    }

    /**
     * Differentiates task for each worker based on the workers title
     */
    public void doTask() {
        switch (title) {
            case "fetcher" -> {
                while (running) {
                    Orange o = startFetching();
                    if (o != null) {
                        // add to oranges fetched
                        synchronized (o) {
                            orangesFetched++;
                        }
                        fetchedOranges.add(o);
                    } else {
                        System.err.println("Error fetching an orange.");
                    }
                }
            }
            case "peeler" -> {
                while (running) {
                    Orange o = fetchedOranges.poll();
                    // check if null, this is possible when there is nothing in the queue we are polling from
                    if (o != null) {
                        // run process and add it to succeeding queue
                        o = runProcess(o);
                        peeledOranges.add(o);
                    }
                }
            }
            case "squeezer" -> {
                while (running) {
                    Orange o = peeledOranges.poll();
                    // check if null, this is possible when there is nothing in the queue we are polling from
                    if (o != null) {
                        // run process and add it to succeeding queue
                        o = runProcess(o);
                        squeezedOranges.add(o);
                    }
                }
            }
            case "bottler" -> {
                while (running) {
                    Orange o = squeezedOranges.poll();
                    // check if null, this is possible when there is nothing in the queue we are polling from
                    if (o != null) {
                        // run process and add it to succeeding queue
                        o = runProcess(o);
                        bottledOranges.add(o);
                    }
                }
            }
            case "processor" -> {
                while (running) {
                    Orange o = bottledOranges.poll();
                    // check if null, this is possible when there is nothing in the queue we are polling from
                    if (o != null) {
                        // run process and add it to succeeding queue
                        o = runProcess(o);
                        processedOranges.add(o);
                    }
                }
            }
            default -> System.err.println("This worker doesn't have a job");
        }
    }
}
