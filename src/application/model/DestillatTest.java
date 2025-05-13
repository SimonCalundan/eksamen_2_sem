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
    }

    @Test
    void addPåfyldteMængder() {
    }

    @Test
    void removePåfyldtMængde() {
    }

}