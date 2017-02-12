package com.netcompany.machinelearning.neuralNetwork1;

import com.netcompany.machinelearning.preprocessing.DataHjelper;
import com.netcompany.machinelearning.preprocessing.DataLaster;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Oppgaver {

    private static final Logger LOGG = LoggerFactory.getLogger(Oppgaver.class);

    public static void main(final String[] args) throws Exception {

        // ###############################################################
        // OPPGAVE 1: PREPROSSESERING -> NORMALISERE OG FLAT UT
        // ###############################################################

        final Boolean lesInnHeleDatasettet = true;
        final DataLaster dataLaster = new DataLaster(lesInnHeleDatasettet);

        //TODO: Flate ut først, deretter normalisere
        // a) Normaliser bildene
        final double[][][] trainingImages = normalize(dataLaster.getTreningsbilder());
        final double[][][] testbilder = normalize(dataLaster.getTestbilder());

        // b) Flat ut bildene
        final double[][] trainingFlatImages = flatMapImage(trainingImages);
        final double[][] testFlatBilder = flatMapImage(testbilder);

        // ###############################################################
        // OPPGAVE 2: NEVRALT NETTVERK -> BYGG NETTVERK
        // ###############################################################

        final NevraltNettverk nevraltNettverk = new NevraltNettverkBygger().leggTilLag(784, 200)
                                                                           .leggTilLag(200, 10)
                                                                           .bygg();
        nevraltNettverk.setAntallEpoker(300);
        nevraltNettverk.setBatchStorrelse(200);

        nevraltNettverk.tren(trainingFlatImages, dataLaster.getTreningsfasit());

        nevraltNettverk.evaluer(testFlatBilder, dataLaster.getTestfasit());

        LOGG.info("FERDIG");
    }

    private static double[][][] normalize(final int[][][] trainingImages) {
        final int numberOfImages = trainingImages.length;
        final int width = DataHjelper.MNIST_BREDDE;
        final int height = DataHjelper.MNIST_HOYDE;

        final double[][][] normalizedImages = new double[numberOfImages][width][height];

        final double MAX_PIXEL_VERDI = 255.0; // FORDI VI VET DET :)

        for (int iCounter = 0; iCounter < numberOfImages; iCounter++) {
            for (int row = 0; row < width; row++) {
                for (int col = 0; col < height; col++) {
                    normalizedImages[iCounter][row][col] = (trainingImages[iCounter][row][col] / MAX_PIXEL_VERDI) * 2 - 1;
                }
            }
        }
        return normalizedImages;
    }

    private static double[][] flatMapImage(final double[][][] images) {
        final int numberOfImages = images.length;
        final double[][] flatImages = new double[numberOfImages][images[0].length * images[0][0].length];

        for (int i = 0; i < numberOfImages; i++) {
            flatImages[i] = flatImage(images[i]);
        }
        return flatImages;
    }

    private static double[] flatImage(final double[][] image) {
        int counter = 0;
        final double[] flatImage = new double[image.length * image[0].length];
        for (final double[] anImage : image) {
            for (final double anAnImage : anImage) {
                flatImage[counter] = anAnImage;
                counter++;
            }
        }
        return flatImage;
    }
}
