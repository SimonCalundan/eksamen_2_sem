package application.controller;

import application.model.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import storage.ListStorage;
import storage.Storage;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class ControllerTest {
    private static Storage storage;
    private static Lager lager;
    private static Reol reol;
    private static Hylde hylde;

    @BeforeEach
    void setUp() {
        storage = new ListStorage();
        Controller.setStorage(storage);

        lager = Controller.createLager("Lager Lars");
        reol = Controller.createReol(lager, "fadreol 01");
        hylde = Controller.createHylde(reol, "fadhylde 01");
    }

    @Test
    void createDestillat() {
        var fad = Controller.createFad(40.0, "Simon", "Eg", 2, hylde);
        // TC1 - gyldig
        {
            Batch b1 = Controller.createBatch("B1", 30, 0.7, "Sall");
            Batch b2 = Controller.createBatch("B2", 70, 0.6, "Sall");
            List<BatchMængde> bmList = new ArrayList<>(List.of(
                    Controller.createBatchMængde(b1, 30),
                    Controller.createBatchMængde(b2, 70)
            ));
            LocalDateTime datoForPåfyldning = LocalDateTime.now().minusMinutes(1);
            var d = Controller.createDestillat(datoForPåfyldning, fad, bmList);

            assertAll(
                    () -> assertNotNull(d),
                    () -> assertTrue(d.getPåfyldteMængder().containsAll(bmList)),
                    () -> assertEquals(0, b1.getMængdeLiter()),
                    () -> assertEquals(0, b2.getMængdeLiter())
            );
        }
        // TC2 - gyldig
        {
            Batch b1 = Controller.createBatch("B1", 30, 0.7, "Sall");
            Batch b2 = Controller.createBatch("B2", 70, 0.6, "Sall");
            List<BatchMængde> bmList = new ArrayList<>(List.of(
                    Controller.createBatchMængde(b1, 30),
                    Controller.createBatchMængde(b2, 70)
            ));
            LocalDateTime datoForPåfyldning = LocalDateTime.now();
            var d = Controller.createDestillat(datoForPåfyldning, fad, bmList);

            assertAll(
                    () -> assertNotNull(d),
                    () -> assertTrue(d.getPåfyldteMængder().containsAll(bmList)),
                    () -> assertEquals(0, b1.getMængdeLiter()),
                    () -> assertEquals(0, b2.getMængdeLiter())
            );
        }
        // TC3 - ugyldig (datoForPåfyldning = nu + 1 sekund)
        {
            Batch b1 = Controller.createBatch("B1", 30, 0.7, "Sall");
            Batch b2 = Controller.createBatch("B2", 70, 0.6, "Sall");
            List<BatchMængde> bmList = new ArrayList<>(List.of(
                    Controller.createBatchMængde(b1, 30),
                    Controller.createBatchMængde(b2, 70)
            ));
            LocalDateTime datoForPåfyldning = LocalDateTime.now().plusSeconds(1);

            Exception exception = assertThrows(IllegalArgumentException.class, () -> {
                Controller.createDestillat(datoForPåfyldning, fad, bmList);
            });
            assertEquals("Dato for påfyldning må ikke være efter nuværende tidspunk", exception.getMessage());
        }
        // TC4 - ugyldig (fad er null)
        {
            Batch b1 = Controller.createBatch("B1", 30, 0.7, "Sall");
            Batch b2 = Controller.createBatch("B2", 70, 0.6, "Sall");
            List<BatchMængde> bmList = new ArrayList<>(List.of(
                    Controller.createBatchMængde(b1, 30),
                    Controller.createBatchMængde(b2, 70)
            ));
            LocalDateTime datoForPåfyldning = LocalDateTime.now().minusMinutes(5);

            Exception exception = assertThrows(IllegalArgumentException.class, () -> {
                Controller.createDestillat(datoForPåfyldning, null, bmList);
            });
            assertEquals("Fad må ikke være null", exception.getMessage());
        }
        // TC5 - ugyldig (datoForPåfyldning = nu + 5 dage)
        {
            Batch b1 = Controller.createBatch("B1", 30, 0.7, "Sall");
            Batch b2 = Controller.createBatch("B2", 70, 0.6, "Sall");
            List<BatchMængde> bmList = new ArrayList<>(List.of(
                    Controller.createBatchMængde(b1, 30),
                    Controller.createBatchMængde(b2, 70)
            ));
            LocalDateTime datoForPåfyldning = LocalDateTime.now().plusDays(5);

            Exception exception = assertThrows(IllegalArgumentException.class, () -> {
                Controller.createDestillat(datoForPåfyldning, fad, bmList);
            });
            assertEquals("Dato for påfyldning må ikke være efter nuværende tidspunk", exception.getMessage());
        }
        // TC6 - ugyldig (batchMænger er tom)
        {
            List<BatchMængde> bmListEmpty = new ArrayList<>();
            LocalDateTime datoForPåfyldning = LocalDateTime.now();


            Exception exception = assertThrows(IllegalArgumentException.class, () -> {
                Controller.createDestillat(datoForPåfyldning, fad, bmListEmpty);
            });
            assertEquals("batchMængder må ikke være tom", exception.getMessage());
        }
    }

    @Test
    void createBatchMængde() {
        var fad = Controller.createFad(0.5, "Testleverandør", "Eg", 2, hylde);
        var batch = Controller.createBatch("TestBatch", 30, 0.6, "Sall");

        // TC1 - grænseværdi (mængdePåfyldt = 0.1)
        {
            double mængdePåfyldt = 0.1;
            var pm = Controller.createBatchMængde(batch, mængdePåfyldt);

            assertAll("TC1 - grænseværdi",
                    () -> assertEquals(mængdePåfyldt, pm.getMængdeILiter()),
                    () -> assertEquals(batch, pm.getBatch()));
        }
        // TC2 - gyldig
        {
            double mængdePåfyldt = 11.5;
            var pm = Controller.createBatchMængde(batch, mængdePåfyldt);

            assertAll(
                    () -> assertEquals(mængdePåfyldt, pm.getMængdeILiter()),
                    () -> assertEquals(batch, pm.getBatch()));
        }
        // TC3 - ugyldig (mængdePåfyldt = -3.5)
        {
            double mængdePåfyldt = -3.5;
            Exception exception = assertThrows(IllegalArgumentException.class, () -> {
                Controller.createBatchMængde(batch, mængdePåfyldt);
            });
            assertEquals("mængdePåfyldt skal være større end 0", exception.getMessage());
        }
        // TC4 - ugyldig (batch = null)
        {
            double mængdePåfyldt = 15.0;
            Exception exception = assertThrows(IllegalArgumentException.class, () -> {
                Controller.createBatchMængde(null, mængdePåfyldt);
            });
            assertEquals("Batch må ikke være null", exception.getMessage());
        }
        // TC5 - ugyldig (mængdePåfyldt > batch.getMængdeLiter)
        {
            double mængdePåfyldt = 40;
            Exception exception = assertThrows(IllegalStateException.class, () -> {
                Controller.createBatchMængde(batch, mængdePåfyldt);
            });
            assertEquals("mængdePåfyldt må ikke være større end batchets mængdeLite", exception.getMessage());
        }
    }

    @Test
    void createFad() {
        // TC1 - grænseværdi (0.0 liter)
        {
            double størrelseLiter = 0.5;
            String leverandør = "TestLeverandør1";
            String træsort = "Eg";
            int brugtGange = 2;


            Controller.createFad(størrelseLiter, leverandør, træsort, brugtGange, hylde);
            Fad fad = Controller.getFade().getLast();

            assertAll("TC1 - grænseværdi",
                    () -> assertEquals(størrelseLiter, fad.getStørrelseLiter()),
                    () -> assertEquals(leverandør, fad.getLeverandør()),
                    () -> assertEquals(træsort, fad.getTræsort()),
                    () -> assertEquals(brugtGange, fad.getBrugtGange()),
                    () -> assertEquals(hylde, fad.getHylde())
            );
        }

        // TC2 - gyldig værdi (30.0 liter)
        {
            double størrelseLiter = 30.0;
            String leverandør = "TestLeverandør2";
            String træsort = "Eg";
            int brugtGange = 2;

            Controller.createFad(størrelseLiter, leverandør, træsort, brugtGange, hylde);
            Fad fad = Controller.getFade().getLast();
            assertAll(
                    () -> assertEquals(størrelseLiter, fad.getStørrelseLiter()),
                    () -> assertEquals(leverandør, fad.getLeverandør()),
                    () -> assertEquals(træsort, fad.getTræsort()),
                    () -> assertEquals(brugtGange, fad.getBrugtGange()),
                    () -> assertEquals(hylde, fad.getHylde())
            );
        }
        // TC3 - ugyldig værdi (-3.1 liter)
        {
            double størrelseLiter = -3.1;
            String leverandør = "TestLeverandør3";
            String træsort = "Eg";
            int brugtGange = 2;

            Exception exception = assertThrows(IllegalArgumentException.class, () -> {
                Controller.createFad(størrelseLiter, leverandør, træsort, brugtGange, hylde);
            });

            assertEquals("størrelseLiter må ikke være mindre end 0", exception.getMessage(), "TC3 - ugyldig værdi");
        }
    }


    @Test
    void createBatch() {
        // TC1 - grænseværdi (mængdeLiter = 0.5)
        {
            String navn = "TestBatch";
            double mængdeLiter = 0.5;
            double alkoholProcent = 0.10;
            String mark = "Sall";
            Controller.createBatch(navn, mængdeLiter, alkoholProcent, mark);
            Batch batch = Controller.getBatches().getLast();
            assertAll(
                    () -> assertEquals(navn, batch.getNavn()),
                    () -> assertEquals(mængdeLiter, batch.getMængdeLiter())
            );
        }
        // TC2 - almen input
        {
            String navn = "TestBatch";
            double mængdeLiter = 12.0;
            double alkoholProcent = 0.6;
            String mark = "Sall";
            Controller.createBatch(navn, mængdeLiter, alkoholProcent, mark);
            Batch batch = Controller.getBatches().getLast();
            assertAll(
                    () -> assertEquals(navn, batch.getNavn()),
                    () -> assertEquals(mængdeLiter, batch.getMængdeLiter())
            );
        }
        // TC3 - grænseværdi (alkoholProcent = 0)
        {
            String navn = "TestBatch";
            double mængdeLiter = 12.0;
            double alkoholProcent = 0;
            String mark = "Sall";
            Controller.createBatch(navn, mængdeLiter, alkoholProcent, mark);
            Batch batch = Controller.getBatches().getLast();
            assertAll(
                    () -> assertEquals(navn, batch.getNavn()),
                    () -> assertEquals(mængdeLiter, batch.getMængdeLiter())
            );
        }
        // TC4 - ugyldig værdi (mængdeLiter = -3.1)
        {
            String navn = "TestBatch";
            double mængdeLiter = -3.1;
            double alkoholProcent = 0.6;
            String mark = "Sall";
            Exception exception = assertThrows(IllegalArgumentException.class, () -> {
                Controller.createBatch(navn, mængdeLiter, alkoholProcent, mark);
            });
            assertEquals("mængdeLiter må ikke være mindre en 0", exception.getMessage(), "TC4 - ugyldig værdi (mængdeLiter = -3.1)");
        }
        // TC5 - ugyldig værdi (alkoholProcent = -0.3)
        {
            String navn = "TestBatch";
            double mængdeLiter = 10.5;
            double alkoholProcent = -0.3;
            String mark = "Sall";
            Exception exception = assertThrows(IllegalArgumentException.class, () -> {
                Controller.createBatch(navn, mængdeLiter, alkoholProcent, mark);
            });
            assertEquals("alkoholProcent må ikke være mindre en 0 eller større end 1", exception.getMessage(), "TC4 - ugyldig værdi (mængdeLiter = -3.1)");
        }
    }

    @Test
    void flytFadTilNyPlacering() {
        Lager nyLager = Controller.createLager("Lager Lars");
        Reol nyReol = Controller.createReol(nyLager, "fadreol 01");
        Hylde nyHylde = Controller.createHylde(nyReol, "fadhylde 01");

        var fad = Controller.createFad(10.5, "Test leverandør", "Eg", 3, hylde);
        assertEquals(hylde, fad.getHylde());

        Controller.flytFadTilNyPlacering(fad, nyHylde);
        assertEquals(nyHylde, fad.getHylde());
        assertNotEquals(hylde, fad.getHylde());
    }

    // TODO
    @Test
    void createFærdigProdukt() {
        String navn = "Golden Hour Whisky";
        ProduktVariant type = ProduktVariant.WHISKY;
        var fad = Controller.createFad(30.0,
                "Simon", "Eg", 2, hylde);

        // TC1 - Grænseværdi (vandMænggde = 0)
        {
            var batch = Controller.createBatch("B1", 30.0, 0.6, "Sall");
            var bm = Controller.createBatchMængde(batch, 30.0);
            var destillat = Controller.createDestillat(
                    LocalDateTime.now(), fad, List.of(bm));
            DestillatMængde dm = Controller.createDestillatMængde(destillat,
                    30.0);
            double vandMængde = 0.0;

            List<DestillatMængde> dmList = new ArrayList<>(List.of(dm));
            Controller.createFærdigProdukt(navn, type, vandMængde, dmList);

            var fp = Controller.getFærdigProdukter().getLast();
            assertAll(
                    () -> assertEquals(navn, fp.getNavn()),
                    () -> assertEquals(type, fp.getType()),
                    () -> assertEquals(vandMængde, fp.getVandMængde()),
                    () -> assertTrue(fp.getTappetmængder().containsAll(dmList)),
                    () -> {
                        double listSum = dmList.stream()
                                .mapToDouble(el -> el.getDestillat().getFaktiskMængdeLiter())
                                .sum();
                        assertEquals(0, listSum);
                    }

            );
        }
        // TC2
        {
            var batch = Controller.createBatch("B1", 30.0, 0.6, "Sall");
            var bm = Controller.createBatchMængde(batch, 30.0);
            var destillat = Controller.createDestillat(
                    LocalDateTime.now(), fad, List.of(bm));
            DestillatMængde dm = Controller.createDestillatMængde(destillat,
                    30.0);
            double vandMængde = 30.0;

            List<DestillatMængde> dmList = new ArrayList<>(List.of(dm));
            Controller.createFærdigProdukt(navn, type, vandMængde, dmList);

            var fp = Controller.getFærdigProdukter().getLast();
            assertAll(
                    () -> assertEquals(navn, fp.getNavn()),
                    () -> assertEquals(type, fp.getType()),
                    () -> assertEquals(vandMængde, fp.getVandMængde()),
                    () -> assertTrue(fp.getTappetmængder().containsAll(dmList)),
                    () -> {
                        double listSum = dmList.stream()
                                .mapToDouble(el -> el.getDestillat().getFaktiskMængdeLiter())
                                .sum();
                        assertEquals(0, listSum);
                    }
            );
        }
        // TC3 - ugyldig værdi (vandMængde = -30.0)
        {
            var batch = Controller.createBatch("B1", 30.0, 0.6, "Sall");
            var bm = Controller.createBatchMængde(batch, 30.0);
            var destillat = Controller.createDestillat(
                    LocalDateTime.now(), fad, List.of(bm));
            DestillatMængde dm = Controller.createDestillatMængde(destillat,
                    30.0);
            double vandMængde = -30.0;
            List<DestillatMængde> dmList = new ArrayList<>(List.of(dm));
            Exception exception = assertThrows(IllegalArgumentException.class, () -> {
                Controller.createFærdigProdukt(navn, type, vandMængde, dmList);
            });
            assertEquals("vandMængde må ikke være mindre end 0", exception.getMessage(), "TC3 - ugyldig værdi (vandMængde = -30.0)");
        }
    }

    @Test
    void createDestillatMængde() {
        var fad = Controller.createFad(
                40.0, "Peter", "Eg",
                3, hylde);

        var batch = Controller.createBatch("Simons Batch", 40, 0.6, "Sall");
        List<BatchMængde> batchMængdeList = new ArrayList<>(List.of(
                Controller.createBatchMængde(batch, 40)
        ));

        // TC1 - grænseværdi (mængdeLiter = 0.1)
        {
            var destillat = Controller.createDestillat(LocalDateTime.now(), fad, batchMængdeList);
            double mængdeLiter = 0.1;

            DestillatMængde dm = Controller.createDestillatMængde(destillat, mængdeLiter);
            assertNotNull(dm);
        }

        // TC2 - normal værdi (mængdeLiter = 30)
        {
            var destillat = Controller.createDestillat(LocalDateTime.now(), fad, batchMængdeList);
            double mængdeLiter = 30;
            double forventetEfter = destillat.getFaktiskMængdeLiter() - mængdeLiter;

            DestillatMængde dm = Controller.createDestillatMængde(destillat, mængdeLiter);

            assertNotNull(dm);
        }

        // TC3 - grænseværdi (mængdeLiter = 40)
        {
            var destillat = Controller.createDestillat(LocalDateTime.now(), fad, batchMængdeList);
            double mængdeLiter = 40;
            double forventetEfter = 0.0;


            DestillatMængde dm = Controller.createDestillatMængde(destillat, mængdeLiter);
            assertNotNull(dm);
        }

        // TC4 - Ugyldig (mængdeLiter = 50 > faktiskMængdeLiter)
        {
            var destillat = Controller.createDestillat(LocalDateTime.now(), fad, batchMængdeList);
            double mængdeLiter = 50;

            IllegalStateException exception = assertThrows(IllegalStateException.class, () ->
                    Controller.createDestillatMængde(destillat, mængdeLiter)
            );

            assertEquals("Der er ikke nok mængde i Destillatet", exception.getMessage());
        }

        // TC5 - Ugyldig (mængdeLiter = -0.3)
        {
            var destillat = Controller.createDestillat(LocalDateTime.now(), fad, batchMængdeList);
            double mængdeLiter = -0.3;

            IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () ->
                    Controller.createDestillatMængde(destillat, mængdeLiter)
            );

            assertEquals("Mængden skal være større end 0", exception.getMessage());
        }
    }
}
