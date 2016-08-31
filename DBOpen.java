package com.example.tuananh.manhinhchinh;

import android.content.Context;
import android.database.DatabaseErrorHandler;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by TUANANH on 19-May-16.
 */
public class DBOpen extends SQLiteOpenHelper {
    @Override
    public void onCreate(SQLiteDatabase db) {
        String createTableSport = "CREATE TABLE SPORTS (name text, imageID integer, calories decimal, imageBodyID integer, enable integer, constSport decimal);";
        String createUser = "CREATE TABLE USER (age integer, gender text, weight decimal, height decimal);";
//        String createTableRun = "CREATE TABLE Run (name text, steps integer, distance double, calories decimal);";
        db.execSQL(createTableSport);
//        db.execSQL(createTableRun);
        //db.execSQL(createUser);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

    public DBOpen(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    public DBOpen(Context context, String name, SQLiteDatabase.CursorFactory factory, int version, DatabaseErrorHandler errorHandler) {
        super(context, name, factory, version, errorHandler);
    }
}
