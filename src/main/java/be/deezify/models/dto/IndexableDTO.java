package be.deezify.models.dto;

/**
 * Interface for any DTO that has an integer ID.
 * In this case, for tags, playlists and tracks.
 */
public interface IndexableDTO {

    int getId();
    default boolean isNew() {
        return getId() == 0;
    }

}
