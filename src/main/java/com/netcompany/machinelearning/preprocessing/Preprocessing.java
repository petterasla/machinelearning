package com.netcompany.machinelearning.preprocessing;


import javax.imageio.ImageIO;
import java.awt.image.*;
import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;

public class Preprocessing {

    private int[][][] trainingImages;
    private int[][][] testImages;

    public static void main(String[] args) {
        Preprocessing preprocessing = Preprocessing.create();
    }

    private Preprocessing() {
        System.out.println("This may take a minute.\n\nLoading training set.");

        trainingImages = new int
                [PreprocessingFactory.getTotalNumberOfImages(PreprocessingFactory.TRAINING_DIR)]
                [PreprocessingFactory.height]
                [PreprocessingFactory.height];

        readImagesToArray(trainingImages, PreprocessingFactory.TRAINING_DIR);

        System.out.println("Total training images: " + getTrainingImages().length);
        System.out.println();
        System.out.println("Loading test set");

        testImages = new int
                [PreprocessingFactory.getTotalNumberOfImages(PreprocessingFactory.TEST_DIR)]
                [PreprocessingFactory.height]
                [PreprocessingFactory.height];

        readImagesToArray(testImages, PreprocessingFactory.TEST_DIR);
        System.out.println("Total testing images: " + getTestImages().length);

        System.out.println("\nFinished loading images");

    }

    public static Preprocessing create()  {
        return new Preprocessing();
    }

    private int[][] getImageAsArray(String imagePath){
        try {
            BufferedImage image = ImageIO.read(new File(imagePath));
            WritableRaster raster = image.getRaster();
            DataBufferByte data   = (DataBufferByte) raster.getDataBuffer();

            int w = image.getWidth();
            int h = image.getHeight();

            int[][] array = new int[w][h];
            for (int row = 0; row < w; row++) {
                for (int col = 0; col < h; col++) {
                    data.getElemFloat(row);
                    array[row][col] = raster.getSample(row, col, 0);
                }

            }
            return array;
        } catch (Exception e) {
            System.out.println("Something went wrong while converting the image to 8-bit.\nImage path was:" + imagePath);
        }
        return null;
    }

    private void readImagesToArray(int[][][] images, String datasetPath) {
        int counter = 0;
        File f = new File(datasetPath);
        ArrayList<String> folderPaths = new ArrayList<String>(Arrays.asList(f.list()));
        for (final String folder : folderPaths) {
            String imageFolderPath = datasetPath + "/" + folder;
            ArrayList<String> imagePaths = getImagePaths(imageFolderPath);
            for (final String imagePath : imagePaths) {
                images[counter] = getImageAsArray(imageFolderPath + "/" + imagePath);
            }
        }
    }

    private ArrayList<String> getImagePaths(String imagePath) {
        File f = new File(imagePath);
        return new ArrayList<String>(Arrays.asList(f.list()));
    }

    public int[][][] getTrainingImages() {
        return trainingImages;
    }

    public int[][][] getTestImages() {
        return testImages;
    }

}
