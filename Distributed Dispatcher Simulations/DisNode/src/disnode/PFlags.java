package disnode;

public class PFlags {
    volatile boolean newNodeProcessFlag;
    volatile boolean newGenProcessFlag;
    volatile boolean working;
    
    PFlags(){
        newNodeProcessFlag=false;
        newGenProcessFlag=false;
        working=false;
    }

    public boolean isNewNodeProcessFlag() {
        return newNodeProcessFlag;
    }

    public void setNewNodeProcessFlag(boolean newNodeProcessFlag) {
        this.newNodeProcessFlag = newNodeProcessFlag;
    }

    public boolean isNewGenProcessFlag() {
        return newGenProcessFlag;
    }

    public void setNewGenProcessFlag(boolean newGenProcessFlag) {
        this.newGenProcessFlag = newGenProcessFlag;
    }

    public boolean isWorking() {
        return working;
    }

    public void setWorking(boolean working) {
        this.working = working;
    }
    
    
}
