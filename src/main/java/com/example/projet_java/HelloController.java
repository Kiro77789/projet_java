package com.example.projet_java;

import javafx.fxml.FXML;
import javafx.scene.control.Label;

public class HelloController {
    @FXML
    private Label welcomeText;

    @FXML
    public void onPlayButtonClick() {
        welcomeText.setText("Let's go !");
    }
}