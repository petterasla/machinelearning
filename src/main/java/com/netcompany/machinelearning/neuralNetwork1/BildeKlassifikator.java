package com.netcompany.machinelearning.neuralNetwork1;

import javafx.application.Application;
import javafx.embed.swing.SwingFXUtils;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * User: Oystein Kvamme Repp
 */
public class BildeKlassifikator extends Application {

    private ImageView myImageView;
    private Text klasse;
    private Button btnKlassifiser;
    private File valgtFil;

    @Override
    public void start(final Stage stage) {

        final GridPane gridPane = new GridPane();

        final Button btnLoad = new Button("Load");
        final Label klasseLabel = new Label("Predikert tall: ");
        btnKlassifiser = new Button("Klassifiser");
        btnLoad.setOnAction(lastOppKnappLytter);
        btnKlassifiser.setOnAction(klassifiserKnappLytter);
        btnKlassifiser.setDisable(true);
        klasse = new Text();
        klasse.setText("-");

        myImageView = new ImageView();

        gridPane.add(btnLoad, 0, 0);
        gridPane.add(btnKlassifiser, 1, 0);
        gridPane.add(myImageView, 0, 1, 2, 1);
        gridPane.add(klasseLabel, 0, 2);
        gridPane.add(klasse, 1, 2);

        final Group gruppe = new Group();
        gruppe.getChildren().add(gridPane);
        final Scene scene = new Scene(gruppe, 300, 350, Color.WHITE);

        btnLoad.setPrefWidth(150);
        btnKlassifiser.setPrefWidth(150);
        myImageView.setPreserveRatio(true);
        myImageView.setFitWidth(300);

        btnLoad.setMaxWidth(Double.MAX_VALUE);
        btnKlassifiser.setMaxWidth(Double.MAX_VALUE);

        stage.setTitle("Jaja, det er i alle fall ikke Swing...");
        stage.setScene(scene);
        stage.show();
    }

    public static void main(final String[] args) {
        launch();
    }

    private final EventHandler<ActionEvent> klassifiserKnappLytter = new EventHandler<ActionEvent>() {
        @Override
        public void handle(final ActionEvent event) {

            /*TODO Oppgave 5? Bytt ut den heller dårlige random-klassifikatoren under.
            Hint: Tren et nevralt nettverk å få modellen inn her på en eller annen måte.
            Hint: Valgt fil er tilgjengelig i valgtFil
            Hint: DataHjelper har en bildeTilIntArray-metode som sannsynligvis vil være til hjelp*/

            final Integer predikertKlasse = (int) (Math.random() * 10);
            klasse.setText(predikertKlasse.toString());
        }
    };

    private final EventHandler<ActionEvent> lastOppKnappLytter = new EventHandler<ActionEvent>() {

        @Override
        public void handle(final ActionEvent t) {
            final FileChooser filvelger = new FileChooser();

            final FileChooser.ExtensionFilter filtypeFilterPng = new FileChooser.ExtensionFilter("PNG files (*.png)",
                                                                                                 "*.PNG");
            filvelger.getExtensionFilters().add(filtypeFilterPng);

            valgtFil = filvelger.showOpenDialog(null);

            try {
                final BufferedImage bufferedImage = ImageIO.read(valgtFil);
                final Image image = SwingFXUtils.toFXImage(bufferedImage, null);
                myImageView.setImage(image);
                btnKlassifiser.setDisable(valgtFil == null);
            } catch (final IOException ex) {
                Logger.getLogger(BildeKlassifikator.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    };
}