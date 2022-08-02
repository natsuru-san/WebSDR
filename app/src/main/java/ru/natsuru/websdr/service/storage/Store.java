package ru.natsuru.websdr.service.storage;

import org.json.JSONArray;
import java.util.List;

import ru.natsuru.websdr.model.Bucket;
import ru.natsuru.websdr.model.MemoryCell;

public interface Store {
    void saveStations(List<MemoryCell> cells);
    void saveSettings(List<Bucket<Boolean>> sets);
    void saveServers(List<Bucket<String>> servers);
    List<MemoryCell> loadStations();
    List<Bucket<Boolean>> loadSettings();
    List<Bucket<String>> loadServers();
}
