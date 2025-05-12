package application.model;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Destillat implements Serializable {
    private static int nextId = 1;
    private int id;
    private LocalDateTime datoForPåfyldning;
    private Fad fad;
    private final List<BatchMængde> påfyldteMængder = new ArrayList<>();
    private double faktiskAlkoholProcent;
    private double faktiskMængdeLiter;

    /**
     * Pre: datoForPåfyldning er ikke efter i dag og Fad er ikke null
     * @param datoForPåfyldning
     * @param fad
     */
    public Destillat(LocalDateTime datoForPåfyldning, Fad fad) {
        this.id = nextId++;
        this.datoForPåfyldning = datoForPåfyldning;
        fad.setDestillat(this);
        this.fad = fad;
    }

    public int getId() {
        return id;
    }

    public LocalDateTime getDatoForPåfyldning() {
        return datoForPåfyldning;
    }

    public double getFaktiskMængdeLiter()   {
        return faktiskMængdeLiter;
    }

    public void tapMængdeLiter(double mængdeLiter) {
        this.faktiskMængdeLiter -= mængdeLiter;
    }

    public double getFaktiskAlkoholProcent() {
        return faktiskAlkoholProcent;
    }

    @Override
    public String toString() {
        return "Destillat{" +
                "id=" + id +
                ", datoForPåfyldning=" + datoForPåfyldning +
                ", fad=" + fad +
                '}';
    }

    //link metoder
    public List<BatchMængde> getPåfyldteMængder() {
        return Collections.unmodifiableList(påfyldteMængder);
    }

    public void addPåfyldteMængder(BatchMængde påfyldtMængde) {
        if (!påfyldteMængder.contains(påfyldtMængde)) {
            påfyldteMængder.add(påfyldtMængde);
            //egentlig alkoholprocent og total mængde i liter
            double totalAlkoholMængde = faktiskMængdeLiter * faktiskAlkoholProcent + påfyldtMængde.getMængdeILiter() * påfyldtMængde.getBatch().getAlkoholProcent();
            faktiskMængdeLiter += påfyldtMængde.getMængdeILiter();
            faktiskAlkoholProcent += totalAlkoholMængde / faktiskMængdeLiter;
        }
    }

    public void removePåfyldtMængde(BatchMængde påfyldning) {
        if (påfyldteMængder.contains(påfyldning)) {
            påfyldteMængder.remove(påfyldning);
        }
    }

    public Fad getFad() {
        return fad;
    }
}