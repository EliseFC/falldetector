package com.android.falldetector;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.provider.BaseColumns;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Fall event history database helper.
 * Created by elise on 2017-02-25.
 */

public class HistoryDBHelper extends SQLiteOpenHelper {
    public static final String TABLE_NAME = "HISTORY";

    public static final String KEY_TIME = "time";
    public static final String KEY_LOCATION = "location";
    public static final String KEY_EVAL = "evaluation";

    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_NAME = "DetectInform.db";
    private static final String SQL_CREATE_TABLE =
            "CREATE TABLE_NAME " + TABLE_NAME
            + "(" + BaseColumns._ID + " INTEGER PRIMARY KEY, " // Cursor requires '_id'
            + KEY_TIME + " TEXT, "
            + KEY_LOCATION + " TEXT, "
            + KEY_EVAL + " INTEGER)";

    public HistoryDBHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(SQL_CREATE_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.delete(HistoryDBHelper.TABLE_NAME, null, null); // DROP TABLE IF EXISTS <table_name>
        onCreate(db);
    }

    boolean insertData(String time_id, String location, int eval) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(KEY_TIME, time_id);
        contentValues.put(KEY_LOCATION, location);
        contentValues.put(KEY_EVAL, eval);
        long newRowId = db.insert(TABLE_NAME, null, contentValues);
        return newRowId != -1;
    }

    //delete information from database by Prime Key
    public void deleteData(String time_id, String location, int eval) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_NAME, KEY_TIME + "=?", new String[]{time_id});
    }

    Cursor getCursor() {
        SQLiteDatabase db = this.getReadableDatabase();
        String selectQuery = "SELECT * FROM " + TABLE_NAME; // must include '_id' for Cursor to use
        return db.rawQuery(selectQuery, null);
    }

    public ArrayList<HashMap<String, String>> getList() {
        ArrayList<HashMap<String, String>> recordList = new ArrayList<HashMap<String, String>>();
        Cursor cursor = this.getCursor();

        if (cursor.moveToFirst()) {
            do {
                HashMap<String, String> map = new HashMap<String, String>();
                map.put("time", cursor.getString(cursor.getColumnIndex(this.KEY_TIME)));
                map.put("location", cursor.getString(cursor.getColumnIndex(this.KEY_LOCATION)));
                map.put("evaluation", cursor.getString(cursor.getColumnIndex(this.KEY_EVAL)));
                recordList.add(map);
            } while (cursor.moveToNext());
        }
        cursor.close();
        return recordList;
    }

    void dropTable() {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(HistoryDBHelper.TABLE_NAME, null, null); // DROP TABLE IF EXISTS <table_name>
    }
}
