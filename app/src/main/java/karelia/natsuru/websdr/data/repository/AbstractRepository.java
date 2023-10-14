//Author - Natsuru-san (natsuru-san@mail.com)
//Created 01.10.2023

package karelia.natsuru.websdr.data.repository;

import android.content.Context;
import android.database.Cursor;
import java.util.ArrayList;
import java.util.List;
import karelia.natsuru.websdr.data.util.DataBase;

public abstract class AbstractRepository<T> implements Repository<T> {

    private final Context context;

    public AbstractRepository(Context context) {
        this.context = context;
    }

    @Override
    public final void save(T entity) {
        exec(getSaveSql(entity));
    }

    @Override
    public final void remove(long id) {
        exec(getRemoveSql(id));
    }

    @Override
    public final List<T> getAll() {
        List<T> records = new ArrayList<>();
        try (var db = new DataBase(context)) {
            var cursor = db.getReadableDatabase().rawQuery(getAllRecordsSql(), null);
            while (cursor.moveToNext()) {
                records.add(toEntity(cursor));
            }
            cursor.close();
        }
        return records;
    }

    private void exec(String query) {
        try (var db = new DataBase(context)) {
            db.getWritableDatabase().execSQL(query);
        }
    }

    abstract String getSaveSql(T entity);
    abstract String getRemoveSql(long id);
    abstract String getAllRecordsSql();
    abstract T toEntity(Cursor cursor);
}