package be.deezify.controllers;

import be.deezify.models.User;
import be.deezify.models.dto.UserDTO;
import be.deezify.services.UserService;
import be.deezify.views.UserView;
import javafx.fxml.FXML;

import java.io.IOException;

/**
 * Controller responsible for managing users.
 * Handles user creation, deletion, switching, and updates to the associated views (library and playlist).
 */
public class UserController extends Controller<UserView> {

    private final UserService userService;

    /**
     * Constructs a UserController instance and initializes the user list in the view.
     *
     * @param metaController The global meta controller.
     * @param userService    The service managing user accounts.
     * @throws IOException If the view fails to load.
     */
    public UserController(MetaController metaController, UserService userService) throws IOException {
        super(metaController, new UserView());
        this.userService = userService;
        view.setUserController(this);
        view.updateUserList(userService.getAll().toArray(new UserDTO[0]));
    }

    @FXML
    public void createUser(String name) throws IOException {
        User user = new User(name);
        userService.addUser(user);
        view.addUserToList(user);
    }

    public void setActiveUser(UserDTO user) {
        userService.setActiveUser(user);
    }

    public final void removeUser(UserDTO user) throws IOException {
        if (user.equals(userService.getActiveUser())) {
            setActiveUser(User.GUEST_USER);
        }
        userService.delete(user);
    }

    public void logIn(UserDTO user) {
        setActiveUser(user);
    }

}