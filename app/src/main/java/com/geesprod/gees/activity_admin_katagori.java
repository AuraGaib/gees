package com.geesprod.gees;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import java.util.zip.Inflater;

public class activity_admin_katagori extends AppCompatActivity {
    private ImageView buku,halaman,pamflet,wallpaper,garskin,vector;
    private Button tombolKeluar, cekOrderan;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_katagori);
        buku = (ImageView) findViewById(R.id.buku);
        halaman = (ImageView) findViewById(R.id.page);
        pamflet = (ImageView) findViewById(R.id.pamflet_promosi);
        wallpaper = (ImageView) findViewById(R.id.wallpaper);
        garskin = (ImageView) findViewById(R.id.garskin);
        vector = (ImageView) findViewById(R.id.vector);
        tombolKeluar = (Button) findViewById(R.id.tombolLogoutAdmin);
        cekOrderan = (Button) findViewById(R.id.tombolOrderanAdmin);

        tombolKeluar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(activity_admin_katagori.this, MainActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
                finish();

            }
        });

        cekOrderan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(activity_admin_katagori.this, activity_admin_orderan_baru.class);
                startActivity(intent);
            }
        });

        buku.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(activity_admin_katagori.this, activity_tambah_dagangan.class);
                intent.putExtra("kategori", "buku");
                startActivity(intent);
            }
        });

        halaman.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(activity_admin_katagori.this, activity_tambah_dagangan.class);
                intent.putExtra("kategori", "halaman");
                startActivity(intent);
            }
        });

        pamflet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(activity_admin_katagori.this, activity_tambah_dagangan.class);
                intent.putExtra("kategori", "pamflet");
                startActivity(intent);
            }
        });

        wallpaper.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(activity_admin_katagori.this, activity_tambah_dagangan.class);
                intent.putExtra("kategori", "wallpaper");
                startActivity(intent);
            }
        });

        garskin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(activity_admin_katagori.this, activity_tambah_dagangan.class);
                intent.putExtra("kategori", "garskin");
                startActivity(intent);
            }
        });

        vector.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(activity_admin_katagori.this, activity_tambah_dagangan.class);
                intent.putExtra("kategori", "vector");
                startActivity(intent);
            }
        });

    }
}
