package be.deezify.controllers;

import be.deezify.listeners.PlaybackServiceListener;
import be.deezify.views.VisualizerView;

/**
 * Controller responsible for managing the visualizer.
 * Listens to real-time audio data (amplitude and frequency) and updates the visualizer view accordingly.
 */
public class VisualizerController extends Controller<VisualizerView> implements PlaybackServiceListener {

    /**
     * Constructs the VisualizerController and links it to the visualizer view.
     *
     * @param metaController The global meta controller.
     * @throws Exception If the view fails to initialize.
     */
    public VisualizerController(MetaController metaController) throws Exception {
        super(metaController, new VisualizerView());
    }

    @Override
    public void onAmplitudeUpdate(double amplitude) {
        view.onAmplitudeUpdate(amplitude);
    }

    @Override
    public void onFrequencyUpdate(double frequency) {
        view.onFrequencyUpdate(frequency);
    }
}
