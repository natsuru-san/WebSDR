package ru.natsuru.websdr.radioengine.storage;

import android.content.Context;

public class SettingsStorage extends AbstractStorage {

    public SettingsStorage(Context context) {
        super(context);
    }

    @Override
    String getFileName() {
        return "settings.json";
    }
}
