//Author - Natsuru-san (natsuru-san@mail.com)
//Created 01.10.2023

package karelia.natsuru.websdr.data.service;

import java.util.List;
import karelia.natsuru.websdr.data.entity.Param;
import karelia.natsuru.websdr.data.entity.Station;

public interface DataService {
    void saveStation(Station station);
    void deleteStation(long id);
    List<Station> getAllStations();
    void updateParam(Param param);
    List<Param> getParams();
}