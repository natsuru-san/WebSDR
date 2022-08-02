package ru.natsuru.websdr.model;

import java.util.Map;

public class Bucket<T> implements Map.Entry<String, T> {

    private final String key;
    private final T value;

    public Bucket(String key, T value) {
        this.key = key;
        this.value = value;
    }

    @Override
    public String getKey() {
        return key;
    }

    @Override
    public T getValue() {
        return value;
    }

    //Метод заглушен
    @Override
    public T setValue(T s) {
        return value;
    }
}
