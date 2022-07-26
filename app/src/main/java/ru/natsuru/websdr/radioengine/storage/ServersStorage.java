package ru.natsuru.websdr.radioengine.storage;

import android.content.Context;

public class ServersStorage extends AbstractStorage {

    public ServersStorage(Context context) {
        super(context);
    }

    @Override
    String getFileName() {
        return "servers.json";
    }
}
