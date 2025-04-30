package application.model;

import java.io.Serializable;

public class Fad implements Serializable {
    private int nr;
    private double størrelseLiter;
    private Destillat destillat;
    private boolean erGenbrugt;
    private String træsort;
    private String leverandør;

    public Fad(double størrelseLiter, String leverandør, String træsort, boolean erGenbrugt) {
        this.størrelseLiter = størrelseLiter;
        this.leverandør = leverandør;
        this.træsort = træsort;
        this.erGenbrugt = erGenbrugt;
        this.destillat = null;
    }

    public void setDestillat(Destillat destillat) {
        if (this.destillat != destillat) {
            Destillat oldDestillat = this.destillat;
            if (oldDestillat != null) {
                oldDestillat.removeFad(this);
            }
            this.destillat = destillat;
            if (destillat != null) {
                destillat.addFad(this);
            }
        }
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

    public boolean isErGenbrugt() {
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
}
