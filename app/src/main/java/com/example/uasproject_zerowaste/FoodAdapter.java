package com.example.uasproject_zerowaste;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class FoodAdapter extends RecyclerView.Adapter<FoodAdapter.FoodViewHolder> {

    private ArrayList<FoodDonation> foodList;
    private UserRepository userRepository;
    private String currentUserId;

    public FoodAdapter(ArrayList<FoodDonation> foodList) {
        this.foodList = foodList;
    }

    public FoodAdapter(ArrayList<FoodDonation> foodList, String currentUserId) {
        this.foodList = foodList;
        this.currentUserId = currentUserId;
    }

    public void updateData(ArrayList<FoodDonation> newList) {
        this.foodList = newList;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public FoodViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_food, parent, false);

        userRepository = new UserRepository(parent.getContext());

        return new FoodViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull FoodViewHolder holder, int position) {

        FoodDonation food = foodList.get(position);

        holder.tvFoodName.setText(food.getFoodName());

        // Menampilkan nama donatur
        String donorId = food.getUploaderId();
        User donor = userRepository.getUserById(donorId);
        if (donor != null) {
            holder.tvDonorName.setText("Donatur: " + donor.getName());
        } else {
            holder.tvDonorName.setText("Donatur: Anonim");
        }

        String deskripsi = food.getDescription();

        if (deskripsi == null || deskripsi.isEmpty()) {
            deskripsi = "Tidak ada deskripsi.";
        }

        holder.tvDescription.setText(
                deskripsi +
                        "\n\nLokasi : " + food.getLocation() +
                        "\nJarak : " + food.getDistance() + " km"
        );

        holder.tvQty.setText("Porsi : " + food.getQuantity() + " Pax");
        holder.tvExpiry.setText("Batas : " + food.getExpiryTime());
        holder.tvStatus.setText(food.getStatus());

        // Logika visibilitas tombol "Batalkan Donasi"
        if (currentUserId != null && currentUserId.equals(food.getUploaderId())) {
            holder.btnCancelDonation.setVisibility(View.VISIBLE);
            holder.btnClaim.setVisibility(View.GONE); // Pemilik tidak bisa klaim donasinya sendiri
        } else {
            holder.btnCancelDonation.setVisibility(View.GONE);
            holder.btnClaim.setVisibility(View.VISIBLE);
        }

        holder.btnCancelDonation.setOnClickListener(v -> {
            new AlertDialog.Builder(v.getContext())
                    .setTitle("Batalkan Donasi")
                    .setMessage("Apakah anda yakin ingin membatalkan donasi ini?")
                    .setPositiveButton("Ya, Batalkan", (dialog, which) -> {
                        boolean deleted = userRepository.deleteFoodDonation(food.getDonationId());
                        if (deleted) {
                            Toast.makeText(v.getContext(), "Donasi berhasil dibatalkan.", Toast.LENGTH_SHORT).show();
                            foodList.remove(position);
                            notifyItemRemoved(position);
                            notifyItemRangeChanged(position, foodList.size());
                        } else {
                            Toast.makeText(v.getContext(), "Gagal membatalkan donasi.", Toast.LENGTH_SHORT).show();
                        }
                    })
                    .setNegativeButton("Tidak", null)
                    .show();
        });

        if (food.getQuantity() <= 0 || food.getStatus().equalsIgnoreCase("Habis")) {

            holder.tvStatus.setText("Habis");
            holder.tvStatus.setBackgroundColor(Color.parseColor("#D32F2F"));

            holder.btnClaim.setEnabled(false);
            holder.btnClaim.setText("Stok Habis");
            holder.btnClaim.setBackgroundColor(Color.parseColor("#BDBDBD"));

            holder.btnClaim.setOnClickListener(null);

        } else {

            holder.tvStatus.setText("Tersedia");
            holder.tvStatus.setBackgroundColor(Color.parseColor("#2E7D32"));

            holder.btnClaim.setEnabled(true);
            holder.btnClaim.setText("Klaim Makanan");
            holder.btnClaim.setBackgroundColor(Color.parseColor("#1565C0"));

            holder.btnClaim.setOnClickListener(v -> {
                Intent intent = new Intent(v.getContext(), tampilan_barcode.class);
                intent.putExtra("DONATION_ID", food.getDonationId());
                intent.putExtra("FOOD_NAME", food.getFoodName());
                intent.putExtra("USER_ID", currentUserId);
                
                // Add more details
                User donorObj = userRepository.getUserById(food.getUploaderId());
                intent.putExtra("DONOR_NAME", donorObj != null ? donorObj.getName() : "Anonim");
                intent.putExtra("LOCATION", food.getLocation());
                intent.putExtra("DISTANCE", food.getDistance());
                intent.putExtra("EXPIRY", food.getExpiryTime());
                intent.putExtra("QTY", food.getQuantity());
                intent.putExtra("DESCRIPTION", food.getDescription());

                v.getContext().startActivity(intent);
            });

        }

    }

    @Override
    public int getItemCount() {
        return foodList.size();
    }

    public static class FoodViewHolder extends RecyclerView.ViewHolder {

        TextView tvFoodName;
        TextView tvDonorName;
        TextView tvDescription;
        TextView tvQty;
        TextView tvExpiry;
        TextView tvStatus;
        Button btnClaim;
        Button btnCancelDonation;

        public FoodViewHolder(@NonNull View itemView) {
            super(itemView);

            tvFoodName = itemView.findViewById(R.id.tvItemFoodName);
            tvDonorName = itemView.findViewById(R.id.tvItemDonorName);
            tvDescription = itemView.findViewById(R.id.tvItemDescription);
            tvQty = itemView.findViewById(R.id.tvItemQty);
            tvExpiry = itemView.findViewById(R.id.tvItemExpiry);
            tvStatus = itemView.findViewById(R.id.tvItemStatus);
            btnClaim = itemView.findViewById(R.id.btnItemClaim);
            btnCancelDonation = itemView.findViewById(R.id.btnItemCancelDonation);
        }
    }
}