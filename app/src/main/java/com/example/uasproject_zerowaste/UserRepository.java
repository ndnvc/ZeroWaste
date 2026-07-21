package com.example.uasproject_zerowaste;

import android.content.Context;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.UUID;

public class UserRepository {

    private final UserDao userDao;
    private final FoodDao foodDao;

    public UserRepository(Context context) {
        AppDatabase db = AppDatabase.getInstance(context);
        this.userDao = db.userDao();
        this.foodDao = db.foodDao();
        seedDummyFoodData();
    }

    private void seedDummyFoodData() {
        if (foodDao.getFoodCount() == 0) {
            uploadFoodDonation("Nasi Goreng Spesial", "Masih hangat, sisa acara.", 5, "22:00", "UPH Karawaci", 0.0, "unknown_user");
            uploadFoodDonation("Roti Bakar", "Roti gandum utuh.", 2, "20:00", "Lippo Village", 0.0, "unknown_user");
            uploadFoodDonation("Salad Buah", "Segar, baru dibuat pagi.", 3, "18:00", "Gading Serpong", 0.0, "unknown_user");
        }
    }

    public boolean registerUser(String name, String email, String password, String phone) {
        if (userDao.getUserByEmail(email) != null) {
            return false; // Email duplikat
        }
        User newUser = new User(UUID.randomUUID().toString(), name, email, password, phone, "");
        userDao.registerUser(newUser);
        return true;
    }

    public User loginUser(String email, String password) {
        return userDao.loginUser(email, password);
    }

    public User getUserById(String userId) {
        return userDao.getUserById(userId);
    }

    public boolean updateUserAddress(String userId, String address) {
        return userDao.updateUserAddress(userId, address) > 0;
    }

    public String getUserAddress(String userId) {
        if (userId == null) return "";
        String address = userDao.getUserAddress(userId);
        return address != null ? address : "";
    }

    public boolean uploadFoodDonation(String foodName, String description, int quantity, String expiryTime, String location, double distance) {
        return uploadFoodDonation(foodName, description, quantity, expiryTime, location, distance, "unknown_user");
    }

    public boolean uploadFoodDonation(String foodName, String description, int quantity, String expiryTime, String location, double distance, String uploaderId) {
        FoodDonation newFood = new FoodDonation(
                UUID.randomUUID().toString(),
                foodName,
                description,
                quantity,
                expiryTime,
                "Tersedia",
                location,
                distance,
                uploaderId != null ? uploaderId : "unknown_user"
        );
        foodDao.insertFood(newFood);
        return true;
    }

    public boolean deleteFoodDonation(String donationId) {
        return foodDao.deleteFoodDonation(donationId) > 0;
    }

    public ArrayList<FoodDonation> getAllFoodDonations() {
        List<FoodDonation> list = foodDao.getAllFoodDonations();
        return new ArrayList<>(list);
    }

    public boolean claimFoodDonation(String donationId) {
        return claimFoodDonation(donationId, "unknown_user");
    }

    public boolean claimFoodDonation(String donationId, String userId) {
        FoodDonation food = foodDao.getFoodById(donationId);
        if (food == null || food.getQuantity() <= 0) {
            return false;
        }

        int newQty = food.getQuantity() - 1;
        food.setQuantity(newQty);

        if (newQty == 0) {
            food.setStatus("Habis");
        }

        foodDao.updateFood(food);

        // Tambah ke riwayat klaim
        String currentDate = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(new Date());
        ClaimHistory claimHistory = new ClaimHistory(UUID.randomUUID().toString(), userId, donationId, currentDate);
        foodDao.insertClaimHistory(claimHistory);

        return true;
    }

    public ArrayList<FoodDonation> getMyUploadedDonations(String userId) {
        List<FoodDonation> list = foodDao.getMyUploadedDonations(userId);
        return new ArrayList<>(list);
    }

    public ArrayList<FoodDonation> getMyClaimedHistory(String userId) {
        List<FoodDonation> list = foodDao.getMyClaimedHistory(userId);
        return new ArrayList<>(list);
    }

    public void close() {
        // Room mengelola sesi koneksi secara otomatis
    }
}