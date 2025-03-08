package com.example.projet_java;

import javafx.collections.FXCollections;
import javafx.scene.Scene;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.Tooltip;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.util.Callback;

import java.util.Arrays;
import java.util.List;

public class ItemSelectionUI {
    public static Item showItemSelectionDialog() {
        Stage dialog = new Stage();
        dialog.initModality(Modality.APPLICATION_MODAL);
        dialog.setTitle("Sélectionnez un objet");

        // Liste d'objets pré-définis
        List<Item> items = Arrays.asList(
                new Item("Reste", "Restaure 8% des HP à la fin du tour."),
                new Item("Ceinture de Force", "Augmente l'attaque physique."),
                new Item("Poings", "Augmente l'attaque physique."),
                new Item("Plume", "Augmente la vitesse."),
                new Item("Pierre", "Augmente la défense.")
        );

        ListView<Item> listView = new ListView<>(FXCollections.observableArrayList(items));
        // Personnalisation des cellules avec Tooltip pour afficher la description
        listView.setCellFactory(new Callback<ListView<Item>, ListCell<Item>>() {
            @Override
            public ListCell<Item> call(ListView<Item> param) {
                return new ListCell<Item>() {
                    @Override
                    protected void updateItem(Item item, boolean empty) {
                        super.updateItem(item, empty);
                        if (item == null || empty) {
                            setText(null);
                            setTooltip(null);
                        } else {
                            setText(item.getName());
                            Tooltip tooltip = new Tooltip(item.getDescription());
                            setTooltip(tooltip);
                        }
                    }
                };
            }
        });

        // Sélection via double-clic
        listView.setOnMouseClicked(event -> {
            if (event.getClickCount() == 2) {
                dialog.setUserData(listView.getSelectionModel().getSelectedItem());
                dialog.close();
            }
        });

        Scene scene = new Scene(listView, 300, 200);
        dialog.setScene(scene);
        dialog.showAndWait();

        return (Item) dialog.getUserData();
    }
}
