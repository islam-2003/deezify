package be.deezify.views.global;

import be.deezify.controllers.LanguageMenuController;
import be.deezify.views.View;
import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import lombok.Setter;

import java.util.Locale;
import java.util.ResourceBundle;

public class LanguageMenuView extends View {
    @Setter
    private LanguageMenuController languageMenuController;

    @FXML
    private ComboBox<String> languageComboBox;

    public LanguageMenuView() throws Exception {
        super("/fxml/LanguageMenu.fxml");
    }

    @FXML
    private void initialize() {
        configureLanguageSelector();
    }

    private void configureLanguageSelector() {
        languageComboBox.getItems().addAll("EN", "FR", "NL");
        languageComboBox.setValue("EN");

        languageComboBox.setOnAction(event -> {
            String selected = languageComboBox.getValue();
            Locale locale = switch (selected) {
                case "FR" -> Locale.FRENCH;
                case "NL" -> new Locale("nl");
                default -> Locale.ENGLISH;
            };

            languageMenuController.getMetaController().setLocale(locale);
        });
    }


    @Override
    protected String getTitle() {
        return "";
    }

    @Override
    public void updateLanguage() {}
}
