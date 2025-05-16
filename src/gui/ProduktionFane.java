package gui;

import application.controller.Controller;
import application.model.*;
import javafx.beans.value.ChangeListener;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class ProduktionFane extends MainFane {

    private CreateBatchVindue batchVindue;
    private PåfyldningsVindue påfyldningsVindue;

    private ListView<Batch> batchListView;
    private ListView<Fad> fadListView;
    private ListView<ProduktVariant> produktVariantListView;
    private ListView<Destillat> destillatMin3årListView;
    private ListView<BatchMængde> påfyldtMængdeBatchListview;
    private ListView<DestillatMængde> påfyldtMængdeDestillatListview;

    private TextField txfDateTimeForOprettelseAfDestillat = new TextField();
    private TextField txfNavn = new TextField();
    private TextField txfVandMængde = new TextField();

    private Button btnTilføjBatch, btnTilføjDestillat;

    public Tab getTab() {
        Tab tab = new Tab("Produktion");

        GridPane grid = new GridPane();
        grid.setHgap(15);
        grid.setVgap(15);
        grid.setPadding(new Insets(10));
        grid.setAlignment(Pos.CENTER);
        grid.setGridLinesVisible(false);

        VBox lblProduktionsBox = new VBox();
        lblProduktionsBox.setAlignment(Pos.CENTER);
        lblProduktionsBox.setSpacing(0);
        lblProduktionsBox.setPadding(new Insets(10));
        grid.add(lblProduktionsBox, 0, 0);
        Label lblProduktRubrik = new Label("Produktion");
        lblProduktionsBox.getChildren().add(lblProduktRubrik);

        HBox samletBoxTop = new HBox();
        samletBoxTop.setAlignment(Pos.CENTER);
        samletBoxTop.setSpacing(10);
        samletBoxTop.setPadding(new Insets(10));
        samletBoxTop.setStyle("-fx-border-color: gray; -fx-background-color: #f0f0f0;");
        grid.add(samletBoxTop, 0, 1);

        VBox lblFærdigProduktBox = new VBox();
        lblFærdigProduktBox.setAlignment(Pos.CENTER);
        lblFærdigProduktBox.setSpacing(0);
        lblFærdigProduktBox.setPadding(new Insets(10));
        grid.add(lblFærdigProduktBox, 0, 2);
        Label lblFærdigprodukt = new Label("Færdig produkt");
        lblFærdigProduktBox.getChildren().add(lblFærdigprodukt);

        HBox samletBoxMidt = new HBox();
        samletBoxMidt.setAlignment(Pos.CENTER);
        samletBoxMidt.setSpacing(10);
        samletBoxMidt.setPadding(new Insets(10));
        samletBoxMidt.setStyle("-fx-border-color: gray; -fx-background-color: #f0f0f0;");
        grid.add(samletBoxMidt, 0, 3);

        //Venstrebox
        //------------------------------------------------------------------------------------------------------------
        VBox venstreBox = new VBox();
        venstreBox.setSpacing(5);
        venstreBox.setAlignment(Pos.CENTER);
        samletBoxTop.getChildren().add(venstreBox);

        Label lblBatches = new Label("Batches");
        venstreBox.getChildren().add(lblBatches);
        batchListView = new ListView();
        venstreBox.getChildren().add(batchListView);
        batchListView.setPrefWidth(200);
        batchListView.setPrefHeight(200);
        batchListView.getItems().setAll(batchesMedIndhold());

        ChangeListener<Batch> listenerBatch = (ov, oldString, newString) -> this.selectionChangedBatch();
        batchListView.getSelectionModel().selectedItemProperty().addListener(listenerBatch);

        Label lblNone = new Label();
        venstreBox.getChildren().add(lblNone);

        btnTilføjBatch = new Button("Tilføj batch til påfyldning");
        btnTilføjBatch.setDisable(true);
        btnTilføjBatch.setOnAction(event -> {
            Batch batch = batchListView.getSelectionModel().getSelectedItem();
            createPåfyldningVindue(batch);
        });
        venstreBox.getChildren().add(btnTilføjBatch);

        //Midterbox
        //------------------------------------------------------------------------------------------------------------
        VBox midterBoxTop = new VBox();
        midterBoxTop.setSpacing(0);
        midterBoxTop.setAlignment(Pos.CENTER);
        samletBoxTop.getChildren().add(midterBoxTop);

        Label lblTappeMængde = new Label("Påfyldt mængde");
        midterBoxTop.getChildren().add(lblTappeMængde);

        påfyldtMængdeBatchListview = new ListView<>();
        påfyldtMængdeBatchListview.setPrefWidth(200);
        påfyldtMængdeBatchListview.setPrefHeight(150);
        midterBoxTop.getChildren().add(påfyldtMængdeBatchListview);

        Label lbldato = new Label("Dato for påfyldning");
        midterBoxTop.getChildren().add(lbldato);

        //TODO jeg kan ikke få det her lort til at blive parset korrekt!
        midterBoxTop.getChildren().add(txfDateTimeForOprettelseAfDestillat);
        txfDateTimeForOprettelseAfDestillat.setPromptText("ex: 2020-12-03T10:15:30");

        Label lblDatoFormat = new Label("yyyy-mm-ddThh:mm:ss");
        lblDatoFormat.setStyle("-fx-font-style: italic;");
        midterBoxTop.getChildren().add(lblDatoFormat);


        //Højrebox
        //------------------------------------------------------------------------------------------------------------

        VBox højreBox = new VBox();
        højreBox.setSpacing(5);
        højreBox.setAlignment(Pos.CENTER);
        samletBoxTop.getChildren().add(højreBox);

        Label lblFade = new Label("Fade");
        højreBox.getChildren().add(lblFade);

        fadListView = new ListView();
        højreBox.getChildren().add(fadListView);
        fadListView.setPrefWidth(400);
        fadListView.setPrefHeight(200);
        fadListView.getItems().setAll(Controller.getFade());

        ChangeListener<Fad> listenerFad = (ov, oldString, newString) -> this.selectionChangedFade();
        fadListView.getSelectionModel().selectedItemProperty().addListener(listenerFad);

        Button btnNulstilProduktion = new Button("Nulstil");
        højreBox.getChildren().add(btnNulstilProduktion);
        btnNulstilProduktion.setOnAction(event -> nulstilProduktionAction());

        Button btnOpretPåfyldning = new Button("Påfyld fad");
        højreBox.getChildren().add(btnOpretPåfyldning);
        btnOpretPåfyldning.setOnAction(event -> this.påfyldMængdeAction());

        //MidtSamletBox starter --->

        //Venstre bundbox
        //------------------------------------------------------------------------------------------------------------
        VBox venstreBundBox = new VBox();
        venstreBundBox.setSpacing(5);
        venstreBundBox.setAlignment(Pos.CENTER);
        samletBoxMidt.getChildren().add(venstreBundBox);

        Label lblFadMin3Aar = new Label("Fade der har lagt på lager i min. 3 år");
        venstreBundBox.getChildren().add(lblFadMin3Aar);

        destillatMin3årListView = new ListView<>();
        venstreBundBox.getChildren().add(destillatMin3årListView);
        destillatMin3årListView.setPrefWidth(300);
        destillatMin3årListView.setPrefHeight(100);
        destillatMin3årListView.getItems().setAll(getDestillaterPåLagerMin3År());

        ChangeListener<Destillat> listenerDestillat = (ov, oldString, newString) -> this.selectionChangedDestillatMin3år();
        destillatMin3årListView.getSelectionModel().selectedItemProperty().addListener(listenerDestillat);

        btnTilføjDestillat = new Button("Tilføj destillat til påfyldning");
        btnTilføjDestillat.setDisable(true);
        btnTilføjDestillat.setOnAction(event -> {
                Destillat destillat = destillatMin3årListView.getSelectionModel().getSelectedItem();
                createPåfyldningVindue(destillat);
                });
        venstreBundBox.getChildren().add(btnTilføjDestillat);

        Label lblProduktNavn = new Label("Produktnavn");
        venstreBundBox.getChildren().add(lblProduktNavn);

        txfNavn.setPromptText("ex: Muld 1.1");
        venstreBundBox.getChildren().add(txfNavn);

        //Bundboxmidt
        //------------------------------------------------------------------------------------------------------------
        VBox midtBundBox = new VBox();
        midtBundBox.setAlignment(Pos.CENTER);
        midtBundBox.setSpacing(5);
        samletBoxMidt.getChildren().add(midtBundBox);

        Label lblProduktkategoriVariant = new Label("Produkt variant");
        midtBundBox.getChildren().add(lblProduktkategoriVariant);

        produktVariantListView = new ListView<>();
        midtBundBox.getChildren().add(produktVariantListView);
        produktVariantListView.setPrefWidth(50);
        produktVariantListView.setPrefHeight(100);
        produktVariantListView.getItems().setAll(ProduktVariant.values());

        ChangeListener<ProduktVariant> listenerProduktVariant = (ov, oldString, newString) -> this.selectionChangedProduktionsVariant();
        produktVariantListView.getSelectionModel().selectedItemProperty().addListener(listenerProduktVariant);

        Label lblNone1 = new Label();
        midtBundBox.getChildren().add(lblNone1);

        Label lblVandindhold = new Label("Vandindhold");
        midtBundBox.getChildren().add(lblVandindhold);

        txfVandMængde.setPromptText("I liter");
        midtBundBox.getChildren().add(txfVandMængde);

        //HøjreBundBox
        //------------------------------------------------------------------------------------------------------------
        VBox højreBundBox = new VBox();
        højreBundBox.setAlignment(Pos.CENTER);
        højreBundBox.setSpacing(5);
        samletBoxMidt.getChildren().add(højreBundBox);

        Label lblPåfyldteMængderDestillat = new Label("Påfyldte mængder fra fade til færdig produkt");
        højreBundBox.getChildren().add(lblPåfyldteMængderDestillat);

        påfyldtMængdeDestillatListview = new ListView<>();
        højreBundBox.getChildren().add(påfyldtMængdeDestillatListview);
        påfyldtMængdeDestillatListview.setPrefWidth(300);
        påfyldtMængdeDestillatListview.setPrefHeight(100);

        Button btnNulstilFærdigProdukt = new Button("Nulstil");
        højreBundBox.getChildren().add(btnNulstilFærdigProdukt);
        btnNulstilFærdigProdukt.setOnAction(event -> nulstilFærdigProduktAction());

        Label lblNone2 = new Label();
        højreBundBox.getChildren().add(lblNone2);

        Button btnCreateFærdigProdukt = new Button("Opret færdig produkt");
        højreBundBox.getChildren().add(btnCreateFærdigProdukt);
        btnCreateFærdigProdukt.setOnAction(event -> opretFærdigProduktAction());

        //under boxe starter -->

        //under bundbox
        //------------------------------------------------------------------------------------------------------------
        VBox underBundboxVenstre = new VBox();
        underBundboxVenstre.setAlignment(Pos.CENTER);
        underBundboxVenstre.setSpacing(0);
        samletBoxMidt.getChildren().add(underBundboxVenstre);

        VBox underBundboxMidt = new VBox();
        underBundboxMidt.setAlignment(Pos.CENTER);
        underBundboxMidt.setSpacing(0);
        samletBoxMidt.getChildren().add(underBundboxMidt);

        VBox underBundboxHøjre = new VBox();
        underBundboxHøjre.setAlignment(Pos.CENTER);
        underBundboxHøjre.setSpacing(0);
        samletBoxMidt.getChildren().add(underBundboxHøjre);

        //Nederst
        //------------------------------------------------------------------------------------------------------------
        Button btnCreateBatch = new Button("Producer batch");
        grid.add(btnCreateBatch, 0, 4);
        btnCreateBatch.setOnAction(event -> createBatchAction());
        batchVindue = new CreateBatchVindue("Opret Batch", tab);

        tab.setContent(grid);
        return tab;
    }

    //knapfunktion til batch
    private void createBatchAction() {
        batchVindue.showAndWait();
        batchListView.getItems().setAll(Controller.getBatches());
    }

    //knap til at nulstille produktion
    private void nulstilProduktionAction() {
        txfDateTimeForOprettelseAfDestillat.clear();
        batchListView.getSelectionModel().clearSelection();
        fadListView.getSelectionModel().clearSelection();
        påfyldtMængdeBatchListview.getItems().clear();
        btnTilføjBatch.setDisable(true);
    }

    //knap til at nulstille færdigprodukt
    private void nulstilFærdigProduktAction() {
        txfVandMængde.clear();
        produktVariantListView.getSelectionModel().clearSelection();
        txfNavn.clear();
        destillatMin3årListView.getSelectionModel().clearSelection();
        påfyldtMængdeDestillatListview.getItems().clear();
        btnTilføjDestillat.setDisable(true);
    }

    //finder batch med indhold
    private List<Batch> batchesMedIndhold() {
        List<Batch> batchesMedIndhold = new ArrayList<>();
        for (Batch batch : Controller.getBatches()) {
            if (batch.getMængdeLiter() > 0) {
                batchesMedIndhold.add(batch);
            }
        }
        return batchesMedIndhold;
    }

    //hvilket item du har valgt i produktionsvariationView
    private void selectionChangedProduktionsVariant() {
        produktVariantListView.getSelectionModel().getSelectedItem();
    }

    //hvilket item du har valgt i fadeView
    private void selectionChangedFade() {
        fadListView.getSelectionModel().getSelectedItem();
    }

    //hvilket batch du har valgt i batchView
    private void selectionChangedBatch() {
        batchListView.getSelectionModel().getSelectedItem();
        btnTilføjBatch.setDisable(false);
    }

    //hvilket item i fademin3årView du har valgt
    private void selectionChangedDestillatMin3år() {
        destillatMin3årListView.getSelectionModel().getSelectedItem();
        btnTilføjDestillat.setDisable(false);
    }

    //finder destillat på lager i min 3 år
    private List<Destillat> getDestillaterPåLagerMin3År() {
        LocalDateTime treÅrSiden = LocalDateTime.now().minusYears(3);
        List<Destillat> destillatMin3År = new ArrayList<>();
        System.out.println(Controller.getDestillater());
        for (Destillat destillat : Controller.getDestillater()) {
            if (destillat.getDatoForPåfyldning().isBefore(treÅrSiden)) {
                destillatMin3År.add(destillat);
            }
        }
        return destillatMin3År;
    }

    //action til tilføj mængde
    private <T> void createPåfyldningVindue(T type) {
        try {
            if (type instanceof Destillat) {
                påfyldningsVindue = new PåfyldningsVindue("Destillat påfyldning", type);
                påfyldningsVindue.showAndWait();
                if (påfyldningsVindue.getDm() != null) {
                    påfyldtMængdeDestillatListview.getItems().add(påfyldningsVindue.getDm());
                }
            } else if (type instanceof Batch) {
                påfyldningsVindue = new PåfyldningsVindue("Batch påfyldning", type);
                påfyldningsVindue.showAndWait();
                if (påfyldningsVindue.getBm() != null) {
                    påfyldtMængdeBatchListview.getItems().add(påfyldningsVindue.getBm());
                }
            }
        } catch (IllegalArgumentException e) {
            showAlert("Fejl", "Fejl under oprettelse", e.getMessage());
        }
    }

    //action til påfyld fad knap
    private void påfyldMængdeAction() {
        try {
            LocalDateTime dato = LocalDateTime.parse(txfDateTimeForOprettelseAfDestillat.getText().trim());
            Destillat påfyldning = Controller.createDestillat(dato, fadListView.getSelectionModel().getSelectedItem(), påfyldtMængdeBatchListview.getItems());
            nulstilProduktionAction();
            showAlert("Påfyldning udført", "Du har nu påført et destillat til et fad", påfyldning.GUIview());
        } catch (Exception e) {
            showAlert("Fejl", "Fejl under påfyldning af fad", e.getMessage());
        }
    }

    //action til opretfærdig produkt
    private void opretFærdigProduktAction() {
        try {
            double vandmængde = Double.parseDouble(txfVandMængde.getText().trim());
            FærdigProdukt færdigprodukt = Controller.createFærdigProdukt(txfNavn.getText().trim(), produktVariantListView.getSelectionModel().getSelectedItem(), vandmængde, påfyldtMængdeDestillatListview.getItems());
            nulstilFærdigProduktAction();
            showAlert("Whisky er produceret", "Du har produceret whisky ",færdigprodukt.GUIview());
        } catch (Exception e) {
            showAlert("Fejl", "Fejl under oprettelse af færdig produkt", e.getMessage());
        }
    }

    //viser alert
    private void showAlert(String window, String headerText, String contentText) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(window);
        alert.setHeaderText(headerText);
        alert.setContentText(contentText);
        alert.showAndWait();
    }
}