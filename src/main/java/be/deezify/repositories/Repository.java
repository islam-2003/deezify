package be.deezify.repositories;

import be.deezify.models.Indexable;
import lombok.NonNull;

import java.io.IOException;
import java.util.*;

/**
 * A Generic In-Memory Repository Class
 * @param <T>
 */
public class Repository<T extends Indexable> {

    protected int nextId = 1;
    protected final Map<Integer, T> indexables = new HashMap<>();

    public Set<T> findAll() {
        return new HashSet<>(indexables.values());
    }

    public Optional<T> findById(int id) {
        return Optional.ofNullable(indexables.get(id));
    }

    public void save(@NonNull T object) throws IOException {
        if (object.isNew()) {
            object.setId(nextId++);
        }
        indexables.put(object.getId(), object);
    }

    public void delete(@NonNull T object) throws IOException {
        indexables.remove(object.getId());
    }

}
