package be.deezify.listeners;

import be.deezify.models.dto.UserDTO;

/**
 * Listener interface for reacting to user-related events.
 */
public interface UserServiceListener {

    default void onUserRemoved(UserDTO user) {};
    default void onUserNameChanged(UserDTO user) {};
    default void onUserAdded(UserDTO user) {};
    default void onActiveUserChanged(UserDTO user) {};

}