package ru.natsuru.websdr.dao.storage;

import android.content.Context;
import org.json.JSONArray;
import org.json.JSONException;
import java.io.IOException;
import java.util.List;
import ru.natsuru.websdr.model.Bucket;

public class ServersStorage extends AbstractStorage<Bucket<String>> {

    public ServersStorage(Context context) throws IOException {
        super(context);
    }

    @Override
    String getFileName() {
        return "servers.json";
    }

    @Override
    List<Bucket<String>> getConverted(JSONArray jArray) {
        return null;
    }

    @Override
    JSONArray getConverted(List<Bucket<String>> list) throws JSONException {
        return null;
    }

}
