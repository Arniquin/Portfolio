package mpiProyect;

import static java.lang.Thread.sleep;
import java.util.concurrent.Semaphore;

public class ProductorS extends Thread{
    rotoplas rotoplas; 
    volatile private Semaphore sem;
    private int ID;
    int[] cont;
    
    ProductorS(rotoplas rotoplas, Semaphore sem,int ID,int[] cont){
        this.rotoplas=rotoplas;
        this.ID=ID;
        this.sem=sem;
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
               // if(ID==0){
                    try {sem.acquire(1);
                    } catch (Exception e) {}
                //} else{
                    //try {sem.acquire(2);
                    //} catch (Exception e) {}
                //}
                
                produce();
                
                //if(ID==0){
                    try {sem.release(1); 
                    } catch (Exception e) {}
                //} else{
                    //try {sem.release(2); 
                    //} catch (Exception e) {}
                //}
                 
            }
             
            try {sleep((int) (Math.random()*100)+900);
            } catch (Exception e) {} 
        }
    }
}
