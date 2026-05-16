package be.deezify.repositories;

import be.deezify.models.Indexable;
import com.google.gson.Gson;
import lombok.NonNull;

import java.io.IOException;
import java.nio.file.Path;

public abstract class JsonRepository<T extends Indexable> extends Repository<T> {

    protected Path jsonPath;
    protected Gson gson;

    public JsonRepository(Path jsonPath, Gson gson) throws IOException {
        this.jsonPath = jsonPath;
        this.gson = gson;
        this.readFromJson();
    }

    protected abstract void writeToJson() throws IOException;

    protected abstract void readFromJson() throws IOException;

    @Override
    public void save(@NonNull T object) throws IOException {
        super.save(object);
        writeToJson();
    }

    @Override
    public void delete(@NonNull T object) throws IOException {
        super.delete(object);
        writeToJson();
    }
}
