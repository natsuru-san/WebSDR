//Author - Natsuru-san (natsuru-san@mail.com)
//Created 01.10.2023

package karelia.natsuru.websdr.data.service;

import android.content.Context;
import java.util.List;
import karelia.natsuru.websdr.data.entity.Param;
import karelia.natsuru.websdr.data.entity.Station;
import karelia.natsuru.websdr.data.exceptions.LimitStationsException;
import karelia.natsuru.websdr.data.repository.Repository;
import karelia.natsuru.websdr.data.repository.SettingsRepository;
import karelia.natsuru.websdr.data.repository.StationRepository;

public abstract class AbstractDataService implements DataService {

    private final Repository<Param> paramRepository;
    private final Repository<Station> stationRepository;

    public AbstractDataService(Context context) {
        this.paramRepository = new SettingsRepository(context);
        this.stationRepository = new StationRepository(context);
    }

    @Override
    public final void saveStation(Station station) throws LimitStationsException {
        int count = stationRepository.getAll().size();
        if (count < getLimitStations()) {
            stationRepository.save(station);
            return;
        }
        throw new LimitStationsException(getLimitStations(), station);
    }

    @Override
    public final void deleteStation(long id) {
        stationRepository.remove(id);
    }

    @Override
    public final List<Station> getAllStations() {
        return stationRepository.getAll();
    }

    @Override
    public final void updateParam(Param param) {
        paramRepository.save(param);
    }

    @Override
    public final List<Param> getParams() {
        return paramRepository.getAll();
    }

    abstract int getLimitStations();
}