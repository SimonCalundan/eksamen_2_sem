package gui;

import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.Tab;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;

public class CreateDestillatVindue extends Stage {

    public CreateDestillatVindue(String title, Tab tab) {
        this.setTitle(title);
        GridPane grid = new GridPane();

        Label label = new Label("Her kan du oprette et destillat.");
        grid.add(label,0,0);

        Scene scene = new Scene(grid, 300, 200);
        this.setScene(scene);
    }
}