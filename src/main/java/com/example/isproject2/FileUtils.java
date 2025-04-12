package com.example.isproject2;

import javafx.scene.image.Image;
import javafx.scene.image.PixelReader;
import javafx.scene.image.WritablePixelFormat;
import javafx.stage.FileChooser;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.IntBuffer;

public class FileUtils {
    public static Image downloadImage() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Открыть");
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Image Files", "*.jpg", "*.png", "*.bmp"));
        File file = fileChooser.showOpenDialog(null);
        if(file != null) {
            try {
                return new Image(new FileInputStream(file));
            } catch(IOException e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    public static void saveImageResult(Image image) throws IOException {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setInitialFileName("alisher-get-automatic.png");
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Image Files", "*.png", "*.jpg", "*.bmp"));
        File file = fileChooser.showSaveDialog(null);
        if(file!= null) {
            BufferedImage bufferedImage = transformToBufferedImage(image);
            ImageIO.write(bufferedImage, "png", file);
        }
    }

    public static BufferedImage transformToBufferedImage(Image image) {
        int height = (int) image.getHeight();
        int width = (int) image.getWidth();

        BufferedImage bufferedImage = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
        PixelReader pixelReader = image.getPixelReader();
        int[] pixels = new int[height * width];
        WritablePixelFormat<IntBuffer> writablePixelFormat = WritablePixelFormat.getIntArgbInstance();

        pixelReader.getPixels(0, 0, width, height, writablePixelFormat, pixels, 0, width);
        bufferedImage.setRGB(0, 0, width, height, pixels, 0, width);

        return bufferedImage;
    }
}
