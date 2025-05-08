package application.controller;

import application.model.*;
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
     * @param brugtGange mængden af gange fadet er blevet brugt
     * @return Det oprettede fad
     */
    public static Fad createFad(double størrelseLiter, String leverandør, String træsort, int brugtGange, Hylde hylde) {
        if (størrelseLiter <= 0) {
            throw new IllegalArgumentException("størrelseLiter må ikke være mindre end 0");
        }
        var fadToAdd = new Fad(størrelseLiter, leverandør, træsort, brugtGange, hylde);
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

    //Lager
    public static Lager createLager(String navn)    {
        var lagerToAdd = new Lager(navn);
        storage.addLager(lagerToAdd);
        return lagerToAdd;
    }
    public static List<Lager> getLagre()    {
        return storage.getLagre();
    }
    public static Reol createReol(Lager lager, String navn) {
        return lager.createReol(navn);
    }
    public static List<Reol> getReoler(Lager lager) {
        return lager.getReoler();
    }
    public static Hylde createHylde(Reol reol, String navn) {
        return reol.createHylde(navn);
    }
    public static List<Hylde> getHylder(Reol reol)   {
        return reol.getHylder();
    }

    //FærdigProdukt
    public static FærdigProdukt createFærdigProdukt(String navn, ProduktVariant type, double vandMængde)    {
        var færdigProduktToAdd = new FærdigProdukt(navn, type, vandMængde);
        storage.addFærdigProdukt(færdigProduktToAdd);
        return færdigProduktToAdd;
    }
    public static List<FærdigProdukt> getFærdigProdukter()  {
        return storage.getFærdigProdukter();
    }

    //TappetMængde
    public static TappetMængde tapMængdeTilFærdigProdukt(double mængdeLiter, Destillat destillat, FærdigProdukt færdigProdukt) {

        if (mængdeLiter > destillat.getFaktiskMængdeLiter())    {
            throw new RuntimeException("MængdeLiter er større end den totale mægnde væske i destillat");
        }

        var tappetMængdeToAdd = færdigProdukt.createTappetMængde(mængdeLiter, destillat);
        tappetMængdeToAdd.getDestillat().tapMængdeLiter(mængdeLiter);

        return tappetMængdeToAdd;
    }
    public static List<TappetMængde> getTappetMængder(FærdigProdukt færdigProdukt)  {
        return færdigProdukt.getTappetmængder();
    }
}
