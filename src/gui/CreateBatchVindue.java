package gui;

import application.controller.Controller;
import application.model.Batch;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;

public class CreateBatchVindue extends Stage {

    private TextField txfNavn = new TextField();
    private TextField txfMængdeILiter = new TextField();
    private TextField txfAlkoholProcent = new TextField();
    private TextField txfMark = new TextField();

    public CreateBatchVindue(String title, Tab tab) {
        this.setTitle(title);
        GridPane grid = new GridPane();
        grid.setHgap(5);
        grid.setVgap(5);
        grid.setPadding(new Insets(10));
        grid.setAlignment(Pos.CENTER);
        grid.setGridLinesVisible(false);

        Label lblNavn = new Label("Navn:");
        grid.add(lblNavn,0,0);

        grid.add(txfNavn, 1,0);

        Label lblMængdeILiter = new Label("Mængde i liter:");
        grid.add(lblMængdeILiter,0,1);

        grid.add(txfMængdeILiter,1,1);

        Label lblAlkoholProcent = new Label("Alkoholprocent:");
        grid.add(lblAlkoholProcent,0,2);

        grid.add(txfAlkoholProcent,1,2);

        Label lblMark = new Label("Mark");
        grid.add(lblMark, 0, 3);
        grid.add(txfMark, 1, 3);

        Button btnCreateBatch = new Button("Godkend");
        grid.add(btnCreateBatch,0,4);
        btnCreateBatch.setOnAction(event -> this.createBatch());

        Button btnLuk = new Button("Luk");
        grid.add(btnLuk,1,4);
        btnLuk.setOnAction(event -> this.closeWindow());

        Scene scene = new Scene(grid, 300, 200);
        this.setScene(scene);
    }



    private void createBatch() {
        try {
            String navn = txfNavn.getText().trim();
            String mark  = txfMark.getText().trim();
            double mængdeILiter;
            double alkoholProcent;
            mængdeILiter = Double.parseDouble(txfMængdeILiter.getText().trim());
            alkoholProcent = Double.parseDouble(txfAlkoholProcent.getText().trim());
            Batch batch = Controller.createBatch(navn,mængdeILiter,alkoholProcent, mark);
            showAlert("Batch oprettet", "Du har oprettet et batch", batch.GUIview());
            closeWindow();
            hide();
        } catch (NumberFormatException e) {
            showAlert("Fejl","Fejl under opret batch", "Forkert tal input");
        } catch
         (Exception e) {
            showAlert("Fejl","Fejl under opret batch", e.getMessage());
        }
    }

    private void showAlert(String top, String headerText, String contentText) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(top);
        alert.setHeaderText(headerText);
        alert.setContentText(contentText);
        alert.showAndWait();
    }

    private void closeWindow(){
        txfNavn.clear();
        txfMængdeILiter.clear();
        txfAlkoholProcent.clear();
        hide();
    }
}