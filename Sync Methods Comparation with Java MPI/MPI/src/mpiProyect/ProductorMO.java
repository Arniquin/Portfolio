package mpiProyect;

import static java.lang.Thread.sleep;

public class ProductorMO extends Thread {
    rotoplas rotoplas; 
    private int ID;
    int[] cont;
    
    ProductorMO(rotoplas rotoplas,int ID,int[] cont){
        this.rotoplas=rotoplas;
        this.ID=ID;
        this.cont=cont;
    }

    private synchronized void produce(){
        synchronized(rotoplas){
            rotoplas.push(ID);
        }
        if(ID==0)cont[0]++;
        else cont[1]++;
    }
    
    public void run(){
        while(true){
            if (rotoplas.size() < 13) {
                synchronized(rotoplas){
                    produce();
                }
            }
             
            try {sleep((int) (Math.random()*100)+900);
            } catch (Exception e) {} 
        }
    }
}
