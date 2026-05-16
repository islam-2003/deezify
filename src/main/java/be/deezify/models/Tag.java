package be.deezify.models;

import be.deezify.json.ColorAdapter;
import be.deezify.models.dto.TagDTO;
import com.google.gson.annotations.JsonAdapter;
import javafx.scene.paint.Color;
import lombok.*;

import java.util.HashSet;
import java.util.Set;

/**
 * Model class representing a tag that can be applied to tracks.
 */
@With
@Builder
@Getter
@Setter
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@ToString
public class Tag implements TagDTO, Indexable {

    @Getter
    private static final Set<Tag> internalTags = new HashSet<>();

    // Tag properties
    private int id;
    private String name;
    private String description;
    @JsonAdapter(ColorAdapter.class)
    private Color color;
    private transient final boolean internal;

    // Predefined tags
    public static final Tag OST = new Tag(-1, "OST", "Original Soundtrack of a movie, game", Color.BLUE, true);
    public static final Tag SUMMER = new Tag(-2, "SUMMER", "Summer vibes", Color.YELLOW, true);
    public static final Tag BLUE = new Tag(-3, "BLUE", "Reminds Blue Color", Color.BLUE, true);
    public static final Tag BEIGE = new Tag(-4, "BEIGE", "Reminds Beige Color", Color.BEIGE, true);
    public static final Tag FIREBRICK = new Tag(-5, "FIREBRICK", "Reminds Firebrick Color", Color.FIREBRICK, true);
    public static final Tag CYAN = new Tag(-6, "CYAN", "Reminds Cyan Color", Color.CYAN, true);
    public static final Tag ORANGE = new Tag(-7, "ORANGE", "Reminds Orange Color", Color.ORANGE, true);

    // Static block to initialize internalTags
    static {
        internalTags.add(OST);
        internalTags.add(SUMMER);
        internalTags.add(BLUE);
        internalTags.add(BEIGE);
        internalTags.add(FIREBRICK);
        internalTags.add(CYAN);
        internalTags.add(ORANGE);
    }

    /**
     * Public constructor for user-defined tags.
     *
     * @param name        Tag name.
     * @param description Tag description.
     * @param color       Tag display color.
     */
    public Tag(String name, String description, Color color) {
        this(0, name, description, color, false);
    }
}
