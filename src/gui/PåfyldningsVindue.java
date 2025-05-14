package gui;

import application.controller.Controller;
import application.model.Batch;
import application.model.BatchMængde;
import application.model.Destillat;
import application.model.DestillatMængde;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;

import java.util.ArrayList;
import java.util.List;

public class PåfyldningsVindue extends Stage {

    private Label lblBeskrivelse;
    private TextArea txaType;
    private TextField txfMængde;

    private DestillatMængde dm;
    private BatchMængde bm;

    public <T> PåfyldningsVindue(String title, T type) {
        this.setTitle(title);
        GridPane grid = new GridPane();
        grid.setHgap(5);
        grid.setVgap(5);
        grid.setPadding(new Insets(10));
        grid.setAlignment(Pos.CENTER);
        grid.setGridLinesVisible(false);

        lblBeskrivelse = new Label();
        txaType = new TextArea();
        txaType.setPrefWidth(10);
        txaType.setPrefHeight(10);
        txaType.setEditable(false);

        if (type instanceof Destillat destillat) {
            lblBeskrivelse = new Label("Hvor meget vil du tappe fra dette destillat?");
            txaType.setText(destillat.toString());
        } else if (type instanceof Batch batch){
            lblBeskrivelse = new Label("Hvor meget vil du tappe fra dette batch?");
            txaType.setText(batch.toString());
        }

        grid.add(lblBeskrivelse, 0, 0);
        grid.add(txaType,0,1);
        Label lblMængde = new Label("Mængde");
        grid.add(lblMængde, 0, 2);
        txfMængde = new TextField();
        txfMængde.setPromptText("i liter");
        grid.add(txfMængde, 0, 3);

        Button btnCreateBatch = new Button("Godkend");
        grid.add(btnCreateBatch,0,4);
        btnCreateBatch.setOnAction(event -> create(type));

        Button btnLuk = new Button("Luk");
        grid.add(btnLuk,1,4);
        btnLuk.setOnAction(event -> closeWindow());

        Scene scene = new Scene(grid, 300, 200);
        this.setScene(scene);
    }

    private void closeWindow() {
        txaType.clear();
        txfMængde.clear();
        hide();
    }

    private  <T> void create(T type){
        try {
            double mængde = Double.parseDouble(txfMængde.getText().trim());
            if (type instanceof Destillat) {
                dm = Controller.createDestillatMængde((Destillat) type, mængde);
            } else if (type instanceof Batch) {
                bm = Controller.createBatchMængde((Batch) type, mængde);
            }
            closeWindow();
        }catch (IllegalArgumentException e) {
            showAlert("Fejl i mængde", e.getMessage());
        }
    }

    public BatchMængde getBm() {
        return bm;
    }

    public DestillatMængde getDm() {
        return dm;
    }

    //allert
    private void showAlert(String headerText, String contentText) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Fejl");
        alert.setHeaderText(headerText);
        alert.setContentText(contentText);
        alert.showAndWait();
    }
}
