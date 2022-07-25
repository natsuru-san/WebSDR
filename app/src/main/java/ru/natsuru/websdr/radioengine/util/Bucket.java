package ru.natsuru.websdr.radioengine.util;

import java.util.Map;

public class Bucket implements Map.Entry<String, String> {

    private final String key;
    private final String value;

    public Bucket(String key, String value) {
        this.key = key;
        this.value = value;
    }

    @Override
    public String getKey() {
        return key;
    }

    @Override
    public String getValue() {
        return value;
    }

    //Метод заглушен
    @Override
    public String setValue(String s) {
        return value;
    }
}
