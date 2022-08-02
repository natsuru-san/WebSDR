package ru.natsuru.websdr.dao.storage;

import android.content.Context;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import ru.natsuru.websdr.model.MemoryCell;

public class StationsStorage extends AbstractStorage<MemoryCell> {

    public StationsStorage(Context context) throws IOException {
        super(context);
    }

    @Override
    String getFileName() {
        return "memory.json";
    }

    @Override
    List<MemoryCell> getConverted(JSONArray jArray) throws JSONException {
        List<MemoryCell> cells = new ArrayList<>();
        for (int i = 0; i < jArray.length(); i++) {
            JSONObject jObject = jArray.getJSONObject(i);
            int mode = Integer.parseInt(jObject.getString("Mode"));
            float minBorder = Float.parseFloat(jObject.getString("MinBorder"));
            float maxBorder = Float.parseFloat(jObject.getString("MaxBorder"));
            float freq = Float.parseFloat(jObject.getString("Freq"));
            MemoryCell cell = new MemoryCell(mode, minBorder, maxBorder, freq, i);
            cells.add(cell);
        }
        return cells;
    }

    @Override
    JSONArray getConverted(List<MemoryCell> list) throws JSONException {
        JSONArray jArray = new JSONArray();
        for (int i = 0; i < list.size(); i++) {
            MemoryCell cell = list.get(i);
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("Mode", String.valueOf(cell.getMode()));
            jsonObject.put("MinBorder", String.valueOf(cell.getMinBorder()));
            jsonObject.put("MaxBorder", String.valueOf(cell.getMaxBorder()));
            jsonObject.put("Freq", String.valueOf(cell.getFreq()));
            jArray.put(jsonObject);
        }
        return jArray;
    }
}
