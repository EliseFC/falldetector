package com.android.falldetector;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by elise on 2017-02-25.
 */

public class HistoryDBHelper extends SQLiteOpenHelper {
    public static final String TABLE = "HISTORY";

    public static final String KEY_ID = "time";
    public static final String KEY_location = "location";
    public static final String KEY_evalu = "evaluation";


    private static final int DATABASE_VERSION = 4;
    private static final String DATABASE_NAME = "DetectInform.db";  //name of the database

    public HistoryDBHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        //create data list
        String CREATE_TABLE_DETECTINFORM = "CREATE TABLE " + TABLE
                + "( " + KEY_ID + " Time ,"
                + " " + KEY_location + " " + "Location, "
                + " " + KEY_evalu + " integer)";
        db.execSQL(CREATE_TABLE_DETECTINFORM);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

    public boolean insertData(String time_id, String location, int eval) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(KEY_ID, time_id);
        contentValues.put(KEY_location, location);
        contentValues.put(KEY_evalu, eval);
        long newRowId = db.insert(TABLE, null, contentValues);
        return newRowId != -1;
    }

    //delete information from database by Prime Key
    public void deleteData(String time_id, String location, int eval) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE, KEY_ID + "=?", new String[]{time_id});
    }

    public ArrayList<HashMap<String, String>> getList() {
        SQLiteDatabase db = this.getReadableDatabase();
        String selectQuery = "SELECT " +
                KEY_ID + "," +
                KEY_location + ","
                + KEY_evalu + " FROM " + TABLE;
        ArrayList<HashMap<String, String>> recordList = new ArrayList<HashMap<String, String>>();
        Cursor cursor = db.rawQuery(selectQuery, null);

        if (cursor.moveToFirst()) {
            do {
                HashMap<String, String> table = new HashMap<String, String>();
                table.put("time", cursor.getString(cursor.getColumnIndex(this.KEY_ID)));
                table.put("location", cursor.getString(cursor.getColumnIndex(this.KEY_location)));
                table.put("evaluation", cursor.getString(cursor.getColumnIndex(this.KEY_evalu)));
                recordList.add(table);
            } while (cursor.moveToNext());
        }
        cursor.close();
        return recordList;
    }
}
