package com.example.uasproject_zerowaste;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Spinner;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import java.util.ArrayList;

public class FoodListActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private FoodAdapter adapter;
    private UserRepository userRepository;
    private Spinner spinnerFilter;
    private ArrayList<FoodDonation> originalList; // Menyimpan list master asli

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_food_list);
        userRepository = new UserRepository(this);
        recyclerView = findViewById(R.id.rvFoodList);
        spinnerFilter = findViewById(R.id.spinnerDistanceFilter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        originalList = userRepository.getAllFoodDonations();
        if (originalList.isEmpty()) {
            Toast.makeText(this, "Belum ada donasi makanan saat ini.", Toast.LENGTH_SHORT).show();
        }
        adapter = new FoodAdapter(new ArrayList<>(originalList));
        recyclerView.setAdapter(adapter);
        spinnerFilter.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                filterDataByDistance(position);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {}
        });
    }

    private void filterDataByDistance(int filterOption) {
        ArrayList<FoodDonation> filteredList = new ArrayList<>();
        for (FoodDonation food : originalList) {
            if (filterOption == 0) {
                // Pilihan: "Semua Jarak"
                filteredList.add(food);
            } else if (filterOption == 1) {
                // Pilihan: "Terdekat (< 2 km)"
                if (food.getDistance() < 2.0) {
                    filteredList.add(food);
                }
            } else if (filterOption == 2) {
                // Pilihan: "Dekat (< 5 km)"
                if (food.getDistance() < 5.0) {
                    filteredList.add(food);
                }
            }
        }
        adapter.updateData(filteredList);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (userRepository != null) {
            userRepository.close();
        }
    }
}