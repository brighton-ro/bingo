package org.idiotnation.bingo;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class StatisticsDatabaseHelper extends SQLiteOpenHelper {

    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_NAME = "BingoStatistics.db";
    private static final String SQL_CREATE_NUMBERS =
            "CREATE TABLE IF NOT EXISTS NumberStatistics (" +
                    "Id INTEGER PRIMARY KEY," +
                    "Number Integer" + " )";
    private static final String SQL_CREATE_PICKS =
            "CREATE TABLE IF NOT EXISTS NumberStatistics (" +
                    "Id INTEGER PRIMARY KEY," +
                    "Number Integer" + " )";

    public StatisticsDatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(SQL_CREATE_NUMBERS);
        db.execSQL(SQL_CREATE_PICKS);

    }
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        onCreate(db);
    }
    public void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        onUpgrade(db, oldVersion, newVersion);
    }

}
