package disnode;

import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JTextArea;

public class shortTermDis extends Thread {
    volatile Process current; // Current process being handled
    volatile Process last; // Last completed process
    volatile PFlags flags; // Flags for process state
    JTextArea textArea; // UI text area for logging

    /**
     * Constructor to initialize short-term dispatcher.
     */
    public shortTermDis(Process current, Process last, PFlags flags, JTextArea textArea) {
        this.current = current;
        this.last = last;
        this.flags = flags;
        this.textArea = textArea;
    }
    
    /**
     * Main loop for dispatching processes.
     */
    public void run() {
        Process temp = new Process();
        while (true) {
            if (flags.working) {
                // Set up the temporary process
                temp.setID(current.getID());
                temp.setONode(current.getONode());
                temp.setPTime(current.getPTime());
                
                // Log the dispatched process
                System.out.println("Despachado Proceso: " + temp.ID + "| Tiempo: " + temp.PTime + "| Nodo de Origen: " + temp.ONode);
                textArea.append("Despachado Proceso: " + temp.ID + "| Tiempo: " + temp.PTime + "| Nodo de Origen: " + temp.ONode + "\n");
                
                // Simulate process execution time
                try {
                    sleep(temp.getPTime() * 1000);
                } catch (InterruptedException ex) {
                    Logger.getLogger(shortTermDis.class.getName()).log(Level.SEVERE, null, ex);
                }
                
                // Log the completed process
                System.out.println("Terminado Proceso: " + temp.ID + "| Tiempo: " + temp.PTime + "| Nodo de Origen: " + temp.ONode);
                textArea.append("Terminado Proceso: " + temp.ID + "| Tiempo: " + temp.PTime + "| Nodo de Origen: " + temp.ONode + "\n");
                
                // Update last process details
                last.setID(temp.getID());
                last.setONode(temp.getONode());
                last.setPTime(temp.getPTime());
                
                flags.setWorking(false); // Reset working flag
            }
        }
    }
}
