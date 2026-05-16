package be.deezify.repositories.impl;

import be.deezify.models.Tag;
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
 * Loads and persists tag data from/to a JSON file.
 * Internal tags are preserved separately and excluded from saving.
 */
public class JsonTagRepository extends JsonRepository<Tag> {


    public JsonTagRepository(Path jsonPath, Gson gson) throws IOException {
        super(jsonPath, gson);
    }

    @Override
    protected void readFromJson() throws IOException {
        try (FileReader reader = new FileReader(jsonPath.toFile())) {
            Type setType = new TypeToken<Set<Tag>>() {}.getType();
            Set<Tag> loadedTags = gson.fromJson(reader, setType);
            if (loadedTags != null) {
                indexables.clear();
                for (Tag tag : loadedTags) {
                    indexables.put(tag.getId(), tag);
                    if (tag.getId() >= nextId) {
                        nextId = tag.getId() + 1;
                    }
                }
                for (Tag tag : Tag.getInternalTags()) {
                    indexables.put(tag.getId(), tag);
                }
            }
        } catch (IOException e) {
            throw new IOException(e.getMessage());
        }
    }

    @Override
    protected void writeToJson() throws IOException {
        try (FileWriter writer = new FileWriter(jsonPath.toFile())) {
            Set<Tag> tempTags = new HashSet<>(indexables.values());
            tempTags.removeIf(Tag::isInternal);
            gson.toJson(tempTags, writer);
        } catch (IOException e) {
            throw new IOException(e.getMessage());
        }
    }
}
