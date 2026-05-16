package be.deezify.json;

import be.deezify.models.Metadata;
import be.deezify.models.Track;
import be.deezify.services.UserService;
import com.google.gson.*;
import lombok.AllArgsConstructor;

import java.lang.reflect.Type;
import java.nio.file.Path;

@AllArgsConstructor
public class TrackAdapter implements JsonSerializer<Track>, JsonDeserializer<Track> {

    private final MetadataAdapter metadataAdapter;
    private final PathAdapter pathAdapter;
    private final UserService userService;

    @Override
    public Track deserialize(JsonElement jsonElement, Type type, JsonDeserializationContext context) throws JsonParseException {
        JsonObject jsonObj = jsonElement.getAsJsonObject();

        // Deserialize filePath using PathAdapter
        JsonElement filePathElement = jsonObj.get("filePath");
        Path filePath = pathAdapter.deserialize(filePathElement, Path.class, context);

        // Deserialize metadata using MetadataAdapter
        JsonElement metadataElement = jsonObj.get("metadata");
        Metadata metadata = metadataAdapter.deserialize(metadataElement, Metadata.class, context);

        // Construct the Track object
        Track track = new Track(filePath, metadata);

        track.setId(jsonObj.get("id").getAsInt());

        // Deserialize owner if present
        if (jsonObj.has("owner") && !jsonObj.get("owner").isJsonNull()) {
            int ownerId = jsonObj.get("owner").getAsInt();
            userService.getById(ownerId).ifPresent(track::setOwner);
        }

        return track;
    }

    @Override
    public JsonElement serialize(Track track, Type type, JsonSerializationContext context) {
        JsonObject jsonObj = new JsonObject();

        // Serialize filePath
        JsonElement filePathElement = pathAdapter.serialize(track.getFilePath(), Path.class, context);
        jsonObj.add("filePath", filePathElement);

        // Serialize metadata
        JsonElement metadataElement = metadataAdapter.serialize(track.getMetadata(), Metadata.class, context);
        jsonObj.add("metadata", metadataElement);

        // Serialize ID
        jsonObj.addProperty("id", track.getId());

        // Serialize owner ID if present
        track.getOwner().ifPresent(owner -> jsonObj.addProperty("owner", owner.getId()));

        return jsonObj;
    }
}
