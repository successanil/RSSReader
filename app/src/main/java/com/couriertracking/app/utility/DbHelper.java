package com.couriertracking.app.utility;

/**
 * DB classes would implmented in it.
 * application admin and admin user related
 */

import java.util.ArrayList;
import java.util.List;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class DbHelper {

    static final String TABLE_COURIER_DATA = "courier_data";
    static final String TABLE_HISTORY_COURIER_DATA = "history_courier_data";

    public static final String FIELD_ID = "id";
    public static final String FIELD_IMG_SRC = "img_src";
    public static final String FIELD_COURIER_NAME = "courier_name";
    public static final String FIELD_COURIER_URL = "courier_url";

    public static final String FIELD_TRACK_NO = "trackno";
    public static final String FIELD_LAST_SEARCHED_DATE = "searched_date";

    static final int DB_VERSION = 1;
    static final String DATABASE_NAME = "couriertracking";

    static final String QUERY_COURIER_DATA = "create table if not exists " + TABLE_COURIER_DATA
            + "(" + FIELD_ID + " integer primary key autoincrement, "
            + FIELD_IMG_SRC + " text not null, "
            + FIELD_COURIER_NAME + " text not null, "
            + FIELD_COURIER_URL + " text not null);";

    static final String QUERY_HISTORY_COURIER_DATA = "create table if not exists " + TABLE_HISTORY_COURIER_DATA
            + "(" + FIELD_ID + " integer primary key autoincrement, "
            + FIELD_COURIER_NAME + " text not null, "
            + FIELD_TRACK_NO + " text not null, "
            + FIELD_COURIER_URL + " text not null, "
            + FIELD_LAST_SEARCHED_DATE + " text not null);";

    static SQLiteDatabase database;
    Context context;
    static Helper helper;

    public DbHelper(Context c) {
        this.context = c;
        helper = new Helper(c);
    }

    public void emptyTables() {
        database.delete(TABLE_COURIER_DATA,"1",null);
    }

    static class Helper extends SQLiteOpenHelper {
        Helper(Context c) {
            super(c, DATABASE_NAME, null, DB_VERSION);

        }

        @Override
        public void onCreate(SQLiteDatabase db) {
            try {
                db.execSQL(QUERY_COURIER_DATA);
                db.execSQL(QUERY_HISTORY_COURIER_DATA);
            } catch (Exception e) {
                e.printStackTrace();
            }

        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            db.execSQL("DROP TABLE IF EXISTS " + TABLE_COURIER_DATA + "");
            db.execSQL("DROP TABLE IF EXISTS " + TABLE_HISTORY_COURIER_DATA + "");
            onCreate(db);
        }
    }

    public DbHelper open() throws SQLException {
        database = helper.getWritableDatabase();
        return this;
    }

    public void close() {
        helper.close();
    }

    public long insert_courier_data(String imgSrc,String courierName,String courierUrl) {
        ContentValues cov = new ContentValues();
        cov.put(FIELD_IMG_SRC, imgSrc);
        cov.put(FIELD_COURIER_NAME,courierName);
        cov.put(FIELD_COURIER_URL,courierUrl);
        return database.insert(TABLE_COURIER_DATA, null, cov);
    }

    public Cursor read_courier_data() {
        String selectQuery = "SELECT  * FROM " + TABLE_COURIER_DATA;
        Cursor cursor = database.rawQuery(selectQuery, null);
        return cursor;
    }

    public long insert_history_data(String courierName,String trackNo,String courierUrl,String lastSearchedDate) {
        ContentValues cov = new ContentValues();
        cov.put(FIELD_COURIER_NAME,courierName);
        cov.put(FIELD_TRACK_NO,trackNo);
        cov.put(FIELD_COURIER_URL,courierUrl);
        cov.put(FIELD_LAST_SEARCHED_DATE,lastSearchedDate);
        return database.insert(TABLE_HISTORY_COURIER_DATA, null, cov);
    }

    public Cursor read_history_data() {
        String selectQuery = "SELECT  * FROM " + TABLE_HISTORY_COURIER_DATA;
        Cursor cursor = database.rawQuery(selectQuery, null);
        return cursor;
    }
}
