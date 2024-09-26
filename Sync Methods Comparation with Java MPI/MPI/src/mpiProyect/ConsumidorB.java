package mpiProyect;

import static java.lang.Thread.sleep;
import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.CyclicBarrier;
import java.util.logging.Level;
import java.util.logging.Logger;

public class ConsumidorB extends Thread {
    rotoplas rotoplas; 
    volatile private CyclicBarrier barrier;
    private int ID;
    int[] cont;
    
    ConsumidorB(rotoplas rotoplas,CyclicBarrier barrier,int ID,int[] cont){
        this.rotoplas=rotoplas;
        this.ID=ID;
        this.barrier=barrier;
        this.cont=cont;
    }

    private void consume(){
        //System.out.println("pop");
        rotoplas.pop();
        if(ID==0)cont[2]++;
        else cont[3]++;
    }
    
    public void run(){
        while(true){
            if (rotoplas.size() > 0) {
                try {
                    barrier.await();
                } catch (InterruptedException ex) {
                    Logger.getLogger(ConsumidorB.class.getName()).log(Level.SEVERE, null, ex);
                } catch (BrokenBarrierException ex) {
                    Logger.getLogger(ConsumidorB.class.getName()).log(Level.SEVERE, null, ex);
                }
                consume();
            }
             
            try {sleep((int) (Math.random()*100)+900);
            } catch (Exception e) {} 
        }
    }
}
