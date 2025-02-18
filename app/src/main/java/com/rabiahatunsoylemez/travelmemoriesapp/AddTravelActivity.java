package com.rabiahatunsoylemez.travelmemoriesapp;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;


import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;

public class AddTravelActivity extends AppCompatActivity {
    private EditText edtCity, edtNote, edtMapLink;
    private CheckBox chkFavorite;
    private Button btnSave;
    private DatabaseHelper dbHelper;

    private int travelId = -1; // Varsayılan olarak -1, yani yeni kayıt

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_travel);

        edtCity = findViewById(R.id.edtCity);
        edtNote = findViewById(R.id.edtNote);
        edtMapLink = findViewById(R.id.edtMapLink);
        chkFavorite = findViewById(R.id.chkFavorite);
        btnSave = findViewById(R.id.btnSave);

        dbHelper = new DatabaseHelper(this);

        // Intent'ten gelen verileri kontrol et
        Intent intent = getIntent();
        travelId = intent.getIntExtra("id", -1); // Varsayılan olarak -1 döner
        if (travelId != -1) {
            // Güncelleme için gelen veriler
            edtCity.setText(intent.getStringExtra("city"));
            edtNote.setText(intent.getStringExtra("note"));
            edtMapLink.setText(intent.getStringExtra("mapLink"));
            chkFavorite.setChecked(intent.getBooleanExtra("isFavorite", false));
        }

        btnSave.setOnClickListener(v -> {
            String city = edtCity.getText().toString();
            String note = edtNote.getText().toString();
            String mapLink = edtMapLink.getText().toString();
            boolean isFavorite = chkFavorite.isChecked();

            if (travelId == -1) {
                // Yeni kayıt ekleme
                dbHelper.addTravel(city, note, mapLink, isFavorite);
            } else {
                // Mevcut kaydı güncelleme
                dbHelper.updateTravel(travelId, city, note, mapLink, isFavorite);
            }

            finish(); // Aktiviteyi kapat
        });
    }
}

