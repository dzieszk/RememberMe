package com.example.dzieszk.rememberme;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

/**
 * Created by dzieszk on 06.01.18.
 */

public class NoteDBHelper extends SQLiteOpenHelper {

    private static final String DB_NAME = "notes.db";
    private static final int DB_VERSION = 1;

    private Context context;

    public static final String TABLE_NOTES = "NOTE";
    public static final String COLUMN_ID = "NOTE_ID";
    public static final String COLUMN_TITLE = "NOTE_TITLE";
    public static final String COLUMN_CONTENT = "NOTE_CONTENT";

    private static final String TABLE_CREATE =
            "CREATE TABLE " + TABLE_NOTES + " (" +
                    COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    COLUMN_TITLE + " TEXT, " +
                    COLUMN_CONTENT + " TEXT" +
                    ")";

    public NoteDBHelper(Context context){
        super(context, DB_NAME, null, DB_VERSION);
        this.context = context;
    }

    public boolean addNote(String title, String content){
        SQLiteDatabase db = getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put(COLUMN_TITLE, title);
        cv.put(COLUMN_CONTENT, content);
        db.insert(TABLE_NOTES, null, cv);
        return true;
    }

    public Note getNote(int id){
        Note note = new Note();
        String query = "SELECT * FROM " + TABLE_NOTES +
                " WHERE " + COLUMN_ID + " = " + String.valueOf(id);
        Cursor cursor= getReadableDatabase().rawQuery(query, null);
        cursor.moveToNext();
        note.setTitle(cursor.getString(1));
        note.setContent(cursor.getString(2));
        cursor.close();
        return note;
    }

    public Note getNote(String title){
        String query = "SELECT * FROM " + TABLE_NOTES +
                " WHERE " + COLUMN_TITLE + " = '" + title +"'";
        Cursor cursor= getReadableDatabase().rawQuery(query, null);
        cursor.moveToNext();
        Note note = cursorToNote(cursor);
        cursor.close();
        return note;
    }

    public Note cursorToNote(Cursor cursor){
        Note note = new Note();
        note.setTitle(cursor.getString(1));
        note.setContent(cursor.getString(2));
        return note;
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL(TABLE_CREATE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + TABLE_NOTES);
        onCreate(sqLiteDatabase);
    }

    @Override
    public void onDowngrade(SQLiteDatabase sqLiteDatabase, int i, int j){
        onUpgrade(sqLiteDatabase, i, j);
    }
}
