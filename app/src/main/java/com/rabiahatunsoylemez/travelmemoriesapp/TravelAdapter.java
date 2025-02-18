package com.rabiahatunsoylemez.travelmemoriesapp;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import android.app.AlertDialog; // Silme işlemi için onay kutusu
import android.content.Intent; // Düzenleme için yeni bir aktivite başlatma
import android.net.Uri; // Harita bağlantısını açmak için
import android.view.View; // Görünüm olaylarını yönetmek için

public class TravelAdapter extends RecyclerView.Adapter<TravelAdapter.ViewHolder> {

    private ArrayList<Travel> travelList;
    private Context context;

    public TravelAdapter(ArrayList<Travel> travelList, Context context) {
        this.travelList = travelList;
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_travel, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Travel travel = travelList.get(position);
        holder.txtCity.setText(travel.getCity());
        holder.txtNote.setText(travel.getNote());
        holder.imgFavorite.setVisibility(travel.isFavorite() ? View.VISIBLE : View.GONE);

        // Harita bağlantısına tıklama
        holder.itemView.setOnClickListener(v -> {
            Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(travel.getMapLink()));
            context.startActivity(browserIntent);
        });

        // Düzenle düğmesine tıklama
        holder.btnEdit.setOnClickListener(v -> {
            Intent intent = new Intent(context, AddTravelActivity.class);
            intent.putExtra("id", travel.getId());
            intent.putExtra("city", travel.getCity());
            intent.putExtra("note", travel.getNote());
            intent.putExtra("mapLink", travel.getMapLink());
            intent.putExtra("isFavorite", travel.isFavorite());
            context.startActivity(intent);
        });

        // Sil düğmesine tıklama
        holder.btnDelete.setOnClickListener(v -> {
            new AlertDialog.Builder(context)
                    .setTitle("Seyahati Sil")
                    .setMessage("Bu seyahati silmek istediğinize emin misiniz?")
                    .setPositiveButton("Evet", (dialog, which) -> {
                        DatabaseHelper dbHelper = new DatabaseHelper(context);
                        dbHelper.deleteTravel(travel.getId());
                        travelList.remove(position);
                        notifyItemRemoved(position);
                        notifyItemRangeChanged(position, travelList.size());
                    })
                    .setNegativeButton("Hayır", null)
                    .show();
        });
    }

    @Override
    public int getItemCount() {
        return travelList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView txtCity, txtNote;
        ImageView imgFavorite;
        Button btnEdit, btnDelete;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            txtCity = itemView.findViewById(R.id.txtCity);
            txtNote = itemView.findViewById(R.id.txtNote);
            imgFavorite = itemView.findViewById(R.id.imgFavorite);
            btnEdit = itemView.findViewById(R.id.btnEdit);
            btnDelete = itemView.findViewById(R.id.btnDelete);
        }
    }
}