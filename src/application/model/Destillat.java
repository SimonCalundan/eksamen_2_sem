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
    private final List<PåfyldtMængde> påfyldteMængder = new ArrayList<>();

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

    @Override
    public String toString() {
        return "Destillat{" +
                "id=" + id +
                ", datoForPåfyldning=" + datoForPåfyldning +
                ", fad=" + fad +
                '}';
    }

    //link metoder
    public List<PåfyldtMængde> getPåfyldteMængder() {
        return Collections.unmodifiableList(påfyldteMængder);
    }

    public PåfyldtMængde createPåfyldtMængde(double mængdeILiter, Batch batch) {
        PåfyldtMængde påfyldning = new PåfyldtMængde(mængdeILiter, batch);
        påfyldteMængder.add(påfyldning);
        return påfyldning;
    }

    public void removePåfyldtMængde(PåfyldtMængde påfyldning) {
        if (påfyldteMængder.contains(påfyldning)) {
            påfyldteMængder.remove(påfyldning);
        }
    }

    public Fad getFad() {
        return fad;
    }
}