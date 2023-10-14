//Author - Natsuru-san (natsuru-san@mail.com)
//Created 01.10.2023

package karelia.natsuru.websdr.data.util;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public final class DataBase extends SQLiteOpenHelper {

    private static final String DB_NAME = "websdr";
    public static final String TABLE_NAME_STATIONS = "stations";
    public static final String TABLE_NAME_SETTINGS = "settings";
    private static final String INIT_STATION_TABLE =
            "CREATE TABLE IF NOT EXISTS " +
            TABLE_NAME_STATIONS +
            "(_id INTEGER PRIMARY KEY AUTOINCREMENT, " +
            "mode TEXT UNIQUE, " +
            "minBorder TEXT, " +
            "maxBorder TEXT, " +
            "time TEXT, " +
            "freq TEXT);";
    private static final String INIT_SETTINGS_TABLE =
            "CREATE TABLE IF NOT EXISTS " +
            TABLE_NAME_SETTINGS +
            "(parameter INTEGER UNIQUE NOT NULL, " +
            "value TEXT);";

    public DataBase(Context context) {
        super(context, DB_NAME, null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(INIT_STATION_TABLE);
        db.execSQL(INIT_SETTINGS_TABLE);
        db.execSQL("INSERT INTO " +
                TABLE_NAME_SETTINGS +
                "(parameter, value) VALUES(1, 'false');"); //Background play
        db.execSQL("INSERT INTO " +
                TABLE_NAME_SETTINGS +
                "(parameter, value) VALUES(2, 'false');"); //Sync sound volume with system
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        //Not yet implemented. Unnessesary operation.
    }
}