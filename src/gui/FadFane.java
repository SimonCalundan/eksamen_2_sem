package gui;

import application.controller.Controller;
import application.model.Fad;
import application.model.Hylde;
import application.model.Lager;
import application.model.Reol;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.layout.*;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class FadFane {
    private final Tab tab;
    private final VBox contentBox = new VBox(10);
    private final TextField træsortField = new TextField();
    private final TextField leverandørField = new TextField();
    private final TextField minLiterField = new TextField();
    private final TextField maxLiterField = new TextField();
    private final ComboBox<Integer> brugtGangeCombo = new ComboBox<>();

    public FadFane() {
        this.tab = new Tab("Fade");
        tab.setClosable(false);
        tab.setContent(buildContent());
    }

    private BorderPane buildContent() {
        BorderPane root = new BorderPane();
        root.setPadding(new Insets(10));

        GridPane filterPane = new GridPane();
        filterPane.setHgap(10);
        filterPane.setVgap(10);
        filterPane.setPadding(new Insets(10));

        træsortField.setPromptText("Træsort");
        leverandørField.setPromptText("Leverandør");
        minLiterField.setPromptText("Min. Liter");
        maxLiterField.setPromptText("Max. Liter");
        brugtGangeCombo.getItems().addAll(0, 1, 2);
        brugtGangeCombo.setPromptText("Brugt gange");

        Button filtrerBtn = new Button("Filtrer");
        Button nulstilBtn = new Button("Nulstil");
        Button opretBtn = new Button("Opret Fad");

        filtrerBtn.setOnAction(e -> visFade());
        nulstilBtn.setOnAction(e -> {
            træsortField.clear();
            leverandørField.clear();
            minLiterField.clear();
            maxLiterField.clear();
            brugtGangeCombo.getSelectionModel().clearSelection();
            visFade();
        });
        opretBtn.setOnAction(e -> opretFadDialog());

        filterPane.add(new Label("Træsort:"), 0, 0);
        filterPane.add(træsortField, 1, 0);
        filterPane.add(new Label("Leverandør:"), 0, 1);
        filterPane.add(leverandørField, 1, 1);
        filterPane.add(new Label("Min liter:"), 2, 0);
        filterPane.add(minLiterField, 3, 0);
        filterPane.add(new Label("Max liter:"), 2, 1);
        filterPane.add(maxLiterField, 3, 1);
        filterPane.add(new Label("Brugt gange:"), 4, 0);
        filterPane.add(brugtGangeCombo, 5, 0);
        filterPane.add(filtrerBtn, 4, 1);
        filterPane.add(nulstilBtn, 5, 1);
        filterPane.add(opretBtn, 6, 0);

        ScrollPane scroll = new ScrollPane(contentBox);
        scroll.setFitToWidth(true);
        scroll.setPadding(new Insets(10));

        root.setTop(filterPane);
        root.setCenter(scroll);
        visFade();
        return root;
    }

    private void visFade() {
        contentBox.getChildren().clear();

        List<Fad> filtreret = Controller.getFade().stream()
                .filter(f -> træsortField.getText().isEmpty() || f.getTræsort().toLowerCase().contains(træsortField.getText().toLowerCase()))
                .filter(f -> leverandørField.getText().isEmpty() || f.getLeverandør().toLowerCase().contains(leverandørField.getText().toLowerCase()))
                .filter(f -> {
                    double min = parseDouble(minLiterField.getText(), -1);
                    return min == -1 || f.getStørrelseLiter() >= min;
                })
                .filter(f -> {
                    double max = parseDouble(maxLiterField.getText(), -1);
                    return max == -1 || f.getStørrelseLiter() <= max;
                })
                .filter(f -> brugtGangeCombo.getValue() == null || f.getBrugtGange() == brugtGangeCombo.getValue())
                .collect(Collectors.toList());

        GridPane grid = new GridPane();
        grid.setHgap(20);
        grid.setVgap(20);

        int col = 0, row = 0;
        for (Fad fad : filtreret) {
            VBox box = fadBox(fad);
            grid.add(box, col++, row);
            if (col == 3) {
                col = 0;
                row++;
            }
        }
        contentBox.getChildren().add(grid);
    }

    private VBox fadBox(Fad fad) {
        VBox box = new VBox(5);
        box.setPadding(new Insets(10));
        box.setStyle("-fx-border-color: gray; -fx-background-color: #f0f0f0;");

        String lager = "-", reol = "-", hylde = "-";
        if (fad.getHylde() != null) {
            hylde = fad.getHylde().toString();
            if (fad.getHylde().getReol() != null) {
                reol = fad.getHylde().getReol().getNavn();
                if (fad.getHylde().getReol().getLager() != null) {
                    lager = fad.getHylde().getReol().getLager().getNavn();
                }
            }
        }

        box.getChildren().addAll(
                new Label("Nr.: " + fad.getNr()),
                new Label("Træsort: " + fad.getTræsort()),
                new Label("Leverandør: " + fad.getLeverandør()),
                new Label("Størrelse: " + fad.getStørrelseLiter() + " L"),
                new Label("Brugt gange: " + fad.getBrugtGange()),
                new Label("Placering: " + lager + " > " + reol + " > " + hylde)
        );

        Button flytBtn = new Button("Flyt");
        flytBtn.setOnAction(e -> flytFadDialog(fad));
        Button fjernBtn = new Button("Fjern");
        fjernBtn.setOnAction(e -> {
            Controller.removeFad(fad);
            visFade();
        });

        box.getChildren().add(new HBox(10, flytBtn, fjernBtn));
        return box;
    }

    private void flytFadDialog(Fad fad) {
        Dialog<Hylde> dialog = new Dialog<>();
        dialog.setTitle("Flyt Fad");

        ComboBox<Lager> lagerValg = new ComboBox<>();
        ComboBox<Reol> reolValg = new ComboBox<>();
        ComboBox<Hylde> hyldeValg = new ComboBox<>();

        lagerValg.getItems().addAll(Controller.getLagre());

        // UI-opdatering
        lagerValg.setOnAction(e -> {
            Lager valgtLager = lagerValg.getValue();
            if (valgtLager != null) {
                reolValg.getItems().setAll(Controller.getReoler(valgtLager));
                reolValg.setValue(null);
                hyldeValg.getItems().clear();
                hyldeValg.setValue(null);
            }
        });

        reolValg.setOnAction(e -> {
            Reol valgtReol = reolValg.getValue();
            if (valgtReol != null) {
                hyldeValg.getItems().setAll(Controller.getHylder(valgtReol));
                hyldeValg.setValue(null);
            }
        });

        // ➤ Deaktiver OK-knap indtil hylde er valgt
        ButtonType okButtonType = new ButtonType("OK", ButtonBar.ButtonData.OK_DONE);
        dialog.getDialogPane().getButtonTypes().addAll(okButtonType, ButtonType.CANCEL);
        Node okButton = dialog.getDialogPane().lookupButton(okButtonType);
        okButton.setDisable(true);

        hyldeValg.setOnAction(e -> {
            okButton.setDisable(hyldeValg.getValue() == null);
        });

        // Nuværende placering visning
        String lagerNavn = "-", reolNavn = "-", hyldeNavn = "-";
        if (fad.getHylde() != null) {
            hyldeNavn = fad.getHylde().toString();
            if (fad.getHylde().getReol() != null) {
                reolNavn = fad.getHylde().getReol().getNavn();
                if (fad.getHylde().getReol().getLager() != null) {
                    lagerNavn = fad.getHylde().getReol().getLager().getNavn();
                }
            }
        }

        Label nuværendePlacering = new Label("Nuværende placering: " + lagerNavn + " > " + reolNavn + " > " + hyldeNavn);

        VBox box = new VBox(10,
                nuværendePlacering,
                new Label("Vælg nyt Lager:"), lagerValg,
                new Label("Vælg Reol:"), reolValg,
                new Label("Vælg Hylde:"), hyldeValg
        );
        box.setPadding(new Insets(10));
        dialog.getDialogPane().setContent(box);

        dialog.setResultConverter(bt -> bt == okButtonType ? hyldeValg.getValue() : null);
        Optional<Hylde> res = dialog.showAndWait();

        res.ifPresent(nyHylde -> {
            Hylde gammelHylde = fad.getHylde();
            Reol gammelReol = (gammelHylde != null) ? gammelHylde.getReol() : null;
            Lager gammelLager = (gammelReol != null) ? gammelReol.getLager() : null;

            Reol nyReol = nyHylde.getReol();
            Lager nyLager = nyReol.getLager();

            Alert confirm = new Alert(Alert.AlertType.CONFIRMATION);
            confirm.setTitle("Bekræft flytning");
            confirm.setHeaderText("Bekræft ny placering");

            String tekst =
                    "Tidligere placering:\n" +
                            "Lager: " + (gammelLager != null ? gammelLager.getNavn() : "-") + "\n" +
                            "Reol: " + (gammelReol != null ? gammelReol.getNavn() : "-") + "\n" +
                            "Hylde: " + (gammelHylde != null ? gammelHylde: "-") + "\n\n" +
                            "Ny placering:\n" +
                            "Lager: " + nyLager.getNavn() + "\n" +
                            "Reol: " + nyReol.getNavn() + "\n" +
                            "Hylde: " + nyHylde;

            confirm.setContentText(tekst);
            confirm.getButtonTypes().setAll(
                    new ButtonType("Bekræft", ButtonBar.ButtonData.OK_DONE),
                    new ButtonType("Fortryd", ButtonBar.ButtonData.CANCEL_CLOSE)
            );

            Optional<ButtonType> svar = confirm.showAndWait();
            if (svar.isPresent() && svar.get().getButtonData() == ButtonBar.ButtonData.OK_DONE) {
                Controller.flytFadTilNyPlacering(fad, nyHylde);
                visFade();
            }
        });
    }




    private void opretFadDialog() {
        Dialog<ButtonType> dialog = new Dialog<>();
        dialog.setTitle("Opret Fad");

        TextField literField = new TextField();
        TextField leverandørField = new TextField();
        TextField træsortField = new TextField();
        TextField brugtField = new TextField();

        ComboBox<Lager> lagerValg = new ComboBox<>();
        ComboBox<Reol> reolValg = new ComboBox<>();
        ComboBox<Hylde> hyldeValg = new ComboBox<>();

        lagerValg.getItems().addAll(Controller.getLagre());
        lagerValg.setOnAction(e -> {
            reolValg.getItems().setAll(Controller.getReoler(lagerValg.getValue()));
            reolValg.getSelectionModel().clearSelection();
            hyldeValg.getItems().clear();
        });

        reolValg.setOnAction(e -> {
            hyldeValg.getItems().setAll(Controller.getHylder(reolValg.getValue()));
        });

        VBox vbox = new VBox(10,
                new Label("Liter:"), literField,
                new Label("Leverandør:"), leverandørField,
                new Label("Træsort:"), træsortField,
                new Label("Brugt gange:"), brugtField,
                new Label("Lager:"), lagerValg,
                new Label("Reol:"), reolValg,
                new Label("Hylde:"), hyldeValg
        );
        vbox.setPadding(new Insets(10));

        dialog.getDialogPane().setContent(vbox);
        dialog.getDialogPane().getButtonTypes().addAll(ButtonType.OK, ButtonType.CANCEL);

        Optional<ButtonType> res = dialog.showAndWait();
        if (res.isPresent() && res.get() == ButtonType.OK) {
            try {
                double liter = Double.parseDouble(literField.getText());
                String lev = leverandørField.getText();
                String træ = træsortField.getText();
                int brugt = Integer.parseInt(brugtField.getText());
                Hylde hylde = hyldeValg.getValue();
                if (hylde != null) {
                    Controller.createFad(liter, lev, træ, brugt, hylde);
                    visFade();
                }
            } catch (Exception ex) {
                new Alert(Alert.AlertType.ERROR, "Ugyldig input: " + ex.getMessage()).showAndWait();
            }
        }
    }

    private double parseDouble(String s, double fallback) {
        try {
            return Double.parseDouble(s);
        } catch (Exception e) {
            return fallback;
        }
    }

    public Tab getTab() {
        return tab;
    }
}

