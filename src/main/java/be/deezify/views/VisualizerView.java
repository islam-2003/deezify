package be.deezify.views;

import javafx.animation.AnimationTimer;
import javafx.fxml.FXML;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Color;

public class VisualizerView extends View {

    private final Canvas canvas;
    private double amplitude = 0;
    private double displayedAmplitude = 0;

    @FXML
    private Label visualizerLabel;
    @FXML
    private AnchorPane anchorPane;

    public VisualizerView() throws Exception {
        super("/fxml/Visualizer.fxml");

        this.canvas = new Canvas();
        anchorPane.getChildren().add(canvas);

        // Anchor the Canvas so it takes the whole place
        AnchorPane.setTopAnchor(canvas, 0.0);
        AnchorPane.setBottomAnchor(canvas, 0.0);
        AnchorPane.setLeftAnchor(canvas, 0.0);
        AnchorPane.setRightAnchor(canvas, 0.0);

        // Adjust the size dynamically
        canvas.widthProperty().bind(anchorPane.widthProperty());
        canvas.heightProperty().bind(anchorPane.heightProperty());

        AnimationTimer timer = new AnimationTimer() {
            @Override
            public void handle(long now) {
                double smoothingFactor = 0.7;
                displayedAmplitude += (amplitude - displayedAmplitude) * smoothingFactor;
                draw();
            }
        };
        timer.start();
    }


    private void draw() {
        GraphicsContext gc = canvas.getGraphicsContext2D();

        double width = canvas.getWidth();
        double height = canvas.getHeight();

        double centerX = width / 2;
        double centerY = height / 2;

        double baseRadius = Math.min(width, height) / 7; // smaller to leave space for spikes

        gc.clearRect(0, 0, width, height);

        gc.setStroke(Color.CYAN);
        gc.setLineWidth(1.5);

        int numSpikes = 30;
        double angleStep = 360.0 / numSpikes;

        double spikeLength = baseRadius + displayedAmplitude * 75; // *MUCH smaller base, but still multiplied!

        for (int i = 0; i < numSpikes; i++) {

            double angle = Math.toRadians(i * angleStep);

            double innerX = centerX + baseRadius * Math.cos(angle);
            double innerY = centerY + baseRadius * Math.sin(angle);

            double outerX = centerX + (baseRadius + spikeLength) * Math.cos(angle);
            double outerY = centerY + (baseRadius + spikeLength) * Math.sin(angle);

            gc.strokeLine(innerX, innerY, outerX, outerY);
        }
    }

    public void onAmplitudeUpdate(double amplitude) {
        this.amplitude = amplitude;
    }

    public void onFrequencyUpdate(double frequency) {
        // not yet used
    }

    /**
     * Returns the title of the view.
     *
     * @return The title of the view.
     */
    @Override
    protected String getTitle() {
        return "Visualizer";
    }

    @Override
    public void updateLanguage() {

    }
}
