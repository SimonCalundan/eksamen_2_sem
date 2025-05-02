package application.model;

import java.io.Serializable;

public class Batch implements Serializable {
    private String navn;
    private double mængdeLiter;
    private double alkoholProcent;

    public Batch(String navn, double mængdeLiter, double alkoholProcent) {
        this.navn = navn;
        this.mængdeLiter = mængdeLiter;
        this.alkoholProcent = alkoholProcent;
    }

    public String getNavn() {
        return navn;
    }

    public double getMængdeLiter() {
        return mængdeLiter;
    }

    public void setMængdeLiter(double mængdeLiter) {
        this.mængdeLiter = mængdeLiter;
    }

    public double getAlkoholProcent() {
        return alkoholProcent;
    }

    @Override
    public String toString(){
        return "%s %.2f".formatted(navn, mængdeLiter);
    }
}
