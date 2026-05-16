package be.deezify.controllers;

import be.deezify.views.global.LanguageMenuView;
import lombok.Getter;


/**
 * Controller for managing the language selection menu.
 * Handles interactions between the language menu view and the application's meta controller.
 */

@Getter
public class LanguageMenuController extends Controller<LanguageMenuView> {

    private final MetaController metaController;

    /**
     * Constructs a LanguageMenuController and binds it to the specified view and meta controller.
     *
     * @param metaController     The global meta controller used for managing state and language.
     * @param languageMenuView   The view representing the language menu.
     */
    public LanguageMenuController(MetaController metaController,
                                  LanguageMenuView languageMenuView) {
        super(metaController, languageMenuView);
        this.metaController = metaController;
        view.setLanguageMenuController(this);
    }
}
