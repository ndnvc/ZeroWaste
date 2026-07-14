package com.example.uasproject_zerowaste;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class FoodListActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private FoodAdapter adapter;
    private UserRepository userRepository;

    private Spinner spinnerFilter;
    private SearchView searchFood;

    private ArrayList<FoodDonation> originalList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_food_list);

        userRepository = new UserRepository(this);

        recyclerView = findViewById(R.id.rvFoodList);
        spinnerFilter = findViewById(R.id.spinnerDistanceFilter);
        searchFood = findViewById(R.id.searchFood);

        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        originalList = userRepository.getAllFoodDonations();

        if (originalList.isEmpty()) {
            Toast.makeText(
                    this,
                    "Belum ada donasi makanan saat ini.",
                    Toast.LENGTH_SHORT
            ).show();
        }

        adapter = new FoodAdapter(new ArrayList<>(originalList));
        recyclerView.setAdapter(adapter);

        // Filter berdasarkan jarak
        spinnerFilter.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                applySearchAndFilter();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }

        });

        // Search makanan
        searchFood.setOnQueryTextListener(new SearchView.OnQueryTextListener() {

            @Override
            public boolean onQueryTextSubmit(String query) {

                applySearchAndFilter();
                return true;

            }

            @Override
            public boolean onQueryTextChange(String newText) {

                applySearchAndFilter();
                return true;

            }

        });

    }

    // Search + Filter Jarak
    private void applySearchAndFilter() {

        ArrayList<FoodDonation> filteredList = new ArrayList<>();

        String keyword = searchFood.getQuery().toString().trim().toLowerCase();

        int filterOption = spinnerFilter.getSelectedItemPosition();

        for (FoodDonation food : originalList) {

            // Search berdasarkan nama makanan
            boolean cocokNama =
                    food.getFoodName().toLowerCase().contains(keyword);

            // Search berdasarkan deskripsi
            if (food.getDescription() != null) {
                cocokNama = cocokNama ||
                        food.getDescription().toLowerCase().contains(keyword);
            }

            boolean cocokJarak = false;

            switch (filterOption) {

                case 0:
                    cocokJarak = true;
                    break;

                case 1:
                    cocokJarak = food.getDistance() <= 2.0;
                    break;

                case 2:
                    cocokJarak = food.getDistance() <= 5.0;
                    break;

                case 3:
                    cocokJarak = food.getDistance() <= 10.0;
                    break;

            }

            if (cocokNama && cocokJarak) {
                filteredList.add(food);
            }

        }

        adapter.updateData(filteredList);

        if (filteredList.isEmpty()) {

            Toast.makeText(
                    this,
                    "Makanan tidak ditemukan.",
                    Toast.LENGTH_SHORT
            ).show();

        }

    }

    @Override
    protected void onResume() {
        super.onResume();

        originalList = userRepository.getAllFoodDonations();

        adapter.updateData(new ArrayList<>(originalList));

        applySearchAndFilter();

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        if (userRepository != null) {
            userRepository.close();
        }

    }

}