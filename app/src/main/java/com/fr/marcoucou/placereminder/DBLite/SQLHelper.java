package com.fr.marcoucou.placereminder.DBLite;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

/**
 * Created by Marc on 16/04/2016.
 */
public class SQLHelper extends SQLiteOpenHelper {

    // constant to instanciate the table and comlumn
    public static final String TABLE_PLACES = "places";
    public static final String COLUMN_ID = "_id";
    public static final String COLUMN_TITLE = "title";
    public static final String COLUMN_ADRESSE = "adresse";
    public static final String COLUMN_CATEGORY = "category";

    // when fetching the column used
    public static final int COLUMN_TITLE_ID = 1;
    public static final int COLUMN_ADRESSE_ID = 2;

    //DATABASE Info
    private static final String DATABASE_NAME = "placereminder.db";
    private static final int DATABASE_VERSION = 1;

    // Database creation sql statement
    private static final String DATABASE_CREATE = "create table "
            + TABLE_PLACES + "(" + COLUMN_ID
            + " integer primary key autoincrement, " + COLUMN_TITLE
            + " text not null, " + COLUMN_ADRESSE + " text not null, " + COLUMN_CATEGORY + " integer);";

    // Variable to fetch the data in the db
    public static final String QUERY_GET_PLACES_IN_CATEGORY = " SELECT from * in " + TABLE_PLACES+ " WHERE "+ COLUMN_CATEGORY+ "= ?";


    public SQLHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }


    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(DATABASE_CREATE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        Log.w(SQLHelper.class.getName(),
                "Upgrading database from version " + oldVersion + " to "
                        + newVersion + ", which will destroy all old data");
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_PLACES);
        onCreate(db);
    }
}
