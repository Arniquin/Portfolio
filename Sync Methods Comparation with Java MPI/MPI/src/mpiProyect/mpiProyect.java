package mpiProyect;

import java.awt.Color;
import java.awt.ComponentOrientation;
import java.util.concurrent.CyclicBarrier;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;
import mpi.*;
import javax.swing.*;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.plot.XYPlot;
import org.jfree.chart.renderer.xy.XYLineAndShapeRenderer;
import org.jfree.data.category.DefaultCategoryDataset;
import org.jfree.data.xy.XYSeriesCollection;
import java.util.concurrent.Semaphore;
import java.util.concurrent.locks.Condition;

public class mpiProyect {
    public static void main(String[] args) {
        MPI.Init(args); // Initialize MPI
        int core = MPI.COMM_WORLD.Rank(); // Get the rank of the core (process)
        int size = MPI.COMM_WORLD.Size(); // Get the total number of cores (processes)
        
        switch(core) {
            case 0: // Core for the graphical display (Frame, Tanks, and Graphs)
                // Initialize JFrame for the GUI
                JFrame frame = new JFrame();
                frame.setExtendedState(JFrame.MAXIMIZED_BOTH);
                frame.setTitle("Tanques de Agua");
                frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
                frame.setLocationRelativeTo(null);
                frame.setLayout(null);
                
                // Initialize water tanks for different synchronization methods
                rotoplas rotoplasMP = new rotoplas(); // Mutex
                rotoplas rotoplasSP = new rotoplas(); // Semaphore
                rotoplas rotoplasVCP = new rotoplas(); // Condition Variable
                rotoplas rotoplasMOP = new rotoplas(); // Monitors
                rotoplas rotoplasBP = new rotoplas(); // Barriers
                
                // Create the main panel for displaying tanks
                TanquesAgua panel = new TanquesAgua(rotoplasMP, rotoplasSP, rotoplasVCP, rotoplasMOP, rotoplasBP);
                frame.add(panel);
                
                // Initialize counters for each panel (Mutex, Semaphore, etc.)
                int[] contMP = new int[4]; // Counter for Mutex Panel
                int[] contSP = new int[4]; // Counter for Semaphore Panel
                int[] contVCP = new int[4]; // Counter for Condition Variable Panel
                int[] contMOP = new int[4]; // Counter for Monitors Panel
                int[] contBP = new int[4]; // Counter for Barrier Panel
                
                // Initialize the graphics panel
                JPanel pGraficas = new JPanel();
                pGraficas.setLayout(null);
                pGraficas.setBackground(Color.white);
                pGraficas.setBounds(580, 10, 775, 680);
                
                // Create datasets for the charts
                DefaultCategoryDataset datasetM = new DefaultCategoryDataset();
                DefaultCategoryDataset datasetS = new DefaultCategoryDataset();
                DefaultCategoryDataset datasetVC = new DefaultCategoryDataset();
                DefaultCategoryDataset datasetMO = new DefaultCategoryDataset();
                DefaultCategoryDataset datasetB = new DefaultCategoryDataset();
                XYSeriesCollection datasetT = new XYSeriesCollection();
                
                // Create a thread for drawing the graphics
                HiloGraficos graficador = new HiloGraficos(contMP, contSP, contVCP, contMOP, contBP, datasetM, datasetS, datasetVC, datasetMO, datasetB, datasetT);
                
                // Labels for the graphs
                JLabel M = new JLabel("Mutex:");
                JLabel S = new JLabel("Semaforo:");
                JLabel VC = new JLabel("Variable de condicion:");
                JLabel MO = new JLabel("Monitores:");
                JLabel B = new JLabel("Barreras:");
                JLabel T = new JLabel("Todo:");
                M.setBounds(0, 0, 100, 20);
                S.setBounds(0, 226, 100, 20);
                VC.setBounds(0, 452, 200, 20);
                MO.setBounds(387, 0, 100, 20);
                B.setBounds(387, 226, 100, 20);
                T.setBounds(387, 452, 100, 20);
                
                // Create charts for different synchronization methods
                JFreeChart barChartM = ChartFactory.createBarChart("Mutex", // Mutex Chart
                "Categoria",            
                "Accion", datasetM,          
                PlotOrientation.VERTICAL,           
                true, true, false);
                ChartPanel chartPanelM = new ChartPanel(barChartM);
                chartPanelM.setPreferredSize(new java.awt.Dimension(2500, 206));
                JScrollPane spM = new JScrollPane(chartPanelM, JScrollPane.VERTICAL_SCROLLBAR_NEVER, JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);
                spM.setComponentOrientation(ComponentOrientation.RIGHT_TO_LEFT);
                spM.setBounds(0, 20, 387, 206);

                // Repeat for other charts...
                JFreeChart barChartS = ChartFactory.createBarChart("Semaforo", // Semaphore Chart
                "Categoria",            
                "Accion", datasetS,          
                PlotOrientation.VERTICAL,           
                true, true, false);
                ChartPanel chartPanelS = new ChartPanel(barChartS);
                chartPanelS.setPreferredSize(new java.awt.Dimension(2500, 206));
                JScrollPane spS = new JScrollPane(chartPanelS, JScrollPane.VERTICAL_SCROLLBAR_NEVER, JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);
                spS.setComponentOrientation(ComponentOrientation.RIGHT_TO_LEFT);
                spS.setBounds(0, 246, 387, 206);    

                JFreeChart barChartVC = ChartFactory.createBarChart("Variables de Condicion", // Condition Variables Chart
                "Categoria",            
                "Accion", datasetVC,          
                PlotOrientation.VERTICAL,           
                true, true, false);
                ChartPanel chartPanelVC = new ChartPanel(barChartVC);
                chartPanelVC.setPreferredSize(new java.awt.Dimension(2500, 206));
                JScrollPane spVC = new JScrollPane(chartPanelVC, JScrollPane.VERTICAL_SCROLLBAR_NEVER, JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);
                spVC.setComponentOrientation(ComponentOrientation.RIGHT_TO_LEFT);
                spVC.setBounds(0, 472, 387, 206);

                JFreeChart barChartMO = ChartFactory.createBarChart("Monitores", // Monitors Chart
                "Categoria",            
                "Accion", datasetMO,          
                PlotOrientation.VERTICAL,           
                true, true, false);
                ChartPanel chartPanelMO = new ChartPanel(barChartMO);
                chartPanelMO.setPreferredSize(new java.awt.Dimension(2500, 206));
                JScrollPane spMO = new JScrollPane(chartPanelMO, JScrollPane.VERTICAL_SCROLLBAR_NEVER, JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);
                spMO.setComponentOrientation(ComponentOrientation.RIGHT_TO_LEFT);
                spMO.setBounds(387, 20, 387, 206);

                JFreeChart barChartB = ChartFactory.createBarChart("Barreras", // Barriers Chart
                "Categoria",            
                "Accion", datasetB,          
                PlotOrientation.VERTICAL,           
                true, true, false);
                ChartPanel chartPanelB = new ChartPanel(barChartB);
                chartPanelB.setPreferredSize(new java.awt.Dimension(2500, 206));
                JScrollPane spB = new JScrollPane(chartPanelB, JScrollPane.VERTICAL_SCROLLBAR_NEVER, JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);
                spB.setComponentOrientation(ComponentOrientation.RIGHT_TO_LEFT);
                spB.setBounds(387, 246, 387, 206);

                // Create a line chart for all data
                JFreeChart linearChartT = ChartFactory.createXYLineChart("Todo", // Overall Chart
                "Categoria",            
                "Accion", datasetT,          
                PlotOrientation.VERTICAL,           
                true, true, false);
                XYPlot plot = linearChartT.getXYPlot();
                XYLineAndShapeRenderer renderer = new XYLineAndShapeRenderer();
                plot.setRenderer(renderer);
                ChartPanel chartPanelT = new ChartPanel(linearChartT);
                chartPanelT.setPreferredSize(new java.awt.Dimension(2500, 206));
                JScrollPane spT = new JScrollPane(chartPanelT, JScrollPane.VERTICAL_SCROLLBAR_NEVER, JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);
                spT.setComponentOrientation(ComponentOrientation.RIGHT_TO_LEFT);
                spT.setBounds(387, 472, 387, 206);

                // Add components to the graphics panel
                pGraficas.add(M);
                pGraficas.add(S);
                pGraficas.add(VC);
                pGraficas.add(MO);
                pGraficas.add(B);
                pGraficas.add(T);
                pGraficas.add(spM);
                pGraficas.add(spS);
                pGraficas.add(spVC);
                pGraficas.add(spMO);
                pGraficas.add(spT);
                pGraficas.add(spB);
                
                // Add graphics panel to the main frame
                frame.add(pGraficas);
                frame.setVisible(true);
                graficador.start(); // Start the graphics thread
                
                // Infinite loop to receive data from other cores
                while(true) {
                    MPI.COMM_WORLD.Recv(rotoplasMP.tanque, 0, 13, MPI.INT, 1, 16); // Receive from Mutex core
                    MPI.COMM_WORLD.Recv(contMP, 0, 4, MPI.INT, 1, 17);
                    MPI.COMM_WORLD.Recv(rotoplasSP.tanque, 0, 13, MPI.INT, 2, 16); // Receive from Semaphore core
                    MPI.COMM_WORLD.Recv(contSP, 0, 4, MPI.INT, 2, 17);
                    MPI.COMM_WORLD.Recv(rotoplasVCP.tanque, 0, 13, MPI.INT, 3, 16); // Receive from Condition Variable core
                    MPI.COMM_WORLD.Recv(contVCP, 0, 4, MPI.INT, 3, 17);
                    MPI.COMM_WORLD.Recv(rotoplasMOP.tanque, 0, 13, MPI.INT, 4, 16); // Receive from Monitors core
                    MPI.COMM_WORLD.Recv(contMOP, 0, 4, MPI.INT, 4, 17);
                    MPI.COMM_WORLD.Recv(rotoplasBP.tanque, 0, 13, MPI.INT, 5, 16); // Receive from Barriers core
                    MPI.COMM_WORLD.Recv(contBP, 0, 4, MPI.INT, 5, 17);
                }
            break;
            
            case 1: // Core for Mutex synchronization
                TanqueMutex mutex = new TanqueMutex();
                mutex.iniciar(rotoplasMP);
            break;
            
            case 2: // Core for Semaphore synchronization
                TanqueSemaphore semaphore = new TanqueSemaphore();
                semaphore.iniciar(rotoplasSP);
            break;
            
            case 3: // Core for Condition Variable synchronization
                TanqueConditionVariable conditionVariable = new TanqueConditionVariable();
                conditionVariable.iniciar(rotoplasVCP);
            break;
            
            case 4: // Core for Monitors synchronization
                TanqueMonitores monitor = new TanqueMonitores();
                monitor.iniciar(rotoplasMOP);
            break;
            
            case 5: // Core for Barriers synchronization
                TanqueBarreras barrier = new TanqueBarreras();
                barrier.iniciar(rotoplasBP);
            break;
        }
        MPI.Finalize(); // Finalize MPI
    }
}

// Other classes like TanqueMutex, TanqueSemaphore, etc., will have their own logic to implement synchronization
