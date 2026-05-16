package be.deezify.repositories.impl;

import be.deezify.models.Track;
import be.deezify.repositories.JsonRepository;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.reflect.Type;
import java.nio.file.Path;
import java.util.List;

/**
 * Provides persistent storage of tracks and their metadata using a JSON file.
 */
public class JsonTrackRepository extends JsonRepository<Track> {

    public JsonTrackRepository(Path jsonPath, Gson gson) throws IOException {
        super(jsonPath, gson);
    }

    @Override
    protected void readFromJson() throws IOException {
        try (FileReader reader = new FileReader(jsonPath.toFile())) {
            Type listType = new TypeToken<List<Track>>() {
            }.getType();
            List<Track> loadedTracks = gson.fromJson(reader, listType);
            if (loadedTracks != null) {
                indexables.clear();
                for (Track track : loadedTracks) {
                    indexables.put(track.getId(), track);
                    if (track.getId() >= nextId) {
                        nextId = track.getId() + 1;
                    }
                }
            }
        } catch (IOException e) {
            throw new IOException(e);
        }
    }

    @Override
    protected void writeToJson() throws IOException {
        try (FileWriter writer = new FileWriter(jsonPath.toFile())) {
            gson.toJson(indexables.values(), writer);
        } catch (IOException e) {
            throw new IOException("Error writing to JSON file: " + e.getMessage());
        }
    }

}
