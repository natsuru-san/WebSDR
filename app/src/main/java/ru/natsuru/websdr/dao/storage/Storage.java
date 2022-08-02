package ru.natsuru.websdr.dao.storage;

import org.json.JSONException;
import java.io.IOException;
import java.util.List;

public interface Storage<T> {

    void save(List<T> saving) throws IOException, JSONException;

    List<T> load() throws JSONException, IOException;

    void createDefaultIfNotExist() throws IOException;

}
