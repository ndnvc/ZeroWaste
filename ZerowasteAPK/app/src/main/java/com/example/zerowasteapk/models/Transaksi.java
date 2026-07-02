package com.example.zerowasteapk.models;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;
import java.util.Date;

public class Transaksi extends RealmObject {
    @PrimaryKey
    private String id;
    private int makananId;
    private String namaMakanan;
    private String userEmail; // Email orang yang mengklaim
    private String status; // Contoh: "Pending", "Berhasil"
    private Date tanggal;

    public Transaksi() {}

    public Transaksi(String id, int makananId, String namaMakanan, String userEmail, String status, Date tanggal) {
        this.id = id;
        this.makananId = makananId;
        this.namaMakanan = namaMakanan;
        this.userEmail = userEmail;
        this.status = status;
        this.tanggal = tanggal;
    }

    // Getter dan Setter
    public String getId() { return id; }
    public void setId(String id) { this.id = id; }

    public int getMakananId() { return makananId; }
    public void setMakananId(int makananId) { this.makananId = makananId; }

    public String getNamaMakanan() { return namaMakanan; }
    public void setNamaMakanan(String namaMakanan) { this.namaMakanan = namaMakanan; }

    public String getUserEmail() { return userEmail; }
    public void setUserEmail(String userEmail) { this.userEmail = userEmail; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }

    public Date getTanggal() { return tanggal; }
    public void setTanggal(Date tanggal) { this.tanggal = tanggal; }
}
