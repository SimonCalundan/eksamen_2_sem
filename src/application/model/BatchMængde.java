package application.model;

import java.io.Serializable;

public class BatchMængde implements Serializable {

    private double mængdeILiter;
    private Batch batch;

    public BatchMængde(double mængdeILiter, Batch batch) {
        this.mængdeILiter = mængdeILiter;
        this.batch = batch;
    }

    public String toString() {
        return "Mængde: " + this.mængdeILiter + " af batch: " + this.batch;
    }

    //linkmetoder
    public Batch getBatch() {
        return batch;
    }

    public double getMængdeILiter() {
        return mængdeILiter;
    }

    public void setMængdeILiter(double mængde)  {
        mængdeILiter = mængde;
    }
}
