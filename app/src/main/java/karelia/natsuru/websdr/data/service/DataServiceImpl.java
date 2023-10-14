//Author - Natsuru-san (natsuru-san@mail.com)
//Created 01.10.2023

package karelia.natsuru.websdr.data.service;

import android.content.Context;
import karelia.natsuru.websdr.data.exceptions.NegativeLimitStations;

public final class DataServiceImpl extends AbstractDataService {

    private final int limit;

    public DataServiceImpl(Context context, int limit) {
        super(context);
        if (limit <= 0) throw new NegativeLimitStations(limit);
        this.limit = limit;
    }

    @Override
    int getLimitStations() {
        return limit;
    }
}