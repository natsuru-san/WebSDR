//Copyright by Natsuru-san

package karelia.natsuru.websdr;

import android.content.Context;
import androidx.annotation.NonNull;
import org.json.JSONArray;
import org.json.JSONException;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;

@SuppressWarnings({"SameParameterValue", "FieldCanBeLocal"})
public class Storage {
    private final Context context;
    private final String FILE_MEMORIES = "memory.json";
    private final String FILE_SETTINGS = "settings.json";
    public Storage(@NonNull Context context) throws IOException {
        this.context = context;
        File file = context.getFileStreamPath(FILE_MEMORIES);
        if(!file.exists()){
            JSONArray jArray = new JSONArray();
            FileOutputStream fos = context.openFileOutput(FILE_MEMORIES, Context.MODE_PRIVATE);
            fos.write(jArray.toString().getBytes(StandardCharsets.UTF_8));
            fos.flush();
            fos.close();
        }
        file = context.getFileStreamPath(FILE_SETTINGS);
        if(!file.exists()){
            JSONArray jArray = new JSONArray();
            FileOutputStream fos = context.openFileOutput(FILE_SETTINGS, Context.MODE_PRIVATE);
            fos.write(jArray.toString().getBytes(StandardCharsets.UTF_8));
            fos.flush();
            fos.close();
        }
    }
    //Сохраняем файл
    @SuppressWarnings("ResultOfMethodCallIgnored")
    protected void save(@NonNull JSONArray jArray, boolean isSetting) throws IOException {
        String type;
        if(isSetting){
            type = FILE_SETTINGS;
        }else{
            type = FILE_MEMORIES;
        }
        File file = context.getFileStreamPath(type);
        file.delete();
        FileOutputStream fos = context.openFileOutput(type, Context.MODE_PRIVATE);
        fos.write(jArray.toString().getBytes(StandardCharsets.UTF_8));
        fos.flush();
        fos.close();
    }
    //Вытаскиваем файл
    protected JSONArray load(boolean isSetting) throws IOException, JSONException {
        String type;
        if(isSetting){
            type = FILE_SETTINGS;
        }else{
            type = FILE_MEMORIES;
        }
        FileInputStream fis = context.openFileInput(type);
        BufferedReader reader = new BufferedReader(new InputStreamReader(fis, StandardCharsets.UTF_8));
        StringBuilder builder = new StringBuilder();
        String line;
        while ((line = reader.readLine()) != null) {
            builder.append(line).append("\n");
        }
        JSONArray jArray = new JSONArray(builder.toString());
        reader.close();
        return jArray;
    }
}