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

    /**
     * Pre: størrelseLiter > 0
     *
     * @param størrelseLiter
     * @param leverandør
     * @param træsort
     * @param brugtGange
     * @param hylde
     */
    public Fad(double størrelseLiter, String leverandør, String træsort, int brugtGange, Hylde hylde) {
        this.nr = nextNr++;
        this.størrelseLiter = størrelseLiter;
        this.leverandør = leverandør;
        this.træsort = træsort;
        this.brugtGange = brugtGange;
        this.destillat = null;
        this.hylde = hylde;
        hylde.addFad(this);
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
        if (this.hylde != hylde) {
            this.hylde.removeFad(this);
        }
        this.hylde = hylde;
        if (hylde != null) {
            hylde.addFad(this);
        }
    }

    //TODO mangler et eller andet for at logikken kan forstået i produktionspanen
    public String fadOversigt() {
        return """
                Fad nr: %s
                Størrelse i Liter: %.2f
                Nuværende destillat: %s
                Gange brugt: %d
                Træsort: %s
                Leverandør: %s
                """.formatted(nr, størrelseLiter,
                (getDestillat() == null ? "intet" : getDestillat().getId()),
                brugtGange, træsort, leverandør);
    }

    //TODO mangler lidt så det kan forståes ordentligt
    @Override
    public String toString() {
        return "%s %d, %s %.2f, %s %s, %s, %s, %s, %s"
                .formatted("Fad nr: ",nr, "Størrelse: ", størrelseLiter,"Destillater: ", (getDestillat() == null ? "intet" : getDestillat().getId()), "Gange brugt: " + brugtGange, hylde,
                        træsort, leverandør);
    }

    public String GUIwiev(){
        return "%s %d, %s %.2f, %s %s".formatted("Fad nr: ",nr, "Størrelse: ", størrelseLiter,"Destillater: ", (getDestillat() == null ? "intet" : getDestillat().getId()));
    }

    //linkmetoder
    public void setDestillat(Destillat destillat) {
        if (this.destillat != destillat) {
            this.destillat = destillat;
        }
    }
}
