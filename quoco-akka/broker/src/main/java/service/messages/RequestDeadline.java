package service.messages;

public class RequestDeadline {
    private int SEED_ID;

    public RequestDeadline(int SEED_ID){
        this.SEED_ID = SEED_ID;
    }
    
    public int getSEED_ID() {
        return SEED_ID;
    }
}
