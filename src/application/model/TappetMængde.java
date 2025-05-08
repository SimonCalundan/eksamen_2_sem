package application.model;

import java.io.Serializable;
import java.util.List;

public class TappetMængde implements Serializable {
    private double mængdeLiter;
    private Destillat destillat;

    TappetMængde(double mængdeLiter, Destillat destillat)   {
        this.mængdeLiter = mængdeLiter;
        this.destillat = destillat;
    }

    public double getMængdeLiter() {
        return mængdeLiter;
    }
    public Destillat getDestillat() {
        return destillat;
    }
}
