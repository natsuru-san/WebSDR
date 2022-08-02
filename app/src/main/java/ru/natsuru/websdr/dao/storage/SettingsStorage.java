package ru.natsuru.websdr.dao.storage;

import android.content.Context;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import ru.natsuru.websdr.model.Bucket;

public class SettingsStorage extends AbstractStorage<Bucket<Boolean>> {

    public SettingsStorage(Context context) throws IOException {
        super(context);
    }

    @Override
    String getFileName() {
        return "settings.json";
    }

    @Override
    List<Bucket<Boolean>> getConverted(JSONArray jArray) throws JSONException {
        List<Bucket<Boolean>> list = new ArrayList<>();
        JSONObject jObject = jArray.getJSONObject(0);
        Bucket<Boolean> bucket = new Bucket<>("Parameter #0", jObject.getBoolean("AsService"));
        list.add(bucket);
        return list;
    }

    @Override
    JSONArray getConverted(List<Bucket<Boolean>> list) throws JSONException {
        JSONArray jsonArray = new JSONArray();
        for (int i = 0; i < list.size(); i++) {
            Bucket<Boolean> bucket = list.get(i);
            JSONObject jObject = new JSONObject();
            jObject.put("AsService", bucket.getValue());
            jsonArray.put(jObject);
        }
        return jsonArray;
    }
}
