//Author - Natsuru-san (natsuru-san@mail.com)
//Created 01.10.2023

package karelia.natsuru.websdr.data.entity;

import androidx.annotation.NonNull;
import java.util.Map;

public class Param implements Map.Entry<Long, String> {

    private final long key;
    private String value;

    public Param(long key, @NonNull String value) {
        this.key = key;
        this.value = value;
    }

    @Override
    public Long getKey() {
        return key;
    }

    @Override
    public String getValue() {
        return value;
    }

    @Override
    public String setValue(String value) {
        this.value = value;
        return this.value;
    }
}