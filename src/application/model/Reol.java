package application.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class Reol implements Serializable {
    private String navn;
    private Lager lager;
    private List<Hylde> hylder;

    Reol(String navn, Lager lager)  {
        this.navn = navn;
        this.lager = lager;

        hylder = new ArrayList<>();
    }
    public Hylde createHylde(String navn)   {

        var newHylde = new Hylde(navn, this);
        hylder.add(newHylde);
        return newHylde;
    }

    public String getNavn() {
        return navn;
    }
    public Lager getLager() {
        return lager;
    }

    public List<Hylde> getHylder() {
        return new ArrayList<>(hylder);
    }

    @Override
    public String toString() {
        return navn;
    }
}
