package application.controller;

import application.model.Batch;
import application.model.Destillat;
import application.model.Fad;
import storage.ListStorage;
import storage.Storage;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class ControllerTest {
    private static Storage storage;

    @org.junit.jupiter.api.BeforeEach
    void setUp() {
        storage = new ListStorage();
        Controller.setStorage(storage);
    }

    @org.junit.jupiter.api.Test
    void getDestillater() {
        fail();
    }

    @org.junit.jupiter.api.Test
    void registrerPåfyldning() {
        fail();
    }

    @org.junit.jupiter.api.Test
    void removeDestillat() {
        fail();
    }

    @org.junit.jupiter.api.Test
    void getFade() {
        Controller.createFad(2.2, "Per", "Eg", true);
        assertFalse(Controller.getFade().isEmpty());
    }

    @org.junit.jupiter.api.Test
    void createFad() {
        double størrelseLiter = 2.2;
        String leverandør = "Per";
        String træsort = "Eg";
        boolean erGenbrugt = true;
        Controller.createFad(størrelseLiter, leverandør, træsort, erGenbrugt);

        Fad fad = Controller.getFade().getFirst();

        assertAll(
                () -> assertEquals(størrelseLiter, fad.getStørrelseLiter()),
                () -> assertEquals(leverandør, fad.getLeverandør()),
                () -> assertEquals(træsort, fad.getTræsort()),
                () -> assertEquals(erGenbrugt, fad.erGenbrugt())
        );
    }

    @org.junit.jupiter.api.Test
    void removeFad() {
        Fad fad = Controller.createFad(2.2, "Per", "Eg", true);
        assertFalse(Controller.getFade().isEmpty());
        Controller.removeFad(fad);
        assertTrue(Controller.getFade().isEmpty());
    }

    @org.junit.jupiter.api.Test
    void getBatches() {
        Controller.createBatch("Testnavn", 2.5, 0.8);
        assertFalse(Controller.getBatches().isEmpty());
    }

    @org.junit.jupiter.api.Test
    void createBatch() {
        String navn = "Testnavn";
        double mængdeLiter = 2.5;
        double alkoholProcent = 0.10;
        Controller.createBatch(navn, mængdeLiter, alkoholProcent);

        Batch batch = Controller.getBatches().getFirst();

        assertAll(
                () -> assertEquals(navn, batch.getNavn()),
                () -> assertEquals(mængdeLiter, batch.getMængdeLiter())
        );
    }

    @org.junit.jupiter.api.Test
    void removeBatch() {
        Batch batch = Controller.createBatch("Testnavn", 2.5, 0.42);
        assertFalse(Controller.getBatches().isEmpty());
        Controller.removeBatch(batch);
        assertTrue(Controller.getBatches().isEmpty());
    }
}