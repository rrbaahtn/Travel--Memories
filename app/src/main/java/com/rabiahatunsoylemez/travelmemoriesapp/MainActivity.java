package com.rabiahatunsoylemez.travelmemoriesapp;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    private RecyclerView recyclerView;
    private TravelAdapter travelAdapter;
    private ArrayList<Travel> travelList;
    private DatabaseHelper dbHelper;
    private Button btnAddTravel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // RecyclerView ve DatabaseHelper bağlantısı
        recyclerView = findViewById(R.id.recyclerView);
        Button btnAddTravel = findViewById(R.id.btnAddTravel); // Buton tanımlaması

        dbHelper = new DatabaseHelper(this);

        // Verileri çek ve adaptörü ata
        travelList = new ArrayList<>(dbHelper.getAllTravels(false)); // Tüm seyahatleri al
        travelAdapter = new TravelAdapter(travelList, this);

        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(travelAdapter);

        // Butonun tıklama işlevi
        btnAddTravel.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, AddTravelActivity.class);
            startActivity(intent);
        });


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Menü kaynağını bağla
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.menu_show_all) {
            // Tüm seyahatleri göster
            travelList.clear();
            travelList.addAll(dbHelper.getAllTravels(false)); // Tüm seyahatleri getir
            travelAdapter.notifyDataSetChanged();
            return true;
        } else if (id == R.id.menu_add_travel) {
            // Yeni seyahat ekleme ekranına git
            Intent intent = new Intent(MainActivity.this, AddTravelActivity.class);
            startActivity(intent);
            return true;
        } else if (id == R.id.menu_show_favorites) {
            // Sadece favorileri göster
            travelList.clear();
            travelList.addAll(dbHelper.getAllTravels(true)); // Sadece favorileri getir
            travelAdapter.notifyDataSetChanged();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onResume() {
        super.onResume();
        // Veri güncelleme (özellikle yeni seyahat eklendiğinde geri döndüğünde güncelleme yapar)
        travelList.clear();
        travelList.addAll(dbHelper.getAllTravels(false)); // Tüm seyahatleri güncelle
        travelAdapter.notifyDataSetChanged();
    }
}
