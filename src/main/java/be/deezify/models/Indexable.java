package be.deezify.models;

import be.deezify.models.dto.IndexableDTO;

/**
 * Extension of IndexableDTO allowing the ID to be set.
 */
public interface Indexable extends IndexableDTO {

    void setId(int id);

}
