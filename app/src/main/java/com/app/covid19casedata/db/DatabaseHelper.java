package com.app.covid19casedata.db;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.app.covid19casedata.data.CaseData;

import java.util.ArrayList;
import java.util.List;
public class DatabaseHelper extends SQLiteOpenHelper {
    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_NAME = "Covid19";
    private static final String TABLE_CASEDATALIST = "CaseDataList";
    private static final String TABLE_FAVOURITE = "Favourite";
    private static final String KEY_ID = "id";
    private static final String KEY_COUNTRY = "country";
    private static final String KEY_DATE = "date";
    private static final String KEY_PROVINCE = "province";
    private static final String KEY_COUNT = "count";
    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }
    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_COUNTRY_TABLE = "CREATE TABLE " + TABLE_CASEDATALIST + "("
                + KEY_ID + " INTEGER PRIMARY KEY," + KEY_COUNTRY + " TEXT," + KEY_PROVINCE + " TEXT," + KEY_COUNT + " INTEGER,"
                + KEY_DATE + " TEXT,  UNIQUE("+KEY_COUNTRY+","+KEY_PROVINCE+","+ KEY_DATE +") ON CONFLICT REPLACE)";
        String CREATE_FAVOURITE_TABLE = "CREATE TABLE " + TABLE_FAVOURITE + "("
                + KEY_ID + " INTEGER PRIMARY KEY," + KEY_COUNTRY + " TEXT," + KEY_PROVINCE + " TEXT," + KEY_COUNT + " INTEGER,"
                + KEY_DATE + " TEXT,  UNIQUE("+KEY_COUNTRY+","+KEY_PROVINCE+","+ KEY_DATE +") ON CONFLICT REPLACE)";
        db.execSQL(CREATE_COUNTRY_TABLE);
        db.execSQL(CREATE_FAVOURITE_TABLE);
    }
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Drop older table if existed
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_CASEDATALIST);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_FAVOURITE);

        onCreate(db);
    }
    public long addCaseData(CaseData caseData) {
        try {
            SQLiteDatabase db = this.getWritableDatabase();

            ContentValues values = new ContentValues();
            values.put(KEY_COUNTRY, caseData.getCountry()); // Country Name
            values.put(KEY_DATE, caseData.getDate()); // Date
            values.put(KEY_PROVINCE, caseData.getProvince()); // Date
            values.put(KEY_COUNT, caseData.getCount()); // Date
            long status;
            status = db.insertOrThrow(TABLE_CASEDATALIST, null, values);
            db.close();
            return status;
        }catch (Exception e){
            return -1;
        }
    }
    public long addFavouriteCaseData(CaseData caseData) {
        try {
            SQLiteDatabase db = this.getWritableDatabase();
            ContentValues values = new ContentValues();
            values.put(KEY_COUNTRY, caseData.getCountry());
            values.put(KEY_DATE, caseData.getDate());
            values.put(KEY_PROVINCE, caseData.getProvince());
            values.put(KEY_COUNT, caseData.getCount());
            long status;
            status = db.insertOrThrow(TABLE_FAVOURITE, null, values);
            db.close();
            return status;
        }catch (Exception e){
            return -1;
        }

    }
    @SuppressLint("Range")
    public List<CaseData> getAllCountryDateList() {
        List<CaseData> caseDataList = new ArrayList<CaseData>();
        String selectQuery = "SELECT  * FROM " + TABLE_CASEDATALIST + " GROUP BY " + KEY_COUNTRY+","+ KEY_DATE ;

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);
        if (cursor.moveToFirst()) {
            do {
                CaseData caseData = new CaseData();
                caseData.setId(Integer.parseInt(cursor.getString(cursor.getColumnIndex(KEY_ID))));
                caseData.setCountry(cursor.getString(cursor.getColumnIndex(KEY_COUNTRY)));
                caseData.setDate(cursor.getString(cursor.getColumnIndex(KEY_DATE)));
                caseDataList.add(caseData);
            } while (cursor.moveToNext());
        }
        return caseDataList;
    }
    @SuppressLint("Range")
    public List<CaseData> getSearchedCountryDateList(String country,String fromDate,String toDate) {
        List<CaseData> caseDataList = new ArrayList<CaseData>();
        String selectQuery = "SELECT  * FROM " + TABLE_CASEDATALIST + " WHERE " + KEY_COUNTRY +" = '"+ country +"' COLLATE NOCASE AND "+ KEY_DATE + " between '" + fromDate +"' and '" + toDate + "' GROUP BY " + KEY_COUNTRY+","+ KEY_DATE ;
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);
        if (cursor.moveToFirst()) {
            do {
                CaseData caseData = new CaseData();
                caseData.setId(Integer.parseInt(cursor.getString(cursor.getColumnIndex(KEY_ID))));
                caseData.setCountry(cursor.getString(cursor.getColumnIndex(KEY_COUNTRY)));
                caseData.setDate(cursor.getString(cursor.getColumnIndex(KEY_DATE)));
                caseDataList.add(caseData);
            } while (cursor.moveToNext());
        }
        return caseDataList;
    }


    @SuppressLint("Range")
    public List<CaseData> getAllFavouriteList() {
        List<CaseData> caseDataList = new ArrayList<CaseData>();
        String selectQuery = "SELECT  * FROM " + TABLE_FAVOURITE + " GROUP BY " + KEY_COUNTRY+","+ KEY_DATE ;
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);
        if (cursor.moveToFirst()) {
            do {
                CaseData caseData = new CaseData();
                caseData.setId(Integer.parseInt(cursor.getString(cursor.getColumnIndex(KEY_ID))));
                caseData.setCountry(cursor.getString(cursor.getColumnIndex(KEY_COUNTRY)));
                caseData.setDate(cursor.getString(cursor.getColumnIndex(KEY_DATE)));
                caseDataList.add(caseData);
            } while (cursor.moveToNext());
        }
        return caseDataList;
    }
    @SuppressLint("Range")
    public List<CaseData> getAllProvince(String country,String date) {
        List<CaseData> caseDataList = new ArrayList<CaseData>();
        String selectQuery = "SELECT  * FROM " + TABLE_CASEDATALIST + " WHERE " + KEY_COUNTRY +" =? AND "+ KEY_DATE +" = ?";

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, new String[]{country,date});
        if (cursor.moveToFirst()) {
            do {
                CaseData caseData = new CaseData();
                caseData.setId(Integer.parseInt(cursor.getString(cursor.getColumnIndex(KEY_ID))));
                caseData.setProvince(cursor.getString(cursor.getColumnIndex(KEY_PROVINCE)));
                caseData.setCount(Integer.parseInt(cursor.getString(cursor.getColumnIndex(KEY_COUNT))));
                caseDataList.add(caseData);
            } while (cursor.moveToNext());
        }
        return caseDataList;
    }
    @SuppressLint("Range")
    public List<CaseData> getAllFavoriteProvince(String country,String date) {
        List<CaseData> caseDataList = new ArrayList<CaseData>();
        String selectQuery = "SELECT  * FROM " + TABLE_FAVOURITE + " WHERE " + KEY_COUNTRY + " =? AND " + KEY_DATE + " = ?";

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, new String[]{country, date});
        if (cursor.moveToFirst()) {
            do {
                CaseData caseData = new CaseData();
                caseData.setId(Integer.parseInt(cursor.getString(cursor.getColumnIndex(KEY_ID))));
                caseData.setProvince(cursor.getString(cursor.getColumnIndex(KEY_PROVINCE)));
                caseData.setCount(Integer.parseInt(cursor.getString(cursor.getColumnIndex(KEY_COUNT))));
                caseDataList.add(caseData);
            } while (cursor.moveToNext());
        }
        return caseDataList;
    }
    public int deleteCountryDate(CaseData caseData) {
        SQLiteDatabase db = this.getWritableDatabase();
        int status = db.delete(TABLE_CASEDATALIST, KEY_COUNTRY + " = ? AND " + KEY_DATE +" = ?" ,
                new String[] { caseData.getCountry(), caseData.getDate() });
        db.close();
        return status;
    }
    public void deleteFavourite(CaseData caseData) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_FAVOURITE, KEY_COUNTRY + " = ? AND " + KEY_DATE +" =?" ,
                new String[] { caseData.getCountry(), caseData.getDate() });
        db.close();
    }
}
