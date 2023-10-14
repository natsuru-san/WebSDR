//Author - Natsuru-san (natsuru-san@mail.com)
//Created 01.10.2023

package karelia.natsuru.websdr.data.repository;

import android.annotation.SuppressLint;
import android.content.Context;
import android.database.Cursor;
import java.math.BigDecimal;
import java.time.ZonedDateTime;
import karelia.natsuru.websdr.data.entity.Station;
import karelia.natsuru.websdr.data.util.DataBase;

public final class StationRepository extends AbstractRepository<Station> {

    private static final String BEGIN_PART_SAVE = "INSERT INTO " + DataBase.TABLE_NAME_STATIONS;
    private static final String VALUES_PART_SAVE = "(freq, mode, minBorder, maxBorder, time) VALUES('";
    private static final String BEGIN_PART_UPDATE = "UPDATE " + DataBase.TABLE_NAME_STATIONS;
    private static final String SELECT = "SELECT * FROM " + DataBase.TABLE_NAME_STATIONS + ";";
    private static final String DELETE = "DELETE FROM " + DataBase.TABLE_NAME_STATIONS + " WHERE _id=";

    public StationRepository(Context context) {
        super(context);
    }

    @Override
    public String getSaveSql(Station entity) {
        String query;
        Long id = entity.getId();
        var freq = entity.getFreq();
        var maxBorder = entity.getMaxBorder();
        var minBorder = entity.getMinBorder();
        var mode = entity.getMode();
        var time = entity.getTime().toString();
        if (id == null) {
            query = BEGIN_PART_SAVE +
                    VALUES_PART_SAVE +
                    freq + "','" +
                    mode + "','" +
                    minBorder + "','" +
                    maxBorder + "','" +
                    time + "'" +
                    ");";
        } else {
            query = BEGIN_PART_UPDATE +
                    " SET freq='" + freq + "'," +
                    "SET mode='" + mode + "'," +
                    "SET minBorder='" + minBorder + "'," +
                    "SET maxBorder='" + maxBorder + "'," +
                    "SET time='" + time + "'" +
                    " WHERE _id=" + id +
                    ";";
        }
        return query;
    }

    @Override
    public String getRemoveSql(long id) {
        return DELETE + id + ";";
    }

    @Override
    public String getAllRecordsSql() {
        return SELECT;
    }

    @SuppressLint("Range")
    @Override
    public Station toEntity(Cursor cursor) {
        return new Station(
                cursor.getString(cursor.getColumnIndex("mode")),
                new BigDecimal(cursor.getString(cursor.getColumnIndex("minBorder"))),
                new BigDecimal(cursor.getString(cursor.getColumnIndex("maxBorder"))),
                new BigDecimal(cursor.getString(cursor.getColumnIndex("freq"))),
                ZonedDateTime.parse(cursor.getString(cursor.getColumnIndex("time"))),
                cursor.getLong(cursor.getColumnIndex("mode"))
        );
    }
}