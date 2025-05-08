package application.model;

public class PåfyldtMængde {

    private double mængdeILiter;
    private Batch batch;

    public PåfyldtMængde(double mængdeILiter, Batch batch) {
        this.mængdeILiter = mængdeILiter;
        this.batch = batch;
    }

    public String toString() {
        return "påfyldt mængde: " + this.mængdeILiter + " af batch: " + this.batch;
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
