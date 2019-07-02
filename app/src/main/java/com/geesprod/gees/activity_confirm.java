package com.geesprod.gees;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.geesprod.gees.Prevalent.Prevalent;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;

public class activity_confirm extends AppCompatActivity {
    private EditText nameEdit, phoneEdit, addressEdit, cityEdit;
    private Button tombolKonfirmasi;
    private String totalAmount = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_confirm);

        totalAmount = getIntent().getStringExtra("Total_Price");
        Toast.makeText(this, "Total Harga Belanja Anda IDR "+totalAmount, Toast.LENGTH_SHORT).show();

        tombolKonfirmasi = (Button) findViewById(R.id.tombol_konfirmasi);
        nameEdit = (EditText) findViewById(R.id.namaPembeli);
        phoneEdit = (EditText) findViewById(R.id.nomorPembeli);
        addressEdit = (EditText) findViewById(R.id.alamatPembeli);
        cityEdit = (EditText) findViewById(R.id.kotaPembeli);

        tombolKonfirmasi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Check();
            }
        });

    }

    private void Check() {
        if (TextUtils.isEmpty(nameEdit.getText().toString())){
            Toast.makeText(this, "Masukan Nama Penerima", Toast.LENGTH_SHORT).show();
        }
        else if (TextUtils.isEmpty(phoneEdit.getText().toString())){
            Toast.makeText(this, "Masukan Nomor Penerima", Toast.LENGTH_SHORT).show();
        }
        else if (TextUtils.isEmpty(addressEdit.getText().toString())){
            Toast.makeText(this, "Masukan Alamat Penerima", Toast.LENGTH_SHORT).show();
        }
        else if (TextUtils.isEmpty(cityEdit.getText().toString())){
            Toast.makeText(this, "Masukan Kota Penerima", Toast.LENGTH_SHORT).show();
        }
        else {
            confirmOrder();
        }
    }

    private void confirmOrder() {
       final String waktuDisimpan,tanggalDisimpan;

        Calendar kalenderTanggal = Calendar.getInstance();
        SimpleDateFormat tanggal = new SimpleDateFormat("MMMM dd, yyyy");
        tanggalDisimpan = tanggal.format(kalenderTanggal.getTime());

        SimpleDateFormat waktu = new SimpleDateFormat("HH:mm:ss a");
        waktuDisimpan = waktu.format(kalenderTanggal.getTime());

        final DatabaseReference orderRef = FirebaseDatabase.getInstance().getReference().child("Pesanan")
                .child(Prevalent.currentOnlineUser.getPhone());

        HashMap<String, Object> orderMap = new HashMap<>();
        orderMap.put("totalAmount",totalAmount);
        orderMap.put("penerima",nameEdit.getText().toString());
        orderMap.put("telpon",phoneEdit.getText().toString());
        orderMap.put("alamat",addressEdit.getText().toString());
        orderMap.put("kota",cityEdit.getText().toString());
        orderMap.put("date",waktuDisimpan);
        orderMap.put("time",tanggalDisimpan);
        orderMap.put("state", "Belum");

        orderRef.updateChildren(orderMap).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()){
                    FirebaseDatabase.getInstance().getReference().child("Daftar_Keranjang")
                            .child("Transaksi_User")
                            .child(Prevalent.currentOnlineUser.getPhone())
                            .removeValue()
                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if (task.isSuccessful()){
                                        Toast.makeText(activity_confirm.this, "Pesanan Anda Berhasil Diinput",Toast.LENGTH_SHORT).show();
                                        Intent intent = new Intent(activity_confirm.this, activity_home_user.class);
                                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                        startActivity(intent);
                                        finish();
                                    }
                                }
                            });
                }

            }
        });
    }
}
