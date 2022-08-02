package ru.natsuru.websdr.dao.storage;

import android.content.Context;
import org.json.JSONArray;
import org.json.JSONException;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.List;

public abstract class AbstractStorage<T> implements Storage<T> {

    private final Context context;

    public AbstractStorage(Context context) throws IOException {
        this.context = context;
        createDefaultIfNotExist();
    }

    @SuppressWarnings("ResultOfMethodCallIgnored")
    @Override
    public void save(List<T> saving) throws IOException, JSONException {
        File file = context.getFileStreamPath(getFileName());
        file.delete();
        FileOutputStream fos = context.openFileOutput(getFileName(), Context.MODE_PRIVATE);
        fos.write(getConverted(saving).toString().getBytes(StandardCharsets.UTF_8));
        fos.flush();
        fos.close();
    }

    @Override
    public List<T> load() throws JSONException, IOException {
        FileInputStream fis = context.openFileInput(getFileName());
        BufferedReader reader = new BufferedReader(new InputStreamReader(fis, StandardCharsets.UTF_8));
        StringBuilder builder = new StringBuilder();
        String line;
        while ((line = reader.readLine()) != null) {
            builder.append(line).append("\n");
        }
        JSONArray jArray = new JSONArray(builder.toString());
        reader.close();
        return getConverted(jArray);
    }

    @Override
    public void createDefaultIfNotExist() throws IOException {
        File file = context.getFileStreamPath(getFileName());
        if(!file.exists()){
            JSONArray jArray = new JSONArray();
            FileOutputStream fos = context.openFileOutput(getFileName(), Context.MODE_PRIVATE);
            fos.write(jArray.toString().getBytes(StandardCharsets.UTF_8));
            fos.flush();
            fos.close();
        }
    }
    abstract String getFileName();
    abstract List<T> getConverted(JSONArray jArray) throws JSONException;
    abstract JSONArray getConverted(List<T> list) throws JSONException;
}
