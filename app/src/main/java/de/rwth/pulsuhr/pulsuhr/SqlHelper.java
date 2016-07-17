package de.rwth.pulsuhr.pulsuhr;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.widget.EditText;


/**
 * Created by steffen on 16.06.16.
 */
public class SqlHelper extends SQLiteOpenHelper {

    public static final int DATABASE_VERSION = 1;
    public static final String DATABASE_NAME = "PulsUhrData.db";
    public static final String SQL_CREATE_ENTRIES = "CREATE TABLE Graphs (Timestamp INTEGER PRIMARY KEY, Measurement BLOB, Pulse INTEGER, Comment TEXT, Rating INTEGER)";
    public static final String SQL_DELETE_ENTRIES = "DROP TABLE IF EXISTS Graphs";

    public SqlHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    public void onCreate(SQLiteDatabase db) {
        db.execSQL(SQL_CREATE_ENTRIES);
    }

    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // This database is only a cache for online data, so its upgrade policy is
        // to simply to discard the data and start over
        db.execSQL(SQL_DELETE_ENTRIES);
        onCreate(db);
    }

    public boolean addMeasurement(String measurement, String pulse, String comment, float rating  ){

        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        String timeStamp = String.valueOf(System.currentTimeMillis());
        contentValues.put("Timestamp",timeStamp);
        contentValues.put("Measurement",measurement);
        contentValues.put("Pulse", pulse);
        contentValues.put("Comment", comment);
        contentValues.put("Rating", rating);
        long Result = db.insert("Graphs",null ,contentValues);
        if(Result == -1)
            return false;
        else
            return true;
    }

    public Cursor showMeasurement(){
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor res = db.rawQuery("select * from Graphs",null);
        return res;
    }

    public Integer deleteMeasurment(String position){
        SQLiteDatabase db = this.getWritableDatabase();
        return db.delete("Graphs",position,null);
    }

}
