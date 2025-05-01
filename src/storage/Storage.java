package storage;

import application.model.Batch;
import application.model.Destillat;
import application.model.Fad;

import java.util.List;

public interface Storage {
    List<Destillat> getDestillater();

    void addDestillat(Destillat destillat);

    void removeDestillat(Destillat destillat);

    List<Fad> getFade();

    void addFad(Fad fad);

    void removeFad(Fad fad);

    List<Batch> getBatches();

    void addBatch(Batch batch);

    void removeBatch(Batch batch);

}
