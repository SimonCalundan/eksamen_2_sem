package application.controller;

import application.model.*;
import storage.Storage;

import java.time.LocalDateTime;
import java.util.*;

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
     *
     * @param navn           navnet på det oprettede batch
     * @param mængdeILiter   hvor mange liter er batchet
     * @param alkoholProcent batchests alkoholprocent. Skal være en værdi mellem 0 og 1
     * @param mark marken hvor batchens malt er produceret på
     * @return det oprettede batch
     * @throws IllegalArgumentException hvis mængdeILiter er mindre eller lig med 0,
     *                                  alkoholProcent ikke er mellem 0 og 1 eller hvis navn input er tomt
     */
    public static Batch createBatch(String navn, double mængdeILiter, double alkoholProcent, String mark) throws IllegalArgumentException {
        if (mængdeILiter <= 0) {
            throw new IllegalArgumentException("mængdeLiter må ikke være mindre en 0");
        }
        if (alkoholProcent < 0 || alkoholProcent >= 1) {
            throw new IllegalArgumentException("alkoholProcent må ikke være mindre en 0 eller større end 1");
        }
        if (navn.isBlank()) {
            throw new IllegalArgumentException("Navn må ikke være tomt");
        } if (mark.isBlank()) {
            throw new IllegalArgumentException("Mark må ikke være tomt");
        }
        var batchToAdd = new Batch(navn, mængdeILiter, alkoholProcent, mark);
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
     * Flytter et fad til en anden hylde. Kan godt være en hylde tilhørende
     * en reol på et andet lager
     *
     * @param fad   fadet man ønsker flyttet
     * @param hylde den nye hylde fadet flyttes til
     * @throws IllegalArgumentException hvis fad eller hylde er null
     */
    public static void flytFadTilNyPlacering(Fad fad, Hylde hylde) throws IllegalArgumentException {
        if (fad == null || hylde == null) {
            throw new IllegalArgumentException(
                    "Et fad og en hylde skal være valgt for en flytning"
            );
        }
        fad.setHylde(hylde);
    }

    /**
     * Opretter og gemmer et nyt fad i systemet
     *
     * @param størrelseLiter fadets kapacitet
     * @param leverandør     fadets leverandør
     * @param træsort        den træsort fadet er lavet af
     * @param brugtGange     hvor mange gange fadet er blevet brugt før
     * @param hylde          den hylde fadet skal stå på. Bemærk: dette kan ændres senere
     * @return det oprettede fad
     * @throws IllegalArgumentException hvis størrelseLiter er mindre eller ligmed 0,
     *                                  leverandør eller træsort er tomt, brugtgange er < 0 eller hvis hylde er null
     */
    public static Fad createFad(double størrelseLiter, String leverandør, String træsort,
                                int brugtGange, Hylde hylde) throws IllegalArgumentException {
        if (størrelseLiter <= 0) {
            throw new IllegalArgumentException("størrelseLiter må ikke være mindre end 0");
        }
        if (leverandør.isBlank() || træsort.isBlank()) {
            throw new IllegalArgumentException("Leverandør og træsort skal være angivet");
        }
        if (brugtGange < 0) {
            throw new IllegalArgumentException("Et fad kan ikke være brugt mindre end 0 gange");
        }
        if (hylde == null) {
            throw new IllegalArgumentException("Hylde må ikke være null");
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
     *
     * @param datoForPåfyldning Tidspunktet hvor påfyldningen fandt sted
     * @param fad               det fad som destillatet skal være på
     * @param batchMængder      batchmængder som destillatet består af
     * @return Destillat objektet som oprettes
     * @throws IllegalArgumentException hvis dateForPåfyldning er efter nuværende tidspunkt
     *                                  eller at Fad er null
     */
    public static Destillat createDestillat(LocalDateTime datoForPåfyldning, Fad fad, List<BatchMængde> batchMængder)
            throws IllegalArgumentException {
        if (datoForPåfyldning.isAfter(LocalDateTime.now())) {
            throw new IllegalArgumentException("Dato for påfyldning må ikke være efter nuværende tidspunk");
        }
        if (fad == null) {
            throw new IllegalArgumentException("Fad må ikke være null");
        }
        if (batchMængder.isEmpty()) {
            throw new IllegalArgumentException("batchMængder må ikke være tom");
        }
        var destillatToAdd = new Destillat(datoForPåfyldning, fad);
        for (BatchMængde batchMængde : batchMængder) {
            destillatToAdd.addPåfyldteMængder(batchMængde);
            batchMængde.getBatch().tapMængdeLiter(batchMængde.getMængdeILiter());
        }
        storage.addDestillat(destillatToAdd);
        return destillatToAdd;
    }

    public static void removeDestillat(Destillat destillat) {
        storage.removeDestillat(destillat);
    }

    public static List<Destillat> getDestillater() {
        return storage.getDestillater();
    }

    //Batch mængde
    /**
     * Opretter og gemmer en Batchmængde
     *
     * @param batch         det batch man tapper fra
     * @param mængdePåfyldt den mængde der tappes fra batches
     * @return Batchmængde objektet som oprettes
     * @throws IllegalArgumentException hvis batch er null og hvis mængde man påfydler er <= 0
     * @throws IllegalStateException    hvis batch totalte mængde - den mængde man vil påfylde giver samlet < 0
     */
    public static BatchMængde createBatchMængde(Batch batch, double mængdePåfyldt)
            throws IllegalArgumentException, IllegalStateException {
        if (batch == null) {
            throw new IllegalArgumentException("Batch må ikke være null");
        }
        if (batch.getMængdeLiter() - mængdePåfyldt < 0) {
            throw new IllegalStateException("mængdePåfyldt må ikke være større end batchets mængdeLite");
        }
        if (mængdePåfyldt <= 0) {
            throw new IllegalArgumentException("mængdePåfyldt skal være større end 0");
        }
        var BatchMængdeToBeAdded = new BatchMængde(mængdePåfyldt, batch);
        return BatchMængdeToBeAdded;
    }

    //Destillat mængde
    /**
     * Opretter og gemmer destillatmængde
     *
     * @param destillat   det destillat der skal tappes fra
     * @param mængdeLiter den mængde der skal tappes fra destillatet
     * @return destillatMængde objekt
     * @throws IllegalArgumentException hvis destillat er null
     *                                  og hvis mængdeLiter <= 0
     * @throws IllegalStateException    hvis destillatets faktiske mængde - mængdeLiter man tapper er mindre end 0
     */
    public static DestillatMængde createDestillatMængde(Destillat destillat, double mængdeLiter) throws IllegalArgumentException {
        if (destillat == null) {
            throw new IllegalArgumentException("Destillatet kan ikke være null");
        }
        if (destillat.getFaktiskMængdeLiter() - mængdeLiter < 0) {
            throw new IllegalStateException("Der er ikke nok mængde i Destillatet");
        }
        if (mængdeLiter <= 0) {
            throw new IllegalArgumentException("Mængden skal være større end 0");
        }
        var DestillatMængdeToAdded = new DestillatMængde(mængdeLiter, destillat);
        return DestillatMængdeToAdded;
    }


    //Lager
    /**
     * Opretter og gemmer et nyt lager i systemet
     *
     * @param navn en ikke unik identifier
     * @return Lager objektet
     * @throws IllegalArgumentException hvis navnet er tomt
     */
    public static Lager createLager(String navn) throws IllegalArgumentException {
        if (navn.isBlank()) {
            throw new IllegalArgumentException("Navn skal være angivet");
        }
        var lagerToAdd = new Lager(navn);
        storage.addLager(lagerToAdd);
        return lagerToAdd;
    }

    public static List<Lager> getLagre() {
        return storage.getLagre();
    }


    /**
     * Opretter og gemmer en ny reol i systemet ud fra et lager
     *
     * @param lager det lager som reolen skal oprettes i
     * @param navn  en ikke unik identifier
     * @return Reol objektet
     * @throws IllegalArgumentException hvis lager er null eller navn er tomt
     */
    public static Reol createReol(Lager lager, String navn) throws IllegalArgumentException {
        if (lager == null) {
            throw new IllegalArgumentException("Lager må ikke være null");
        }
        if (navn.isBlank()) {
            throw new IllegalArgumentException("Navn skal være angivet");
        }
        return lager.createReol(navn);
    }

    public static List<Reol> getReoler(Lager lager) {
        return lager.getReoler();
    }

    /**
     * Opretter og gemmer en ny hylde i systemet
     *
     * @param reol den tilkyttede reol som hylden oprettes på
     * @param navn en ikke unik identifier for hylden
     * @return Hylde objektet
     * @throws IllegalArgumentException hvis reol er null eller navn er tomt
     */
    public static Hylde createHylde(Reol reol, String navn) throws IllegalArgumentException {
        if (reol == null) {
            throw new IllegalArgumentException("Lager må ikke være null");
        }
        if (navn.isBlank()) {
            throw new IllegalArgumentException("Navn skal være angivet");
        }
        return reol.createHylde(navn);
    }

    public static List<Hylde> getHylder(Reol reol) {
        return reol.getHylder();
    }

    //FærdigProdukt
    /**
     * Opretter og gemmer færdig produkt
     *
     * @param navn             det færdige produkts navn
     * @param type             valg af type (gin eller whisky)
     * @param vandMængde       hvor meget vand der skal tilføjes til produktet
     * @param destillatMængder liste af destillatMænger der udgør det færdige produkt
     * @return FærdigeProdukt objektet
     * @throws IllegalArgumentException hvis navn er tomt, hvis type er null og hvis vandmængde er < 0
     */
    public static FærdigProdukt createFærdigProdukt(String navn, ProduktVariant type, double vandMængde, List<DestillatMængde> destillatMængder) throws IllegalArgumentException {
        if (navn.isBlank()) {
            throw new IllegalArgumentException("Du skal give det færdige produkt et navn");
        }
        if (type == null) {
            throw new IllegalArgumentException("Du skal vælge en type");
        }
        if (vandMængde < 0) {
            throw new IllegalArgumentException("vandMængde må ikke være mindre end 0");
        }
        if (destillatMængder.isEmpty()) {
            throw new IllegalArgumentException("DestillatMængder må ikke være tom");
        }
        var færdigProduktToAdd = new FærdigProdukt(navn, type, vandMængde);
        storage.addFærdigProdukt(færdigProduktToAdd);
        for (DestillatMængde destillatMængde : destillatMængder) {
            færdigProduktToAdd.addTappetMængde(destillatMængde);
            destillatMængde.getDestillat().tapMængdeLiter(destillatMængde.getMængdeLiter());
        }
        return færdigProduktToAdd;
    }

    /**
     * Returnerer en String af det inputtede produkts historik
     *
     * @param færdigProdukt
     * @return String med historik
     * @author Simon Kidde Calundan
     */
    public static String getFærdigProduktHistorik(FærdigProdukt færdigProdukt) {
        List<DestillatMængde> destillatMængder = new ArrayList<>();
        List<BatchMængde> batchMængder = new ArrayList<>();
        List<Fad> fade = new ArrayList<>();
        LocalDateTime firstDate = null;

        for (DestillatMængde mængde : færdigProdukt.getTappetmængder()) {
            Destillat destillat = mængde.getDestillat();

            destillatMængder.add(mængde);

            batchMængder.addAll(destillat.getPåfyldteMængder());

            fade.add(destillat.getFad());

            LocalDateTime dato = destillat.getDatoForPåfyldning();
            if (firstDate == null || dato.isBefore(firstDate)) {
                firstDate = dato;
            }
        }

        StringBuilder sb = new StringBuilder("Historik for " + færdigProdukt.getNavn() + "\n");

        sb.append("""
                
                ============================================================
                Destillater
                ============================================================
                """);
        sb.append("%-15s %-15s %-15s %n".formatted("Andel i Liter", "Alkohol %", "Dato for påfyldning"));
        destillatMængder.forEach(dm -> {
            double alkProcent = dm.getDestillat().getFaktiskAlkoholProcent() * 100;
            String s = "%-15.2f %-15.2f %-15s %n"
                    .formatted(dm.getMængdeLiter(),
                            alkProcent,
                            dm.getDestillat().getDatoForPåfyldning());
            sb.append(s);
        });

        sb.append("""
                
                ============================================================
                Fade
                ============================================================
                """);
        sb.append("%-15s %-15s %-15s %-15s %n".formatted("Størrelse", "Leverandør", "Træsort", "Gange brugt"));
        fade.forEach(fad -> {
            String s = "%-15.2f %-15s %-15s %-15d %n"
                    .formatted(fad.getStørrelseLiter(), fad.getLeverandør(),
                            fad.getTræsort(), fad.getBrugtGange());
            sb.append(s);
        });

        sb.append("""
                
                ============================================================
                Batches
                ============================================================
                """);
        sb.append("%-15s %-15s %-15s %-15s %n".formatted("Navn", "Andel i liter", "alkohol %", "Mark"));
        batchMængder.forEach(bm -> {
            double alkProcent = bm.getBatch().getAlkoholProcent() * 100;
            String s = "%-15s %-15.2f %-15.2f %-15s %n".formatted(
                    bm.getBatch().getNavn(),
                    bm.getMængdeILiter(), alkProcent,
                    bm.getBatch().getMark()
            );
            sb.append(s);
        });
        sb.append("\nProduktions start: " + firstDate + "\n");

        return sb.toString();
    }

    public static List<FærdigProdukt> getFærdigProdukter() {
        return storage.getFærdigProdukter();
    }

    public static List<DestillatMængde> getTappetMængder(FærdigProdukt færdigProdukt) {
        return færdigProdukt.getTappetmængder();
    }
}
