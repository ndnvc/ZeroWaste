package com.example.uasproject_zerowaste;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import java.util.UUID;

public class UserRepository {
    private DatabaseHelper dbHelper;
    private SQLiteDatabase db;

    public UserRepository(Context context) {
        this.dbHelper = new DatabaseHelper(context);
        this.db = dbHelper.getWritableDatabase();
    }

    public boolean registerUser(String name, String email, String password, String phone) {
        // Cek email duplikat
        Cursor cursor = db.rawQuery("SELECT * FROM User WHERE email = ?", new String[]{email});
        if (cursor.getCount() > 0) {
            cursor.close();
            return false;
        }
        cursor.close();

        // Insert data user baru
        ContentValues values = new ContentValues();
        values.put("userId", UUID.randomUUID().toString());
        values.put("name", name);
        values.put("email", email);
        values.put("password", password);
        values.put("phoneNumber", phone);

        long result = db.insert("User", null, values);
        return result != -1;
    }

    public User loginUser(String email, String password) {
        Cursor cursor = db.rawQuery("SELECT * FROM User WHERE email = ? AND password = ?", new String[]{email, password});

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
        Cursor cursor = db.rawQuery("SELECT * FROM User WHERE userId = ?", new String[]{userId});
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

    public boolean uploadFoodDonation(String foodName, String description, int quantity, String expiryTime) {
        ContentValues values = new ContentValues();
        values.put("donationId", UUID.randomUUID().toString());
        values.put("foodName", foodName);
        values.put("description", description);
        values.put("quantity", quantity);
        values.put("expiryTime", expiryTime);
        values.put("status", "Tersedia"); // Default status makanan baru

        long result = db.insert("FoodDonation", null, values);
        return result != -1;
    }

    // UPDATE: Menambahkan array dummy lokasi dan jarak saat mapping object
    public java.util.ArrayList<FoodDonation> getAllFoodDonations() {
        java.util.ArrayList<FoodDonation> list = new java.util.ArrayList<>();
        Cursor cursor = db.rawQuery("SELECT * FROM FoodDonation ORDER BY rowid DESC", null);

        // Data dummy untuk simulasi lokasi sekitar kampus/daerah terdekat
        String[] dummyLocations = {"Karawaci", "Gading Serpong", "Alam Sutera", "Tangerang Kota"};
        double[] dummyDistances = {1.2, 3.5, 4.8, 7.2};
        int index = 0;

        if (cursor.moveToFirst()) {
            do {
                // Selang-seling lokasi dan jarak dummy berdasarkan urutan data
                String loc = dummyLocations[index % dummyLocations.length];
                double dist = dummyDistances[index % dummyDistances.length];

                FoodDonation food = new FoodDonation(
                        cursor.getString(cursor.getColumnIndexOrThrow("donationId")),
                        cursor.getString(cursor.getColumnIndexOrThrow("foodName")),
                        cursor.getString(cursor.getColumnIndexOrThrow("description")),
                        cursor.getInt(cursor.getColumnIndexOrThrow("quantity")),
                        cursor.getString(cursor.getColumnIndexOrThrow("expiryTime")),
                        cursor.getString(cursor.getColumnIndexOrThrow("status")),
                        loc,  // Parameter ke-7: lokasi dummy
                        dist  // Parameter ke-8: jarak dummy (km)
                );
                list.add(food);
                index++;
            } while (cursor.moveToNext());
        }
        cursor.close();
        return list;
    }

    public boolean claimFoodDonation(String donationId) {
        int currentQty = 0;

        // 1. Ambil porsi stok saat ini berdasarkan ID makanan
        Cursor cursor = db.rawQuery("SELECT quantity FROM FoodDonation WHERE donationId = ?", new String[]{donationId});
        if (cursor.moveToFirst()) {
            currentQty = cursor.getInt(0);
        }
        cursor.close();

        // Jika makanan tidak sengaja terklik padahal sudah 0, gagalkan operasi
        if (currentQty <= 0) {
            return false;
        }

        // 2. Kurangi porsi saat ini dengan 1
        int newQty = currentQty - 1;

        ContentValues values = new ContentValues();
        values.put("quantity", newQty);

        // 3. Jika setelah dikurangi stok porsinya habis, set status menjadi Habis
        if (newQty == 0) {
            values.put("status", "Habis");
        }

        // 4. Update row di dalam database SQLite
        int rowsAffected = db.update("FoodDonation", values, "donationId = ?", new String[]{donationId});
        return rowsAffected > 0;
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