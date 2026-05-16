package be.deezify.utils;

import be.deezify.controllers.MetaController;
import javafx.scene.control.Alert;

import java.util.ResourceBundle;

public class AlertUtils {

    public static void showError(String titleI18nKey, String messageI18nKey) {
        ResourceBundle bundle = MetaController.getResourceBundle();
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(bundle.getString(titleI18nKey));
        alert.setHeaderText(null);
        alert.setContentText(bundle.getString(messageI18nKey));
        alert.showAndWait();
    }

}
