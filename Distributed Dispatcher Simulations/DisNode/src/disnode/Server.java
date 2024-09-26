package disnode;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import javax.swing.JTextArea;

public class Server extends Thread {
    int ID; // ID of the server
    volatile PFlags flags; // Flags for process state
    volatile Process newP; // New process received
    volatile WorkLoad wl; // Workload manager
    JTextArea textArea; // UI text area for logging

    ServerSocket servidor = null; // Server socket
    Socket sc = null; // Client socket
    DataInputStream in; // Input stream from client
    DataOutputStream out; // Output stream to client
    final int PUERTO = 5001; // Port number for the server

    /**
     * Constructor to initialize the server.
     */
    Server(int ID, PFlags flags, Process newP, JTextArea textArea) {
        this.ID = ID;
        this.flags = flags;
        this.newP = newP;
        this.textArea = textArea;
    }

    /**
     * Main loop for the server to handle incoming connections.
     */
    public void run() {
        String[] temp;
        try {
            // Create the server socket
            servidor = new ServerSocket(PUERTO);
            textArea.append("Nodo " + ID + " iniciado\n");
            // Continuously listen for client requests
            while (true) {
                // Wait for a client to connect
                sc = servidor.accept();
                textArea.append("Cliente conectado\n");
                in = new DataInputStream(sc.getInputStream());
                out = new DataOutputStream(sc.getOutputStream());

                // Read the message from the client
                String mensaje = in.readUTF();
                temp = mensaje.split(";");

                // Handle different message types
                if (Integer.parseInt(temp[0]) == 0) { // Send workload
                    out.writeUTF("" + wl.getWorkLoad());
                    textArea.append("Carga de trabajo enviada\n");
                } else if (Integer.parseInt(temp[0]) == 1) { // Receive process
                    newP.setONode(Integer.parseInt(temp[1]));
                    newP.setID(Integer.parseInt(temp[2]));
                    newP.setPTime(Integer.parseInt(temp[3]));
                    flags.setNewNodeProcessFlag(true);
                    out.writeUTF(ID + ": Proceso recibido");
                } else if (Integer.parseInt(temp[0]) == 2) { // Termination message
                    textArea.append("Proceso: " + temp[1] + " terminado\n");
                    out.writeUTF(ID + ": Mensaje recibido");
                }
                // Close the client socket
                sc.close();
                textArea.append("Cliente desconectado\n");
            }
        } catch (IOException ex) {
            textArea.append("Error de conexion\n");
        }
    }
}
