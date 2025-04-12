package com.example.isproject2;

import javafx.scene.image.Image;
import javafx.scene.image.PixelReader;
import javafx.scene.image.PixelWriter;
import javafx.scene.image.WritableImage;
import javafx.scene.paint.Color;

import java.util.ArrayList;

public class ImageProcessor {
    Image image;
    public ImageProcessor(Image image) {
        this.image = image;
    }

    public Image processes(Image image, String filter) {
        int height = (int) image.getHeight();
        int width = (int) image.getWidth();

        PixelReader pixelReader = image.getPixelReader();
        ArrayList<ArrayList<Pixels>> pixelsList = new ArrayList<>();
        for(int y = 0; y < height; y++) {
            ArrayList<Pixels> row = new ArrayList<>();
            for(int x = 0; x < width; x++){
                row.add(new Pixels(0, 0, 0));
            }
            pixelsList.add(row);
        }

        for(int y = 0; y < height; y++) {
            for(int x = 0; x < width; x++){
                int argb = pixelReader.getArgb(x, y);

                int r = (argb >> 16) & 0xff;
                int g = (argb >> 8) & 0xff;
                int b = (argb) & 0xff;

                switch (filter) {
                    case "GrayScale" -> {
                        int gray = grayScale(r, g, b);
                        Pixels pixels = pixelsList.get(y).get(x);
                        pixels.setR(gray);
                        pixels.setG(gray);
                        pixels.setB(gray);
                    }
                    case "Invert" -> {
                        Pixels pixel = pixelsList.get(y).get(x);
                        pixel.setR(invert(r));
                        pixel.setG(invert(g));
                        pixel.setB(invert(b));
                    }
                    case "Blur" -> {
                        return gaussianBlur(image, 21, 3);
                    }
                    default -> {
                        return image;
                    }
                }
            }
        }

        WritableImage writableImage = new WritableImage(width, height);
        PixelWriter pixelWriter = writableImage.getPixelWriter();

        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                Pixels pixels = pixelsList.get(y).get(x);
                Color color = Color.rgb(pixels.getR(), pixels.getG(), pixels.getB());
                pixelWriter.setColor(x, y, color);
            }
        }
        return writableImage;

    }

    public int grayScale(int r, int g, int b){
        int avg = (r + g + b) / 3;
        return avg;
    }

    private int invert(int a) {
        int invert = 255 - a;
        return invert;
    }

    public Image gaussianBlur(Image image, int kernelSize, double sigma) {
        int width = (int) image.getWidth();
        int height = (int) image.getHeight();

        PixelReader pixelReader = image.getPixelReader();
        WritableImage tempWritableImage = new WritableImage(width, height);
        PixelWriter tempPixelWriter = tempWritableImage.getPixelWriter();

        double[] kernel = createGaussianKernel1D(kernelSize, sigma);
        int radius = kernelSize / 2;

        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                double r = 0, g = 0, b = 0;
                double weightSum = 0;

                for (int i = -radius; i <= radius; i++) {
                    int xi = clampIndex(x + i, width);
                    Color color = pixelReader.getColor(xi, y);
                    double weight = kernel[i + radius];

                    r += color.getRed() * weight;
                    g += color.getGreen() * weight;
                    b += color.getBlue() * weight;
                    weightSum += weight;
                }

                tempPixelWriter.setColor(x, y, new Color(clamp(r / weightSum), clamp(g / weightSum), clamp(b / weightSum), 1.0));
            }
        }

        PixelReader tempPixelReader = tempWritableImage.getPixelReader();
        WritableImage output = new WritableImage(width, height);
        PixelWriter outputPixelWriter = output.getPixelWriter();

        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                double r = 0, g = 0, b = 0;
                double weightSum = 0;

                for (int i = -radius; i <= radius; i++) {
                    int yi = clampIndex(y + i, height);
                    Color color = tempPixelReader.getColor(x, yi);
                    double weight = kernel[i + radius];

                    r += color.getRed() * weight;
                    g += color.getGreen() * weight;
                    b += color.getBlue() * weight;
                    weightSum += weight;
                }

                outputPixelWriter.setColor(x, y, new Color(clamp(r / weightSum), clamp(g / weightSum), clamp(b / weightSum), 1.0));
            }
        }

        return output;
    }

    private double[] createGaussianKernel1D(int size, double sigma) {
        double[] kernel = new double[size];
        int radius = size / 2;
        double sum = 0;

        for (int i = -radius; i <= radius; i++) {
            double value = Math.exp(-(i * i) / (2 * sigma * sigma));
            kernel[i + radius] = value;
            sum += value;
        }

        for (int i = 0; i < size; i++) {
            kernel[i] /= sum;
        }

        return kernel;
    }

    private double clamp(double value) {
        return Math.max(0, Math.min(1, value));
    }

    private int clampIndex(int index, int max) {
        return Math.max(0, Math.min(index, max - 1));
    }
}
