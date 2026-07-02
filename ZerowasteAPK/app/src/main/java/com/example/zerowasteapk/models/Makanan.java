package com.example.zerowasteapk.models;

import java.io.Serializable;
import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

public class Makanan extends RealmObject implements Serializable {
    @PrimaryKey
    private int id;
    private String nama;
    private String porsi;
    private String lokasi;
    private String deskripsi;
    private String sisaWaktu;
    private String uploadedBy; // Nama pengunggah
    private boolean isClaimed;

    public Makanan() {
    }

    public Makanan(int id, String nama, String porsi, String lokasi, String deskripsi, String sisaWaktu, String uploadedBy) {
        this.id = id;
        this.nama = nama;
        this.porsi = porsi;
        this.lokasi = lokasi;
        this.deskripsi = deskripsi;
        this.sisaWaktu = sisaWaktu;
        this.uploadedBy = uploadedBy;
        this.isClaimed = false;
    }

    // Getter dan Setter
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }
    
    public String getNama() { return nama; }
    public void setNama(String nama) { this.nama = nama; }
    
    public String getPorsi() { return porsi; }
    public void setPorsi(String porsi) { this.porsi = porsi; }
    
    public String getLokasi() { return lokasi; }
    public void setLokasi(String lokasi) { this.lokasi = lokasi; }
    
    public String getDeskripsi() { return deskripsi; }
    public void setDeskripsi(String deskripsi) { this.deskripsi = deskripsi; }
    
    public String getSisaWaktu() { return sisaWaktu; }
    public void setSisaWaktu(String sisaWaktu) { this.sisaWaktu = sisaWaktu; }

    public String getUploadedBy() { return uploadedBy; }
    public void setUploadedBy(String uploadedBy) { this.uploadedBy = uploadedBy; }

    public boolean isClaimed() { return isClaimed; }
    public void setClaimed(boolean claimed) { isClaimed = claimed; }
}
