package application.controller;

import application.model.Batch;
import application.model.Destillat;
import application.model.Fad;
import storage.Storage;

import java.time.LocalDateTime;
import java.util.List;

public abstract class Controller {
    private static Storage storage;

    public static void setStorage(Storage newStorage) {
        storage = newStorage;
    }

    public static List<Destillat> getDestillater() {
        return storage.getDestillater();
    }

    public static Destillat registrerPåfyldning(LocalDateTime datoForPåfyldning, double alkoholProcent,
                                                List<Batch> batches, List<Fad> fade) {
        var destillatToAdd = new Destillat(datoForPåfyldning, alkoholProcent, batches);
        for (var fad : fade){
            fad.setDestillat(destillatToAdd);
        }
        storage.addDestillat(destillatToAdd);
        return destillatToAdd;
    }

    public static void removeDestillat(Destillat destillat) {
        storage.removeDestillat(destillat);
    }

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

    public static List<Batch> getBatches() {
        return storage.getBatches();
    }

    public static Batch createBatch(String navn, double mængdeLiter) {
        var batchToAdd = new Batch(navn, mængdeLiter);
        storage.addBatch(batchToAdd);
        return batchToAdd;
    }

    public static void removeBatch(Batch batch) {
        storage.removeBatch(batch);
    }


}
