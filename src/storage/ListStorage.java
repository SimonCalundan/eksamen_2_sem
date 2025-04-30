package storage;

import application.model.Batch;
import application.model.Destillat;
import application.model.Fad;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class ListStorage implements Storage, Serializable {
    private final List<Destillat> destillater;
    private final List<Fad> fade;
    private final List<Batch> batches;

    public ListStorage() {
        this.destillater = new ArrayList<>();
        this.fade = new ArrayList<>();
        this.batches = new ArrayList<>();
    }

    @Override
    public List<Destillat> getDestillater() {
        return Collections.unmodifiableList(destillater);
    }

    @Override
    public void addDestillat(Destillat destillat) {
        destillater.add(destillat);

    }

    @Override
    public void removeDestillat(Destillat destillat) {
        destillater.remove(destillat);
    }

    @Override
    public List<Fad> getFade() {
        return Collections.unmodifiableList(fade);
    }

    @Override
    public void addFad(Fad fad) {
        fade.add(fad);
    }

    @Override
    public void removeFad(Fad fad) {
        fade.remove(fad);
    }

    @Override
    public List<Batch> getBatches() {
        return Collections.unmodifiableList(batches);
    }

    @Override
    public void addBatch(Batch batch) {
        batches.add(batch);
    }

    @Override
    public void removeBatch(Batch batch) {
        batches.remove(batch);
    }
}
