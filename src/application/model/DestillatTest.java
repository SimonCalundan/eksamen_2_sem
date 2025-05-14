package application.model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

class DestillatTest {
    private Fad fad;

    @BeforeEach
    void setup() {
        var lager = new Lager("Simons lager");
        var reol = new Reol("Simons reol", lager);
        var hylde = new Hylde("Simons hylde", reol);
        this.fad = new Fad(30.0, "Peter", "Eg",
                3, hylde);
    }

    @Test
    void constructorTest() {
        LocalDateTime datoForPåfyldning = LocalDateTime.now();
        Destillat destillat = new Destillat(datoForPåfyldning, fad);

        assertAll(
                () -> assertEquals(datoForPåfyldning, destillat.getDatoForPåfyldning()),
                () -> assertEquals(fad, destillat.getFad())
        );
    }

    @Test
    void tapMængdeLiter() {
        var batch = new Batch("Simons Batch", 30.0, 0.6);
        BatchMængde pm = new BatchMængde(30.0, batch);
        var d = new Destillat(LocalDateTime.now(), fad);
        double tappetMængde = 30.0;
        double expectedMængdeLiter = d.getFaktiskMængdeLiter() - tappetMængde;

        d.tapMængdeLiter(tappetMængde);
        double actualtMængdeLiter = d.getFaktiskMængdeLiter();
        assertEquals(expectedMængdeLiter, actualtMængdeLiter);
    }

    @Test
    void addPåfyldteMængder() {
        var batch = new Batch("Simons Batch", 30.0, 0.6);
        BatchMængde pm = new BatchMængde(30.0, batch);
        var d = new Destillat(LocalDateTime.now(), fad);

        assertFalse(d.getPåfyldteMængder().contains(pm));
        d.addPåfyldteMængder(pm);
        assertTrue(d.getPåfyldteMængder().contains(pm));
    }

    @Test
    void removePåfyldtMængde() {
        var batch = new Batch("Simons Batch", 30.0, 0.6);
        BatchMængde pm = new BatchMængde(30.0, batch);
        var d = new Destillat(LocalDateTime.now(), fad);

        d.addPåfyldteMængder(pm);
        assertTrue(d.getPåfyldteMængder().contains(pm));
        d.removePåfyldtMængde(pm);
        assertFalse(d.getPåfyldteMængder().contains(pm));
    }

    @Test
    void getDatoForPåfyldning() {
        var batch = new Batch("Simons Batch", 30.0, 0.6);
        BatchMængde pm = new BatchMængde(30.0, batch);
        LocalDateTime expectedTidspunkt = LocalDateTime
                .of(2025, 1, 1, 1, 1);
        var d = new Destillat(expectedTidspunkt, fad);
        LocalDateTime actualtTidspunkt = d.getDatoForPåfyldning();

        assertEquals(expectedTidspunkt, actualtTidspunkt);
    }

    @Test
    void getFaktiskMængdeLiter() {
        var batch = new Batch("Simons Batch", 30.0, 0.6);
        BatchMængde pm = new BatchMængde(30.0, batch);
        var d = new Destillat(LocalDateTime.now(), fad);
        d.addPåfyldteMængder(pm);

        double expectedMængdeLiter = 30.0;
        double actualMængdeLiter = d.getFaktiskMængdeLiter();
        assertEquals(expectedMængdeLiter, actualMængdeLiter);
    }

    @Test
    void getFaktiskAlkoholProcent() {
        var batch = new Batch("Simons Batch", 30.0, 0.6);
        BatchMængde pm = new BatchMængde(30.0, batch);
        var d = new Destillat(LocalDateTime.now(), fad);
        d.addPåfyldteMængder(pm);

        // FAP = faktiskAlkoholProcent
        double expectedFAP = 0.6;
        double actualFAP = d.getFaktiskAlkoholProcent();
        assertEquals(expectedFAP, actualFAP);
    }

    @Test
    void getPåfyldteMængder() {
        var batch = new Batch("Simons Batch", 30.0, 0.6);
        BatchMængde pm = new BatchMængde(30.0, batch);
        var d = new Destillat(LocalDateTime.now(), fad);
        d.addPåfyldteMængder(pm);

        assertTrue(d.getPåfyldteMængder().contains(pm));
    }

    @Test
    void getFad() {
        var batch = new Batch("Simons Batch", 30.0, 0.6);
        BatchMængde pm = new BatchMængde(30.0, batch);
        var d = new Destillat(LocalDateTime.now(), fad);

        assertEquals(fad, d.getFad());
    }
}