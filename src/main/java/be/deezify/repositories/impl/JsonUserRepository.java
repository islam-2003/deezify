package be.deezify.repositories.impl;

import be.deezify.models.User;
import be.deezify.repositories.JsonRepository;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.reflect.Type;
import java.nio.file.Path;
import java.util.HashSet;
import java.util.Set;

/**
 * Provides persistent storage and retrieval of user accounts via a JSON file.
 * Excludes guest users from serialization, but loads them by default.
 */
public class JsonUserRepository extends JsonRepository<User> {


    public JsonUserRepository(Path jsonPath, Gson gson) throws IOException {
        super(jsonPath, gson);
    }

    @Override
    protected void readFromJson() throws IOException {
        try (FileReader reader = new FileReader(jsonPath.toFile())) {
            Type setType = new TypeToken<Set<User>>() {
            }.getType();
            Set<User> loadedUsers = gson.fromJson(reader, setType);
            if (loadedUsers != null) {
                indexables.clear();
                indexables.put(0, User.GUEST_USER);
                for (User loadedUser : loadedUsers) {
                    indexables.put(loadedUser.getId(), loadedUser);
                }
            }
        } catch (IOException e) {
            throw new IOException(e);
        }
    }

    public void writeToJson() throws IOException {
        try (FileWriter writer = new FileWriter(jsonPath.toFile())) {
            Set<User> tempUsers = new HashSet<>(indexables.values());
            tempUsers.removeIf(User::isGuest);
            gson.toJson(tempUsers, writer);
        } catch (IOException e) {
            throw new IOException(e);
        }
    }

}
