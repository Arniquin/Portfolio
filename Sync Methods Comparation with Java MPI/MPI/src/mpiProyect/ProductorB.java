package mpiProyect;

import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.CyclicBarrier;
import java.util.logging.Level;
import java.util.logging.Logger;

public class ProductorB extends Thread {
    rotoplas rotoplas; 
    volatile private CyclicBarrier barrier;
    private int ID;
    int[] cont;
    
    ProductorB(rotoplas rotoplas,CyclicBarrier barrier,int ID,int[] cont){
        this.rotoplas=rotoplas;
        this.ID=ID;
        this.barrier=barrier;
        this.cont=cont;
    }

    private void produce(){
        rotoplas.push(ID);
        if(ID==0)cont[0]++;
        else cont[1]++;
    }
    
    public void run(){
        while(true){
            if (rotoplas.size() < 13) {
                try {
                    barrier.await();
                } catch (InterruptedException ex) {
                    Logger.getLogger(ProductorB.class.getName()).log(Level.SEVERE, null, ex);
                } catch (BrokenBarrierException ex) {
                    Logger.getLogger(ProductorB.class.getName()).log(Level.SEVERE, null, ex);
                }
                produce();
            }
             
            try {sleep((int) (Math.random()*100)+900);
            } catch (Exception e) {} 
        }
    }
}
