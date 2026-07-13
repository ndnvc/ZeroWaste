package com.example.uasproject_zerowaste;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import java.util.ArrayList;

public class FoodAdapter extends RecyclerView.Adapter<FoodAdapter.FoodViewHolder> {

    private ArrayList<FoodDonation> foodList;
    private UserRepository userRepository;

    public FoodAdapter(ArrayList<FoodDonation> foodList) {
        this.foodList = foodList;
    }

    // UPDATE: Fungsi baru untuk memperbarui list dari Spinner filter di FoodListActivity
    public void updateData(ArrayList<FoodDonation> newList) {
        this.foodList = newList;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public FoodViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_food, parent, false);
        userRepository = new UserRepository(parent.getContext());
        return new FoodViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull FoodViewHolder holder, int position) {
        FoodDonation food = foodList.get(position);
        holder.tvFoodName.setText(food.getFoodName());

        // UPDATE: Menampilkan Deskripsi dibarengi dengan Lokasi & Jarak Dummy agar terlihat di UI kartu
        String deskripsiLengkap = food.getDescription().isEmpty() ? "Tidak ada deskripsi." : food.getDescription();
        holder.tvDescription.setText(deskripsiLengkap + "\n📍 " + food.getLocation() + " (" + food.getDistance() + " km)");

        holder.tvQty.setText("Porsi: " + food.getQuantity() + " Pax");
        holder.tvExpiry.setText("Batas: " + food.getExpiryTime());
        holder.tvStatus.setText(food.getStatus());

        // Memeriksa apakah statusnya "Habis" atau angka porsinya sudah 0
        if ("Habis".equalsIgnoreCase(food.getStatus()) || food.getQuantity() <= 0) {
            holder.tvStatus.setText("Habis");
            holder.tvStatus.setBackgroundColor(android.graphics.Color.parseColor("#D32F2F")); // Warna Merah
            holder.btnClaim.setEnabled(false);
            holder.btnClaim.setText("Stok Habis");
            holder.btnClaim.setBackgroundColor(android.graphics.Color.parseColor("#BDBDBD")); // Abu-abu pasif
            holder.btnClaim.setOnClickListener(null); // Menghapus fungsi klik
        } else {
            // Jika stok masih tersedia, tombol aktif berwarna biru untuk semua user
            holder.tvStatus.setBackgroundColor(android.graphics.Color.parseColor("#2E7D32")); // Warna Hijau
            holder.btnClaim.setEnabled(true);
            holder.btnClaim.setText("Klaim Makanan (Ambil 1)");
            holder.btnClaim.setBackgroundColor(android.graphics.Color.parseColor("#1565C0")); // Biru aktif

            holder.btnClaim.setOnClickListener(v -> {
                // Panggil fungsi pemotong stok di SQLite
                boolean isSuccess = userRepository.claimFoodDonation(food.getDonationId());
                if (isSuccess) {
                    Toast.makeText(v.getContext(), "Berhasil mengklaim 1 porsi " + food.getFoodName() + "!", Toast.LENGTH_SHORT).show();

                    // Hitung nilai kuantitas dan status baru secara real-time untuk layar HP
                    int updatedQty = food.getQuantity() - 1;
                    String updatedStatus = (updatedQty == 0) ? "Habis" : "Tersedia";

                    // UPDATE: Menyertakan food.getLocation() dan food.getDistance() agar data lokasi tidak hilang saat diklaim
                    foodList.set(position, new FoodDonation(
                            food.getDonationId(),
                            food.getFoodName(),
                            food.getDescription(),
                            updatedQty,
                            food.getExpiryTime(),
                            updatedStatus,
                            food.getLocation(),
                            food.getDistance()
                    ));

                    // Segarkan baris list ini agar perubahan langsung terlihat di layar
                    notifyItemChanged(position);
                } else {
                    Toast.makeText(v.getContext(), "Gagal mengklaim, stok mungkin sudah habis.", Toast.LENGTH_SHORT).show();
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        return foodList.size();
    }

    public static class FoodViewHolder extends RecyclerView.ViewHolder {
        TextView tvFoodName, tvDescription, tvQty, tvExpiry, tvStatus;
        Button btnClaim;

        public FoodViewHolder(@NonNull View itemView) {
            super(itemView);
            tvFoodName = itemView.findViewById(R.id.tvItemFoodName);
            tvDescription = itemView.findViewById(R.id.tvItemDescription);
            tvQty = itemView.findViewById(R.id.tvItemQty);
            tvExpiry = itemView.findViewById(R.id.tvItemExpiry);
            tvStatus = itemView.findViewById(R.id.tvItemStatus);
            btnClaim = itemView.findViewById(R.id.btnItemClaim);
        }
    }
}