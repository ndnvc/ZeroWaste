package com.example.uasproject_zerowaste;

public class FoodDonation {
    private String donationId;
    private String foodName;
    private String description;
    private int quantity;
    private String expiryTime;
    private String status;
    // UPDATE: Tambahkan variabel baru untuk menampung lokasi teks dan jarak angka dummy
    private String location;
    private double distance; // dalam satuan km

    // UPDATE: Konstruktor diperbarui untuk menginisialisasi lokasi dan jarak dummy
    public FoodDonation(String donationId, String foodName, String description, int quantity, String expiryTime, String status, String location, double distance) {
        this.donationId = donationId;
        this.foodName = foodName;
        this.description = description;
        this.quantity = quantity;
        this.expiryTime = expiryTime;
        this.status = status;
        this.location = location;
        this.distance = distance;
    }

    // Getter bawaan sebelumnya
    public String getDonationId() { return donationId; }
    public String getFoodName() { return foodName; }
    public String getDescription() { return description; }
    public int getQuantity() { return quantity; }
    public String getExpiryTime() { return expiryTime; }
    public String getStatus() { return status; }

    // UPDATE: Tambahkan Getter baru untuk lokasi dan jarak
    public String getLocation() { return location; }
    public double getDistance() { return distance; }
}