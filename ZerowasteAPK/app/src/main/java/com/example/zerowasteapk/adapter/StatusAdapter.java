package com.example.zerowasteapk.adapter;

import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.example.zerowasteapk.R;
import com.example.zerowasteapk.models.Transaksi;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Locale;

public class StatusAdapter extends RecyclerView.Adapter<StatusAdapter.StatusViewHolder> {

    private List<Transaksi> listTransaksi;
    private SimpleDateFormat dateFormat = new SimpleDateFormat("dd MMM yyyy, HH:mm", Locale.getDefault());

    public StatusAdapter(List<Transaksi> listTransaksi) {
        this.listTransaksi = listTransaksi;
    }

    @NonNull
    @Override
    public StatusViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_status, parent, false);
        return new StatusViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull StatusViewHolder holder, int position) {
        Transaksi transaksi = listTransaksi.get(position);
        holder.tvNama.setText(transaksi.getNamaMakanan());
        
        String tanggalStr = transaksi.getTanggal() != null ? dateFormat.format(transaksi.getTanggal()) : "-";
        holder.tvLokasi.setText("Waktu: " + tanggalStr);

        holder.tvStatus.setText("Status: " + transaksi.getStatus());
        if ("Berhasil".equalsIgnoreCase(transaksi.getStatus())) {
            holder.tvStatus.setTextColor(Color.parseColor("#2E7D32")); // Hijau
        } else {
            holder.tvStatus.setTextColor(Color.parseColor("#FFA000")); // Oranye
        }
    }

    @Override
    public int getItemCount() {
        return listTransaksi.size();
    }

    public static class StatusViewHolder extends RecyclerView.ViewHolder {
        TextView tvNama, tvStatus, tvLokasi;

        public StatusViewHolder(@NonNull View itemView) {
            super(itemView);
            tvNama = itemView.findViewById(R.id.tvStatusNama);
            tvStatus = itemView.findViewById(R.id.tvStatusLabel);
            tvLokasi = itemView.findViewById(R.id.tvStatusLokasi);
        }
    }
}
