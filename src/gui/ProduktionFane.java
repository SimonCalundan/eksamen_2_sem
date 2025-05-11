package gui;

import application.controller.Controller;
import application.model.*;
import javafx.beans.value.ChangeListener;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class ProduktionFane extends Main{

    private CreateBatchVindue batchVindue;

    private ListView<Batch> batchListView;
    private ListView<Fad> fadListView;
    private ListView<ProduktVariant> produktVariantListView;
    private ListView<Fad> fadMin3AarView;

    private TextField txfTappetMængdeTilFærdigProdukt = new TextField();
    private TextField txfPåfyldtMængdePåFad = new TextField();
    private TextField txfDateTimeForOprettelseAfDestillat = new TextField();
    private TextField txfNavn = new TextField();
    private TextField txfVandMængde = new TextField();

    public Tab getTab() {
        Tab tab = new Tab("Produktion");

        GridPane grid = new GridPane();
        grid.setHgap(15);
        grid.setVgap(15);
        grid.setPadding(new Insets(10));
        grid.setAlignment(Pos.CENTER);
        grid.setGridLinesVisible(false);

        //Venstrebox
        //------------------------------------------------------------------------------------------------------------
        VBox venstreBox = new VBox();
        venstreBox.setSpacing(0);
        venstreBox.setAlignment(Pos.CENTER);
        grid.add(venstreBox,0,0);

        Label lblBatches = new Label("Batches");
        lblBatches.setStyle("-fx-font-weight: bold");
        venstreBox.getChildren().add(lblBatches);
        // add a listView to the pane(at col=1, row=1)
        batchListView = new ListView();
        venstreBox.getChildren().add(batchListView);
        batchListView.setPrefWidth(200);
        batchListView.setPrefHeight(200);
        batchListView.getItems().setAll(Controller.getBatches());

        ChangeListener<Batch> listenerBatch = (ov, oldString, newString) -> this.selectionChangedBatch();
        batchListView.getSelectionModel().selectedItemProperty().addListener(listenerBatch);

//        lvwNames.getSelectionModel().clearSelection();


        //Midterbox
        //------------------------------------------------------------------------------------------------------------
        VBox midterBoxTop = new VBox();
        midterBoxTop.setSpacing(5);
        midterBoxTop.setAlignment(Pos.CENTER);
        grid.add(midterBoxTop,1,0);

        Label lblTappeMængde = new Label("Påfyldt mængde");
        midterBoxTop.getChildren().add(lblTappeMængde);
        lblTappeMængde.setStyle("-fx-font-weight: bold");

        midterBoxTop.getChildren().add(txfPåfyldtMængdePåFad);
        txfPåfyldtMængdePåFad.setPromptText("I liter");

        Label lbldato = new Label("Dato for påfyldning");
        lbldato.setStyle("-fx-font-weight: bold");
        midterBoxTop.getChildren().add(lbldato);

        midterBoxTop.getChildren().add(txfDateTimeForOprettelseAfDestillat);
        txfDateTimeForOprettelseAfDestillat.setPromptText("dato ex: 2007,17,06,10,30");

        Button btnOpretPåfyldning = new Button("Påfyld fad");
        midterBoxTop.getChildren().add(btnOpretPåfyldning);
        btnOpretPåfyldning.setOnAction(event ->this.påfyldMængdeAction());

        //Højrebox
        //------------------------------------------------------------------------------------------------

        VBox højreBox = new VBox();
        højreBox.setSpacing(0);
        højreBox.setAlignment(Pos.CENTER);
        grid.add(højreBox,2,0);

        Label lblFade = new Label("Fade");
        lblFade.setStyle("-fx-font-weight: bold");
        højreBox.getChildren().add(lblFade);

        fadListView = new ListView();
        højreBox.getChildren().add(fadListView);
        fadListView.setPrefWidth(200);
        fadListView.setPrefHeight(200);
        fadListView.getItems().setAll(Controller.getFade());

        ChangeListener<Fad> listenerFad = (ov, oldString, newString) -> this.selectionChangedFade();
        fadListView.getSelectionModel().selectedItemProperty().addListener(listenerFad);

//        lvwNames.getSelectionModel().clearSelection();


        //Venstre bundbox
        //-----------------------------------------------------------------------------------------------
        VBox venstreBundBox = new VBox();
        venstreBundBox.setSpacing(0);
        venstreBundBox.setAlignment(Pos.CENTER);
        grid.add(venstreBundBox,0,1);

        Label lblFadMin3Aar = new Label("Fade der har lagt på lager i min. 3 år");
        lblFadMin3Aar.setStyle("-fx-font-weight: bold");
        venstreBundBox.getChildren().add(lblFadMin3Aar);

        fadMin3AarView = new ListView<>();
        venstreBundBox.getChildren().add(fadMin3AarView);
        fadMin3AarView.setPrefWidth(50);
        fadMin3AarView.setPrefHeight(100);
        fadMin3AarView.getItems().setAll(getFadePaaLagerIMin3Aar());

        ChangeListener<Fad> listenerFadMin3Aar = (ov, oldString, newString) -> this.selectionChangedFadeMin3Aar();
        fadListView.getSelectionModel().selectedItemProperty().addListener(listenerFadMin3Aar);

        //Bundboxmidt
        //------------------------------------------------------------------------------------------------
        VBox midtBundBox = new VBox();
        midtBundBox.setAlignment(Pos.CENTER);
        midtBundBox.setSpacing(0);
        grid.add(midtBundBox,1,1);


        Label lblProduktkategoriVariant = new Label("Produkt variant");
        lblProduktkategoriVariant.setStyle("-fx-font-weight: bold");
        midtBundBox.getChildren().add(lblProduktkategoriVariant);

        produktVariantListView = new ListView<>();
        midtBundBox.getChildren().add(produktVariantListView);
        produktVariantListView.setPrefWidth(50);
        produktVariantListView.setPrefHeight(100);
        produktVariantListView.getItems().setAll(ProduktVariant.values());
        ChangeListener<ProduktVariant> listenerProduktVariant = (ov, oldString, newString) -> this.selectionChangedProduktionsVariant();
        produktVariantListView.getSelectionModel().selectedItemProperty().addListener(listenerProduktVariant);

        //HøjreBundBox
        //------------------------------------------------------------------------------------------------
        VBox højreBundBox = new VBox();
        højreBundBox.setAlignment(Pos.CENTER);
        højreBundBox.setSpacing(5);
        grid.add(højreBundBox,2,1);

        højreBundBox.getChildren().add(txfNavn);
        txfNavn.setPromptText("Produktnavn");
        højreBundBox.getChildren().add(txfVandMængde);
        txfVandMængde.setPromptText("Vandindhold i liter");
        højreBundBox.getChildren().add(txfTappetMængdeTilFærdigProdukt);
        txfTappetMængdeTilFærdigProdukt.setPromptText("Tappet mængde til færdig produkt");

        //Bund bund
        //----------------------------------------------------------------------------------------------------
//        VBox bundBundBox = new VBox();
//        bundBundBox.setAlignment(Pos.CENTER);
//        bundBundBox.setSpacing(0);
//        grid.add(bundBundBox,0,3);

        Button btnCreateBatch = new Button("Producer batch");
        grid.add(btnCreateBatch,0,3);
//        bundBundBox.getChildren().add(btnCreateBatch);
        btnCreateBatch.setOnAction(event -> createBatchAction());
        batchVindue = new CreateBatchVindue("Opret Batch", tab);

        Button btnCreateFærdigProdukt = new Button("Opret færdig produkt");
        grid.add(btnCreateFærdigProdukt,2,3);
//        bundBundBox.getChildren().add(btnCreateFærdigProdukt);
        btnCreateFærdigProdukt.setOnAction(event -> opretFærdigProduktAction());

        tab.setContent(grid);
        return tab;
    }

    //knapfunktion til batch
    private void createBatchAction(){
        batchVindue.showAndWait();
        updateListviewBatch();
    }

    //opdatere batch listviewet
    private void updateListviewBatch(){
        batchListView.getItems().setAll(Controller.getBatches());
    }

    //opdatere fadeview
    private void updateListviewFade(){
        fadListView.getItems().setAll(Controller.getFade());
    }

    //opdatere fadeMin3AarPåLagerView
    private void updateListviewFadeMin3AarPåLager(){
        fadMin3AarView.getItems().setAll(getFadePaaLagerIMin3Aar());
    }

    //hvilket item du har valgt i produktionsvariationView
    private void selectionChangedProduktionsVariant(){
        produktVariantListView.getSelectionModel().getSelectedItem();
    }

    //hvilket item du har valgt i fadeView
    private void selectionChangedFade(){
        fadListView.getSelectionModel().getSelectedItem();
    }

    //hvilket batch du har valgt i batchView
    private void selectionChangedBatch(){
        batchListView.getSelectionModel().getSelectedItem();
    }

    //hvilket item i fademin3årView du har valgt
    private void selectionChangedFadeMin3Aar(){
        fadMin3AarView.getSelectionModel().getSelectedItem();
    }

    //finder fade på laager i min 3 år
    //TODO skal det her være i GUI eller i Controller??
    private List<Fad> getFadePaaLagerIMin3Aar(){
        LocalDateTime treÅrSiden = LocalDateTime.now().minusYears(3);
        List<Fad> fadeMin3År = new ArrayList<>();
        for (Destillat destillat : Controller.getDestillater()) {
            destillat.getDatoForPåfyldning();
            if (destillat.getDatoForPåfyldning().isBefore(treÅrSiden)) {
                fadeMin3År.add(destillat.getFad());
            }
        }
        return fadeMin3År;
    }

    //action til påfyldtmængde knap
    //TODO mangler at laves
    private void påfyldMængdeAction(){
        try {
            double mændge = Double.parseDouble(txfPåfyldtMængdePåFad.getText().trim());
            Fad valgtFadMedDestillat = fadListView.getSelectionModel().getSelectedItem();
            Batch valgtBatch = batchListView.getSelectionModel().getSelectedItem();
            Controller.createPåfyldtMængde(valgtFadMedDestillat.getDestillat(), valgtBatch,mændge);
            LocalDateTime tidspunkt = LocalDateTime.parse(txfDateTimeForOprettelseAfDestillat.getText().trim());
            Controller.createDestillat(tidspunkt,valgtFadMedDestillat);
        } catch (Exception e) {
            showAlert("Fejl", "Fejl under påfyldning af fad", e.getMessage());
        }
    }

    //action til opretfærdig produkt
    //TODO mangler exceptions!
    //mangler at håndtere exceptions i GUI, da det ikke er lavet i controlleren.
    private void opretFærdigProduktAction(){
        try {
            String navn = txfNavn.getText().trim();
            double vandMængde = Double.parseDouble(txfVandMængde.getText().trim());
            double tappetLiter = Double.parseDouble(txfTappetMængdeTilFærdigProdukt.getText().trim());
            ProduktVariant valgteProduktVariation = produktVariantListView.getSelectionModel().getSelectedItem();
            FærdigProdukt returProdukt = Controller.createFærdigProdukt(navn,valgteProduktVariation, vandMængde);
            Fad valgteFadMin3Aar = fadMin3AarView.getSelectionModel().getSelectedItem();
            Controller.tapMængdeTilFærdigProdukt(tappetLiter, valgteFadMin3Aar.getDestillat(),returProdukt);
            cleartxfFields();
            updateListviewFade();
            updateListviewFade();
            showAlert("Produktet er oprettet", "Produktet er oprettet", "Du har oprettet produktet: " + returProdukt);
        } catch (Exception e) {
            showAlert("Fejl", "Fejl under oprettelse af færdig produkt", "!!WTF der er ikke håndteret exceptions i controlleren!!");
        }
    }

    //clear alle tekstfelter
    private void cleartxfFields(){
        txfNavn.clear();
        txfVandMængde.clear();
        produktVariantListView.getSelectionModel().clearSelection();
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