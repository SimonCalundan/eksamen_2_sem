package gui;

import application.controller.Controller;
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

    public CreateBatchVindue(String title, Tab tab) {
        this.setTitle(title);
        GridPane grid = new GridPane();
        grid.setHgap(25);
        grid.setVgap(25);
        grid.setPadding(new Insets(20));
        grid.setAlignment(Pos.CENTER);
        grid.setGridLinesVisible(true);

        Label lblNavn = new Label("Navn:");
        grid.add(lblNavn,0,0);

        grid.add(txfNavn, 1,0);

        Label lblMængdeILiter = new Label("Mængde i liter:");
        grid.add(lblMængdeILiter,0,1);

        grid.add(txfMængdeILiter,1,1);

        Label lblAlkoholProcent = new Label("Alkoholprocent:");
        grid.add(lblAlkoholProcent,0,2);

        grid.add(txfAlkoholProcent,1,2);

        Button btnCreateBatch = new Button("Godkend");
        grid.add(btnCreateBatch,0,3);
        btnCreateBatch.setOnAction(event -> this.createBatch());

        Scene scene = new Scene(grid, 400, 300);
        this.setScene(scene);
    }



    private void createBatch() {
        String title = "";
        String headerText = "Du skal udfylde tekstfeltet";
        String contentText = "";
        try {
            String navn = txfNavn.getText().trim();
            double mængdeILiter;
            double alkoholProcent;

            if (txfNavn.getText().trim().isEmpty()) {
                title = "Fejl i tekstfeltet navn";
                contentText = "Giv batchet et navn";
                throw new IllegalArgumentException();
            } else if (txfMængdeILiter.getText().trim().isEmpty()) {
                title = "Fejl i tekstfeltet mængde i liter";
                contentText = "Giv batchet en mængde i liter";
                throw new IllegalArgumentException();
            } else if (txfAlkoholProcent.getText().trim().isEmpty()) {
                title = "Fejl i tekstfeltet alkoholprocent";
                contentText = "Giv batchet en alkoholprocent";
                throw new IllegalArgumentException();
            }
            mængdeILiter = Double.parseDouble(txfMængdeILiter.getText().trim());
            alkoholProcent = Double.parseDouble(txfAlkoholProcent.getText().trim());
            Controller.createBatch(navn,mængdeILiter,alkoholProcent);
            hide();
        } catch (NumberFormatException e) {
            showAlert("Forkert tal input", "Du skal skrive et tal i alkoholprocent og mængde", "Det kan ikke være under 0");
        } catch (IllegalArgumentException e) {
            showAlert(title, headerText,contentText);
        }
    }

    private void showAlert(String title, String headerText, String contentText) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
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