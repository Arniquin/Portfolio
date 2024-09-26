package mpiProyect;

import static java.lang.Thread.sleep;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.jfree.data.category.DefaultCategoryDataset;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;

public class HiloGraficos extends Thread {

    // Volatile variables to ensure visibility across threads
    volatile private int[] contM; // Array to hold values for 'contM'
    volatile private int[] contS; // Array to hold values for 'contS'
    volatile private int[] contVC; // Array to hold values for 'contVC'
    volatile private int[] contMO; // Array to hold values for 'contMO'
    volatile private int[] contB; // Array to hold values for 'contB'
    volatile private int contQ; // Quantum counter
    String Cuantum; // String to represent the current quantum
    DefaultCategoryDataset datasetM; // Dataset for mutex visualization
    DefaultCategoryDataset datasetS; // Dataset for semaphore visualization
    DefaultCategoryDataset datasetVC; // Dataset for condition variable visualization
    DefaultCategoryDataset datasetMO; // Dataset for monitor object visualization
    DefaultCategoryDataset datasetB; // Dataset for other conditions
    XYSeriesCollection datasetT; // Collection for XY series
    XYSeries series1 = new XYSeries("MP0"); // Series for producer 0 metrics
    XYSeries series2 = new XYSeries("MP1"); // Series for producer 1 metrics
    // ... (more series definitions for other metrics)
    XYSeries series20 = new XYSeries("BC1"); // Series for consumer 1 of condition B

    // Constructor
    public HiloGraficos(int[] contM, int[] contS, int[] contVC, int[] contMO, int[] contB,
                        DefaultCategoryDataset datasetM, DefaultCategoryDataset datasetS,
                        DefaultCategoryDataset datasetVC, DefaultCategoryDataset datasetMO,
                        DefaultCategoryDataset datasetB, XYSeriesCollection datasetT) {
        // Initialize instance variables
        this.contM = contM;
        this.contS = contS;
        this.contVC = contVC;
        this.contMO = contMO;
        this.contB = contB;
        this.contQ = 1; // Start the quantum counter at 1
        this.Cuantum = new String();
        this.datasetM = datasetM;
        this.datasetS = datasetS;
        this.datasetVC = datasetVC;
        this.datasetMO = datasetMO;
        this.datasetB = datasetB;
        this.datasetT = datasetT;

        // Add series to the dataset
        datasetT.addSeries(series1);
        datasetT.addSeries(series2);
        // ... (add all series to datasetT)
        datasetT.addSeries(series20);
    }

    // Method that runs the thread
    public void run() {
        // Auxiliary counters to track changes
        int[] auxContM = {0, 0, 0, 0};
        int[] auxContS = {0, 0, 0, 0};
        int[] auxContVC = {0, 0, 0, 0};
        int[] auxContMO = {0, 0, 0, 0};
        int[] auxContB = {0, 0, 0, 0};

        // Infinite loop to update graphics periodically
        while (true) {
            Cuantum = "Q" + contQ; // Update quantum representation

            // Update datasets for mutex visualization
            datasetM.addValue(contM[0] - auxContM[0], "Productor0", Cuantum);
            datasetM.addValue(contM[1] - auxContM[1], "Productor1", Cuantum);
            datasetM.addValue(contM[2] - auxContM[2], "Consumidor0", Cuantum);
            datasetM.addValue(contM[3] - auxContM[3], "Consumidor1", Cuantum);
            
            // Update datasets for semaphore visualization
            datasetS.addValue(contS[0] - auxContS[0], "Productor0", Cuantum);
            datasetS.addValue(contS[1] - auxContS[1], "Productor1", Cuantum);
            datasetS.addValue(contS[2] - auxContS[2], "Consumidor0", Cuantum);
            datasetS.addValue(contS[3] - auxContS[3], "Consumidor1", Cuantum);
            
            // Update datasets for condition variable visualization
            datasetVC.addValue(contVC[0] - auxContVC[0], "Productor0", Cuantum);
            datasetVC.addValue(contVC[1] - auxContVC[1], "Productor1", Cuantum);
            datasetVC.addValue(contVC[0] - auxContVC[0], "Consumidor0", Cuantum);
            datasetVC.addValue(contVC[1] - auxContVC[1], "Consumidor1", Cuantum);
            
            // Update datasets for monitor object visualization
            datasetMO.addValue(contMO[0] - auxContMO[0], "Productor0", Cuantum);
            datasetMO.addValue(contMO[1] - auxContMO[1], "Productor1", Cuantum);
            datasetMO.addValue(contMO[2] - auxContMO[2], "Consumidor0", Cuantum);
            datasetMO.addValue(contMO[3] - auxContMO[3], "Consumidor1", Cuantum);
            
            // Update datasets for other condition visualization
            datasetB.addValue(contB[0] - auxContB[0], "Productor0", Cuantum);
            datasetB.addValue(contB[1] - auxContB[1], "Productor1", Cuantum);
            datasetB.addValue(contB[2] - auxContB[2], "Consumidor0", Cuantum);
            datasetB.addValue(contB[3] - auxContB[3], "Consumidor1", Cuantum);

            // Update XY series data
            series1.add(contQ, contM[0] - auxContM[0]);
            series2.add(contQ, contM[1] - auxContM[1]);
            // ... (add other series updates)
            series20.add(contQ, contB[3] - auxContB[3]);

            // Update auxiliary counters
            for (int i = 0; i < 4; i++) auxContM[i] = contM[i];
            for (int i = 0; i < 4; i++) auxContS[i] = contS[i];
            for (int i = 0; i < 4; i++) auxContVC[i] = contVC[i];
            for (int i = 0; i < 4; i++) auxContMO[i] = contMO[i];
            for (int i = 0; i < 4; i++) auxContB[i] = contB[i];

            contQ++; // Increment quantum counter

            try {
                sleep(2000); // Pause for 2 seconds before next update
            } catch (InterruptedException ex) {
                Logger.getLogger(HiloGraficos.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }
}
