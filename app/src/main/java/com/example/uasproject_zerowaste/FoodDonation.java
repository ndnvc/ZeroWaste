package com.example.uasproject_zerowaste;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

@Entity(tableName = "FoodDonation")
public class FoodDonation {

    @PrimaryKey
    @NonNull
    private String donationId;
    private String foodName;
    private String description;
    private int quantity;
    private String expiryTime;
    private String status;
    private String location;
    private double distance;
    private String uploaderId;

    public FoodDonation(@NonNull String donationId, String foodName, String description, int quantity,
                        String expiryTime, String status, String location, double distance, String uploaderId) {
        this.donationId = donationId;
        this.foodName = foodName;
        this.description = description;
        this.quantity = quantity;
        this.expiryTime = expiryTime;
        this.status = status;
        this.location = location;
        this.distance = distance;
        this.uploaderId = uploaderId;
    }

    @Ignore
    public FoodDonation(String donationId, String foodName, String description, int quantity, String expiryTime, String status, String location, double distance) {
        this(donationId, foodName, description, quantity, expiryTime, status, location, distance, "unknown_user");
    }

    @NonNull
    public String getDonationId() { return donationId; }
    public void setDonationId(@NonNull String donationId) { this.donationId = donationId; }

    public String getFoodName() { return foodName; }
    public void setFoodName(String foodName) { this.foodName = foodName; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public int getQuantity() { return quantity; }
    public void setQuantity(int quantity) { this.quantity = quantity; }

    public String getExpiryTime() { return expiryTime; }
    public void setExpiryTime(String expiryTime) { this.expiryTime = expiryTime; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }

    public String getLocation() { return location; }
    public void setLocation(String location) { this.location = location; }

    public double getDistance() { return distance; }
    public void setDistance(double distance) { this.distance = distance; }

    public String getUploaderId() { return uploaderId; }
    public void setUploaderId(String uploaderId) { this.uploaderId = uploaderId; }
}