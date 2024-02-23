/**
 * @author Hank Rugg
 * @date Feb. 23, 2024
 * Running this file will output results of the specified plants running with each plant on
 * its own thread
 *
 */
public class PlantOperation implements Runnable {

    public final long PROCESSING_TIME = 5 * 1000;
    private final Thread thread;
    public int NUM_PLANTS = 5;
    Plant[] plants = new Plant[NUM_PLANTS];

    /**
     * Constructor for PlantOperation
     */
    PlantOperation() {
        thread = new Thread(this, "Plant Operation");
    }

    /**
     * delay to make the plant operation wait for threads to do stuff
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
     * Main method for plant operation. Runs the plants
     * @param args
     */
    public static void main(String[] args) {
        final PlantOperation p = new PlantOperation();
        p.makePlants();
        p.startOperating();

        // Give the plants time to do work
        p.delay();

        // Stop the plant, and wait for it to shut down
        p.stopOperating();


        // Summarize the results
        System.out.println("Total provided/processed = " + p.getProvidedOranges() + "/" + p.getProcessedOranges());
        System.out.println("Created " + p.getBottles() + " bottles of orange juice" +
                ", wasted " + p.getWaste() + " oranges");
    }

    /**
     * Overridden method. Called when the thread starts
     */
    public void run() {
        for (Plant plant : plants) {
            plant.startPlant();
        }

    }

    /**
     * Start the plant operations
     */
    public void startOperating() {
        makePlants();
        thread.start();
    }

    /**
     * Make the plants and add them to the plants list
     */
    private void makePlants() {
        for (int i = 0; i < NUM_PLANTS; i++) {
            plants[i] = new Plant(i);
        }
    }

    /**
     * Stops the plant operation and all the plants
     */
    public void stopOperating() {
        try {
            thread.join();
            for (Plant plant : plants) {
                plant.stopPlant();
                System.out.println("Plant stopped");
            }
        } catch (InterruptedException e) {
            System.err.println(thread.getName() + " stop malfunction");
        }
    }

    /**
     * Gets the amount of oranges provided for each worker
     * @return The amount of oranges provided for this plant
     */
    private int getProvidedOranges() {
        int count = 0;
        for (Plant plant : plants) {
            count += plant.getProvidedOranges();
        }
        return count;
    }

    /**
     * Gets amount of oranges processed for all plants
     * @return amount of oranges processed for all plants
     */
    private int getProcessedOranges() {
        int count = 0;
        for (Plant plant : plants) {
            count += plant.getProcessedOranges();
        }
        return count;
    }

    /**
     * Gets amount of bottles produced for all plants
     * @return The amount of bottles produced for all plants
     */
    private int getBottles() {
        int count = 0;
        for (Plant plant : plants) {
            count += plant.getBottles();
        }
        return count;
    }

    /**
     * Gets the amount of oranges wasted for all plants
     * @return Number of oranges wasted for all plants
     */
    private int getWaste() {
        int count = 0;
        for (Plant plant : plants) {
            count += plant.getWaste();
        }
        return count;
    }
}
