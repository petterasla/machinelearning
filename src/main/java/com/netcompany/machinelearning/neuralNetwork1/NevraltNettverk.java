package com.netcompany.machinelearning.neuralNetwork1;

import org.deeplearning4j.nn.conf.MultiLayerConfiguration;
import org.deeplearning4j.nn.conf.NeuralNetConfiguration;
import org.deeplearning4j.nn.multilayer.MultiLayerNetwork;

/**
 * User: Oystein Kvamme Repp
 * Date: 29.01.2017
 * Time: 20.59
 */
public class NevraltNettverk {

    private static final Integer RAND_SEED = 42;

    private Integer inputStorrelse;
    private Integer antallKlasser;
    private Integer batchStorrelse;
    private Integer antallEpoker;

    private MultiLayerNetwork modell;


    public NevraltNettverk(final MultiLayerConfiguration nnKonfigurasjon) {
        modell = new MultiLayerNetwork(nnKonfigurasjon);
        modell.init();
    }
}
