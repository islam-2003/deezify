package be.deezify.services;

import be.deezify.listeners.UserServiceListener;
import be.deezify.models.User;
import be.deezify.models.dto.UserDTO;
import be.deezify.repositories.Repository;
import lombok.Getter;

import java.io.IOException;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

/**
 * Service for managing user operations such as creation, deletion, and updates.
 * Notifies listeners on relevant user changes.
 */
public class UserService extends ModelService<UserServiceListener, UserDTO, User> {

    @Getter
    private User activeUser = User.GUEST_USER;

    public UserService(Repository<User> repository) {
        super(repository);
    }

    @Override
    public Set<UserDTO> getAll() {
        return new HashSet<>(repository.findAll());
    }

    @Override
    public Set<UserDTO> getAllForUser(UserDTO user) {
        return getAll();
    }

    @Override
    public Optional<UserDTO> getById(int id) {
        return repository.findById(id).map(track -> track);
    }

    /**
     * Adds a new user if it passes validation.
     *
     * @param user The user to add.
     * @throws IllegalArgumentException if the username is blank or already exists.
     */
    public void addUser(User user) throws IllegalArgumentException, IOException {
        if (user.getUsername().isBlank()) {
            throw new IllegalArgumentException("Username cannot be blank.");
        }
        if (!isNameValid(user.getUsername())) {
            throw new IllegalArgumentException("Username already exists.");
        }

        repository.save(user);
        notifyUserAdded(user);
    }

    public void setActiveUser(UserDTO activeUser) {
        repository.findById(activeUser.getId()).ifPresent(user -> {
            this.activeUser = user;
            notifyActiveUserChanged(activeUser);
        });
    }

    public void delete(UserDTO user) throws IOException {
        Optional<User> userOptional = repository.findById(user.getId());
        if (userOptional.isPresent()) {
            repository.delete(userOptional.get());
            notifyUserRemoved(user);
        }
    }

    public final void changeUserName(UserDTO user, String name) throws IllegalArgumentException, IOException {

        if (name.isBlank() || !isNameValid(name)) {
            throw new IllegalArgumentException("Username cannot be blank or an already existing name.");
        }

        Optional<User> userOptional = repository.findById(user.getId());
        if (userOptional.isPresent()) {
            userOptional.get().setUsername(name);
            repository.delete(userOptional.get());
            notifyUserNameChanged(user);
        }
    }

    public void notifyUserRemoved(UserDTO user) {
        for (UserServiceListener listener : listeners) {
            listener.onUserRemoved(user);
        }
    }

    public void notifyUserNameChanged(UserDTO user) {
        for (UserServiceListener listener : listeners) {
            listener.onUserNameChanged(user);
        }
    }

    public void notifyActiveUserChanged(UserDTO user) {
        for (UserServiceListener listener : listeners) {
            listener.onActiveUserChanged(user);
        }
    }

    public void notifyUserAdded(UserDTO user) {
        for (UserServiceListener listener : listeners) {
            listener.onUserAdded(user);
        }
    }

    private boolean isNameValid(String name) {
        return repository.findAll().stream().noneMatch(user -> user.getUsername().equals(name));
    }
}