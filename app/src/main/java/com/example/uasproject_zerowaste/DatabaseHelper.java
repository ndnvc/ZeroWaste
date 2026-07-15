package com.example.uasproject_zerowaste;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DatabaseHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "zerowaste_native.db";
    private static final int DATABASE_VERSION = 4;

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String createTableUser = "CREATE TABLE User (" +
                "userId TEXT PRIMARY KEY, name TEXT, email TEXT UNIQUE, " +
                "password TEXT, phoneNumber TEXT, address TEXT)";
        db.execSQL(createTableUser);

        String createTableFood = "CREATE TABLE FoodDonation (" +
                "donationId TEXT PRIMARY KEY, foodName TEXT, description TEXT, " +
                "quantity INTEGER, expiryTime TEXT, status TEXT, " +
                "location TEXT, distance REAL, uploaderId TEXT)";
        db.execSQL(createTableFood);

        String createTableClaimHistory = "CREATE TABLE ClaimHistory (" +
                "claimId TEXT PRIMARY KEY, userId TEXT, donationId TEXT, claimDate TEXT)";
        db.execSQL(createTableClaimHistory);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS User");
        db.execSQL("DROP TABLE IF EXISTS FoodDonation");
        db.execSQL("DROP TABLE IF EXISTS ClaimHistory");
        onCreate(db);
    }
}