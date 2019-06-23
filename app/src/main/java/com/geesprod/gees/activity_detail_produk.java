package com.geesprod.gees;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.cepheuen.elegantnumberbutton.view.ElegantNumberButton;
import com.geesprod.gees.Model.Product;
import com.geesprod.gees.Prevalent.Prevalent;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import org.w3c.dom.Text;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;

public class activity_detail_produk extends AppCompatActivity {
    private Button tombolKeranjang;
    private ImageView gambarDetalProduk;
    private ElegantNumberButton numberButton;
    private TextView hargaDetailProduk, deskripsiDetailProduk, namaDetailProduk;
    private String productID = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_produk);
        gambarDetalProduk = (ImageView) findViewById(R.id.detailProduk);
        numberButton = (ElegantNumberButton) findViewById(R.id.nomorElegan);
        hargaDetailProduk = (TextView) findViewById(R.id.hargaDetail);
        deskripsiDetailProduk = (TextView) findViewById(R.id.deskripsiDetail);
        namaDetailProduk = (TextView) findViewById(R.id.namaDetail);
        tombolKeranjang = (Button) findViewById(R.id.tombolKeCart);

        productID = getIntent().getStringExtra("pid");

        getProductDetail(productID);
        tombolKeranjang.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                cartList();
            }
        });

    }

    private void cartList() {
        String waktuDisimpan,tanggalDisimpan;

        Calendar kalenderTanggal = Calendar.getInstance();
        SimpleDateFormat tanggal = new SimpleDateFormat("MMMM dd, yyyy");
        tanggalDisimpan = tanggal.format(kalenderTanggal.getTime());

        SimpleDateFormat waktu = new SimpleDateFormat("HH:mm:ss a");
        waktuDisimpan = waktu.format(kalenderTanggal.getTime());

        final DatabaseReference daftarKeranjangRef = FirebaseDatabase.getInstance().getReference().child("Daftar_Keranjang");
        final HashMap<String, Object> cartMap = new HashMap<>();
        cartMap.put("pid",productID);
        cartMap.put("pname",namaDetailProduk.getText().toString());
        cartMap.put("price",hargaDetailProduk.getText().toString());
        cartMap.put("date",tanggalDisimpan);
        cartMap.put("time",waktuDisimpan);
        cartMap.put("banyak",numberButton.getNumber());
        cartMap.put("diskon","");

        daftarKeranjangRef.child("Transaksi_User").child(Prevalent.currentOnlineUser.getPhone())
                .child("Product")
                .child(productID)
                .updateChildren(cartMap)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()){
                            daftarKeranjangRef.child("Transaksi_Admin").child(Prevalent.currentOnlineUser.getPhone())
                                    .child("Product")
                                    .updateChildren(cartMap)
                                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {
                                            if(task.isSuccessful()){
                                                Toast.makeText(activity_detail_produk.this, "Pesanan Berhasil Ditambahkan Ke Keranjang", Toast.LENGTH_SHORT)
                                                        .show();
                                                Intent intent = new Intent(activity_detail_produk.this, activity_home_user.class);
                                                startActivity(intent);

                                            }
                                        }
                                    });
                        }
                    }
                });
    }

    private void getProductDetail(String productID) {
        DatabaseReference refProduk = FirebaseDatabase.getInstance().getReference().child("Product");
        refProduk.child(productID).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()){
                    Product product = dataSnapshot.getValue(Product.class);
                    namaDetailProduk.setText(product.getPname());
                    hargaDetailProduk.setText(product.getPrice());
                    deskripsiDetailProduk.setText(product.getDescription());

                    Picasso.get().load(product.getImage()).resize(100,130).into(gambarDetalProduk);
                }

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }
}
