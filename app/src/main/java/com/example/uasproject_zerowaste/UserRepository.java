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
        seedDummyFoodData();
    }

    private void seedDummyFoodData() {
        Cursor cursor = db.rawQuery("SELECT COUNT(*) FROM FoodDonation", null);
        cursor.moveToFirst();
        int count = cursor.getInt(0);
        cursor.close();
        if (count == 0) {
            uploadFoodDonation("Nasi Goreng Spesial", "Masih hangat, sisa acara.", 5, "22:00", "UPH Karawaci", 0);
            uploadFoodDonation("Roti Bakar", "Roti gandum utuh.", 2, "20:00", "Lippo Village", 0);
            uploadFoodDonation("Salad Buah", "Segar, baru dibuat pagi.", 3, "18:00", "Gading Serpong", 0);
        }
    }

    public boolean registerUser(String name, String email, String password, String phone) {
        Cursor cursor = db.rawQuery("SELECT * FROM User WHERE email = ?", new String[]{email});
        if (cursor.getCount() > 0) { cursor.close(); return false; }
        cursor.close();
        ContentValues values = new ContentValues();
        values.put("userId", UUID.randomUUID().toString());
        values.put("name", name); values.put("email", email);
        values.put("password", password); values.put("phoneNumber", phone); values.put("address", "");
        return db.insert("User", null, values) != -1;
    }

    public User loginUser(String email, String password) {
        Cursor cursor = db.rawQuery("SELECT * FROM User WHERE email=? AND password=?", new String[]{email, password});
        if (cursor.moveToFirst()) {
            User user = new User(cursor.getString(cursor.getColumnIndexOrThrow("userId")), cursor.getString(cursor.getColumnIndexOrThrow("name")), cursor.getString(cursor.getColumnIndexOrThrow("email")), cursor.getString(cursor.getColumnIndexOrThrow("password")), cursor.getString(cursor.getColumnIndexOrThrow("phoneNumber")));
            cursor.close(); return user;
        }
        cursor.close(); return null;
    }

    public User getUserById(String userId) {
        Cursor cursor = db.rawQuery("SELECT * FROM User WHERE userId=?", new String[]{userId});
        if (cursor.moveToFirst()) {
            User user = new User(cursor.getString(cursor.getColumnIndexOrThrow("userId")), cursor.getString(cursor.getColumnIndexOrThrow("name")), cursor.getString(cursor.getColumnIndexOrThrow("email")), cursor.getString(cursor.getColumnIndexOrThrow("password")), cursor.getString(cursor.getColumnIndexOrThrow("phoneNumber")));
            cursor.close(); return user;
        }
        cursor.close(); return null;
    }

    public boolean updateUserAddress(String userId, String address) {
        ContentValues values = new ContentValues();
        values.put("address", address);
        return db.update("User", values, "userId=?", new String[]{userId}) > 0;
    }

    public String getUserAddress(String userId) {
        if (userId == null) return "";
        Cursor cursor = db.rawQuery("SELECT address FROM User WHERE userId=?", new String[]{userId});
        String address = "";
        if (cursor.moveToFirst()) address = cursor.getString(0);
        cursor.close(); return address;
    }

    public boolean uploadFoodDonation(String foodName, String description, int quantity, String expiryTime, String location, double distance) {
        return uploadFoodDonation(foodName, description, quantity, expiryTime, location, distance, "unknown_user");
    }

    public boolean uploadFoodDonation(String foodName, String description, int quantity, String expiryTime, String location, double distance, String uploaderId) {
        ContentValues values = new ContentValues();
        values.put("donationId", UUID.randomUUID().toString());
        values.put("foodName", foodName); values.put("description", description);
        values.put("quantity", quantity); values.put("expiryTime", expiryTime);
        values.put("status", "Tersedia"); values.put("location", location);
        values.put("distance", distance); values.put("uploaderId", uploaderId);
        return db.insert("FoodDonation", null, values) != -1;
    }

    public boolean deleteFoodDonation(String donationId) {
        return db.delete("FoodDonation", "donationId=?", new String[]{donationId}) > 0;
    }

    public ArrayList<FoodDonation> getAllFoodDonations() {
        ArrayList<FoodDonation> list = new ArrayList<>();
        Cursor cursor = db.rawQuery("SELECT * FROM FoodDonation ORDER BY rowid DESC", null);
        if (cursor.moveToFirst()) {
            do {
                list.add(new FoodDonation(
                        cursor.getString(cursor.getColumnIndexOrThrow("donationId")), cursor.getString(cursor.getColumnIndexOrThrow("foodName")),
                        cursor.getString(cursor.getColumnIndexOrThrow("description")), cursor.getInt(cursor.getColumnIndexOrThrow("quantity")),
                        cursor.getString(cursor.getColumnIndexOrThrow("expiryTime")), cursor.getString(cursor.getColumnIndexOrThrow("status")),
                        cursor.getString(cursor.getColumnIndexOrThrow("location")), cursor.getDouble(cursor.getColumnIndexOrThrow("distance")),
                        cursor.getString(cursor.getColumnIndexOrThrow("uploaderId"))));
            } while (cursor.moveToNext());
        }
        cursor.close(); return list;
    }

    public boolean claimFoodDonation(String donationId) {
        return claimFoodDonation(donationId, "unknown_user");
    }

    public boolean claimFoodDonation(String donationId, String userId) {
        int currentQty = 0;
        Cursor cursor = db.rawQuery("SELECT quantity FROM FoodDonation WHERE donationId=?", new String[]{donationId});
        if (cursor.moveToFirst()) currentQty = cursor.getInt(0);
        cursor.close();

        if (currentQty <= 0) return false;

        ContentValues values = new ContentValues();
        values.put("quantity", currentQty - 1);
        if ((currentQty - 1) == 0) values.put("status", "Habis");

        int rows = db.update("FoodDonation", values, "donationId=?", new String[]{donationId});
        if (rows > 0) {
            ContentValues claimHistory = new ContentValues();
            claimHistory.put("claimId", UUID.randomUUID().toString());
            claimHistory.put("userId", userId); claimHistory.put("donationId", donationId);
            claimHistory.put("claimDate", new java.text.SimpleDateFormat("yyyy-MM-dd", java.util.Locale.getDefault()).format(new java.util.Date()));
            db.insert("ClaimHistory", null, claimHistory);
        }
        return rows > 0;
    }

    public ArrayList<FoodDonation> getMyUploadedDonations(String userId) {
        ArrayList<FoodDonation> list = new ArrayList<>();
        Cursor cursor = db.rawQuery("SELECT * FROM FoodDonation WHERE uploaderId=? ORDER BY rowid DESC", new String[]{userId});
        if (cursor.moveToFirst()) {
            do {
                list.add(new FoodDonation(
                        cursor.getString(cursor.getColumnIndexOrThrow("donationId")), cursor.getString(cursor.getColumnIndexOrThrow("foodName")),
                        cursor.getString(cursor.getColumnIndexOrThrow("description")), cursor.getInt(cursor.getColumnIndexOrThrow("quantity")),
                        cursor.getString(cursor.getColumnIndexOrThrow("expiryTime")), cursor.getString(cursor.getColumnIndexOrThrow("status")),
                        cursor.getString(cursor.getColumnIndexOrThrow("location")), cursor.getDouble(cursor.getColumnIndexOrThrow("distance")),
                        cursor.getString(cursor.getColumnIndexOrThrow("uploaderId"))));
            } while (cursor.moveToNext());
        }
        cursor.close(); return list;
    }

    public ArrayList<FoodDonation> getMyClaimedHistory(String userId) {
        ArrayList<FoodDonation> list = new ArrayList<>();
        String query = "SELECT f.* FROM FoodDonation f INNER JOIN ClaimHistory c ON f.donationId = c.donationId WHERE c.userId = ? ORDER BY c.rowid DESC";
        Cursor cursor = db.rawQuery(query, new String[]{userId});
        if (cursor.moveToFirst()) {
            do {
                list.add(new FoodDonation(
                        cursor.getString(cursor.getColumnIndexOrThrow("donationId")), cursor.getString(cursor.getColumnIndexOrThrow("foodName")),
                        cursor.getString(cursor.getColumnIndexOrThrow("description")), cursor.getInt(cursor.getColumnIndexOrThrow("quantity")),
                        cursor.getString(cursor.getColumnIndexOrThrow("expiryTime")), cursor.getString(cursor.getColumnIndexOrThrow("status")),
                        cursor.getString(cursor.getColumnIndexOrThrow("location")), cursor.getDouble(cursor.getColumnIndexOrThrow("distance")),
                        cursor.getString(cursor.getColumnIndexOrThrow("uploaderId"))));
            } while (cursor.moveToNext());
        }
        cursor.close(); return list;
    }

    public void close() {
        if (db != null && db.isOpen()) db.close();
        if (dbHelper != null) dbHelper.close();
    }
}