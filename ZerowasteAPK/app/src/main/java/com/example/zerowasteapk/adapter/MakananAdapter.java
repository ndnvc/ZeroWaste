package com.example.zerowasteapk.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.example.zerowasteapk.R;
import com.example.zerowasteapk.models.Makanan;
import java.util.List;

public class MakananAdapter extends RecyclerView.Adapter<MakananAdapter.MakananViewHolder> {

    private List<Makanan> listMakanan;
    private OnItemClickListener listener;

    public interface OnItemClickListener {
        void onItemClick(Makanan makanan);
    }

    public MakananAdapter(List<Makanan> listMakanan, OnItemClickListener listener) {
        this.listMakanan = listMakanan;
        this.listener = listener;
    }

    @NonNull
    @Override
    public MakananViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_makanan, parent, false);
        return new MakananViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MakananViewHolder holder, int position) {
        Makanan makanan = listMakanan.get(position);
        holder.tvNama.setText(makanan.getNama());
        holder.tvPorsi.setText("Porsi: " + makanan.getPorsi());
        holder.tvUploader.setText("Donatur: " + (makanan.getUploadedBy() != null ? makanan.getUploadedBy() : "Anonim"));
        holder.btnDetail.setOnClickListener(v -> listener.onItemClick(makanan));
    }

    @Override
    public int getItemCount() {
        return listMakanan.size();
    }

    public static class MakananViewHolder extends RecyclerView.ViewHolder {
        TextView tvNama, tvPorsi, tvUploader;
        Button btnDetail;

        public MakananViewHolder(@NonNull View itemView) {
            super(itemView);
            tvNama = itemView.findViewById(R.id.tvItemNama);
            tvPorsi = itemView.findViewById(R.id.tvItemPorsi);
            tvUploader = itemView.findViewById(R.id.tvItemUploader);
            btnDetail = itemView.findViewById(R.id.btnDetail);
        }
    }
}
