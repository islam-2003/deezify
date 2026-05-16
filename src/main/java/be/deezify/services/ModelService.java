package be.deezify.services;

import be.deezify.exceptions.ForbiddenActionException;
import be.deezify.models.Indexable;
import be.deezify.models.dto.IndexableDTO;
import be.deezify.models.dto.UserDTO;
import be.deezify.repositories.Repository;
import lombok.AllArgsConstructor;

import java.io.IOException;
import java.util.Optional;
import java.util.Set;

@AllArgsConstructor
public abstract class ModelService<L, D extends IndexableDTO, M extends Indexable> extends Service<L> {

    protected final Repository<M> repository;

    public abstract Set<D> getAll();
    public abstract Set<D> getAllForUser(UserDTO user);
    public abstract Optional<D> getById(int id);
    public abstract void delete(D dto) throws IOException, ForbiddenActionException;

}
