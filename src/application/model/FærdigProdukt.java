package application.model;

import java.io.Serializable;
import java.time.LocalDate;
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
    private LocalDate produktionsDato;

    public FærdigProdukt(String navn, ProduktVariant type, double vandMængde) {
        this.navn = navn;
        this.type = type;
        this.vandMængde = vandMængde;
        this.totalMængdeLiter = vandMængde;
        this.produktionsDato = LocalDate.now();
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

    @Override
    public String toString() {
        return "%s %.2f%s %.2f%s".formatted(navn + ",", totalMængdeLiter, " liter,", endeligAlkoholdProcent * 100, " %");
    }

    public String GUIview() {
        var sb = new StringBuilder("Navn: " + navn);
        sb.append("\nProdukttype: " + type.toString().toLowerCase());
        sb.append("\nLiter tappet: " + totalMængdeLiter);
        double alkProcent = endeligAlkoholdProcent *100;
        sb.append("\nAlkoholprocent: %.1f%s".formatted(alkProcent," %"));
        if (vandMængde > 0) {
            double vandprocent = vandMængde / totalMængdeLiter * 100;
            sb.append("\n%s %.2f %s (%.2f%s)".formatted("Andel vand:", vandMængde, " liter", vandprocent," %"));
        }
        else {
            sb.append("\nIntet vand tilsat");
        }
        sb.append("\nTappet fra " + tappetmængder.size() + " destillat");
        if (tappetmængder.size() > 1)   {
            sb.append("er");
        }

        return sb.toString();
    }
}
