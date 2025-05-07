package application.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class Lager implements Serializable {
    private String navn;
    private List<Reol> reoler;

    public Lager(String navn)   {
        this.navn = navn;
        reoler = new ArrayList<>();
    }
    public Reol createReol(String navn)    {

        var newReol = new Reol(navn, this);

        return newReol;
    }

    public String getNavn() {
        return navn;
    }
    public List<Reol> getReoler() {
        return new ArrayList<>(reoler);
    }

    @Override
    public String toString() {
        return navn;
    }
}
