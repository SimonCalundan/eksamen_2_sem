package application.controller;

import application.model.*;
import org.junit.jupiter.api.Test;
import storage.ListStorage;
import storage.Storage;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

class ControllerTest {
    private static Storage storage;
    private static Lager lager;
    private static Reol reol;
    private static Hylde hylde;

    @org.junit.jupiter.api.BeforeEach
    void setUp() {
        storage = new ListStorage();
        Controller.setStorage(storage);

        lager = Controller.createLager("Lager Lars");
        reol = Controller.createReol(lager, "fadreol 01");
        hylde = Controller.createHylde(reol, "fadhylde 01");
    }

    @org.junit.jupiter.api.Test
    void createDestillat() {
        // TC1 - grænseværdi (dato = nu)
        {
            var datoForPåfyldning = LocalDateTime.now();
            var fad = Controller.createFad(0.5, "Testleverandør",
                    "Eg", 2, hylde);
            Controller.createDestillat(datoForPåfyldning, fad);
            var destillat = Controller.getDestillater().getLast();
            assertAll("TC1 - grænseværdi",
                    () -> assertEquals(datoForPåfyldning, destillat.getDatoForPåfyldning()),
                    () -> assertEquals(fad, destillat.getFad()));
        }
        // TC2 - gyldig
        {
            var datoForPåfyldning = LocalDateTime.of(2025, 5,
                    6, 11, 50);
            var fad = Controller.createFad(0.5, "Testleverandør",
                    "Eg", 2, hylde);
            Controller.createDestillat(datoForPåfyldning, fad);
            var destillat = Controller.getDestillater().getLast();
            assertAll("TC2 - gyldig",
                    () -> assertEquals(datoForPåfyldning, destillat.getDatoForPåfyldning()),
                    () -> assertEquals(fad, destillat.getFad()));
        }
        // TC3 - ugyldig (Fad = null)
        {
            var datoForPåfyldning = LocalDateTime.of(2025, 5,
                    6, 11, 50);
            Exception exception = assertThrows(IllegalArgumentException.class, () -> {
                Controller.createDestillat(datoForPåfyldning, null);
            });
            assertEquals("Fad må ikke være null", exception.getMessage(), "TC3 - ugyldig (Fad = null)");
        }
        // TC4 - ugyldig (datoForPåfyldning = nu + 10 dage)
        {
            var datoForPåfyldning = LocalDateTime.now().plusDays(10);
            var fad = Controller.createFad(0.5, "Testleverandør",
                    "Eg", 2, hylde);
            Exception exception = assertThrows(IllegalArgumentException.class, () -> {
                Controller.createDestillat(datoForPåfyldning, fad);
            });
            assertEquals("Dato for påfyldning må ikke være efter nuværende tidspunk",
                    exception.getMessage(), "TC4 - ugyldig (datoForPåfyldning = nu + 10 dage)");
        }
    }

    @org.junit.jupiter.api.Test
    void createPåfyldtMængder() {
        var fad = Controller.createFad(0.5, "Testleverandør", "Eg", 2, hylde);
        var destillat = Controller.createDestillat(LocalDateTime.now(), fad);
        var batch = Controller.createBatch("TestBatch", 30, 0.6);

        // TC1 - grænseværdi (mængdePåfyldt = 0.1)
        {
            double mængdePåfyldt = 0.1;
            var pmFromCreation = Controller.createPåfyldtMængde(destillat, batch, mængdePåfyldt);
            BatchMængde pmFromDestillat = destillat.getPåfyldteMængder().getLast();

            assertAll("TC1 - grænseværdi",
                    () -> assertTrue(destillat.getPåfyldteMængder().contains(pmFromCreation)),
                    () -> assertEquals(mængdePåfyldt, pmFromDestillat.getMængdeILiter()),
                    () -> assertEquals(batch, pmFromDestillat.getBatch()));
        }
        // TC2 - gyldig
        {
            double mængdePåfyldt = 11.5;
            var pmFromCreation = Controller.createPåfyldtMængde(destillat, batch, mængdePåfyldt);

            BatchMængde pmFromDestillat = destillat.getPåfyldteMængder().getLast();

            assertAll("TC1 - grænseværdi",
                    () -> assertTrue(destillat.getPåfyldteMængder().contains(pmFromCreation)),
                    () -> assertEquals(mængdePåfyldt, pmFromDestillat.getMængdeILiter()),
                    () -> assertEquals(batch, pmFromDestillat.getBatch()));
        }
        // TC3 - ugyldig (mængdePåfyldt = -3.5)
        {
            double mængdePåfyldt = -3.5;
            Exception exception = assertThrows(IllegalArgumentException.class, () -> {
                Controller.createPåfyldtMængde(destillat, batch, mængdePåfyldt);
            });
            assertEquals("mængdePåfyldt skal være større end 0", exception.getMessage(), "TC3 - ugyldig (mængdePåfyldt = -3.5)");
        }
        // TC4 - ugyldig (destillat = null)
        {
            double mængdePåfyldt = 15.0;
            Exception exception = assertThrows(IllegalArgumentException.class, () -> {
                Controller.createPåfyldtMængde(null, batch, mængdePåfyldt);
            });
            assertEquals("Destillat må ikke være null", exception.getMessage(), "TC4 - ugyldig (destillat = null)");
        }
        // TC5 - ugyldig (batch = null)
        {
            double mængdePåfyldt = 15.0;
            Exception exception = assertThrows(IllegalArgumentException.class, () -> {
                Controller.createPåfyldtMængde(destillat, null, mængdePåfyldt);
            });
            assertEquals("Batch må ikke være null", exception.getMessage(), "TC5 - ugyldig (batch = null)");
        }
    }

