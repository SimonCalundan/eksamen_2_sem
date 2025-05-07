package gui;

import javafx.geometry.Insets;
import javafx.scene.control.Label;
import javafx.scene.control.Tab;
import javafx.scene.layout.GridPane;

public class Lager extends Main {

    public Tab getTab() {
        Tab tab = new Tab("Lageroversigt");

        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(10));
        grid.setGridLinesVisible(false);

        Label l1 = new Label("Test");
        grid.add(l1,0,0);

        tab.setContent(grid);
        return tab;
    }
}
