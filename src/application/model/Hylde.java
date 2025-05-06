package application.model;

import java.util.ArrayList;
import java.util.List;

public class Hylde {
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
}
