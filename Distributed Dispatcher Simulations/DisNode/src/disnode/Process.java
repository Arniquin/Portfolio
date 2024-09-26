package disnode;

public class Process {
    int ONode;
    int ID;
    int PTime; 

    Process(){}
    
    Process(int ONode,int ID,int PTime){
        this.ONode=ONode;
        this.ID=ID;
        this.PTime=PTime;
    }
    
    public int getONode() {
        return ONode;
    }

    public void setONode(int ONode) {
        this.ONode = ONode;
    }

    public int getID() {
        return ID;
    }

    public void setID(int ID) {
        this.ID = ID;
    }

    public int getPTime() {
        return PTime;
    }

    public void setPTime(int PTime) {
        this.PTime = PTime;
    }
    
    
}
