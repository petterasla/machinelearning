package com.netcompany.machinelearning.neuralNetwork1;

import org.apache.commons.lang3.Validate;
import org.deeplearning4j.eval.Evaluation;
import org.deeplearning4j.nn.conf.MultiLayerConfiguration;
import org.deeplearning4j.nn.conf.NeuralNetConfiguration;
import org.deeplearning4j.nn.multilayer.MultiLayerNetwork;
import org.nd4j.linalg.api.ndarray.INDArray;
import org.nd4j.linalg.cpu.nativecpu.NDArray;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * User: Oystein Kvamme Repp
 * Date: 29.01.2017
 * Time: 20.59
 */
public class NevraltNettverk {

    private static final Integer RAND_SEED = 42;
    private static Logger logg = LoggerFactory.getLogger(NevraltNettverk.class);


    private Integer inputStorrelse;
    private Integer antallKlasser;
    private Integer batchStorrelse;
    private Integer antallEpoker;

    private MultiLayerNetwork modell;


    public NevraltNettverk(final MultiLayerConfiguration nnKonfigurasjon) {
        logg.info("Lager modell...");
        antallEpoker = 1;
        batchStorrelse = 1;
        modell = new MultiLayerNetwork(nnKonfigurasjon);
        modell.init();
    }

    /**
     * Setter antallet epoker modellen skal trenes. Én epoke vil si ett pass over treningsdataene. 1 by default.
     *
     * @param antallEpoker antall epoker
     */
    public void setAntallEpoker(final Integer antallEpoker) {
        this.antallEpoker = antallEpoker;
    }

    public void setBatchStorrelse(final Integer batchStorrelse) {
        this.batchStorrelse = batchStorrelse;
    }

    public void tren(final double[][] treningsdata, int[] treningsfasit) {
        Validate.isTrue(treningsdata.length == treningsfasit.length,
                "Kan ikke trene modell når antall treningseksempler er ulik antall fasit-klasser!");

        final INDArray treningsMatrise = new NDArray(treningsdata);
        modell.setInputMiniBatchSize(batchStorrelse);

        logg.info("Trener...");
        for (int i = 0; i < antallEpoker; i++) {
            modell.fit(treningsMatrise, treningsfasit);
        }
    }

    public void evaluer(double[][] testdata, int[] testfasit) {
        logg.info("Evaluerer...");
        final Evaluation eval = new Evaluation(antallKlasser);

        INDArray prediksjoner = modell.output(new NDArray(testdata));
        eval.eval(prediksjoner, new NDArray(testfasit));

        logg.info(eval.stats());
    }
}
