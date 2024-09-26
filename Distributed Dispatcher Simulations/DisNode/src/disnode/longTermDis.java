package disnode;

import java.util.ArrayList;
import javax.swing.JTextArea;

public class longTermDis extends Thread {
    int ID; // ID of the dispatcher
    int Node1ID; // ID of Node 1
    int Node2ID; // ID of Node 2
    ArrayList<Process> ProcList = new ArrayList<Process>(); // List of processes
    volatile PFlags flags; // Process flags
    volatile Process newNodeP; // New process from another node
    volatile Process newGenP; // Newly generated process
    volatile Process current; // Currently handled process
    volatile Process last; // Last handled process
    WorkLoad wl; // Workload manager
    Client c; // Client for inter-node communication
    JTextArea textArea; // Text area for output

    /**
     * Constructor to initialize the long-term dispatcher.
     */
    public longTermDis(int ID, int Node1ID, int Node2ID, PFlags flags, Process newNodeP, Process newGenP, Process current, Process last, WorkLoad wl, Client c, JTextArea textArea) {
        this.ID = ID;
        this.Node1ID = Node1ID;
        this.Node2ID = Node2ID;
        this.flags = flags;
        this.newNodeP = newNodeP;
        this.newGenP = newGenP;
        this.current = current;
        this.last = last;
        this.wl = wl;
        this.c = c;
        this.textArea = textArea;
    }

    /**
     * Calculates the total workload from the process list.
     * 
     * @return Total workload.
     */
    private int workLoad() {
        int workLoad = 0;
        for (Process proc : ProcList) {
            workLoad += proc.getPTime(); // Sum process times
        }
        return workLoad;
    }

    /**
     * Finds the node with the least workload.
     * 
     * @return ID of the node with the least workload.
     */
    private int lessWorkLoad() {
        int IDaux = ID; // Assume this node
        int aux = workLoad(); // Current workload
        int aux1 = c.getWLNode(Node1ID); // Workload of Node 1
        int aux2 = c.getWLNode(Node2ID); // Workload of Node 2

        // Update IDaux based on lower workloads
        if (aux1 < aux) {
            IDaux = Node1ID;
            aux = aux1;
        }
        if (aux2 < aux) {
            IDaux = Node2ID;
        }
        return IDaux;
    }

    /**
     * Finds the index of the shortest process in the list.
     * 
     * @return Index of the shortest process.
     */
    public int ShortestProcess() {
        int minTime = ProcList.get(0).getPTime();
        int pos = 0;
        for (int i = 1; i < ProcList.size(); i++) {
            if (ProcList.get(i).getPTime() < minTime) {
                pos = i;
                minTime = ProcList.get(i).getPTime(); // Update shortest time
            }
        }
        return pos;
    }

    /**
     * Main loop for processing logic.
     */
    public void run() {
        int IDaux;
        
        while (true) {
            // Check for new processes
            if (flags.isNewNodeProcessFlag()) {
                ProcList.add(newNodeP); // Add new node process
                textArea.append("Proceso recibido de otro nodo: \n \tProceso: " + newNodeP.ID + "| Tiempo: " + newNodeP.PTime + " | Nodo de Origen: " + newNodeP.ONode + "\n");
                flags.setNewNodeProcessFlag(false);
            }

            if (flags.isNewGenProcessFlag()) {
                IDaux = lessWorkLoad(); // Find node with least workload
                if (IDaux == ID) {
                    ProcList.add(newGenP); // Add to this node
                    textArea.append("Proceso generado y agregado a la cola de despacho:\n \tProceso: " + newGenP.ID + "| Tiempo: " + newGenP.PTime + " | Nodo de Origen: " + newGenP.ONode + "\n");
                } else {
                    c.sendProcess(IDaux, newGenP); // Send to other node
                    textArea.append("Proceso generado y enviado al nodo " + IDaux + "\n \tProceso: " + newGenP.ID + "| Tiempo: " + newGenP.PTime + " | Nodo de Origen: " + newGenP.ONode + "\n");
                }
                flags.setNewGenProcessFlag(false);
            }

            wl.setWorkLoad(workLoad()); // Update workload

            if (!ProcList.isEmpty() && !flags.isWorking()) {
                int i = ShortestProcess(); // Get shortest process
                current.setONode(ProcList.get(i).getONode());
                current.setID(ProcList.get(i).getID());
                current.setPTime(ProcList.get(i).getPTime());

                // Send last process if necessary
                if (last.getID() != -1 && last.ONode != ID) {
                    if (last.ONode == Node1ID) c.sendMessage(Node1ID, last);
                    if (last.ONode == Node2ID) c.sendMessage(Node2ID, last);
                }

                flags.setWorking(true); // Mark as working
                ProcList.remove(i); // Remove shortest process
            }
        }
    }
}
