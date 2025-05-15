package application.model;

import java.io.Serializable;

public class Batch implements Serializable {
    private String navn;
    private double mængdeLiter;
    private double alkoholProcent;
    private String mark;

    /**
     * Pre: mængdeLiter > 0 og  1 > alkoholProcent > 0
     * @param navn
     * @param mængdeLiter
     * @param alkoholProcent
     */
    public Batch(String navn, double mængdeLiter, double alkoholProcent, String mark) {
        this.navn = navn;
        this.mængdeLiter = mængdeLiter;
        this.alkoholProcent = alkoholProcent;
        this.mark = mark;
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

    public void tapMængdeLiter(double mængdeLiterTappet) {
        this.mængdeLiter -= mængdeLiterTappet;
    }

    public double getAlkoholProcent() {
        return alkoholProcent;
    }

    public String getMark() {
        return mark;
    }

    @Override
    public String toString(){
        return "%s %.2f".formatted(navn, mængdeLiter);
    }
}
