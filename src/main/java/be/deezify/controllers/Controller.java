package be.deezify.controllers;

import be.deezify.views.View;
import lombok.Getter;

/**
 * Abstract base class for all controllers.
 * A controller is responsible for managing interactions between the model and the view.
 *
 * @param <T> The type of view associated with this controller.
 */
abstract class Controller<T extends View> {
    /**
     * Reference to the MetaController for navigation between scenes.
     */
    public final MetaController metaController;
    @Getter
    protected final T view;

    /**
     * Constructor for the Controller class.
     *
     * @param metaController The associated MetaController.
     * @param view           The view associated with this controller.
     */
    public Controller(MetaController metaController, T view) {
        this.metaController = metaController;
        this.view = view;
    }

}