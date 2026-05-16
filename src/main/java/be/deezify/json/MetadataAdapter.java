package be.deezify.json;

import be.deezify.models.Metadata;
import be.deezify.models.dto.TagDTO;
import be.deezify.services.TagService;
import com.google.gson.*;
import lombok.AllArgsConstructor;

import java.io.File;
import java.lang.reflect.Type;
import java.nio.file.Path;

/**
 * Adapter for serializing and deserializing Metadata objects with Gson.
 */
@AllArgsConstructor
public class MetadataAdapter implements JsonSerializer<Metadata>, JsonDeserializer<Metadata> {

    private final TagService tagService;

    /**
     * Serializes a Metadata object to a JsonElement.
     *
     * @param metadata   The metadata object.
     * @param typeOfSrc  Type of the source object.
     * @param context    Context for serialization.
     * @return Json representation of the metadata.
     */
    @Override
    public JsonElement serialize(Metadata metadata, Type typeOfSrc, JsonSerializationContext context) {
        JsonObject object = new JsonObject();
        object.addProperty("name", metadata.getName());
        object.addProperty("artist", metadata.getArtist());
        metadata.getAlbum().ifPresent(album -> object.addProperty("album", album));
        metadata.getCoverImagePath().ifPresent(path -> object.addProperty("coverImagePath", path.toString().replace(File.separatorChar, '/')));
        metadata.getLyricsPath().ifPresent(path -> object.addProperty("lyricsPath", path.toString().replace(File.separatorChar, '/')));

        JsonArray tagsArray = new JsonArray();
        for (TagDTO tag : metadata.getTags()) {
            tagsArray.add(new JsonPrimitive(tag.getId()));
        }
        if (!tagsArray.isEmpty()) {
            object.add("tags", tagsArray);
        }

        return object;
    }

    /**
     * Deserializes a JsonElement into a Metadata object.
     *
     * @param json     The JSON input.
     * @param typeOfT  The expected object type.
     * @param context  Context for deserialization.
     * @return Deserialized Metadata object.
     * @throws JsonParseException If JSON is invalid.
     */
    @Override
    public Metadata deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
        JsonObject object = json.getAsJsonObject();
        String name = object.get("name").getAsString();
        String artist = object.get("artist").getAsString();
        Metadata metadata = new Metadata(name, artist);

        if (object.has("album")) {
            metadata.setAlbum(object.get("album").getAsString());
        }

        if (object.has("coverImagePath")) {
            metadata.setCoverImagePath(Path.of(object.get("coverImagePath").getAsString()));
        }

        if (object.has("lyricsPath")) {
            metadata.setLyricsPath(Path.of(object.get("lyricsPath").getAsString()));
        }

        if (object.has("tags")) {
            JsonArray tagsArray = object.getAsJsonArray("tags");
            for (JsonElement element : tagsArray) {
                tagService.getById(element.getAsInt()).ifPresent(metadata::addTag);
            }
        }

        return metadata;
    }
}
