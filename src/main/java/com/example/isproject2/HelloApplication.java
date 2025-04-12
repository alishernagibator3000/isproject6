package com.example.isproject2;

import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.io.IOException;

public class HelloApplication extends Application {
    @Override
    public void start(Stage stage) throws IOException {
        VBox root = new VBox();
        Scene scene = new Scene(root, 1000, 750);

        Button selectBtn = new Button("Выбрать картинку");
        Button processBtn = new Button("Применить");
        ComboBox<String> filters = new ComboBox<>();
        filters.setItems(FXCollections.observableArrayList("GrayScale", "Invert", "Blur"));
        filters.setValue("GrayScale");
        Button saveBtn = new Button("Сохранить");

        HBox controlPanel = new HBox(10, selectBtn, filters, processBtn, saveBtn);
        root.getChildren().add(controlPanel);

        ImageView original = new ImageView();
        original.setPreserveRatio(true);
        original.fitWidthProperty().bind(scene.widthProperty().multiply(0.66));
        original.fitHeightProperty().bind(scene.heightProperty().multiply(0.33));

        ImageView result = new ImageView();
        result.setPreserveRatio(true);
        result.fitWidthProperty().bind(scene.widthProperty().multiply(0.66));
        result.fitHeightProperty().bind(scene.heightProperty().multiply(0.33));

        HBox imagePanel = new HBox(30, original, result);
        imagePanel.setStyle("-fx-padding: 20px;");
        root.getChildren().add(imagePanel);

        MainController mainController = new MainController(original, result, filters);
        original.setPreserveRatio(true);
        result.setPreserveRatio(true);

        selectBtn.setOnAction(event -> { mainController.openImage(); });
        processBtn.setOnAction(event -> { mainController.applyEffect(); });
        saveBtn.setOnAction(event -> {
            try {
                mainController.saveImage();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });

        stage.setTitle("Manas pidor, Shah niga, Uzar lox");
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }
}