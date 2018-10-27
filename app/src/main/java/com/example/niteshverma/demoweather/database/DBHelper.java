package com.example.niteshverma.demoweather.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteConstraintException;
import android.database.sqlite.SQLiteDatabase;

import com.example.niteshverma.demoweather.Utility.Utilities;
import com.example.niteshverma.demoweather.model.Bookmark;

import java.util.ArrayList;


public class DBHelper {

    private static DBHelper dbHelper;

    private DBHelper() {
    }

    public static DBHelper getInstance() {
        if (dbHelper == null) {
            dbHelper = new DBHelper();
        }
        return dbHelper;
    }

    private SQLiteDatabase getWritableDatabaseInstance(Context context) {

        SqliteDataBaseHelper writableDatabase = new SqliteDataBaseHelper(context);

        return writableDatabase.getWritableDatabase();
    }

    private SQLiteDatabase getReadableDatabaseInstance(Context context) {

        SqliteDataBaseHelper readableDatabase = new SqliteDataBaseHelper(context);
        return readableDatabase.getReadableDatabase();
    }

    public long addBookmark(final Bookmark locationBookmark, final Context context) {
        long row = -1;

        if (locationBookmark == null) {
            return -1;
        }

        try {
            final ContentValues cv = new ContentValues();
            cv.put(DatabaseConstant.col_lat, locationBookmark.getLat());
            cv.put(DatabaseConstant.col_lon, locationBookmark.getLon());
            cv.put(DatabaseConstant.col_name, locationBookmark.getLocationName());

            final SQLiteDatabase db = getWritableDatabaseInstance(context);
            db.beginTransaction();

            try {
                row = db.insert(DatabaseConstant.TABLE_LOCATION_BOOKMARK, null, cv);
                db.setTransactionSuccessful();
            } catch (SQLiteConstraintException sqlce) {
                Utilities.printStackTrace(sqlce);
            } catch (Exception e) {
                Utilities.printStackTrace(e);
            } finally {
                if (db != null && db.isOpen()) {
                    db.endTransaction();
                    db.close();
                }
            }
        } catch (Exception e) {
            Utilities.printStackTrace(e);
        }
        return row;
    }

    public long deleteBookmark(final Bookmark bookmark, final Context context) {
        int row = -1;
        final ContentValues cv = new ContentValues();
        cv.put(DatabaseConstant.col_id, bookmark.getId());
        final SQLiteDatabase db = getWritableDatabaseInstance(context);
        db.beginTransaction();
        try {
            final String selection = DatabaseConstant.col_id + " =?";
            final String[] selectionArgs = {String.valueOf(bookmark.getId())};
            row = db.delete(DatabaseConstant.TABLE_LOCATION_BOOKMARK, selection, selectionArgs);
            db.setTransactionSuccessful();
        } catch (SQLiteConstraintException sqlce) {
            Utilities.printStackTrace(sqlce);
        } catch (Exception e) {
            Utilities.printStackTrace(e);
        } finally {
            if (db != null && db.isOpen()) {
                db.endTransaction();
                db.close();
            }
        }
        return row;
    }

    public ArrayList<Bookmark> getAllBookmark(Context context) {
        ArrayList<Bookmark> itemList = new ArrayList<>();
        Cursor cursor = null;
        SQLiteDatabase db = getReadableDatabaseInstance(context);
        try {

            String sql = "select * from " + DatabaseConstant.TABLE_LOCATION_BOOKMARK;

            cursor = db.rawQuery(sql, null);

            if (cursor != null && cursor.getCount() > 0) {

                for (cursor.moveToFirst(); !cursor.isAfterLast(); cursor.moveToNext()) {
                    Bookmark item = new Bookmark();
                    try {
                        item.setId(cursor.getLong(cursor.getColumnIndex(DatabaseConstant.col_id)));
                        item.setLocationName(cursor.getString(cursor.getColumnIndex(DatabaseConstant.col_name)));
                        item.setLat(cursor.getString(cursor.getColumnIndex(DatabaseConstant.col_lat)));
                        item.setLon(cursor.getString(cursor.getColumnIndex(DatabaseConstant.col_lon)));
                        itemList.add(item);
                    } catch (Exception e) {
                        Utilities.printStackTrace(e);
                    }
                }
            }
        } catch (Exception e) {
            Utilities.printStackTrace(e);
        } finally {
            if (cursor != null && !cursor.isClosed()) {
                cursor.close();
            }
            if (db != null && db.isOpen()) {
                db.close();
            }
        }
        return itemList;
    }


}
