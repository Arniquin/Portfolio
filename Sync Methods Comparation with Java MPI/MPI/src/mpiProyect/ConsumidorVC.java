package mpiProyect;

import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;

public class ConsumidorVC extends Thread{
    rotoplas rotoplas; 
    volatile private Lock mutex;
    private Condition added;
    private Condition removed;
    private int ID;
    int[] cont;
    
    ConsumidorVC(rotoplas rotoplas, Lock mutex, Condition added, Condition removed,int ID,int[] cont){
        this.rotoplas=rotoplas;
        this.ID=ID;
        this.mutex=mutex;
        this.added=added;
        this.removed=removed;
        this.cont=cont;
    }
    
    private void consume(){
        System.out.println("pop:"+ID);
        rotoplas.pop();
        if(ID==0)cont[2]++;
        else cont[3]++;
    }
    
    public void run(){
        while(true){
            if (rotoplas.size() > 0) {
                try {
                  mutex.lock();
                  while(rotoplas.isEmpty())
                      removed.await();
                  consume();
                  added.signal();
                }catch (Exception e) {}
                finally{
                    mutex.unlock(); 
                }
            }
             
            try {sleep((int) (Math.random()*100)+900);
            } catch (Exception e) {} 
        }
    }
}
