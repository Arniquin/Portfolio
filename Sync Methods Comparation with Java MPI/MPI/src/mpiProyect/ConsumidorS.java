package mpiProyect;

import java.util.concurrent.Semaphore;

public class ConsumidorS extends Thread {
    rotoplas rotoplas; 
    volatile private Semaphore sem;
    private int ID;
    int[] cont;
    
    ConsumidorS(rotoplas rotoplas, Semaphore sem,int ID,int[] cont){
        this.rotoplas=rotoplas;
        this.ID=ID;
        this.sem=sem;
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
                
                //if(ID==0){
                    try {sem.acquire(1);
                    } catch (Exception e) {}
                //} else{
                    //try {sem.acquire(2);
                    //} catch (Exception e) {}
                //}
                
                consume();
                
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
