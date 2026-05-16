package be.deezify.json;

import be.deezify.models.Playlist;
import be.deezify.models.dto.TrackDTO;
import be.deezify.services.TrackService;
import be.deezify.services.UserService;
import com.google.gson.*;
import lombok.AllArgsConstructor;

import java.lang.reflect.Type;


/**
 * Custom Json adapter for serializing and deserializing {@link Playlist} objects.
 * Converts playlists to JSON and reconstructs them by resolving tracks via the {@link TrackService}.
 */
@AllArgsConstructor
public class PlaylistAdapter implements JsonSerializer<Playlist>, JsonDeserializer<Playlist> {

    private final TrackService trackService;
    private final UserService userService;

    @Override
    public JsonElement serialize(Playlist playlist, Type typeOfSrc, JsonSerializationContext context) {
        JsonObject object = new JsonObject();
        object.addProperty("id", playlist.getId());
        object.addProperty("name", playlist.getName());

        JsonArray tagsArray = new JsonArray();
        for (TrackDTO track : playlist.getTracks()) {
            tagsArray.add(new JsonPrimitive(track.getId()));
        }
        if (!tagsArray.isEmpty()) {
            object.add("tracks", tagsArray);
        }

        playlist.getOwner().ifPresent(owner -> object.addProperty("owner", owner.getId()));
        if (playlist.isFavorite()) {
            object.addProperty("favorite", true);
        }

        return object;
    }

    @Override
    public Playlist deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
        JsonObject object = json.getAsJsonObject();
        String name = object.get("name").getAsString();

        Playlist playlist = new Playlist(name);
        int id = object.get("id").getAsInt();
        playlist.setId(id);

        if (object.has("tracks")) {
            JsonArray tracks = object.getAsJsonArray("tracks");
            for (JsonElement element : tracks) {
                trackService.getById(element.getAsInt()).ifPresent(playlist::addTrack);
            }
        }

        if (object.has("owner")) {
            userService.getById(object.get("owner").getAsInt()).ifPresent(playlist::setOwner);
        }

        if (object.has("favorite") && object.get("favorite").getAsBoolean()) {
            playlist.setFavorite(true);
        }

        return playlist;
    }

}
