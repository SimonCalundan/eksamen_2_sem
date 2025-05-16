package gui;

import application.controller.Controller;
import application.model.*;
import application.model.Lager;
import javafx.geometry.Insets;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.Tab;
import javafx.scene.control.TextArea;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

import java.util.ArrayList;
import java.util.List;

public class LagerFane extends MainFane {
    private ListView<Lager> lwLagre;
    private ListView<Reol> lwReoler;
    private ListView<Hylde> lwHylder;
    private ListView<Fad> lwFade;
    private TextArea taFadDetails;

    private Lager selectedLager;
    private Reol selectdReol;
    private Hylde seletedHylde;
    private Fad selectedFad;


    public Tab getTab() {
        Tab tab = new Tab("Lageroversigt");

        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(10));
        grid.setGridLinesVisible(false);

        // lagre
        Label lblLager = new Label("Lagre");
        grid.add(lblLager, 0, 0);

        lwLagre = new ListView<>();
        lwLagre.setPrefWidth(150);
        grid.add(lwLagre, 0, 1, 2, 5);
        List<Lager> lagerList = Controller.getLagre();
        selectedLager = !lagerList.isEmpty() ? lagerList.getFirst() : null;
        lwLagre.getItems().setAll(lagerList);

        lwLagre.getSelectionModel().selectedItemProperty()
                .addListener((obs, old, newVal) -> {
                    selectedLager = newVal;

                    // Opdater reoler baseret på valgt lager
                    List<Reol> reoler = selectedLager != null ? selectedLager.getReoler() : new ArrayList<>();
                    lwReoler.getItems().setAll(reoler);
                    selectdReol = !reoler.isEmpty() ? reoler.getFirst() : null;

                    // Opdater hylder baseret på ny valgt reol
                    List<Hylde> hylder = selectdReol != null ? selectdReol.getHylder() : new ArrayList<>();
                    lwHylder.getItems().setAll(hylder);
                    seletedHylde = !hylder.isEmpty() ? hylder.getFirst() : null;

                    // Opdater fade baseret på ny valgt hylde
                    List<Fad> fade = seletedHylde != null ? seletedHylde.getFade() : new ArrayList<>();
                    lwFade.getItems().setAll(fade);
                    selectedFad = !fade.isEmpty() ? fade.getFirst() : null;
                });

        // reoler
        Label lblReol = new Label("Reoler");
        grid.add(lblReol, 3, 0);
        lwReoler = new ListView<>();
        lwReoler.setPrefWidth(60);
        grid.add(lwReoler, 3, 1, 2, 5);
        List<Reol> reolList = selectedLager != null ? selectedLager.getReoler() : new ArrayList<>();
        selectdReol = !reolList.isEmpty() ? reolList.getFirst() : null;
        lwReoler.getItems().setAll(reolList);

        lwReoler.getSelectionModel().selectedItemProperty()
                .addListener((obs, old, newVal) -> {
                    selectdReol = newVal;

                    List<Hylde> hylder = selectdReol != null ? selectdReol.getHylder() : new ArrayList<>();
                    lwHylder.getItems().setAll(hylder);
                    seletedHylde = !hylder.isEmpty() ? hylder.getFirst() : null;

                    List<Fad> fade = seletedHylde != null ? seletedHylde.getFade() : new ArrayList<>();
                    lwFade.getItems().setAll(fade);
                    selectedFad = !fade.isEmpty() ? fade.getFirst() : null;
                });


        // hylder
        Label lblHylde = new Label("Hylder");
        grid.add(lblHylde, 6, 0);

        lwHylder = new ListView<>();
        lwHylder.setPrefWidth(60);
        grid.add(lwHylder, 6, 1, 2, 5);
        List<Hylde> hyldeList = selectdReol != null ? selectdReol.getHylder() : new ArrayList<>();
        seletedHylde = !hyldeList.isEmpty() ? hyldeList.getFirst() : null;
        lwHylder.getItems().setAll(hyldeList);

