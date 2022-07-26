package ru.natsuru.websdr.radioengine.storage;

import org.json.JSONArray;
import org.json.JSONException;
import java.io.IOException;

public interface Storage {

    void save(JSONArray saving) throws IOException;

    JSONArray load() throws JSONException, IOException;

    void createDefaultIfNotExist() throws IOException;

}
