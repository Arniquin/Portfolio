package disnode;

import java.util.logging.Level;
import java.util.logging.Logger;

public class ProcessGen extends Thread {
    int ID; // ID of the process generator
    volatile Process GenProcess; // Generated process
    volatile PFlags flags; // Flags for process state

    /**
     * Constructor to initialize the process generator.
     */
    public ProcessGen(int ID, Process GenProcess, PFlags flags) {
        this.ID = ID;
        this.GenProcess = GenProcess;
        this.flags = flags;
    }

    /**
     * Generates a random number within the specified range.
     * 
     * @param min Minimum value
     * @param max Maximum value
     * @return Random number
     */
    public int getRandomNumber(int min, int max) {
        return (int) ((Math.random() * (max - min)) + min);
    }

    /**
     * Main loop for generating processes.
     */
    public void run() {
        Process temp;
        while (true) {
            temp = new Process(ID, getRandomNumber(300, 200), getRandomNumber(3, 5)); // Create new process
            GenProcess.setONode(temp.getONode());
            GenProcess.setID(temp.getID());
            GenProcess.setPTime(temp.getPTime());
            flags.setNewGenProcessFlag(true); // Set flag to indicate a new process

            try {
                sleep(3000); // Sleep for 3 seconds before generating the next process
            } catch (InterruptedException ex) {
                Logger.getLogger(ProcessGen.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }
}
