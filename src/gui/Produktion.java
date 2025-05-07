package gui;

import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.geometry.VPos;
import javafx.scene.control.Button;
import javafx.scene.control.Tab;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;

public class Produktion extends Main{
    public Tab getTab() {
        Tab tab = new Tab("Produktion");

        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(10));
        grid.setGridLinesVisible(false);

        HBox topBox = new HBox();
        GridPane.setHalignment(topBox, HPos.CENTER);
        GridPane.setValignment(topBox, VPos.CENTER);
        grid.add(topBox,0,0);

        Button createBatch = new Button("Opret batch");
        topBox.getChildren().add(createBatch);
        Button createFad = new Button("Opret fad");
        topBox.getChildren().add(createFad);
        Button createDestillat = new Button("Opret destillat");
        topBox.getChildren().add(createDestillat);

        //------------------------------------------------------------------------------------------------

        Button createFærdigProdukt = new Button();

        tab.setContent(grid);
        return tab;
    }

}
