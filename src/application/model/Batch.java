package application.model;

import java.io.Serializable;

public class Batch implements Serializable {
    private String navn;
    private double mængdeLiter;

    public Batch(String navn, double mængdeLiter) {
        this.navn = navn;
        this.mængdeLiter = mængdeLiter;
    }

    public String getNavn() {
        return navn;
    }

    public double getMængdeLiter() {
        return mængdeLiter;
    }

    @Override
    public String toString(){
        return "%s %.2f".formatted(navn, mængdeLiter);
    }
}
