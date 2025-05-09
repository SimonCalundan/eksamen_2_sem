package gui;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.stage.Stage;

public class Main extends Application {

    @Override
    public void start(Stage primaryStage) {
        TabPane tabPane = new TabPane();

        // Tilføj tabs fra de respektive klasser
        Tab produktionTab = new Produktion().getTab();
        Tab fadTab = new FadFane().getTab();
        Tab lagerTab = new LagerFane().getTab();

        tabPane.setTabClosingPolicy(TabPane.TabClosingPolicy.UNAVAILABLE);
        tabPane.getTabs().addAll(produktionTab, fadTab, lagerTab);

        int antalTabs = tabPane.getTabs().size();
        double tabWidth = 800.0 / antalTabs;
        tabPane.setStyle("-fx-tab-min-width: " + tabWidth + "px;");

        //-----------------------------------------------------------------

        Scene scene = new Scene(tabPane, 855, 600);
        primaryStage.setScene(scene);
        primaryStage.setTitle("Sall Whisky");
        primaryStage.show();
    }


}
