package gui;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Tab;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;

public class ProduktionTab extends Main{

    private CreateBatchVindue batchVindue;
    private CreateDestillatVindue destillatVindue;
    private CreateFadVindue fadVindue;

    public Tab getTab() {
        Tab tab = new Tab("Produktion");

        GridPane grid = new GridPane();
        grid.setHgap(25);
        grid.setVgap(25);
        grid.setPadding(new Insets(20));
        grid.setAlignment(Pos.CENTER);
        grid.setGridLinesVisible(true);

        HBox topBox = new HBox();
        topBox.setSpacing(50);
        topBox.setAlignment(Pos.CENTER);
        grid.add(topBox,0,0);

        Button btnCreateBatch = new Button("Producer batch");
        topBox.getChildren().add(btnCreateBatch);
        btnCreateBatch.setOnAction(event -> this.createBatchAction());
        batchVindue = new CreateBatchVindue("Producer batch", tab);

        Button btnCreateFad = new Button("Opret fad");
        topBox.getChildren().add(btnCreateFad);
        btnCreateFad.setOnAction(event -> this.createFadAction());
        fadVindue = new CreateFadVindue("Opret Fad", tab);

        Button btnCreateDestillat = new Button("Påfyldning af fad");
        topBox.getChildren().add(btnCreateDestillat);
        btnCreateDestillat.setOnAction(event ->this.createDestillatAction());
        destillatVindue = new CreateDestillatVindue("Påfyldning af fad", tab);

        //------------------------------------------------------------------------------------------------

        HBox bundBox = new HBox();
        bundBox.setAlignment(Pos.CENTER);
        bundBox.setSpacing(0);
        grid.add(bundBox,0,1);

        Button createFærdigProdukt = new Button("Opret færdig produkt");
        bundBox.getChildren().add(createFærdigProdukt);

        tab.setContent(grid);
        return tab;
    }

    //knapfunktion til batch
    private void createBatchAction(){
        batchVindue.showAndWait();
    }

    //knapfunktion til fad
    private void createFadAction(){
        fadVindue.showAndWait();
    }

    //knapfunktion til destillat
    private void createDestillatAction(){
        destillatVindue.showAndWait();
    }
}