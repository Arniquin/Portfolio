package mpiProyect;

public class ConsumidorMO extends Thread{
    rotoplas rotoplas; 
    private int ID;
    int[] cont;
    
    ConsumidorMO(rotoplas rotoplas,int ID,int[] cont){
        this.rotoplas=rotoplas;
        this.ID=ID;
        this.cont=cont;
    }
     private synchronized void consume(){
        //System.out.println("pop");
        
        synchronized(rotoplas){
            rotoplas.pop();
        }
        if(ID==0)cont[2]++;
        else cont[3]++;
    }
    
    public void run(){
        while(true){
            if (rotoplas.size() > 0) {
                synchronized(rotoplas){
                    consume();
                }
            }
             
            try {sleep((int) (Math.random()*100)+900);
            } catch (Exception e) {} 
        }
    }
}
