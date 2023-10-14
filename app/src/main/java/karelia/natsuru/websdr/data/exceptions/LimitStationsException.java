//Author - Natsuru-san (natsuru-san@mail.com)
//Created 14.10.2023

package karelia.natsuru.websdr.data.exceptions;

import karelia.natsuru.websdr.data.entity.Station;

public final class LimitStationsException extends Exception {
    private final int limit;
    private final Station station;

    public LimitStationsException(int limit, Station station) {
        this.limit = limit;
        this.station = station;
    }

    public int getLimit() {
        return limit;
    }

    public Station getStation() {
        return station;
    }
}