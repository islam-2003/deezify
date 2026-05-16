package be.deezify.views;

import be.deezify.controllers.UserController;
import be.deezify.models.User;
import be.deezify.models.dto.UserDTO;
import be.deezify.utils.AlertUtils;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import lombok.Setter;

import java.io.IOException;
import java.util.Objects;

/**
 * View responsible for managing users in the Deezify application.
 * Provides functionality to list users, create new ones, and log out/delete existing users.
 */
public class UserView extends View {

    @FXML
    private ListView<UserDTO> userListView;
    @FXML
    protected Label trackLabel;
    @Setter
    private UserController userController;

    public UserView() throws IOException {
        super("/fxml/user/User.fxml");
        initialize();
    }

    @Override
    protected String getTitle() {
        return "User";
    }

    /**
     * Initializes the view's cell factory for rendering user list entries.
     */
    private void initialize() {
        userListView.setCellFactory(userListView -> new UserListCell());
    }

    /**
     * Updates the user list view with the provided users.
     *
     * @param users one or more UserDTO objects to display
     */
    public void updateUserList(UserDTO... users) {
        clearUserList();
        addUserToList(users);
    }

    /**
     * Clears all users from the list view.
     */
    public void clearUserList() {
        userListView.getItems().clear();
    }

    /**
     * Adds one or more users to the list view.
     *
     * @param users one or more UserDTO objects
     */
    public void addUserToList(UserDTO... users) {
        userListView.getItems().addAll(users);
    }


    /**
     * Displays a dialog to input a username and attempts to create a new user.
     * If the input is invalid or creation fails, an error alert is shown.
     */
    @FXML
    public void addUser() {
        showUserCreationDialog();
    }

    @Override
    public void updateLanguage() {
        // TODO Implement this here
    }

    /**
     * Custom ListCell for rendering UserDTO items in the ListView.
     * Includes username button, logout, and delete buttons depending on whether the user is a guest.
     */
    private class UserListCell extends ListCell<UserDTO> {

        protected HBox hBox;
        @FXML
        protected TextField userNameField;
        @FXML
        protected Button deleteButton;
        @FXML
        protected Button logoutButton;
        @FXML
        protected Button loginButton;

        public UserListCell() {
            try {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/user/UserListCell.fxml"));
                loader.setController(this);
                hBox = loader.load();
            } catch (IOException e) {
                AlertUtils.showError("error.title.generic", "error.text.generic");
            }
        }


        /**
         * Updates the cell's contents with the given user data.
         *
         * @param user  the user to display
         * @param empty whether the cell is empty
         */
        @Override
        protected void updateItem(UserDTO user, boolean empty) {
            super.updateItem(user, empty);

            if (empty || user == null) {
                setGraphic(null);
            } else {
                Button userButton = new Button(user.getUsername());
                HBox hBox = new HBox();
                hBox.getChildren().add(userButton);
                userButton.setOnAction(actionEvent -> {
                        userController.logIn(user);
                });


                // Si l'utilisateur n'est pas "Guest", ajouter le bouton de suppression
                if (!user.isGuest()) {

                    logoutButton.setOnAction(event -> {
                        userController.logIn(User.GUEST_USER);
                    });


                    deleteButton.setOnAction(actionEvent -> {
                        if (userController != null) {
                            Dialog<ButtonType> dialog = new Dialog<>();
                            dialog.setTitle(String.format("Type 'DELETE' to remove '%s'.", user.getUsername()));

                            TextField nameField = new TextField();

                            GridPane grid = new GridPane();
                            grid.setVgap(10);
                            grid.setHgap(10);
                            grid.add(new Label(""), 0, 0);
                            grid.add(nameField, 1, 0);

                            dialog.getDialogPane().setContent(grid);
                            dialog.getDialogPane().getButtonTypes().addAll(ButtonType.OK, ButtonType.CANCEL);

                            dialog.setResultConverter(buttonType -> buttonType);

                            dialog.showAndWait().ifPresent(buttonType -> {
                                if (buttonType == ButtonType.OK) {
                                    if (Objects.equals(nameField.getText(), "DELETE")) {
                                        try {
                                            userController.removeUser(user);
                                        } catch (IOException e) {
                                            AlertUtils.showError("error.title.generic", "error.text.generic");
                                        }
                                        userListView.getItems().remove(user);
                                    } else {
                                        Alert alert = new Alert(Alert.AlertType.ERROR);
                                        alert.setTitle("Error");
                                        alert.setHeaderText("No deletion performed.");
                                        alert.showAndWait();
                                    }
                                }
                            });
                        }
                    });
                    hBox.getChildren().addAll(logoutButton, deleteButton);
                }
                setGraphic(hBox);
            }
        }
    }

    private void showUserCreationDialog() {
        Dialog<ButtonType> dialog = new Dialog<>();
        dialog.setTitle("Register a new user");


        TextField nameField = new TextField();

        GridPane grid = new GridPane();
        grid.setVgap(10);
        grid.setHgap(10);
        grid.add(new Label("Name:"), 0, 0);
        grid.add(nameField, 1, 0);

        dialog.getDialogPane().setContent(grid);
        dialog.getDialogPane().getButtonTypes().addAll(ButtonType.OK, ButtonType.CANCEL);

        dialog.setResultConverter(buttonType -> buttonType);

        dialog.showAndWait().ifPresent(buttonType -> {
            if (buttonType == ButtonType.OK) {
                try {
                    userController.createUser(nameField.getText());
                } catch (IllegalArgumentException e) {
                    Alert alert = new Alert(Alert.AlertType.ERROR);
                    alert.setTitle("Error");
                    alert.setHeaderText("User creation failed");
                    alert.setContentText(e.getMessage());
                    alert.showAndWait();
                    showUserCreationDialog();
                } catch (IOException e) {
                    AlertUtils.showError("error.title.generic", "error.text.generic");
                }
            }
        });
    }
}
