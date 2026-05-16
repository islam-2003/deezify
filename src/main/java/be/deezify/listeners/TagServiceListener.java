package be.deezify.listeners;

import be.deezify.models.dto.TagDTO;
import be.deezify.services.TagService;

/**
 * Listener interface for tag-related events.
 */
public interface TagServiceListener {

    default void onTagRemoved(TagDTO tag) {};

    default void onTagChanged(TagDTO tag, TagService.TagProperty tagProperty) {};

}
