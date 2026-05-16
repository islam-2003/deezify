package be.deezify.models.dto;

/**
 * Interface representing a user in the system.
 */
public interface UserDTO extends IndexableDTO {

    String getUsername();
    boolean isGuest();

}
