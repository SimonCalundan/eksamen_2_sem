package application.model;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class FærdigProdukt implements Serializable {
    private String navn;
    private double vandMængde;
    private ProduktVariant type;
    private List<DestillatMængde> tappetmængder;
    private double totalMængdeLiter;
    private double endeligAlkoholdProcent;

    public FærdigProdukt(String navn, ProduktVariant type, double vandMængde) {
        this.navn = navn;
        this.type = type;
        this.vandMængde = vandMængde;
        this.totalMængdeLiter = vandMængde;

        this.tappetmængder = new ArrayList<>();
    }

    public void addTappetMængde(DestillatMængde tappetMængde) {
        if (!tappetmængder.contains(tappetMængde)) {
            tappetmængder.add(tappetMængde);
            double totalAlkoholMængde = totalMængdeLiter * endeligAlkoholdProcent + tappetMængde.getMængdeLiter() * tappetMængde.getDestillat().getFaktiskAlkoholProcent();
            totalMængdeLiter += tappetMængde.getMængdeLiter();
            endeligAlkoholdProcent += totalAlkoholMængde / totalMængdeLiter;
        }
    }

    public String getNavn() {
        return navn;
    }

    public double getVandMængde() {
        return vandMængde;
    }

    public ProduktVariant getType() {
        return type;
    }

    public List<DestillatMængde> getTappetmængder() {
        return new ArrayList<>(tappetmængder);
    }


}
