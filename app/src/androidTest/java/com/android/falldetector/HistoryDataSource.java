package com.android.falldetector;

import android.content.Context;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;

/**
 * Test HistoryDbHelper class.
 * Created by duanp on 3/15/2017.
 */

public class HistoryDataSource {
    // Database fields
    private SQLiteDatabase mDatabase;
    private HistoryDBHelper mDbHelper;

    public HistoryDataSource(Context context) {
        mDbHelper = new HistoryDBHelper(context);
    }

    public void open() throws SQLException {
        mDatabase = mDbHelper.getWritableDatabase();
    }

    public void close() {
        mDbHelper.close();
    }

    public boolean insertRec() {
        return mDbHelper.insertData(Calendar.getInstance().getTime().toString(),
                "Thunder Bay", 1);
    }

    public void deleteRec(String location) {
        mDatabase.delete(HistoryDBHelper.TABLE_NAME, HistoryDBHelper.KEY_LOCATION
                + " = " + location, null);
    }

    public ArrayList<HashMap<String, String>> getAllRec() {
        return mDbHelper.getList();
    }

    public void deleteAll() {
        mDbHelper.dropTable();
    }
}
