package com.example.uasproject_zerowaste;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import java.util.ArrayList;

public class HistoryActivity extends AppCompatActivity {
    private ListView lvHistory;
    private Button btnMyDonations, btnMyClaims;
    private UserRepository userRepository;
    private String currentUserId;
    private ArrayList<FoodDonation> historyList;
    private ArrayAdapter<FoodDonation> adapter;

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
                TextView tvStatus = convertView.findViewById(R.id.tvHistoryStatus);
                tvTitle.setText(food.getFoodName());
                tvStatus.setText("Status: " + food.getStatus() + " | Sisa: " + food.getQuantity() + " porsi");
                return convertView;
            }
        };
        lvHistory.setAdapter(adapter);

        btnMyDonations.setOnClickListener(v -> loadDonations());
        btnMyClaims.setOnClickListener(v -> loadClaims());
        loadDonations();
    }

    private void loadDonations() {
        historyList.clear();
        historyList.addAll(userRepository.getMyUploadedDonations(currentUserId));
        adapter.notifyDataSetChanged();
        btnMyDonations.setBackgroundColor(android.graphics.Color.parseColor("#2E7D32"));
        btnMyClaims.setBackgroundColor(android.graphics.Color.GRAY);
    }

    private void loadClaims() {
        historyList.clear();
        historyList.addAll(userRepository.getMyClaimedHistory(currentUserId));
        adapter.notifyDataSetChanged();
        btnMyClaims.setBackgroundColor(android.graphics.Color.parseColor("#2E7D32"));
        btnMyDonations.setBackgroundColor(android.graphics.Color.GRAY);
    }
}