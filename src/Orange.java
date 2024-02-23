/***
 * Code written by Nate Williams and used by Hank Rugg.
 * Used in Juice Bottler program as the data being passed through the program.
 * Feb. 23, 2024
 *
 */
public class Orange {
    private State state;

    /**
     * Constructor for orange. Sets the state to fetched and then does work
     */
    Orange() {
        state = State.Fetched;
        doWork();
    }

    /**
     * Gets the state of the orange, possibilites are: fetched, peeled, squeezed, bottled, processed
     * @return the state of the orange
     */
    public State getState() {
        return state;
    }

    /**
     * Runs the process of whatever state the orange is in
     */
    public void runProcess() {
        // Don't attempt to process an already completed orange
        if (state == State.Processed) {
            throw new IllegalStateException("This orange has already been processed");
        }
        doWork();
        state = state.getNext();
    }

    /**
     * Makes the orange thread sleep for the time it takes to complete its work.
     */
    private void doWork() {
        // Sleep for the amount of time necessary to do the work
        try {
            Thread.sleep(state.timeToComplete);
        } catch (InterruptedException e) {
            System.err.println("Incomplete orange processing, juice may be bad");
        }
    }

    /**
     * Enumeration for the state of the orange
     */
    public enum State {
        Fetched(15),
        Peeled(38),
        Squeezed(29),
        Bottled(17),
        Processed(1);

        private static final int finalIndex = State.values().length - 1;

        final int timeToComplete;

        /**
         * sets the time to complete
         * @param timeToComplete
         */
        State(int timeToComplete) {
            this.timeToComplete = timeToComplete;
        }

        /**
         * gets the next state
         * @return the State enumeration
         */
        State getNext() {
            int currIndex = this.ordinal();
            if (currIndex >= finalIndex) {
                throw new IllegalStateException("Already at final state");
            }
            return State.values()[currIndex + 1];
        }
    }
}