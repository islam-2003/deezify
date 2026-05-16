package be.deezify.repositories.impl;

import be.deezify.models.Playlist;
import be.deezify.repositories.JsonRepository;
import be.deezify.utils.AlertUtils;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.reflect.Type;
import java.nio.file.Path;
import java.util.List;

/**
 * Automatically loads and saves playlist data to a JSON file,
 * providing persistent storage between application runs.
 */

public class JsonPlaylistRepository extends JsonRepository<Playlist> {

    public JsonPlaylistRepository(Path jsonPath, Gson gson) throws IOException {
        super(jsonPath, gson);
    }

    @Override
    protected void readFromJson() throws IOException {
        try (FileReader reader = new FileReader(jsonPath.toFile())) {
            Type listType = new TypeToken<List<Playlist>>() {}.getType();
            List<Playlist> loadedPlaylist = gson.fromJson(reader, listType);
            if (loadedPlaylist != null && !loadedPlaylist.isEmpty()) {

                indexables.clear();
                for (Playlist playlist : loadedPlaylist) {
                    playlist.setId(nextId++);
                    indexables.put(playlist.getId(), playlist);
                }
            } else {
                Playlist favPlaylist = new Playlist("Favoris");
                favPlaylist.setId(1);
                indexables.put(1, favPlaylist);
                save(favPlaylist);
            }
        } catch (IOException e) {
            throw new IOException(e.getMessage());
        }
    }

    @Override
    protected void writeToJson() {
        try (FileWriter writer = new FileWriter(jsonPath.toFile())) {
            gson.toJson(indexables.values(), writer);
        } catch (IOException e) {
            AlertUtils.showError("error.title.generic", "error.text.generic");
        }
    }
}
