package mpiProyect;

import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;

public class ProductorVC extends Thread {
    rotoplas rotoplas; 
    volatile private Lock mutex;
    private Condition added;
    private Condition removed;
    private int ID;
    int[] cont;
    
    ProductorVC(rotoplas rotoplas, Lock mutex, Condition added, Condition removed,int ID,int[] cont){
        this.rotoplas=rotoplas;
        this.ID=ID;
        this.mutex=mutex;
        this.added=added;
        this.removed=removed;
        this.cont=cont;
    }
    
    private void produce(){
        //System.out.println("push:"+ID);
        rotoplas.push(ID);
        if(ID==0)cont[0]++;
        else cont[1]++;
    }
    
    public void run(){
        while(true){
            try {
                  mutex.lock();
                  while(!rotoplas.isEmpty())
                      added.await();
                  produce();
                  removed.signal();
                }catch (Exception e) {}
                finally{
                    mutex.unlock(); 
                }
             
            try {sleep((int) (Math.random()*100)+700);
            } catch (Exception e) {}
             
            try {sleep((int) (Math.random()*100)+900);
            } catch (Exception e) {} 
        }
    }
}
