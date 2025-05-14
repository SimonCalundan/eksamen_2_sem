package application.model;

import java.io.Serializable;

public class DestillatMængde implements Serializable {

    private double mængdeLiter;
    private Destillat destillat;

    public DestillatMængde(double mængdeLiter, Destillat destillat)   {
        this.mængdeLiter = mængdeLiter;
        this.destillat = destillat;
    }

    public double getMængdeLiter() {
        return mængdeLiter;
    }

    public Destillat getDestillat() {
        return destillat;
    }

    public String toString(){
        return "Mængde: " + this.mængdeLiter + " af destillat: " + this.destillat;
    }
}
