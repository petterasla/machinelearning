package com.netcompany.machinelearning.preprocessing;

public class DataHjelper {
    private static final String ROTMAPPE = System.getProperty("user.dir");
    private static final String DATAMAPPE = ROTMAPPE + "/src/main/java/com/netcompany/machinelearning/data/";
    static String TRENINGSMAPPE = DATAMAPPE + "mnist_png/training";
    static String TESTMAPPE = DATAMAPPE + "mnist_png/testing";
    public static int ANTALL_KLASSER = 10;

    public static final int height = 28;
    public static final int width = 28;

    public static void printBilde(final int[][] array) {
        final int w = array.length;
        final int h = array[0].length;

        for (final int[] anArray : array) {
            for (int k = 0; k < h; k++) {
                System.out.print(anArray[k] + "\t");
            }
            System.out.println();
        }
    }
}
