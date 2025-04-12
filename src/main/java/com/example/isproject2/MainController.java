package com.example.isproject2;

import javafx.scene.control.ComboBox;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

import java.io.IOException;

public class MainController {
    private Image originalImage;
    private Image resultImage;
    private ImageView originalImageView;
    private ImageView resultImageView;
    private ComboBox<String> filters;

    public MainController(ImageView originalImageView, ImageView resultImageView, ComboBox<String> filters) {
        this.originalImageView = originalImageView;
        this.resultImageView = resultImageView;
        this.filters = filters;
    }

    public void openImage() {
        Image image = FileUtils.downloadImage();
        if(image != null) {
            originalImage = image;
            originalImageView.setImage(originalImage);
            resultImageView.setImage(null);
        }
    }

    public void applyEffect() {
        if(originalImage == null) {
            System.out.println("Загрузи картинку!");
            return;
        }

        String filter = filters.getValue();
        if(originalImage == null || filter.isEmpty()) {
            System.out.println("Выбери фильтр!");
            return;
        }

        ImageProcessor imageProcessor = new ImageProcessor(originalImage);
        resultImage = imageProcessor.processes(originalImage, filter);
        resultImageView.setImage(resultImage);
    }

    public void saveImage() throws IOException {
        if(resultImage != null) {
            FileUtils.saveImageResult(resultImage);
        }
    }
}
