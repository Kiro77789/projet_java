package com.example.projet_java;

import javafx.collections.FXCollections;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.util.List;
import java.util.stream.Collectors;

public class MoveModificationUI {

    public static void showMoveModificationDialog(Pokemon pokemon) {
        Stage dialog = new Stage();
        dialog.initModality(Modality.APPLICATION_MODAL);
        dialog.setTitle("Modifier les Moves de " + pokemon.getName());

        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new javafx.geometry.Insets(10));

        List<Attack> allMoves = MoveDatabase.getAllMoves();

        ComboBox<Attack> moveBox1 = new ComboBox<>(FXCollections.observableArrayList(allMoves));
        ComboBox<Attack> moveBox2 = new ComboBox<>(FXCollections.observableArrayList(allMoves));
        ComboBox<Attack> moveBox3 = new ComboBox<>(FXCollections.observableArrayList(allMoves));
        ComboBox<Attack> moveBox4 = new ComboBox<>(FXCollections.observableArrayList(allMoves));

        List<Attack> currentMoves = pokemon.getMoves();
        if (currentMoves.size() > 0) moveBox1.setValue(currentMoves.get(0));
        if (currentMoves.size() > 1) moveBox2.setValue(currentMoves.get(1));
        if (currentMoves.size() > 2) moveBox3.setValue(currentMoves.get(2));
        if (currentMoves.size() > 3) moveBox4.setValue(currentMoves.get(3));

        grid.add(new Label("Move 1:"), 0, 0);
        grid.add(moveBox1, 1, 0);
        grid.add(new Label("Move 2:"), 0, 1);
        grid.add(moveBox2, 1, 1);
        grid.add(new Label("Move 3:"), 0, 2);
        grid.add(moveBox3, 1, 2);
        grid.add(new Label("Move 4:"), 0, 3);
        grid.add(moveBox4, 1, 3);

        Button btnSave = new Button("Enregistrer");
        grid.add(btnSave, 1, 4);

        btnSave.setOnAction(e -> {
            Attack m1 = moveBox1.getValue();
            Attack m2 = moveBox2.getValue();
            Attack m3 = moveBox3.getValue();
            Attack m4 = moveBox4.getValue();

            List<Attack> selectedMoves = java.util.Arrays.asList(m1, m2, m3, m4).stream()
                    .filter(m -> m != null)
                    .collect(Collectors.toList());

            long distinctCount = selectedMoves.stream().distinct().count();
            if (distinctCount < selectedMoves.size()) {
                Alert alert = new Alert(Alert.AlertType.ERROR, "Vous ne pouvez pas sélectionner la même capacité plusieurs fois.");
                alert.showAndWait();
                return;
            }
            pokemon.setMoves(selectedMoves);
            dialog.close();
        });

        Scene scene = new Scene(grid, 400, 250);
        dialog.setScene(scene);
        dialog.showAndWait();
    }
}
