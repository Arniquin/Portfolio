package mpiProyect;

import static java.lang.Thread.sleep;
import java.util.concurrent.locks.Lock;

public class ProductorM extends Thread {
    rotoplas rotoplas; 
    volatile private Lock mutex;
    private int ID;
    int[] cont;
    
    ProductorM(rotoplas rotoplas, Lock mutex,int ID,int[] cont){
        this.rotoplas=rotoplas;
        this.ID=ID;
        this.mutex=mutex;
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
                mutex.lock();
                produce();
                mutex.unlock();
            }
             
            try {sleep((int) (Math.random()*100)+900);
            } catch (Exception e) {} 
        }
    }
    
    
    
    
}
