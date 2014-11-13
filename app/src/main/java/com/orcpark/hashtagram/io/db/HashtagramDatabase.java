package com.orcpark.hashtagram.io.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.ArrayList;

/**
 * Created by orcpark on 2014. 9. 9..
 */
public class HashtagramDatabase extends SQLiteOpenHelper {

    public static final String DATABASE_NAME = "hashtagram_database";

    public static final String TABLE_NAME_HASH_TAG = "hash_tag";

    public static final String KEY_ID = "id";

    public static final String KEY_HASH_TAG = "hash_tag";

    public static final String SQL_CREATE_TABLE = "create table " + TABLE_NAME_HASH_TAG + " (" +
                                                    KEY_ID + " integer auto_increment, " +
                                                    KEY_HASH_TAG + " text);";

    private static HashtagramDatabase sInstance;

    private HashtagramDatabase(Context context) {
        super(context, DATABASE_NAME, null, 1);
    }

    public static HashtagramDatabase getInstance(Context context) {
        if (sInstance == null) {
            sInstance = new HashtagramDatabase(context);
        }
        return sInstance;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(SQL_CREATE_TABLE);
    }

    public void insert(String hashTag) {
        SQLiteDatabase db = getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(KEY_HASH_TAG, hashTag);
        long insert = db.insert(TABLE_NAME_HASH_TAG, null, contentValues);
        Log.e("jsp", "insert = " + insert);
        db.close();
    }

    public ArrayList<String> getAllHashTag() {
        SQLiteDatabase db = getReadableDatabase();
        ArrayList<String> items = null;
        Cursor cursor = db.query(TABLE_NAME_HASH_TAG, new String[]{KEY_HASH_TAG}, null, null, null, null, null);
        if (cursor.moveToFirst()) {
            items = new ArrayList<String>();
            int max = cursor.getCount();
            for (int i = 0; i < max; i++) {
                String hashTag = cursor.getString(cursor.getColumnIndex(KEY_HASH_TAG));
//                Log.e("jsp", "tag = " + hashTag);
                items.add(hashTag);
                cursor.moveToNext();
            }
        }
        cursor.close();
        db.close();
        return items;
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
