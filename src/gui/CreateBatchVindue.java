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

        Button btnCreateBatch = new Button("Godkend");
        grid.add(btnCreateBatch,0,3);
        btnCreateBatch.setOnAction(event -> this.createBatch());

        Button btnLuk = new Button("Luk");
        grid.add(btnLuk,1,3);
        btnLuk.setOnAction(event -> this.closeWindow());

        Scene scene = new Scene(grid, 300, 200);
        this.setScene(scene);
    }



    private void createBatch() {
        try {
            String navn = txfNavn.getText().trim();
            double mængdeILiter;
            double alkoholProcent;
            mængdeILiter = Double.parseDouble(txfMængdeILiter.getText().trim());
            alkoholProcent = Double.parseDouble(txfAlkoholProcent.getText().trim());
            Controller.createBatch(navn,mængdeILiter,alkoholProcent);
            closeWindow();
            hide();
        } catch (NumberFormatException e) {
            showAlert("Fejl under opret batch", "Forkert tal input");
        } catch
         (Exception e) {
            showAlert("Fejl under opret batch", e.getMessage());
        }
    }

    private void showAlert(String headerText, String contentText) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Fejl");
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