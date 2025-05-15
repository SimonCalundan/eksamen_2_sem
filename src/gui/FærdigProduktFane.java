package gui;

import application.controller.Controller;
import application.model.FærdigProdukt;
import javafx.beans.value.ChangeListener;
import javafx.geometry.Insets;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.Tab;
import javafx.scene.control.TextArea;
import javafx.scene.layout.GridPane;

public class FærdigProduktFane extends MainFane {

    private ListView<FærdigProdukt> lwFærdigProdukt;
    private TextArea taProduktHistorik, taProduktInfo;

    public Tab getTab()   {
        Tab tab = new Tab("Færdige Produkter");

        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(10));
        grid.setGridLinesVisible(false);

        //FÆRDIGPRODUKTLISTE
        Label listViewLabel = new Label("Produkter");
        grid.add(listViewLabel, 0, 0);

        lwFærdigProdukt = new ListView<>();
        grid.add(lwFærdigProdukt, 0, 1, 1, 3);
        lwFærdigProdukt.getItems().setAll(Controller.getFærdigProdukter());

        //PRODUKTINFO
        Label infoLabel = new Label("Produktinfo");
        grid.add(infoLabel, 1, 0);

        taProduktInfo = new TextArea();
        grid.add(taProduktInfo, 1, 1);

        //PRODUKTHISTORIK
        Label historikLabel = new Label("Produkthistorik");
        grid.add(historikLabel, 1, 2);

        taProduktHistorik = new TextArea();
        grid.add(taProduktHistorik, 1, 3);

        ChangeListener<FærdigProdukt> listener = (ov, oldFærdigProdukt, newFærdigProdukt) -> this.færdigProduktSelectionChanged();
        lwFærdigProdukt.getSelectionModel().selectedItemProperty().addListener(listener);

        tab.selectedProperty().addListener((obs, wasSelected, isNowSelected) -> {
            if (isNowSelected) {
                onTabVisited();
            }
        });

        tab.setContent(grid);
        return tab;
    }

    public void færdigProduktSelectionChanged() {
            taProduktInfo.setText(lwFærdigProdukt.getSelectionModel().getSelectedItem().GUIview());
            taProduktHistorik.setText(Controller.getFærdigProduktHistorik(lwFærdigProdukt.getSelectionModel().getSelectedItem()));
    }

    private void onTabVisited() {
        lwFærdigProdukt.getItems().setAll(Controller.getFærdigProdukter());
    }
}
