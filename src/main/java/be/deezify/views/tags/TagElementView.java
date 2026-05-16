package be.deezify.views.tags;

import be.deezify.models.dto.TagDTO;
import be.deezify.utils.ColorUtils;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Tooltip;
import javafx.scene.layout.HBox;
import javafx.scene.paint.Color;
import lombok.Getter;

import java.io.IOException;

public class TagElementView {

    @Getter
    private final HBox root;
    @FXML
    private Label tagLabel;
    @FXML
    private Button removeButton;

    private final TagDTO tag;
    private final Runnable onRemove;

    public TagElementView(TagDTO tag, Runnable onRemove) {
        this.tag = tag;
        this.onRemove = onRemove;

        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/fxml/library/TagElement.fxml"));
            fxmlLoader.setController(this);
            root = fxmlLoader.load();
            initialize();
        } catch (IOException e) {
            throw new RuntimeException("Failed to load TagElement.fxml", e);
        }
    }

    private void initialize() {
        tagLabel.setText(tag.getName());
        Tooltip tooltip = new Tooltip(tag.getDescription());
        Tooltip.install(root, tooltip);

        // Apply dynamic color styles
        Color backgroundColor = tag.getColor();
        root.setStyle("-fx-background-color: #" + ColorUtils.formatColorToHex(backgroundColor) + ";");
        tagLabel.setStyle("-fx-text-fill: #" + ColorUtils.formatColorToHex(backgroundColor.invert()) + ";");

        removeButton.setOnAction(event -> {
            if (onRemove != null) {
                onRemove.run();
            }
        });
    }

}
