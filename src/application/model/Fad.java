package application.model;

import java.io.Serializable;

public class Fad implements Serializable {
    private static int nextNr = 1;
    private int nr;
    private double størrelseLiter;
    private Destillat destillat;
    private boolean erGenbrugt;
    private String træsort;
    private String leverandør;

    /**
     * Pre: størrelseLiter > 0
     * @param størrelseLiter
     * @param leverandør
     * @param træsort
     * @param erGenbrugt
     */
    public Fad(double størrelseLiter, String leverandør, String træsort, boolean erGenbrugt) {
        this.nr = nextNr++;
        this.størrelseLiter = størrelseLiter;
        this.leverandør = leverandør;
        this.træsort = træsort;
        this.erGenbrugt = erGenbrugt;
        this.destillat = null;
    }

    public int getNr() {
        return nr;
    }

    public double getStørrelseLiter() {
        return størrelseLiter;
    }

    public Destillat getDestillat() {
        return destillat;
    }

    public boolean erGenbrugt() {
        return erGenbrugt;
    }

    public String getTræsort() {
        return træsort;
    }

    public String getLeverandør() {
        return leverandør;
    }

    @Override
    public String toString() {
        return "%d, %.2f, %s, %b, %s, %s"
                .formatted(nr, størrelseLiter, (getDestillat() == null ? "intet destillat" : getDestillat().getId()), erGenbrugt,
                        træsort, leverandør);
    }

    //linkmetoder
    public void setDestillat(Destillat destillat) {
        if (this.destillat != destillat) {
            this.destillat = destillat;
        }
    }
}
