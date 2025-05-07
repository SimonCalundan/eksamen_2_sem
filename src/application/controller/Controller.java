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

    /**
     * Opretter og gemmer et nyt batch
     * @param navn navnet på det oprettede batch
     * @param mængdeILiter hvor mange liter er batchet
     * @param alkoholProcent batchests alkoholprocent. Skal være en værdi mellem 0 og 1
     * @return det oprettede batch
     */
    public static Batch createBatch(String navn, double mængdeILiter, double alkoholProcent) {
        if (mængdeILiter <= 0) {
            throw new IllegalArgumentException("mængdeLiter må ikke være mindre en 0");
        }
        if (alkoholProcent < 0 || alkoholProcent > 1) {
            throw new IllegalArgumentException("alkoholProcent må ikke være mindre en 0 eller større end 1");
        }
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

    /**
     * Opretter og gemmer et nyt fad i systemet
     * @param størrelseLiter fadets kapacitet
     * @param leverandør fadets leverandør
     * @param træsort den træsort fadet er lavet af
     * @param erGenbrugt om fadet er blevet brugt for
     * @return Det oprettede fad
     */
    public static Fad createFad(double størrelseLiter, String leverandør, String træsort, boolean erGenbrugt) {
        if (størrelseLiter <= 0) {
            throw new IllegalArgumentException("størrelseLiter må ikke være mindre end 0");
        }
        var fadToAdd = new Fad(størrelseLiter, leverandør, træsort, erGenbrugt);
        storage.addFad(fadToAdd);
        return fadToAdd;
    }

    public static void removeFad(Fad fad) {
        storage.removeFad(fad);
    }

    //Destillat

    /**
     * Opretter et Destillat objekt
     * @param datoForPåfyldning Tidspunktet hvor påfyldningen fandt sted
     * @param fad det fad som destillatet skal være på
     * @return Destillat objektet som oprettes
     */
    public static Destillat createDestillat(LocalDateTime datoForPåfyldning, Fad fad) {
        if (datoForPåfyldning.isAfter(LocalDateTime.now())) {
            throw new IllegalArgumentException("Dato for påfyldning må ikke være efter nuværende tidspunk");
        }
        if (fad == null) {
            throw new IllegalArgumentException("Fad må ikke være null");
        }
        var destillatToAdd = new Destillat(datoForPåfyldning, fad);
        storage.addDestillat(destillatToAdd);
        return destillatToAdd;
    }

    /**
     * Fylder en given mængde af et batch til et destillat
     * @param destillat det destillat som batches hældes på
     * @param batch det batch der ønskes at tappes fra
     * @param mængdePåfyldt hvor meget der ønkes at tappes af batchet
     * @return PåfyldtMængde objekt
     */
    public static PåfyldtMængde createPåfyldtMængde(Destillat destillat, Batch batch, double mængdePåfyldt) {
        if (batch == null){
            throw new IllegalArgumentException("Batch må ikke være null");
        }
        if (batch.getMængdeLiter() - mængdePåfyldt < 0) {
            throw new IllegalStateException("Der er ikke nok mændge i batchen");
        }
        if (mængdePåfyldt <= 0) {
            throw new IllegalArgumentException("mængdePåfyldt skal være større end 0");
        }
        if (destillat == null) {
            throw new IllegalArgumentException("Destillat må ikke være null");
        }
        batch.setMængdeLiter(batch.getMængdeLiter() - mængdePåfyldt);
        return destillat.createPåfyldtMængde(mængdePåfyldt, batch);
    }

    public static void removeDestillat(Destillat destillat) {
        storage.removeDestillat(destillat);
    }

    public static List<Destillat> getDestillater() {
        return storage.getDestillater();
    }


}
