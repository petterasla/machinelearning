package com.netcompany.machinelearning.preprocessing;

import com.google.common.collect.Lists;
import com.twelvemonkeys.lang.Validate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.awt.image.DataBufferByte;
import java.awt.image.WritableRaster;
import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public final class DataLaster {

    private final Logger LOGG = LoggerFactory.getLogger(this.getClass());
    private static final double ANDEL_AV_DATA = 0.05;

    private final Datasett treningsdata;
    private final Datasett testdata;

    public DataLaster(final Boolean lesInnHeleDatasettet) {
        if (lesInnHeleDatasettet) {
            LOGG.info("Laster alle trenings- og testdata. Dette kan ta en stund...");

            treningsdata = lastBilder(1, DataHjelper.TRENINGSMAPPE);
            testdata = lastBilder(1, DataHjelper.TESTMAPPE);

            LOGG.info("Ferdig med å laste inn bilder.");
        } else {
            LOGG.info(String.format("Laster inn subset (%.0f%%) av trenings- og testdata.", ANDEL_AV_DATA * 100));

            treningsdata = lastBilder(ANDEL_AV_DATA, DataHjelper.TRENINGSMAPPE);
            testdata = lastBilder(ANDEL_AV_DATA, DataHjelper.TESTMAPPE);

            LOGG.info("Ferdig med å laste inn bilder.");
        }
    }

    private Datasett lastBilder(final double andelAvData, final String stiTilDatamappe) {
        final List<int[][]> bilder = Lists.newArrayList();
        final List<Integer> fasit = Lists.newArrayList();

        File f = new File(stiTilDatamappe);
        Validate.isTrue(f.exists(), String.format("%s finnes ikke", stiTilDatamappe));

        final ArrayList<String> dataklasseStier = Lists.newArrayList(f.list());
        for (final String mappe : dataklasseStier) {
            int antallPerKlasse = 0;
            if (mappe.equalsIgnoreCase(".DS_STORE")) continue;
            final String bildemappeSti = stiTilDatamappe + File.separator + mappe;
            final ArrayList<String> bildeStier = hentBildeStier(bildemappeSti);
            for (final String bildesti : bildeStier) {
                if (antallPerKlasse++ > bildeStier.size() * andelAvData) {
                    break;
                }
                bilder.add(hentBildeSomIntArray(bildemappeSti + File.separator + bildesti));
                fasit.add(Integer.parseInt(mappe));
            }
        }
        final int[][][] bildeArray = new int[bilder.size()][DataHjelper.height][DataHjelper.width];
        final int[] fasitArray = fasit.stream().mapToInt(Integer::intValue).toArray();
        bilder.toArray(bildeArray);
        return new Datasett(bildeArray, fasitArray);
    }

    /**
     * @param bildeSti stien til ett bilde
     * @return en to-dimensjonal int array representasjon av bilde med verdier innenfor 0-255.
     */
    private int[][] hentBildeSomIntArray(final String bildeSti) {
        try {
            final BufferedImage bilde = ImageIO.read(new File(bildeSti));
            final WritableRaster raster = bilde.getRaster();
            final DataBufferByte data = (DataBufferByte) raster.getDataBuffer();

            final int w = bilde.getWidth();
            final int h = bilde.getHeight();

            final int[][] array = new int[w][h];
            for (int row = 0; row < w; row++) {
                for (int col = 0; col < h; col++) {
                    data.getElemFloat(row);
                    array[row][col] = raster.getSample(row, col, 0);
                }
            }
            return array;
        } catch (final Exception e) {
            LOGG.info("Bæsjen traff vifta ved lesing av bilde:" + bildeSti);
        }
        return null;
    }

    private ArrayList<String> hentBildeStier(final String bildemappeSti) {
        final File f = new File(bildemappeSti);
        return new ArrayList<>(Arrays.asList(f.list()));
    }

    public int[][][] getTreningsbilder() {
        return treningsdata.getBilder();
    }

    public int[][][] getTestbilder() {
        return testdata.getBilder();
    }

    public int[] getTreningsfasit() {
        return treningsdata.getFasit();
    }

    public int[] getTestfasit() {
        return testdata.getFasit();
    }

    /**
     * Intern klasse som brukes til å holde features (bildedata) og fasit (klasse) for datasettene.
     */
    private final class Datasett {

        final int[][][] bilder;
        final int[] fasit;

        private Datasett(final int[][][] bilder, final int[] fasit) {
            this.bilder = bilder;
            this.fasit = fasit;
        }

        private int[][][] getBilder() {
            return bilder;
        }

        private int[] getFasit() {
            return fasit;
        }
    }

}
