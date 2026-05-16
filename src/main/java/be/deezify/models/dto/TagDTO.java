package be.deezify.models.dto;

import javafx.scene.paint.Color;

/**
 * Interface representing a tag that can be associated with tracks.
 */
public interface TagDTO extends IndexableDTO {

    String getName();
    String getDescription();
    Color getColor();
    boolean isInternal();

}
