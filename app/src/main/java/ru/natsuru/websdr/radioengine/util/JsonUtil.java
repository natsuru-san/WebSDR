package ru.natsuru.websdr.radioengine.util;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import ru.natsuru.websdr.radioengine.util.repo.Repository;

public class JsonUtil {

    public static List<MemoryCell> getListStations(JSONArray jArray) throws JSONException {
        List<MemoryCell> converted = new ArrayList<>();
        for(int i = 0; i < jArray.length(); i++){
            JSONObject station = jArray.getJSONObject(i);
            int mode = Integer.parseInt(station.getString("Mode"));
            float minBorder = Float.parseFloat(station.getString("MinBorder"));
            float maxBorder = Float.parseFloat(station.getString("MaxBorder"));
            float freq = Float.parseFloat(station.getString("Freq"));
            converted.add(new MemoryCell(mode, minBorder, maxBorder, freq, i));
        }
        return converted;
    }

    public static JSONObject getCurrentStation() {
        HashMap<String, String> station = new HashMap<>();
        station.put("Mode", String.valueOf(Repository.getMode()));
        station.put("MinBorder", String.valueOf(Repository.getMinBorder()));
        station.put("MaxBorder", String.valueOf(Repository.getMaxBorder()));
        station.put("Freq", String.valueOf(Repository.getFreq()));
        return new JSONObject(station);
    }
}
