package com.example.mcnotes;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteDatabase;
import android.content.Context;
import android.widget.Toast;

import androidx.annotation.Nullable;

class DatabaseHelper extends SQLiteOpenHelper {

    private Context context;
    private static final String dbName = "Notebookdb";
    private static final int dbVer = 1;
    private static final String TableName = "userNotes";
    private static final String columnID = "id";
    private static final String ColumnTitle = "title";
    private static final String ColumnContent = "content";

    public DatabaseHelper(@Nullable Context context) {

        super(context, dbName, null, dbVer);
        this.context = context;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String query = "CREATE TABLE " + TableName +
                "(" + columnID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                ColumnTitle + " TEXT, " +
                ColumnContent + " TEXT);";

        db.execSQL(query);

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TableName);
        onCreate(db);
    }


    void addNote(String title, String content) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues ContVal = new ContentValues();

        ContVal.put(ColumnTitle, title);
        ContVal.put(ColumnContent, content);
        long result = db.insert(TableName, null, ContVal);
        if (result == -1) {
            Toast.makeText(context, "adding to database failed", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(context, "Added successfully in database", Toast.LENGTH_SHORT).show();
        }

    }


    Cursor readAllData() {

        String query = "SELECT * FROM " + TableName;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = null;
        if (db != null) {
            cursor = db.rawQuery(query, null);
        }
        return cursor;
    }


    void updateData(int row_id, String title, String content) {

        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues ContVal = new ContentValues();
        ContVal.put(ColumnTitle, title);
        ContVal.put(ColumnContent, content);

        long result = db.update(TableName, ContVal, "id=?", new String[]{String.valueOf(row_id)});
        if (result == -1) {
            Toast.makeText(context, "Failed", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(context, "Updated Successfully!", Toast.LENGTH_SHORT).show();
        }

    }


    void deleteOneRow(int id) {
        SQLiteDatabase db = this.getWritableDatabase();
        long result = db.delete(TableName, "id=?", new String[]{String.valueOf(id)});
        if (result == -1) {
            Toast.makeText(context, "Failed to Delete.", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(context, "Successfully Deleted.", Toast.LENGTH_SHORT).show();
        }
    }

}