package com.rabiahatunsoylemez.travelmemoriesapp;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;

public class DatabaseHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "travel_memories.db";
    private static final int DATABASE_VERSION = 1;

    private static final String TABLE_TRAVELS = "travels";
    private static final String COLUMN_ID = "id";
    private static final String COLUMN_CITY = "city";
    private static final String COLUMN_NOTE = "note";
    private static final String COLUMN_MAP_LINK = "map_link";
    private static final String COLUMN_IS_FAVORITE = "is_favorite";

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String createTable = "CREATE TABLE " + TABLE_TRAVELS + " (" +
                COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COLUMN_CITY + " TEXT NOT NULL, " +
                COLUMN_NOTE + " TEXT, " +
                COLUMN_MAP_LINK + " TEXT, " +
                COLUMN_IS_FAVORITE + " INTEGER DEFAULT 0)";
        db.execSQL(createTable);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_TRAVELS);
        onCreate(db);
    }

    // Seyahat ekleme
    public void addTravel(String city, String note, String mapLink, boolean isFavorite) {
        SQLiteDatabase db = null;
        try {
            db = this.getWritableDatabase();
            ContentValues values = new ContentValues();
            values.put(COLUMN_CITY, city);
            values.put(COLUMN_NOTE, note);
            values.put(COLUMN_MAP_LINK, mapLink);
            values.put(COLUMN_IS_FAVORITE, isFavorite ? 1 : 0);
            db.insert(TABLE_TRAVELS, null, values);
        } finally {
            if (db != null) db.close();
        }
    }

    // Seyahat listesini alma
    public ArrayList<Travel> getAllTravels(boolean onlyFavorites) {
        ArrayList<Travel> travels = new ArrayList<>();
        SQLiteDatabase db = null;
        Cursor cursor = null;

        try {
            db = this.getReadableDatabase();
            String query = "SELECT * FROM " + TABLE_TRAVELS;
            if (onlyFavorites) {
                query += " WHERE " + COLUMN_IS_FAVORITE + "=1";
            }

            cursor = db.rawQuery(query, null);

            if (cursor != null && cursor.moveToFirst()) {
                do { //cursor üzerinde gezinip verilern alınması
                    Travel travel = new Travel(
                            cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_ID)),
                            cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_CITY)),
                            cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_NOTE)),
                            cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_MAP_LINK)),
                            cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_IS_FAVORITE)) == 1
                    );
                    travels.add(travel);
                } while (cursor.moveToNext());
            }
        } finally {
            if (cursor != null) cursor.close();
            if (db != null) db.close();
        }

        return travels;
    }

    // Seyahat güncelleme
    public void updateTravel(int id, String city, String note, String mapLink, boolean isFavorite) {
        SQLiteDatabase db = null;
        try {
            db = this.getWritableDatabase();
            ContentValues values = new ContentValues();
            values.put(COLUMN_CITY, city);
            values.put(COLUMN_NOTE, note);
            values.put(COLUMN_MAP_LINK, mapLink);
            values.put(COLUMN_IS_FAVORITE, isFavorite ? 1 : 0);
            db.update(TABLE_TRAVELS, values, COLUMN_ID + "=?", new String[]{String.valueOf(id)});
        } finally {
            if (db != null) db.close();
        }
    }

    // Seyahat silme
    public void deleteTravel(int id) {
        SQLiteDatabase db = null;
        try {
            db = this.getWritableDatabase();
            db.delete(TABLE_TRAVELS, COLUMN_ID + "=?", new String[]{String.valueOf(id)});
        } finally {
            if (db != null) db.close();
        }
    }
}
