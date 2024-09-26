package mpiProyect;

public class rotoplas {
    int[] tanque=new int[13];
    int tam;
    rotoplas(){
        for(int i=0;i<13;i++)
            tanque[i]=0;
        tam=0;
    }
    
    public void push(int ID){
        if(tam<=13){
            tam++;
            if(ID==0)tanque[tam-1]=1;
            else tanque[tam-1]=2;
        }
    }
    
    public void pop(){
        if(tam>0){
            tanque[tam-1]=0;
            tam--;
        }
    }
    
    public int size(){
        int i=0;
        while(tanque[i]!=0){
            i++;
        }
        return i;
    }
    
    public int[] getRotoplas(){
        return tanque;
    }
    
    public void printRotoplas(){
        for(int i=0;i<13;i++)System.out.println(""+tanque[i]);
        System.out.println("");
    }
    
    public boolean isEmpty(){
        if(tanque[0]==0)return true;
        else return false;
    }
}
