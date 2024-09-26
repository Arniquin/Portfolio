package disnode;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.text.DefaultCaret;

public class DisNode extends JFrame {
    int ID; // ID of the node
    int Node1ID; // ID of Node 1
    int Node2ID; // ID of Node 2
    volatile Process newNodeP; // New process for the node
    volatile Process newGenP; // New generated process
    volatile Process current; // Current process being handled
    volatile Process last; // Last process that was handled
    Server s; // Server object for this node
    Client c; // Client object for this node
    WorkLoad wl; // Workload manager
    PFlags flags; // Process flags
    longTermDis ltd; // Long-term dispatcher
    shortTermDis std; // Short-term dispatcher
    ProcessGen pg; // Process generator
    
    // GUI components
    JTextArea textArea; // Text area for short-term dispatcher output
    JTextArea textArea2; // Text area for long-term dispatcher output
    JTextArea textArea3; // Text area for server output
    JScrollPane scrollPane; // Scroll pane for textArea
    JScrollPane scrollPane2; // Scroll pane for textArea2
    JScrollPane scrollPane3; // Scroll pane for textArea3
    JLabel DLabel; // Label for short-term dispatcher
    JLabel DLabel2; // Label for long-term dispatcher
    JLabel DLabel3; // Label for server

    // Constructor to initialize the DisNode with a given ID
    DisNode(int ID) {
        setSize(480, 800); // Set the size of the JFrame
        setResizable(false); // Make the JFrame non-resizable
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); // Exit on close
        setLayout(null); // Set layout to null for absolute positioning
        
        // Initialize labels
        DLabel = new JLabel("Despachador de corto plazo:");
        DLabel.setBounds(20, 5, 300, 20);
        DLabel2 = new JLabel("Despachador de largo plazo:");
        DLabel2.setBounds(20, 235, 300, 20);
        DLabel3 = new JLabel("Servidor:");
        DLabel3.setBounds(20, 465, 300, 20);
        
        // Initialize text areas
        textArea = new JTextArea(); // Text area for short-term dispatcher
        DefaultCaret caret = (DefaultCaret) textArea.getCaret();
        caret.setUpdatePolicy(DefaultCaret.ALWAYS_UPDATE); // Always update caret
        textArea2 = new JTextArea(); // Text area for long-term dispatcher
        DefaultCaret caret2 = (DefaultCaret) textArea2.getCaret();
        caret2.setUpdatePolicy(DefaultCaret.ALWAYS_UPDATE); // Always update caret
        textArea3 = new JTextArea(); // Text area for server output
        DefaultCaret caret3 = (DefaultCaret) textArea3.getCaret();
        caret3.setUpdatePolicy(DefaultCaret.ALWAYS_UPDATE); // Always update caret
        
        // Initialize scroll panes
        scrollPane = new JScrollPane(); // Scroll pane for textArea
        scrollPane.setBounds(20, 30, 420, 200);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
        scrollPane.getViewport().add(textArea); // Add textArea to scroll pane
        
        scrollPane2 = new JScrollPane(); // Scroll pane for textArea2
        scrollPane2.setBounds(20, 260, 420, 200);
        scrollPane2.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
        scrollPane2.getViewport().add(textArea2); // Add textArea2 to scroll pane
        
        scrollPane3 = new JScrollPane(); // Scroll pane for textArea3
        scrollPane3.setBounds(20, 490, 420, 200);
        scrollPane3.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
        scrollPane3.getViewport().add(textArea3); // Add textArea3 to scroll pane
        
        // Add components to the JFrame
        add(DLabel);
        add(DLabel2);
        add(DLabel3);
        add(scrollPane);
        add(scrollPane2);
        add(scrollPane3);
        
        this.ID = ID; // Assign ID to the node
        setNodes(); // Set the IDs of the other nodes
        newNodeP = new Process(); // Initialize new process
        newGenP = new Process(); // Initialize new generated process
        current = new Process(); // Initialize current process
        last = new Process(-1, -1, -1); // Initialize last process with default values
        s = new Server(ID, flags, newNodeP, textArea3); // Create server for this node
        c = new Client(Node1ID, Node2ID); // Create client for this node
        wl = new WorkLoad(); // Create workload manager
        flags = new PFlags(); // Create process flags
        ltd = new longTermDis(ID, Node1ID, Node2ID, flags, newNodeP, newGenP, current, last, wl, c, textArea2); // Create long-term dispatcher
        std = new shortTermDis(current, last, flags, textArea); // Create short-term dispatcher
        pg = new ProcessGen(ID, newGenP, flags); // Create process generator
        
        // Start dispatcher threads
        ltd.start();
        std.start();
        pg.start();
        s.start(); // Start server thread
        
        setTitle("Nodo " + ID); // Set the title of the window
    }

    /**
     * Sets the IDs of the other nodes based on this node's ID.
     */
    public void setNodes() {
        switch (ID) {
            case 0:
                Node1ID = 1; // Node 1 for Node 0
                Node2ID = 2; // Node 2 for Node 0
                break;
            case 1:
                Node1ID = 0; // Node 1 for Node 1
                Node2ID = 2; // Node 2 for Node 1
                break;
            case 2:
                Node1ID = 0; // Node 1 for Node 2
                Node2ID = 1; // Node 2 for Node 2
                break;
        }
    }

    /**
     * Main method to run the DisNode application.
     * @param args Command line arguments (not used).
     */
    public static void main(String[] args) {
        DisNode node = new DisNode(1); // Create a new DisNode with ID 1
        node.setVisible(true); // Make the DisNode visible
    }
}
