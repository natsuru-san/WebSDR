//Author - Natsuru-san (natsuru-san@mail.com)
//Created 01.10.2023

package karelia.natsuru.websdr.data.repository;

import android.annotation.SuppressLint;
import android.content.Context;
import android.database.Cursor;
import karelia.natsuru.websdr.data.entity.Param;
import karelia.natsuru.websdr.data.util.DataBase;

public final class SettingsRepository extends AbstractRepository<Param> {

    private static final String BEGIN_PART_UPDATE = "UPDATE " + DataBase.TABLE_NAME_SETTINGS;
    private static final String SELECT = "SELECT * FROM " + DataBase.TABLE_NAME_SETTINGS + ";";

    public SettingsRepository(Context context) {
        super(context);
    }

    @Override
    String getSaveSql(Param entity) {
        return BEGIN_PART_UPDATE +
                " SET value='" + entity.getValue() +
                "' WHERE parameter=" + entity.getKey() +
                ";";
    }

    @Override
    String getRemoveSql(long id) {
        throw new RuntimeException("Settings cannot be removed!");
    }

    @Override
    String getAllRecordsSql() {
        return SELECT;
    }

    @SuppressLint("Range")
    @Override
    Param toEntity(Cursor cursor) {
        return new Param(
                cursor.getLong(cursor.getColumnIndex("parameter")),
                cursor.getString(cursor.getColumnIndex("value"))
        );
    }
}