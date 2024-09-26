package mpiProyect;

// Libraries
import java.awt.*;
import java.awt.geom.Rectangle2D;
import javax.swing.JLabel;
import javax.swing.JPanel;

public class TanquesAgua extends JPanel {
    
    // Water tanks for different synchronization methods
    rotoplas rotoplasM = new rotoplas(); // Mutex
    rotoplas rotoplasS = new rotoplas(); // Semaphore
    rotoplas rotoplasVC = new rotoplas(); // Condition Variable
    rotoplas rotoplasMO = new rotoplas(); // Monitors
    rotoplas rotoplasB = new rotoplas(); // Barriers
    
    // Labels to display the percentage of water in each tank
    volatile private JLabel PorcentajeM; // Mutex
    private final JLabel TituloM;
    volatile private JLabel PorcentajeS; // Semaphore
    private final JLabel TituloS;
    volatile private JLabel PorcentajeVC; // Condition Variable
    private final JLabel TituloVC;
    volatile private JLabel PorcentajeMO; // Monitors
    private final JLabel TituloMO;
    volatile private JLabel PorcentajeB; // Barriers
    private final JLabel TituloB;
    
    // Constructor to initialize the tanks and their corresponding labels
    public TanquesAgua(rotoplas rotoplasM, rotoplas rotoplasS, rotoplas rotoplasVC, rotoplas rotoplasMO, rotoplas rotoplasB) {    
        this.rotoplasM = rotoplasM;
        this.rotoplasS = rotoplasS;
        this.rotoplasVC = rotoplasVC;
        this.rotoplasMO = rotoplasMO;
        this.rotoplasB = rotoplasB;
        
        // Setting the layout and background for the panel
        setLayout(null);
        setBackground(Color.white);
        setBounds(10, 10, 560, 990);
        
        // Initialization of labels
        PorcentajeM = new JLabel("P:");
        TituloM = new JLabel("Mutex:");
        PorcentajeS = new JLabel("P:");
        TituloS = new JLabel("Semaforo:");
        PorcentajeVC = new JLabel("P:");
        TituloVC = new JLabel("VC:");
        PorcentajeMO = new JLabel("P:");
        TituloMO = new JLabel("Monitores:");
        PorcentajeB = new JLabel("P:");
        TituloB = new JLabel("Barreras:");
        
        // Set bounds for the labels
        PorcentajeM.setBounds(0, 670, 100, 15);
        TituloM.setBounds(0, 0, 100, 15);
        PorcentajeS.setBounds(110, 670, 100, 15);
        TituloS.setBounds(110, 0, 100, 15);
        PorcentajeVC.setBounds(220, 670, 100, 15);
        TituloVC.setBounds(220, 0, 100, 15);
        PorcentajeMO.setBounds(330, 670, 100, 15);
        TituloMO.setBounds(330, 0, 100, 15);
        PorcentajeB.setBounds(440, 670, 100, 15);
        TituloB.setBounds(440, 0, 100, 15);

        // Adding labels to the panel
        add(TituloM);
        add(PorcentajeM);
        add(TituloS);
        add(PorcentajeS);
        add(TituloVC);
        add(PorcentajeVC);
        add(TituloMO);
        add(PorcentajeMO);
        add(TituloB);
        add(PorcentajeB);
    }
    
    // Method to paint the components of the panel
    @Override
    public void paintComponent(Graphics g) {
        int porcentaje;
        super.paintComponent(g); // Call to the superclass method
        Graphics2D g2 = (Graphics2D) g;
        
        // Drawing empty tanks
        g2.draw(new Rectangle2D.Double(0, 15, 100, 650)); // Mutex tank
        g2.draw(new Rectangle2D.Double(110, 15, 100, 650)); // Semaphore tank
        g2.draw(new Rectangle2D.Double(220, 15, 100, 650)); // Condition Variable tank
        g2.draw(new Rectangle2D.Double(330, 15, 100, 650)); // Monitors tank
        g2.draw(new Rectangle2D.Double(440, 15, 100, 650)); // Barriers tank
        
        try { // Filling tanks based on their states
            
            // Filling the Mutex tank
            porcentaje = ((rotoplasM.size() * 100) / 13); // Calculate the percentage of the tank filled
            PorcentajeM.setText("P:" + porcentaje + "%"); // Update the label with the percentage
            for (int i = 0; i < 13; i++) { // Iterate through the tank's content
                if (rotoplasM.tanque[i] != 0) { // Check if there is water in the tank
                    g2.setColor(rotoplasM.tanque[i] == 1 ? Color.yellow : Color.black); // Set color based on the content type
                    g2.fill(new Rectangle2D.Double(0, 665 - (50 * i) - 50, 100, 50)); // Draw filled part of the tank
                }
            }
            
            // Filling the Semaphore tank
            porcentaje = ((rotoplasS.size() * 100) / 13);
            PorcentajeS.setText("P:" + porcentaje + "%");
            for (int i = 0; i < 13; i++) {
                if (rotoplasS.tanque[i] != 0) {
                    g2.setColor(rotoplasS.tanque[i] == 1 ? Color.yellow : Color.black);
                    g2.fill(new Rectangle2D.Double(110, 665 - (50 * i) - 50, 100, 50));
                }
            }
            
            // Filling the Condition Variable tank
            porcentaje = ((rotoplasVC.size() * 100) / 13);
            PorcentajeVC.setText("P:" + porcentaje + "%");
            for (int i = 0; i < 13; i++) {
                if (rotoplasVC.tanque[i] != 0) {
                    g2.setColor(rotoplasVC.tanque[i] == 1 ? Color.yellow : Color.black);
                    g2.fill(new Rectangle2D.Double(220, 665 - (50 * i) - 50, 100, 50));
                }
            }
            
            // Filling the Monitors tank
            porcentaje = ((rotoplasMO.size() * 100) / 13);
            PorcentajeMO.setText("P:" + porcentaje + "%");
            for (int i = 0; i < 13; i++) {
                if (rotoplasMO.tanque[i] != 0) {
                    g2.setColor(rotoplasMO.tanque[i] == 1 ? Color.yellow : Color.black);
                    g2.fill(new Rectangle2D.Double(330, 665 - (50 * i) - 50, 100, 50));
                }
            }
            
            // Filling the Barriers tank
            porcentaje = ((rotoplasB.size() * 100) / 13);
            PorcentajeB.setText("P:" + porcentaje + "%");
            for (int i = 0; i < 13; i++) {
                if (rotoplasB.tanque[i] != 0) {
                    g2.setColor(rotoplasB.tanque[i] == 1 ? Color.yellow : Color.black);
                    g2.fill(new Rectangle2D.Double(440, 665 - (50 * i) - 50, 100, 50));
                }
            }
            
        } catch (Exception e) {
            // Handle exceptions silently
        }    
    }
}