        lwHylder.getSelectionModel().selectedItemProperty()
                .addListener((obs, old, newVal) -> {
                    seletedHylde = newVal;

                    // Opdater fade
                    List<Fad> fade = seletedHylde != null ? seletedHylde.getFade() : new ArrayList<>();
                    lwFade.getItems().setAll(fade);
                    selectedFad = !fade.isEmpty() ? fade.getFirst() : null;
                });


        // fade
        Label lblFad = new Label("Fade");
        grid.add(lblFad, 9, 0);

        lwFade = new ListView<>();
        lwFade.minWidth(300);
        grid.add(lwFade, 9, 1, 2, 5);
        List<Fad> fadList = seletedHylde != null ? seletedHylde.getFade() : new ArrayList<>();
        selectedFad = !fadList.isEmpty() ? fadList.getFirst() : null;
        lwFade.getItems().setAll(fadList);

        lwFade.setCellFactory(listView -> new javafx.scene.control.ListCell<>() {
            @Override
            protected void updateItem(Fad fad, boolean empty) {
                super.updateItem(fad, empty);
                if (empty || fad == null) {
                    setText(null);
                    setStyle("");
                } else {
                    setText(fad.GUIwiev());
                    String color = getFadColorFromCapacity(fad);
                    setStyle("-fx-control-inner-background: " + color + ";");
                }
            }
        });

        Label lblFadDetails = new Label("Detaljer om valgt fad:");
        taFadDetails = new TextArea();
        taFadDetails.setEditable(false);
        taFadDetails.setWrapText(true);
        taFadDetails.setPrefWidth(250);
        taFadDetails.setPrefHeight(200);

        grid.add(lblFadDetails, 11, 0);
        grid.add(taFadDetails, 11, 1, 1, 4);



        lwFade.getSelectionModel().selectedItemProperty()
                .addListener((obs, old, newVal) -> {
                    selectedFad = newVal;
                    taFadDetails.setText(newVal != null ? newVal.fadOversigt() : "");
                });

        Label colorSectionTitle = new Label("Fad status betydning");
        colorSectionTitle.setStyle("-fx-font-size: 16px; -fx-font-weight: bold;");
        Label redBox = createColorLabel("red");
        Label yellowBox = createColorLabel("yellow");
        Label greenBox = createColorLabel("green");

        HBox sectionTitle = new HBox(5, colorSectionTitle);
        HBox redDescription = new HBox(5, redBox, new Label("Fyldt"));
        HBox yellowDescription = new HBox(5, yellowBox, new Label("Delvist fyldt"));
        HBox greenDescription = new HBox(5, greenBox, new Label("Tomt"));

        VBox legendBox = new VBox(5, colorSectionTitle, redDescription, yellowDescription, greenDescription);
        legendBox.setPadding(new Insets(10));
        legendBox.setStyle("-fx-border-color: #ccc; -fx-border-radius: 4; -fx-padding: 10;");
        grid.add(legendBox, 0, 6, 12, 1);


        tab.setContent(grid);
        return tab;
    }

    /**
     * Metode som bruges til at inddele fadene i fadlisten efter farver,
     * så brugeren har bedre overblik over fad status
     *
     * @param fad
     * @return hexcode for den farve som Fadet i listen skal have
     */
    private static String getFadColorFromCapacity(Fad fad) {
        if (fad.getDestillat() == null) {
            return "#AAFFAA";
        }

        double fullCapacity = fad.getStørrelseLiter();
        double destillatAmount = fad.getDestillat().getFaktiskMængdeLiter();
        double remainingCapacity = fullCapacity - destillatAmount;

        if (remainingCapacity == 0) {
            return "#FFAAAA";
        } else if (remainingCapacity > 0 && remainingCapacity < fullCapacity) {
            return "#FFFFAA";
        } else {
            return "#AAFFAA";
        }
    }

    /**
     * Metode som opretter farvede Labels
     * @param color den ønskede farve for label
     * @return et farvet label til at illustrere hvad fad status betyder for brugeren
     */
    private Label createColorLabel(String color){
        Label colorLabel = new Label();
        colorLabel.setMinSize(15, 15);
        colorLabel.setStyle("-fx-background-color: " + color + "; -fx-border-color: black;");
        return colorLabel;
    }
}
