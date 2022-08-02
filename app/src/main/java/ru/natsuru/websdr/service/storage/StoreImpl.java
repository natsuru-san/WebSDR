package ru.natsuru.websdr.service.storage;

import org.json.JSONException;
import java.io.IOException;
import java.util.List;
import ru.natsuru.websdr.dao.storage.ServersStorage;
import ru.natsuru.websdr.dao.storage.SettingsStorage;
import ru.natsuru.websdr.dao.storage.StationsStorage;
import ru.natsuru.websdr.dao.storage.Storage;
import ru.natsuru.websdr.model.Bucket;
import ru.natsuru.websdr.model.MemoryCell;
import ru.natsuru.websdr.view.Main;

public class StoreImpl implements Store {

    private Storage<MemoryCell> stationDao;
    private Storage<Bucket<Boolean>> settingsDao;
    private Storage<Bucket<String>> serversDao;

    public StoreImpl(Main main) {
        try {
            stationDao = new StationsStorage(main);
            settingsDao = new SettingsStorage(main);
            serversDao = new ServersStorage(main);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void saveStations(List<MemoryCell> cells) {
        try {
            stationDao.save(cells);
        } catch (IOException | JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void saveSettings(List<Bucket<Boolean>> sets) {
        try {
            settingsDao.save(sets);
        } catch (IOException | JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void saveServers(List<Bucket<String>> servers) {
        try {
            serversDao.save(servers);
        } catch (IOException | JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    public List<MemoryCell> loadStations() {
        try {
            return stationDao.load();
        } catch (JSONException | IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public List<Bucket<Boolean>> loadSettings() {
        try {
            return settingsDao.load();
        } catch (JSONException | IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public List<Bucket<String>> loadServers() {
        try {
            return serversDao.load();
        } catch (JSONException | IOException e) {
            e.printStackTrace();
            return null;
        }
    }
}
