//Author - Natsuru-san (natsuru-san@mail.com)
//Created 01.10.2023

package karelia.natsuru.websdr.data.entity;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import java.math.BigDecimal;
import java.time.ZonedDateTime;

/**
 * The class Station is a object view of records in sql-table
 */
public class Station {

    private final String mode;
    private final BigDecimal minBorder;
    private final BigDecimal maxBorder;
    private final BigDecimal freq;
    private final ZonedDateTime time;
    private final Long id;
    public Station(
            @NonNull String mode,
            @NonNull BigDecimal minBorder,
            @NonNull BigDecimal maxBorder,
            @NonNull BigDecimal freq,
            @NonNull ZonedDateTime time,
            @Nullable Long id
    ) {
        this.mode = mode;
        this.minBorder = minBorder;
        this.maxBorder = maxBorder;
        this.freq = freq;
        this.time = time;
        this.id = id;
    }

    public ZonedDateTime getTime() {
        return time;
    }

    public String getMode() {
        return mode;
    }

    public BigDecimal getMinBorder() {
        return minBorder;
    }

    public BigDecimal getMaxBorder() {
        return maxBorder;
    }

    public BigDecimal getFreq() {
        return freq;
    }

    public Long getId(){
        return id;
    }
}