package application.model;

import java.io.Serializable;

public class Fad implements Serializable {
    private static int nextNr = 1;
    private int nr;
    private double størrelseLiter;
    private Destillat destillat;
    private int brugtGange;
    private String træsort;
    private String leverandør;
    private Hylde hylde;

    public Fad(double størrelseLiter, String leverandør, String træsort, int brugtGange, Hylde hylde) {
        this.nr = nextNr++;
        this.størrelseLiter = størrelseLiter;
        this.leverandør = leverandør;
        this.træsort = træsort;
        this.brugtGange = brugtGange;
        this.destillat = null;
        this.hylde = hylde;
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

    public int getBrugtGange() {
        return brugtGange;
    }

    public String getTræsort() {
        return træsort;
    }

    public String getLeverandør() {
        return leverandør;
    }

    public Hylde getHylde() {
        return hylde;
    }

    public void setHylde(Hylde hylde) {
        if (this.hylde != hylde)    {
            this.hylde.removeFad(this);
        }
        this.hylde = hylde;
        if (hylde != null)  {
            hylde.addFad(this);
        }
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
