package com.example.uasproject_zerowaste;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;
import java.util.UUID;

public class UserRepository {

    private DatabaseHelper dbHelper;
    private SQLiteDatabase db;

    public UserRepository(Context context) {
        dbHelper = new DatabaseHelper(context);
        db = dbHelper.getWritableDatabase();
        seedDummyFoodData(); // Seed data if empty
    }

    private void seedDummyFoodData() {
        Cursor cursor = db.rawQuery("SELECT COUNT(*) FROM FoodDonation", null);
        cursor.moveToFirst();
        int count = cursor.getInt(0);
        cursor.close();

        if (count == 0) {
            // Data Dummy yang sesuai dengan Mock Coordinates di LocationUtils
            uploadFoodDonation("Nasi Goreng Spesial", "Masih hangat, sisa acara.", 5, "22:00", "UPH Karawaci", 0);
            uploadFoodDonation("Roti Bakar", "Roti gandum utuh.", 2, "20:00", "Lippo Village", 0);
            uploadFoodDonation("Salad Buah", "Segar, baru dibuat pagi.", 3, "18:00", "Gading Serpong", 0);
            uploadFoodDonation("Paket Nasi Box", "Menu ayam bakar.", 10, "21:00", "Alam Sutera", 0);
            uploadFoodDonation("Kue Basah", "Aneka jajanan pasar.", 15, "17:00", "Tangerang Kota", 0);
            uploadFoodDonation("Mie Ayam", "Porsi cukup besar.", 4, "19:00", "BSD City", 0);
            uploadFoodDonation("Donat Assorted", "Satu lusin donat.", 1, "23:00", "AEON Mall", 0);
        }
    }

    public boolean registerUser(String name, String email, String password, String phone) {

        Cursor cursor = db.rawQuery(
                "SELECT * FROM User WHERE email = ?",
                new String[]{email});

        if (cursor.getCount() > 0) {
            cursor.close();
            return false;
        }

        cursor.close();

        ContentValues values = new ContentValues();
        values.put("userId", UUID.randomUUID().toString());
        values.put("name", name);
        values.put("email", email);
        values.put("password", password);
        values.put("phoneNumber", phone);
        values.put("address", "");

        long result = db.insert("User", null, values);

        return result != -1;
    }

    public User loginUser(String email, String password) {

        Cursor cursor = db.rawQuery(
                "SELECT * FROM User WHERE email=? AND password=?",
                new String[]{email, password});

        if (cursor.moveToFirst()) {

            User user = new User(
                    cursor.getString(cursor.getColumnIndexOrThrow("userId")),
                    cursor.getString(cursor.getColumnIndexOrThrow("name")),
                    cursor.getString(cursor.getColumnIndexOrThrow("email")),
                    cursor.getString(cursor.getColumnIndexOrThrow("password")),
                    cursor.getString(cursor.getColumnIndexOrThrow("phoneNumber"))
            );

            cursor.close();
            return user;
        }

        cursor.close();
        return null;
    }

    public User getUserById(String userId) {

        Cursor cursor = db.rawQuery(
                "SELECT * FROM User WHERE userId=?",
                new String[]{userId});

        if (cursor.moveToFirst()) {

            User user = new User(
                    cursor.getString(cursor.getColumnIndexOrThrow("userId")),
                    cursor.getString(cursor.getColumnIndexOrThrow("name")),
                    cursor.getString(cursor.getColumnIndexOrThrow("email")),
                    cursor.getString(cursor.getColumnIndexOrThrow("password")),
                    cursor.getString(cursor.getColumnIndexOrThrow("phoneNumber"))
            );

            cursor.close();
            return user;
        }

        cursor.close();
        return null;
    }

    public boolean updateUserAddress(String userId, String address) {
        ContentValues values = new ContentValues();
        values.put("address", address);
        int rows = db.update("User", values, "userId=?", new String[]{userId});
        return rows > 0;
    }

    public String getUserAddress(String userId) {
        if (userId == null) return "";
        Cursor cursor = db.rawQuery("SELECT address FROM User WHERE userId=?", new String[]{userId});
        String address = "";
        if (cursor.moveToFirst()) {
            address = cursor.getString(0);
        }
        cursor.close();
        return address != null ? address : "";
    }

    public boolean uploadFoodDonation(String foodName,
                                      String description,
                                      int quantity,
                                      String expiryTime,
                                      String location,
                                      double distance) {

        ContentValues values = new ContentValues();

        values.put("donationId", UUID.randomUUID().toString());
        values.put("foodName", foodName);
        values.put("description", description);
        values.put("quantity", quantity);
        values.put("expiryTime", expiryTime);
        values.put("status", "Tersedia");
        values.put("location", location);
        values.put("distance", distance);

        long result = db.insert("FoodDonation", null, values);

        return result != -1;
    }

    public ArrayList<FoodDonation> getAllFoodDonations() {

        ArrayList<FoodDonation> list = new ArrayList<>();

        Cursor cursor = db.rawQuery(
                "SELECT * FROM FoodDonation ORDER BY rowid DESC",
                null);

        if (cursor.moveToFirst()) {

            do {

                FoodDonation food = new FoodDonation(

                        cursor.getString(cursor.getColumnIndexOrThrow("donationId")),
                        cursor.getString(cursor.getColumnIndexOrThrow("foodName")),
                        cursor.getString(cursor.getColumnIndexOrThrow("description")),
                        cursor.getInt(cursor.getColumnIndexOrThrow("quantity")),
                        cursor.getString(cursor.getColumnIndexOrThrow("expiryTime")),
                        cursor.getString(cursor.getColumnIndexOrThrow("status")),
                        cursor.getString(cursor.getColumnIndexOrThrow("location")),
                        cursor.getDouble(cursor.getColumnIndexOrThrow("distance"))

                );

                list.add(food);

            } while (cursor.moveToNext());

        }

        cursor.close();

        return list;
    }

    public boolean claimFoodDonation(String donationId) {

        int currentQty = 0;

        Cursor cursor = db.rawQuery(
                "SELECT quantity FROM FoodDonation WHERE donationId=?",
                new String[]{donationId});

        if (cursor.moveToFirst()) {
            currentQty = cursor.getInt(0);
        }

        cursor.close();

        if (currentQty <= 0) {
            return false;
        }

        int newQty = currentQty - 1;

        ContentValues values = new ContentValues();

        values.put("quantity", newQty);

        if (newQty == 0) {
            values.put("status", "Habis");
        }

        int rows = db.update(
                "FoodDonation",
                values,
                "donationId=?",
                new String[]{donationId});

        return rows > 0;
    }

    public void close() {

        if (db != null && db.isOpen()) {
            db.close();
        }

        if (dbHelper != null) {
            dbHelper.close();
        }

    }

}