package ru.natsuru.websdr.radioengine.storage;

import android.content.Context;

public class StationsStorage extends AbstractStorage {
    public StationsStorage(Context context) {
        super(context);
    }

    @Override
    String getFileName() {
        return "memory.json";
    }
}
