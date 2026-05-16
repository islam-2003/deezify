package be.deezify.services;

import be.deezify.listeners.TagServiceListener;
import be.deezify.models.Tag;
import be.deezify.models.dto.TagDTO;
import be.deezify.models.dto.UserDTO;
import be.deezify.repositories.Repository;
import javafx.scene.paint.Color;

import java.io.IOException;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

/**
 * Service for managing tags and their attributes (name, color, description).
 * Notifies listeners on changes.
 */
public class TagService extends ModelService<TagServiceListener, TagDTO, Tag> {

    public TagService(Repository<Tag> repository) {
        super(repository);
    }

    @Override
    public Set<TagDTO> getAll() {
        return new HashSet<>(repository.findAll());
    }

    @Override
    public Set<TagDTO> getAllForUser(UserDTO user) {
        return getAll();
    }

    @Override
    public Optional<TagDTO> getById(int id) {
        return repository.findById(id).map(tag -> tag);
    }

    public void save(Tag tag) throws IllegalArgumentException, IOException {

        if (!isNameValid(tag.getName())) {
            throw new IllegalArgumentException("Tag with name '" + tag.getName() + "' already exists.");
        }

        repository.save(tag);
    }

    @Deprecated
    // TODO à refaire
    public boolean isTagValid(TagDTO newTag) {
        return getAll().stream().noneMatch(existingTag ->
                existingTag.getName().equalsIgnoreCase(newTag.getName()) ||
                        existingTag.getDescription().equalsIgnoreCase(newTag.getDescription()));
    }


    @Override
    public void delete(TagDTO tag) throws IOException {
        Optional<Tag> tagOptional = repository.findById(tag.getId());
        if (tagOptional.isPresent()) {
            repository.delete(tagOptional.get());
            notifyTagRemoved(tag);
        }
    }

    public final void changeDescription(TagDTO tag, String description) throws IOException {
        Optional<Tag> tagOptional = repository.findById(tag.getId());
        if (tagOptional.isPresent()) {
            tagOptional.get().setDescription(description);
            repository.save(tagOptional.get());
            notifyTagChanged(tag, TagProperty.DESCRIPTION);
        }
    }

    public final void changeName(TagDTO tag, String name) throws IllegalArgumentException, IOException {

        if (!isNameValid(name)) {
            throw new IllegalArgumentException("Tag with name '" + name + "' already exists.");
        }

        Optional<Tag> tagOptional = repository.findById(tag.getId());
        if (tagOptional.isPresent()) {
            tagOptional.get().setName(name);
            repository.save(tagOptional.get());
            notifyTagChanged(tag, TagProperty.NAME);
        }
    }

    public final void changeColor(TagDTO tag, Color color) throws IOException {
        Optional<Tag> tagOptional = repository.findById(tag.getId());
        if (tagOptional.isPresent()) {
            tagOptional.get().setColor(color);
            repository.save(tagOptional.get());
            notifyTagChanged(tag, TagProperty.COLOR);
        }
    }

    public void notifyTagRemoved(TagDTO tag) {
        for (TagServiceListener listener : listeners) {
            listener.onTagRemoved(tag);
        }
    }

    public void notifyTagChanged(TagDTO tag, TagProperty tagProperty) {
        for (TagServiceListener listener : listeners) {
            listener.onTagChanged(tag, tagProperty);
        }
    }

    /**
     * Enum representing which tag property has changed.
     */
    public enum TagProperty {
        NAME,
        DESCRIPTION,
        COLOR
    }

    private boolean isNameValid(String name) {
        return repository.findAll().stream().noneMatch(tag -> tag.getName().equals(name));
    }
}
