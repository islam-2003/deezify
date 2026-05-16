package be.deezify.views.tags;

import be.deezify.controllers.MetaController;
import be.deezify.controllers.TagController;
import be.deezify.models.dto.TagDTO;
import be.deezify.utils.AlertUtils;
import be.deezify.utils.ColorUtils;
import be.deezify.views.View;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.paint.Color;
import lombok.Setter;

import java.io.IOException;
import java.util.Collection;
import java.util.ResourceBundle;

public class TagView extends View {

    @FXML
    private Label playQueueLabel;
    @FXML
    private Button clearButton;
    @FXML
    private Button addTagButton;
    @FXML
    private ListView<TagDTO> tagListView;
    @Setter
    private TagController tagController;

    private ResourceBundle bundle;

    /**
     * Constructeur de la classe TagView.
     *
     * @throws IOException Si une erreur survient lors du chargement du fichier FXML.
     */
    public TagView() throws IOException {
        super("/fxml/tag/Tag.fxml");
        initialize();
    }

    @Override
    protected String getTitle() {
        return "Tag";
    }

    private void initialize() {
        tagListView.setCellFactory(tagListView -> new TagListCell());
        addTagButton.setOnAction(event -> addTag());
    }

    public void updateTagList(Collection<TagDTO> tags) {
        clearTagList();
        addTagToList(tags);
    }

    public void clearTagList() {
        tagListView.getItems().clear();
    }

    private void addTagToList(Collection<TagDTO> tags) {
        tagListView.getItems().addAll(tags);
    }

    private class TagListCell extends ListCell<TagDTO> {

        protected HBox hBox;
        @FXML
        protected TextField tagNameLabel;
        @FXML
        protected TextField tagDescriptionLabel;
        @FXML
        protected Button deleteButton;
        @FXML
        protected ColorPicker colorPicker;

        public TagListCell() {
            try {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/tag/TagListCell.fxml"));
                loader.setController(this);
                hBox = loader.load();
            } catch (IOException e) {
                AlertUtils.showError("error.title.generic", "error.text.generic");
            }
        }

        @Override
        protected void updateItem(TagDTO tag, boolean empty) {
            super.updateItem(tag, empty);

            if (empty || tag == null) {
                setGraphic(null);
            } else {
                tagNameLabel.setText(tag.getName());
                tagDescriptionLabel.setText(tag.getDescription());

                if (tag.isInternal()) {
                    tagNameLabel.setEditable(false);
                    tagDescriptionLabel.setEditable(false);
                    colorPicker.setDisable(true);
                    deleteButton.setStyle("-fx-background-color: #" + ColorUtils.formatColorToHex(Color.GRAY) + ";");
                }

                tagNameLabel.textProperty().addListener((observable, oldValue, newValue) -> {
                    if (oldValue.equals(newValue)) {
                        return;
                    }

                    try {
                        if (!getItem().getName().equals(newValue)) {
                            tagController.changeName(getItem(), newValue);
                        }
                        tagNameLabel.getStyleClass().remove("text-field-error");
                        tagNameLabel.setTooltip(null);
                    } catch (IllegalArgumentException e) {
                        if (!tagNameLabel.getStyleClass().contains("text-field-error")) {
                            tagNameLabel.getStyleClass().add("text-field-error"); // Add error styling
                        }

                        Tooltip tooltip = new Tooltip(e.getMessage());
                        tagNameLabel.setTooltip(tooltip);
                    } catch (IOException e) {
                        AlertUtils.showError("error.title.generic", "error.text.generic");
                    }

                });

                tagDescriptionLabel.textProperty().addListener((observable, oldValue, newValue) -> {
                    if (!oldValue.equals(newValue) && tagController != null) {
                        try {
                            tagController.changeDescription(getItem(), newValue);
                        } catch (IOException e) {
                            throw new RuntimeException(e);
                        }
                    }
                });

                colorPicker.setValue(tag.getColor());
                colorPicker.setOnAction(actionEvent -> {
                    if (tagController != null && !colorPicker.getValue().equals(tag.getColor())) {
                        try {
                            tagController.changeColor(getItem(), colorPicker.getValue());
                        } catch (IOException e) {
                            AlertUtils.showError("error.title.generic", "error.text.generic");
                        }
                    }
                });

                deleteButton.setOnAction(actionEvent -> {
                    if (tagController != null && !tag.isInternal()) {
                        try {
                            tagController.removeTag(tag);
                            tagListView.getItems().remove(tag);
                        } catch (IOException e) {
                            AlertUtils.showError("error.title.generic", "error.text.generic");
                        }
                    }
                });

                setGraphic(hBox);
            }
        }
    }

    @FXML
    private void addTag() {
        // Fenêtre avec 3 champs : nom, description, couleur
        Dialog<TagDTO> dialog = new Dialog<>();
        showTagCreationDialog();
    }

    // TODO Should be moved to FXML for a lot
    private void showTagCreationDialog() {
        Dialog<TagDTO> dialog = new Dialog<>();
        dialog.setTitle(bundle.getString("tag.dialog.title"));

        TextField nameField = new TextField();
        TextField descriptionField = new TextField();
        ColorPicker colorPicker = new ColorPicker();

        GridPane grid = new GridPane();
        grid.setVgap(10);
        grid.setHgap(10);
        grid.add(new Label(bundle.getString("name.prompt") + " : "), 0, 0);
        grid.add(nameField, 1, 0);
        grid.add(new Label(bundle.getString("tag.description") + " : "), 0, 1);
        grid.add(descriptionField, 1, 1);
        grid.add(new Label(bundle.getString("tag.color") + " : "), 0, 2);
        grid.add(colorPicker, 1, 2);

        dialog.getDialogPane().setContent(grid);
        dialog.getDialogPane().getButtonTypes().addAll(ButtonType.OK, ButtonType.CANCEL);

        Button okButton = (Button) dialog.getDialogPane().lookupButton(ButtonType.OK);
        Button cancelButton = (Button) dialog.getDialogPane().lookupButton(ButtonType.CANCEL);

        okButton.setText(bundle.getString("confirm.button"));
        cancelButton.setText(bundle.getString("cancel.button"));

        dialog.setResultConverter(dialogButton -> {
            if (dialogButton == ButtonType.OK) {
                try {
                    tagController.addTag(nameField.getText(), descriptionField.getText(), colorPicker.getValue());
                } catch (IOException e) {
                    AlertUtils.showError("error.title.generic", "error.text.generic");
                }
            }
            return null;
        });

        dialog.showAndWait().ifPresent(tag -> {
            if (!tagController.isTagValid(tag)) {
                // Affiche un message d’erreur si invalide
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle(bundle.getString("tag.error"));
                alert.setHeaderText(bundle.getString("tag.invalid"));
                alert.setContentText(bundle.getString("tag.error.description"));
                alert.showAndWait();

                // Relance la pop-up pour corriger
                showTagCreationDialog();
            } else {
                // Si tout est bon, ajoute le tag
                try {
                    tagController.addTag(tag.getName(), tag.getDescription(), tag.getColor());
                } catch (IOException e) {
                    AlertUtils.showError("error.title.generic", "error.text.generic");
                }
            }
        });
    }


    public void addTagToList(TagDTO tag) {
        tagListView.getItems().add(tag);
    }

    private void configureUITexts() {
        addTagButton.setText(bundle.getString("tag.button"));
    }

    public void updateLanguage() {
        this.bundle = MetaController.getResourceBundle();
        configureUITexts();
    }
}
