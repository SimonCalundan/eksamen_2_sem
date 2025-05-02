package application.controller;

import application.model.Batch;
import application.model.Destillat;
import application.model.Fad;
import application.model.PåfyldtMængde;
import storage.Storage;

import java.time.LocalDateTime;
import java.util.List;

public abstract class Controller {
    private static Storage storage;

    public static void setStorage(Storage newStorage) {
        storage = newStorage;
    }

    //Batch
    public static List<Batch> getBatches() {
        return storage.getBatches();
    }

    public static Batch createBatch(String navn, double mængdeILiter, double alkoholProcent) {
        var batchToAdd = new Batch(navn, mængdeILiter, alkoholProcent);
        storage.addBatch(batchToAdd);
        return batchToAdd;
    }

    public static void removeBatch(Batch batch) {
        storage.removeBatch(batch);
    }

    //Fad
    public static List<Fad> getFade() {
        return storage.getFade();
    }

    public static Fad createFad(double størrelseLiter, String leverandør, String træsort, boolean erGenbrugt) {
        var fadToAdd = new Fad(størrelseLiter, leverandør, træsort, erGenbrugt);
        storage.addFad(fadToAdd);
        return fadToAdd;
    }

    public static void removeFad(Fad fad) {
        storage.removeFad(fad);
    }

    //Destillat
    public static Destillat createDestillat(LocalDateTime datoForPåfyldning, Fad fad) {
        var destillatToAdd = new Destillat(datoForPåfyldning, fad);
        storage.addDestillat(destillatToAdd);
        return destillatToAdd;
    }

    public static PåfyldtMængde createPåfyldtMængder(Destillat destillat, Batch batch, double mængdePåfyldt) {
        if (batch.getMængdeLiter() - mængdePåfyldt < 0) {
            throw new RuntimeException("Der er ikke nok mændge i batchen");
        }
        batch.setMængdeLiter(batch.getMængdeLiter() - mængdePåfyldt);
        return destillat.createPåfyldtMængde(mængdePåfyldt,batch);
    }

    public static void removeDestillat(Destillat destillat) {
        storage.removeDestillat(destillat);
    }

    public static List<Destillat> getDestillater() {
        return storage.getDestillater();
    }








}
