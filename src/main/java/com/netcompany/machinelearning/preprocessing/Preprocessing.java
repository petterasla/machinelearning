package com.netcompany.machinelearning.preprocessing;


import com.netcompany.machinelearning.data.FetchMNISTData;

import javax.imageio.ImageIO;
import java.awt.image.*;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;

public class Preprocessing {

    public int[][][] trainingImages;
    public int[][][] testImages;

    public static void main(String[] args) {
        Preprocessing preprocessing = new Preprocessing();

        System.out.println(PreprocessingFactory.getTotalNumberOfImages(PreprocessingFactory.TRAINING_DIR));
        System.out.println(PreprocessingFactory.getTotalNumberOfImages(PreprocessingFactory.TEST_DIR));

        int[][][] imageArray = preprocessing.readImagesToArray(PreprocessingFactory.TRAINING_DIR);
//
//        String s = imageArray != null ? "IKKE NULL :)" : "NULL";
//        System.out.println(s);

    }

    public Preprocessing() {
        trainingImages = new int
                [PreprocessingFactory.getTotalNumberOfImages(PreprocessingFactory.TRAINING_DIR)]
                [PreprocessingFactory.height]
                [PreprocessingFactory.height];
        testImages = new int
                [PreprocessingFactory.getTotalNumberOfImages(PreprocessingFactory.TEST_DIR)]
                [PreprocessingFactory.height]
                [PreprocessingFactory.height];
    }

    private File getMnistData() {

        FetchMNISTData mnistFetcher = new FetchMNISTData();

        try {
            File file = mnistFetcher.downloadAndUntar();
            return file;
        } catch (IOException e) {
            System.out.println("Couldnt fetch the MNIST data");
        }
        return null;

    }

    private int[][] getImageAsArray(String imagePath){
        try {
//            String pathToImages = PreprocessingFactory.TRAINING_DIR + "/0";
//            File f = new File(pathToImages);
//            ArrayList<String> imageNameStrings = new ArrayList<String>(Arrays.asList(f.list()));
//            String imagePath1 = "/" + imageNameStrings.get(0);

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
//            printArray(array);
            return array;
        } catch (Exception e) {
            System.out.println("Something went wrong while reading the images");
        }
        return null;
    }

    private void printArray(int[][] array) {
        int w = array.length;
        int h = array[0].length;

        for (int j = 0; j < w; j++) {
            for (int k = 0; k < h; k++) {
                System.out.print(array[j][k] + "\t");
            }
            System.out.println();
        }
    }

    public int[][][] readImagesToArray(String datasetPath) {
        int counter = 0;
        File f = new File(datasetPath);
        ArrayList<String> folderPaths = new ArrayList<String>(Arrays.asList(f.list()));
        for (final String folder : folderPaths) {
            ArrayList<String> imagePaths = getImagePaths(datasetPath + "/" + folder);
            for (final String imagePath : imagePaths) {
                trainingImages[counter] = getImageAsArray(imagePath);
            }

        }
        return null;
    }

    private ArrayList<String> getImagePaths(String imagePath) {
        File f = new File(imagePath);
        return new ArrayList<String>(Arrays.asList(f.list()));
    }
}
