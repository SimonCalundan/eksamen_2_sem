package application.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Hylde implements Serializable {
    private String navn;
    private Reol reol;
    private List<Fad> fade;

    Hylde(String navn, Reol reol)   {
        this.navn = navn;
        this.reol = reol;

        fade = new ArrayList<>();
    }

    public void addFad(Fad fad)    {
        if (!fade.contains(fad))  {
            fade.add(fad);
        }
    }

    public void removeFad(Fad fad) {
        if (fade.contains(fad)) {
            fade.remove(fad);
            fad.setHylde(null);
        }
    }

    public List<Fad> getFade() {
        return Collections.unmodifiableList(fade);
    }

    public Reol getReol() {
        return reol;
    }

    @Override
    public String toString() {
        return navn;
    }
}
