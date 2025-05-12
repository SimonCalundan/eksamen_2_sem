package storage;

import application.model.*;

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

    List<Lager> getLagre();

    void addLager(Lager lager);

    List<FærdigProdukt> getFærdigProdukter();

    void addFærdigProdukt(FærdigProdukt færdigProdukt);

    List<DestillatMængde> getDestillatMængde();

    void addDestillatMængde(DestillatMængde destillatMængde);
}
