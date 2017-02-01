package com.netcompany.machinelearning.preprocessing;


import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;

public class PreprocessingFactory {
    private static String BASE_DIR = System.getProperty("user.dir");
    private static String DATA_DIR = BASE_DIR + "/src/main/java/com/netcompany/machinelearning/data/";
    public static String TRAINING_DIR = DATA_DIR + "mnist_png/training";
    public static String TEST_DIR = DATA_DIR + "mnist_png/testing";
    public static int SUBSET_TRAINING_SIZE = 100;
    public static int SUBSET_TEST_SIZE = 30;
    public static int ANTALL_KLASSER = 10;

    public static final int height = 28;
    public static final int width = 28;

    public static int getTotalNumberOfImages(String datasetPath) {
        int numberOfImages = 0;
        File f = new File(datasetPath);
        ArrayList<String> folderPaths = new ArrayList<String>(Arrays.asList(f.list()));
        for (final String folder : folderPaths) {
            if (folder.equalsIgnoreCase(".ds_store")) continue;
            ArrayList<String> imagePaths = getImagePaths(datasetPath + "/" + folder);
            numberOfImages += imagePaths.size();
        }
        return numberOfImages;
    }

    public static ArrayList<String> getImagePaths(String imagePath) {
        File f = new File(imagePath);
        return new ArrayList<String>(Arrays.asList(f.list()));
    }

    public static void printImage(int[][] array) {
        int w = array.length;
        int h = array[0].length;

        for (int j = 0; j < w; j++) {
            for (int k = 0; k < h; k++) {
                System.out.print(array[j][k] + "\t");
            }
            System.out.println();
        }
    }
}
