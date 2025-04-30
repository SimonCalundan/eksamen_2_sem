package application.model;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Destillat implements Serializable {
    private int id;
    private LocalDateTime datoForPåfyldning;
    private double alkoholProcent;
    private List<Batch> batches;
    private List<Fad> fade;

    public Destillat(LocalDateTime datoForPåfyldning, double alkoholProcent, List<Batch> batches) {
        this.datoForPåfyldning = datoForPåfyldning;
        this.alkoholProcent = alkoholProcent;
        this.batches = batches;
        this.fade = new ArrayList<>();
    }

    public void addFad(Fad fad) {
        fad.setDestillat(this);
        fade.add(fad);
    }

    public void removeFad(Fad fad) {
        fad.setDestillat(null);
        fade.remove(fad);
    }

    public void addBatch(Batch batch) {
        batches.add(batch);
    }

    public void removeBatch(Batch batch) {
        batches.remove(batch);
    }

    public int getId() {
        return id;
    }

    public LocalDateTime getDatoForPåfyldning() {
        return datoForPåfyldning;
    }

    public double getAlkoholProcent() {
        return alkoholProcent;
    }

    public List<Batch> getBatches() {
        return Collections.unmodifiableList(batches);
    }

    public List<Fad> getFade() {
        return Collections.unmodifiableList(fade);
    }

    @Override
    public String toString() {
        return "Destillat{" +
                "id=" + id +
                ", datoForPåfyldning=" + datoForPåfyldning +
                ", alkoholProcent=" + alkoholProcent +
                ", batches=" + batches +
                ", fade=" + fade +
                '}';
    }
}
