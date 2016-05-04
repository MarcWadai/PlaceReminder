package com.fr.marcoucou.placereminder.DBLite;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

import com.fr.marcoucou.placereminder.model.PlaceCategory;
import com.fr.marcoucou.placereminder.model.Places;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Marc on 16/04/2016.
 */
public class PlacesDataSource {

    private SQLiteDatabase database;
    private SQLHelper databaseHelper;
    private SQLiteDatabase readabledb;
    private String[] allColumns = { SQLHelper.COLUMN_ID,SQLHelper.COLUMN_TITLE, SQLHelper.COLUMN_ADRESSE };
    public PlacesDataSource(Context context){
        databaseHelper = new SQLHelper(context);
    }

    public void open() throws SQLException {
        database = databaseHelper.getWritableDatabase();
        readabledb = databaseHelper.getReadableDatabase();
    }

    public void close() {
        databaseHelper.close();
    }

    public Places createPlaces(String titlePlace, String placeAdresse, PlaceCategory category){

        ContentValues values = new ContentValues();
        values.put(SQLHelper.COLUMN_TITLE, titlePlace);
        values.put(SQLHelper.COLUMN_ADRESSE, placeAdresse);
        values.put(SQLHelper.COLUMN_CATEGORY, category.getCategoryId());
        long insertId = database.insert(SQLHelper.TABLE_PLACES, null,
                values);
        Cursor cursor = readabledb.query(SQLHelper.TABLE_PLACES,
                allColumns, SQLHelper.COLUMN_ID + " = " + insertId, null,
                null, null, null);
        cursor.moveToFirst();
        Places newPlaces = cursorToPlace(cursor);
        cursor.close();
        return newPlaces;
    }

    private Places cursorToPlace(Cursor cursor) {
        Places place = new Places();
        place.setPlaceId(cursor.getLong(0));
        place.setTitle(cursor.getString(SQLHelper.COLUMN_TITLE_ID));
        place.setAdresse(cursor.getString(SQLHelper.COLUMN_ADRESSE_ID));
        return place;
    }


    public void deletePlaces(){

    }

    public ArrayList<Places> getAllPlaces(){
        ArrayList<Places> listPlaces = new ArrayList<Places>();
        Cursor  cursor = readabledb.rawQuery("select * from " +SQLHelper.TABLE_PLACES,null);
        if (cursor .moveToFirst()) {

            while (cursor.isAfterLast() == false) {
                Places placeGet = cursorToPlace(cursor);
                listPlaces.add(placeGet);
                cursor.moveToNext();
            }
        }
        return listPlaces;
    }

    public ArrayList<Places> getPlacesFromCategory(int placeCategory){
        ArrayList<Places> listPlaces = new ArrayList<Places>();
        String query = " SELECT * from " + SQLHelper.TABLE_PLACES + " WHERE " + SQLHelper.COLUMN_CATEGORY + " = " + placeCategory;
        Cursor  cursor = readabledb.rawQuery(query, null);
        if (cursor .moveToFirst()) {

            while (cursor.isAfterLast() == false) {
                Places placeGet = cursorToPlace(cursor);
                listPlaces.add(placeGet);
                cursor.moveToNext();
            }
        }
        return listPlaces;
    }
}
