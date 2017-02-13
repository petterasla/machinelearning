package com.netcompany.machinelearning.neuralNetwork1;

import com.google.common.collect.Lists;
import com.netcompany.machinelearning.preprocessing.DataHjelper;
import org.apache.commons.lang3.Validate;
import org.deeplearning4j.eval.Evaluation;
import org.deeplearning4j.nn.conf.MultiLayerConfiguration;
import org.deeplearning4j.nn.multilayer.MultiLayerNetwork;
import org.deeplearning4j.optimize.listeners.ScoreIterationListener;
import org.nd4j.linalg.api.ndarray.INDArray;
import org.nd4j.linalg.cpu.nativecpu.NDArray;
import org.nd4j.linalg.factory.Nd4j;
import org.nd4j.linalg.util.FeatureUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * User: Oystein Kvamme Repp
 * Date: 29.01.2017
 * Time: 20.59
 */
public class NevraltNettverk {

    private static final Logger LOGG = LoggerFactory.getLogger(NevraltNettverk.class);

    private final Integer antallKlasser;
    private Integer batchStorrelse;
    private Integer antallEpoker;
    private boolean erTrent = false;

    private final MultiLayerNetwork modell;

    public NevraltNettverk(final MultiLayerConfiguration nnKonfigurasjon) {
        LOGG.info("Bygger modell...");
        antallEpoker = 1;
        batchStorrelse = 1;
        antallKlasser = DataHjelper.ANTALL_KLASSER;
        // Lager et dl4j.MultiLayerNetwork (NN) basert på konfigurasjonen som ble bygd i NevraltNettverkBygger
        modell = new MultiLayerNetwork(nnKonfigurasjon);
        // Init validerer konfigurasjonen av nettet
        modell.init();
    }

    /**
     * Setter antallet epoker modellen skal trenes. Én epoke vil si ett pass over treningsdataene. 1 by default.
     *
     * @param antallEpoker antall epoker
     */
    void setAntallEpoker(final Integer antallEpoker) {
        this.antallEpoker = antallEpoker;
    }

    /**
     * Setter batchstørrelsen ved trening. Dette vil si hvor mange datainstanser som leses mellom hver oppdatering av
     * vektene i nettet. Større batcher gir nettet bedre evne til å generalisere (mindre påvirket av outliers),
     * samtidig som det gjør treningen raskere (trenger ikke oppdatere like ofte). Mindre batcher har motsatt virkning.
     * Blir nettet for generelt, er det ikke sikkert det blir i stand til å skille mellom klasser, mens om det blir
     * for lite generelt er det ikke sikkert det klarer å gjenkjenne nye data. Finn riktig størrelse ved prøv og feil;)
     *
     * @param batchStorrelse antall datapunkter som leses mellom hver oppdatering av nettet
     */
    void setBatchStorrelse(final Integer batchStorrelse) {
        this.batchStorrelse = batchStorrelse;
    }

    /**
     * Trener nettet basert på gitt treningsdata og korresponderende fasit.
     *
     * @param treningsdata data (aka. features)
     * @param treningsfasit fasit (aka. data sine korresponderende klasser)
     */
    void tren(final double[][] treningsdata, final int[] treningsfasit) {
        Validate.isTrue(treningsdata.length == treningsfasit.length,
                        "Kan ikke trene modell når antall treningseksempler er ulik antall fasit-klasser!");

        // INDArray er en klasse som blir brukt for lineær algebra
        final INDArray treningsMatrise = new NDArray(treningsdata);
        final INDArray shuffletFasitVektor = new NDArray(treningsfasit.length, 1);
        for (int i = 0; i < treningsfasit.length; i++) {
            shuffletFasitVektor.put(i, 0, treningsfasit[i]);
        }

        LOGG.info("Randomiserer data...");
        Nd4j.shuffle(Lists.newArrayList(treningsMatrise, shuffletFasitVektor), 1);
        final int[] shuffletFasit = new int[treningsfasit.length];
        for (int i = 0; i < shuffletFasitVektor.rows(); i++) {
            shuffletFasit[i] = shuffletFasitVektor.getInt(i, 0);
        }

        modell.setInputMiniBatchSize(batchStorrelse);

        LOGG.info("Trener...");
        modell.setListeners(new ScoreIterationListener(1));
        modell.setListeners(new Graf(10));
        for (int i = 0; i < antallEpoker; i++) {
            modell.fit(treningsMatrise, shuffletFasit);
        }
        erTrent = true;
    }

    /**
     * Brukes til å evaluere nettets ytelse basert på nye (usette!) testdata og korresponderende fasit.
     *
     * @param testdata data (aka. features)
     * @param testfasit fasit (aka. data sine korresponderende klasser)
     */
    void evaluer(final double[][] testdata, final int[] testfasit) {
        LOGG.info("Evaluerer...");
        final Evaluation eval = new Evaluation(antallKlasser);

        final INDArray prediksjoner = modell.output(new NDArray(testdata));

        final INDArray fasitSomOutputMatrise = FeatureUtil.toOutcomeMatrix(testfasit, antallKlasser);

        eval.eval(fasitSomOutputMatrise, prediksjoner);

        LOGG.info(eval.stats());
    }

    /**
     * Prediker klasse for én data-instans. Vil feile ved kall før trening.
     *
     * @param data data som array av samme lengde som modellen var trent på
     * @return predikert klasse som {@link Integer}
     */
    Integer prediker(final double[] data) {
        Validate.isTrue(data.length == modell.input().shape()[1], "Størrelse på input er ulik det modellen er trent for!");
        Validate.isTrue(erTrent, "Kan ikke predikere data før tren(data, fasit) har blitt kalt!");

        return modell.predict(new NDArray(data, new int[]{1, data.length}, 'c'))[0];
    }
}
