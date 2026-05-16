package be.deezify.json;

import com.google.gson.*;
import javafx.scene.paint.Color;

import java.lang.reflect.Type;


/**
 * Custom Json adapter for serializing and deserializing {@link Color} objects.
 * This allows JavaFX Color instances to be saved and restored in JSON format,
 * using their RGBA components.
 */

public class ColorAdapter implements JsonSerializer<Color>, JsonDeserializer<Color> {


    @Override
    public JsonElement serialize(Color color, Type type, JsonSerializationContext context) {
        JsonObject json = new JsonObject();
        json.addProperty("red", color.getRed());
        json.addProperty("green", color.getGreen());
        json.addProperty("blue", color.getBlue());
        json.addProperty("opacity", color.getOpacity());
        return json;
    }

    @Override
    public Color deserialize(JsonElement json, Type type, JsonDeserializationContext context) throws JsonParseException {
        JsonObject jsonObject = json.getAsJsonObject();
        return new Color(
                jsonObject.get("red").getAsDouble(),
                jsonObject.get("green").getAsDouble(),
                jsonObject.get("blue").getAsDouble(),
                jsonObject.get("opacity").getAsDouble()
        );
    }

}
