package be.deezify.controllers;

import be.deezify.models.Tag;
import be.deezify.models.dto.TagDTO;
import be.deezify.services.TagService;
import be.deezify.views.tags.TagView;
import javafx.scene.paint.Color;
import lombok.Getter;

import java.io.IOException;


/**
 * Controller responsible for managing tags (e.g., genre, mood).
 * Handles communication between the tag view and the tag service (add, remove, edit).
 */

public class TagController extends Controller<TagView> {

    private final TagService tagService;

    @Getter
    private final MetaController metaController;

    /**
     * Constructs a TagController and initializes the tag view with all existing tags.
     *
     * @param metaController The global meta controller.
     * @param tagService     The service managing tag operations.
     * @throws IOException If the view fails to load.
     */

    public TagController(MetaController metaController, TagService tagService) throws IOException {
        super(metaController, new TagView());
        this.tagService = tagService;
        view.setTagController(this);
        this.metaController = metaController;
        view.updateTagList(tagService.getAll());
    }

    public final void addTag(String name, String description, Color color) throws IOException {
        Tag tag = Tag.builder().name(name).description(description).color(color).build();
        tagService.save(tag);
        view.addTagToList(tag);
    }

    public final boolean isTagValid(TagDTO tag) {
        return tagService.isTagValid(tag);
    }


    public final void removeTag(TagDTO tag) throws IOException {
        tagService.delete(tag);
    }

    public final void changeDescription(TagDTO tag, String description) throws IOException {
        tagService.changeDescription(tag, description);
    }

    public final void changeName(TagDTO tag, String name) throws IllegalArgumentException, IOException {
        tagService.changeName(tag, name);
    }

    public final void changeColor(TagDTO tag, Color color) throws IOException {
        tagService.changeColor(tag, color);
    }

    public void updateLanguage() {
        view.updateLanguage();
    }
}
