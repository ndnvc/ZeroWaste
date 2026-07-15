package com.example.uasproject_zerowaste;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import java.util.ArrayList;

public class HistoryActivity extends AppCompatActivity {
    private ListView lvHistory;
    private Button btnMyDonations, btnMyClaims;
    private UserRepository userRepository;
    private String currentUserId;
    private ArrayList<FoodDonation> historyList;
    private ArrayAdapter<FoodDonation> adapter;
    private boolean isViewingDonations = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history);

        lvHistory = findViewById(R.id.lvHistory);
        btnMyDonations = findViewById(R.id.btnMyDonations);
        btnMyClaims = findViewById(R.id.btnMyClaims);
        userRepository = new UserRepository(this);

        currentUserId = getIntent().getStringExtra("USER_ID");
        if (currentUserId == null) currentUserId = "unknown_user";

        historyList = new ArrayList<>();

        adapter = new ArrayAdapter<FoodDonation>(this, R.layout.item_history, historyList) {
            @NonNull
            @Override
            public View getView(int position, View convertView, @NonNull ViewGroup parent) {
                if (convertView == null) {
                    convertView = LayoutInflater.from(getContext()).inflate(R.layout.item_history, parent, false);
                }
                FoodDonation food = getItem(position);
                
                TextView tvTitle = convertView.findViewById(R.id.tvHistoryTitle);
                TextView tvStatusBadge = convertView.findViewById(R.id.tvHistoryStatusBadge);
                TextView tvDetails = convertView.findViewById(R.id.tvHistoryDetails);
                TextView tvQty = convertView.findViewById(R.id.tvHistoryQty);
                TextView tvExpiry = convertView.findViewById(R.id.tvHistoryExpiry);
                Button btnCancel = convertView.findViewById(R.id.btnCancelHistoryDonation);
                
                tvTitle.setText(food.getFoodName());
                tvStatusBadge.setText(food.getStatus());
                
                // Atur warna badge status
                if (food.getStatus().equalsIgnoreCase("Habis")) {
                    tvStatusBadge.setBackgroundColor(android.graphics.Color.parseColor("#D32F2F"));
                } else {
                    tvStatusBadge.setBackgroundColor(android.graphics.Color.parseColor("#2E7D32"));
                }

                String details = food.getDescription();
                if (details == null || details.isEmpty()) details = "Tidak ada deskripsi.";
                tvDetails.setText(details + "\nLokasi: " + food.getLocation());
                
                tvQty.setText("Porsi: " + food.getQuantity() + " Pax");
                tvExpiry.setText("Batas: " + food.getExpiryTime());
                
                if (isViewingDonations) {
                    btnCancel.setVisibility(View.VISIBLE);
                    btnCancel.setOnClickListener(v -> {
                        new AlertDialog.Builder(HistoryActivity.this)
                                .setTitle("Batalkan Donasi")
                                .setMessage("Apakah anda yakin ingin membatalkan donasi ini?")
                                .setPositiveButton("Ya, Batalkan", (dialog, which) -> {
                                    boolean success = userRepository.deleteFoodDonation(food.getDonationId());
                                    if (success) {
                                        Toast.makeText(HistoryActivity.this, "Donasi berhasil dibatalkan.", Toast.LENGTH_SHORT).show();
                                        historyList.remove(position);
                                        notifyDataSetChanged();
                                    } else {
                                        Toast.makeText(HistoryActivity.this, "Gagal membatalkan donasi.", Toast.LENGTH_SHORT).show();
                                    }
                                })
                                .setNegativeButton("Tidak", null)
                                .show();
                    });
                } else {
                    btnCancel.setVisibility(View.GONE);
                }
                
                return convertView;
            }
        };
        lvHistory.setAdapter(adapter);

        btnMyDonations.setOnClickListener(v -> loadDonations());
        btnMyClaims.setOnClickListener(v -> loadClaims());
        loadDonations();
    }

    private void loadDonations() {
        isViewingDonations = true;
        historyList.clear();
        historyList.addAll(userRepository.getMyUploadedDonations(currentUserId));
        adapter.notifyDataSetChanged();
        btnMyDonations.setBackgroundColor(android.graphics.Color.parseColor("#2E7D32"));
        btnMyClaims.setBackgroundColor(android.graphics.Color.GRAY);
    }

    private void loadClaims() {
        isViewingDonations = false;
        historyList.clear();
        historyList.addAll(userRepository.getMyClaimedHistory(currentUserId));
        adapter.notifyDataSetChanged();
        btnMyClaims.setBackgroundColor(android.graphics.Color.parseColor("#2E7D32"));
        btnMyDonations.setBackgroundColor(android.graphics.Color.GRAY);
    }
}