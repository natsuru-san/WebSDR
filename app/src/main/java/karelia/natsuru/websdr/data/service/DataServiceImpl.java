//Author - Natsuru-san (natsuru-san@mail.com)
//Created 01.10.2023

package karelia.natsuru.websdr.data.service;

import android.content.Context;

public final class DataServiceImpl extends AbstractDataService {

    public DataServiceImpl(Context context) {
        super(context);
    }

    @Override
    int getLimitStations() {
        return 100;
    }
}