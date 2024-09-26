package disnode;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;

public class Client {
    // Host addresses for the servers
    final String HOST1 = "192.168.0.114";
    final String HOST2 = "192.168.0.135";
    
    // Ports for the servers
    // Node1
    int Node1ID;
    volatile WorkLoad wlN1;
    final int PUERTO1 = 5000;
    
    // Node2
    int Node2ID;
    volatile WorkLoad wlN2;
    final int PUERTO2 = 5002;
    
    DataInputStream in;  // Input stream for receiving data from the server
    DataOutputStream out; // Output stream for sending data to the server

    // Constructor to initialize the Client with Node IDs
    public Client(int Node1ID, int Node2ID) {
        this.Node1ID = Node1ID; // Assign Node1ID
        this.Node2ID = Node2ID; // Assign Node2ID
    }

    /**
     * Gets the workload of the specified node by its ID.
     * @param ID The ID of the node whose workload is to be retrieved.
     * @return The workload of the specified node as an integer.
     */
    public int getWLNode(int ID){ 
        String mensaje = "" + Integer.MAX_VALUE; // Initialize message with maximum integer value
        try {
            // Create a socket to connect to the corresponding server based on Node ID
            Socket sc = new Socket(HOST1, PUERTO1);
            if (ID == Node1ID)
                sc = new Socket(HOST1, PUERTO1);
            if (ID == Node2ID)
                sc = new Socket(HOST2, PUERTO2);
            
            System.out.println("Client Started");
            
            in = new DataInputStream(sc.getInputStream()); // Initialize input stream
            out = new DataOutputStream(sc.getOutputStream()); // Initialize output stream

            // Send a message to the server to request workload
            out.writeUTF("0;0");

            // Receive the message from the server containing the workload
            mensaje = in.readUTF();
            sc.close(); // Close the socket connection
            
        } catch (IOException ex) {
            System.out.println("Client connection error");
        }
        return Integer.parseInt(mensaje); // Return the workload as an integer
    }
    
    /**
     * Sends a process to the specified node.
     * @param ID The ID of the node to which the process is being sent.
     * @param temp The Process object that contains process information.
     */
    public void sendProcess(int ID, Process temp){
        String mensaje; 
        try {
            // Create a socket to connect to the corresponding server based on Node ID
            Socket sc = new Socket(HOST1, PUERTO1);
            if (ID == Node1ID)
                sc = new Socket(HOST1, PUERTO1);
            if (ID == Node2ID)
                sc = new Socket(HOST2, PUERTO2);
            
            System.out.println("Client Started");
            
            in = new DataInputStream(sc.getInputStream()); // Initialize input stream
            out = new DataOutputStream(sc.getOutputStream()); // Initialize output stream

            // Send a message to the server with the process details
            out.writeUTF("1;" + temp.getONode() + ";" + temp.getID() + ";" + temp.PTime);

            // Receive the message from the server
            mensaje = in.readUTF();
            System.out.println(mensaje); // Print the server's response
            sc.close(); // Close the socket connection
        } catch (IOException ex) {
            System.out.println("Client connection error");
        }
    }
    
    /**
     * Sends a message containing a process ID to the specified node.
     * @param ID The ID of the node to which the message is being sent.
     * @param temp The Process object that contains the process ID.
     */
    public void sendMessage(int ID, Process temp){
        String mensaje; 
        try {
            // Create a socket to connect to the corresponding server based on Node ID
            Socket sc = new Socket(HOST1, PUERTO1);
            if (ID == Node1ID)
                sc = new Socket(HOST1, PUERTO1);
            if (ID == Node2ID)
                sc = new Socket(HOST2, PUERTO2);
            
            System.out.println("Client Started");
            
            in = new DataInputStream(sc.getInputStream()); // Initialize input stream
            out = new DataOutputStream(sc.getOutputStream()); // Initialize output stream

            // Send a message to the server with the process ID
            out.writeUTF("2;" + temp.getID());

            // Receive the message from the server
            mensaje = in.readUTF();
            System.out.println(mensaje); // Print the server's response
            sc.close(); // Close the socket connection
        } catch (IOException ex) {
            System.out.println("Client connection error");
        }
    }
}
