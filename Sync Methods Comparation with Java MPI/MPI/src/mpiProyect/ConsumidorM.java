package mpiProyect;

import static java.lang.Thread.sleep;
import java.util.concurrent.locks.Lock;

public class ConsumidorM extends Thread {
    rotoplas rotoplas; 
    volatile private Lock mutex;
    private int ID;
    int[] cont;
    
    ConsumidorM(rotoplas rotoplas, Lock mutex,int ID,int[] cont){
        this.rotoplas=rotoplas;
        this.ID=ID;
        this.mutex=mutex;
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
                mutex.lock();
                consume();
                mutex.unlock();
            }
             
            try {sleep((int) (Math.random()*100)+900);
            } catch (Exception e) {} 
        }
    }
}
