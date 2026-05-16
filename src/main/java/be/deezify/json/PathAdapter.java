package be.deezify.json;

import com.google.gson.*;

import java.io.File;
import java.lang.reflect.Type;
import java.nio.file.Path;

/**
 * Custom Json adapter for serializing and deserializing {@link Path} objects.
 * This ensures that file paths are correctly converted to and from JSON format,
 * using a consistent separator ("/") across different operating systems.
 */

public class PathAdapter implements JsonSerializer<Path>, JsonDeserializer<Path> {


    @Override
    public Path deserialize(JsonElement jsonElement, Type type, JsonDeserializationContext jsonDeserializationContext) throws JsonParseException {
        return Path.of(jsonElement.getAsString());
    }

    @Override
    public JsonElement serialize(Path path, Type type, JsonSerializationContext jsonSerializationContext) {
        String universalPath = path.toString().replace(File.separatorChar, '/');
        return new JsonPrimitive(universalPath);
    }
}
