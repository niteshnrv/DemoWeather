package com.example.niteshverma.demoweather.database;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.text.TextUtils;

import com.example.niteshverma.demoweather.Utility.Utilities;

public class SqliteDataBaseHelper extends SQLiteOpenHelper {

    private static final String DB_NAME = "WEATHER_DB";
    private static final int DB_VERSION = 1;


    public SqliteDataBaseHelper(Context context){
        super(context, DB_NAME, null, DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        setUpAllTable(sqLiteDatabase);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int oldVersion, int newVersion) {

    }

    private void setUpAllTable(SQLiteDatabase sqLiteDatabase){
        try {
            sqLiteDatabase.beginTransaction();
            sqLiteDatabase.execSQL(DatabaseConstant.QUERY_CREATE_TABLE_LOCATION_BOOKMARK);
            sqLiteDatabase.setTransactionSuccessful();

        }catch (Exception e){
            Utilities.printStackTrace(e);
        }finally{
            sqLiteDatabase.endTransaction();
        }
    }
}