    @org.junit.jupiter.api.Test
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


    @org.junit.jupiter.api.Test
    void createBatch() {
        // TC1 - grænseværdi (mængdeLiter = 0.5)
        {
            String navn = "TestBatch";
            double mængdeLiter = 0.5;
            double alkoholProcent = 0.10;
            Controller.createBatch(navn, mængdeLiter, alkoholProcent);
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
            Controller.createBatch(navn, mængdeLiter, alkoholProcent);
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
            Controller.createBatch(navn, mængdeLiter, alkoholProcent);
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
            Exception exception = assertThrows(IllegalArgumentException.class, () -> {
                Controller.createBatch(navn, mængdeLiter, alkoholProcent);
            });
            assertEquals("mængdeLiter må ikke være mindre en 0", exception.getMessage(), "TC4 - ugyldig værdi (mængdeLiter = -3.1)");
        }
        // TC5 - ugyldig værdi (alkoholProcent = -0.3)
        {
            String navn = "TestBatch";
            double mængdeLiter = 10.5;
            double alkoholProcent = -0.3;
            Exception exception = assertThrows(IllegalArgumentException.class, () -> {
                Controller.createBatch(navn, mængdeLiter, alkoholProcent);
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

    @Test
    void createFærdigProdukt() {
        String navn = "Golden Hour Whisky";
        ProduktVariant type = ProduktVariant.WHISKY;

        // TC1 - Grænseværdi (vandMænggde = 0)
        {
            double vandMængde = 0.0;

            Controller.createFærdigProdukt(navn, type, vandMængde);
            var færdigProdukt = Controller.getFærdigProdukter().getLast();
            assertAll(
                    () -> assertEquals(navn, færdigProdukt.getNavn()),
                    () -> assertEquals(type, færdigProdukt.getType()),
                    () -> assertEquals(vandMængde, færdigProdukt.getVandMængde())
            );
        }
        // TC2
        {
            double vandMængde = 30.0;

            Controller.createFærdigProdukt(navn, type, vandMængde);
            var færdigProdukt = Controller.getFærdigProdukter().getLast();
            assertAll(
                    () -> assertEquals(navn, færdigProdukt.getNavn()),
                    () -> assertEquals(type, færdigProdukt.getType()),
                    () -> assertEquals(vandMængde, færdigProdukt.getVandMængde())
            );
        }
        // TC3 - ugyldig værdi (vandMængde = -30.0)
        {
            double vandMængde = 30.0;
            Exception exception = assertThrows(IllegalArgumentException.class, () -> {
                Controller.createFærdigProdukt(navn, type, vandMængde);
            });
            assertEquals("vandMængde må ikke være mindre end 0", exception.getMessage(), "TC3 - ugyldig værdi (vandMængde = -30.0)");
        }
    }

    @Test
    void tapMængdeTilFærdigProdukt() {
        // Lager
        var lager = Controller.createLager("Simons lager");
        var reol = Controller.createReol(lager, "Test reolen");
        var hylde = Controller.createHylde(reol, "Nederest");
        var fad = Controller.createFad(
                30.0, "Peter", "Eg",
                3, hylde);

        // TC1 - grænseværdi (mængdeLiter = 0, destillat faktiskMængdeLiter = 0)
        {
            // Faste værdier
            // Obs. er nødt til at blive redefineret i hver testcase, for at
            // destillatets faktiskMængdeLiter ikke bliver påvirkert af tidligere
            // testcases
            var destillat = Controller.createDestillat(LocalDateTime.now(), fad);
            var batch1 = Controller.createBatch("Heksebryg", 45, 0.6);
            var batch2 = Controller.createBatch("Captain Sall", 55, 0.6);
            destillat.createPåfyldtMængde(45.0, batch1);
            destillat.createPåfyldtMængde(55.0, batch2);
            var færdigProdukt = Controller.createFærdigProdukt(
                    "Simons sovs", ProduktVariant.WHISKY, 0);

            // Skiftende værdier
            double mængdeLiter = 0.0;
            double nyDestillatMængde = destillat.getFaktiskMængdeLiter() - mængdeLiter;
            var tm = Controller.tapMængdeTilFærdigProdukt(mængdeLiter, destillat, færdigProdukt);
            assertAll(
                    () -> assertTrue(færdigProdukt.getTappetmængder().contains(tm)),
                    () -> assertEquals(nyDestillatMængde, destillat.getFaktiskMængdeLiter())

            );
        }

        // TC2
        {
            // Faste værdier
            var destillat = Controller.createDestillat(LocalDateTime.now(), fad);
            var batch1 = Controller.createBatch("Heksebryg", 45, 0.6);
            var batch2 = Controller.createBatch("Captain Sall", 55, 0.6);
            destillat.createPåfyldtMængde(45.0, batch1);
            destillat.createPåfyldtMængde(55.0, batch2);
            var færdigProdukt = Controller.createFærdigProdukt(
                    "Simons sovs", ProduktVariant.WHISKY, 0);

            // Skiftende værdier
            double mængdeLiter = 30.0;
            double nyDestillatMængde = destillat.getFaktiskMængdeLiter() - mængdeLiter;
            var tm = Controller.tapMængdeTilFærdigProdukt(mængdeLiter, destillat, færdigProdukt);
            assertAll(
                    () -> assertTrue(færdigProdukt.getTappetmængder().contains(tm)),
                    () -> assertEquals(nyDestillatMængde, destillat.getFaktiskMængdeLiter())

            );
        }
        // TC3 - grænseværdi (mængdeLiter = 100, destillat faktiskMængdeLiter = 100)
        {
            // Faste værdier
            var destillat = Controller.createDestillat(LocalDateTime.now(), fad);
            var batch1 = Controller.createBatch("Heksebryg", 45, 0.6);
            var batch2 = Controller.createBatch("Captain Sall", 55, 0.6);
            destillat.createPåfyldtMængde(45.0, batch1);
            destillat.createPåfyldtMængde(55.0, batch2);
            var færdigProdukt = Controller.createFærdigProdukt(
                    "Simons sovs", ProduktVariant.WHISKY, 0);

            // Skiftende værdier
            double mængdeLiter = 100.0;
            double nyDestillatMængde = destillat.getFaktiskMængdeLiter() - mængdeLiter;
            var tm = Controller.tapMængdeTilFærdigProdukt(mængdeLiter, destillat, færdigProdukt);
            assertAll(
                    () -> assertTrue(færdigProdukt.getTappetmængder().contains(tm)),
                    () -> assertEquals(nyDestillatMængde, destillat.getFaktiskMængdeLiter())

            );
        }
        // TC4 - ugyldig (mængdeLiter = 101)
        {
            // Faste værdier
            var destillat = Controller.createDestillat(LocalDateTime.now(), fad);
            var batch1 = Controller.createBatch("Heksebryg", 45, 0.6);
            var batch2 = Controller.createBatch("Captain Sall", 55, 0.6);
            destillat.createPåfyldtMængde(45.0, batch1);
            destillat.createPåfyldtMængde(55.0, batch2);
            var færdigProdukt = Controller.createFærdigProdukt(
                    "Simons sovs", ProduktVariant.WHISKY, 0);

            // Skiftende værdier
            double mængdeLiter = 30.0;
            Exception exception = assertThrows(IllegalStateException.class, () -> {
                Controller.tapMængdeTilFærdigProdukt(mængdeLiter, destillat, færdigProdukt);
            });
            assertEquals("mængdeLiter må ikke være større end Destillatets faktiskMængdeLiter", exception.getMessage(), "TC4 - ugyldig (mængdeLiter = 101) ");
        }
        // TC5 - ugyldig (mængdeLiter = -3.0)
        {
            // Faste værdier
            var destillat = Controller.createDestillat(LocalDateTime.now(), fad);
            var batch1 = Controller.createBatch("Heksebryg", 45, 0.6);
            var batch2 = Controller.createBatch("Captain Sall", 55, 0.6);
            destillat.createPåfyldtMængde(45.0, batch1);
            destillat.createPåfyldtMængde(55.0, batch2);
            var færdigProdukt = Controller.createFærdigProdukt(
                    "Simons sovs", ProduktVariant.WHISKY, 0);

            // Skiftende værdier
            double mængdeLiter = -3.0;
            Exception exception = assertThrows(IllegalArgumentException.class, () -> {
                Controller.tapMængdeTilFærdigProdukt(mængdeLiter, destillat, færdigProdukt);
            });
            assertEquals("mængdeLiter må ikke være ligmed eller mindre end 0", exception.getMessage(), "TC5 - ugyldig (mængdeLiter = -3.0)");
        }
    }
}