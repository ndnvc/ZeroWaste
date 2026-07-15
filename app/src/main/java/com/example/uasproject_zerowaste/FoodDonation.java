package com.example.uasproject_zerowaste;

public class FoodDonation {
    private String donationId, foodName, description, expiryTime, status, location, uploaderId;
    private int quantity;
    private double distance;

    public FoodDonation(String donationId, String foodName, String description, int quantity, String expiryTime, String status, String location, double distance) {
        this.donationId = donationId;
        this.foodName = foodName;
        this.description = description;
        this.quantity = quantity;
        this.expiryTime = expiryTime;
        this.status = status;
        this.location = location;
        this.distance = distance;
        this.uploaderId = "unknown_user";
    }

    public FoodDonation(String donationId, String foodName, String description, int quantity, String expiryTime, String status, String location, double distance, String uploaderId) {
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

    public String getDonationId() { return donationId; }
    public String getFoodName() { return foodName; }
    public String getDescription() { return description; }
    public int getQuantity() { return quantity; }
    public String getExpiryTime() { return expiryTime; }
    public String getStatus() { return status; }
    public String getLocation() { return location; }
    public double getDistance() { return distance; }
    public String getUploaderId() { return uploaderId; }

    public void setDistance(double distance) {
        this.distance = distance;
    }
}