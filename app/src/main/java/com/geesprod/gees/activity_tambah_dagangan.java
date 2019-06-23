package com.geesprod.gees;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;

public class activity_tambah_dagangan extends AppCompatActivity {
    private String NamaKategori, namaProdukDagangan, deskripsiDagangan, hargaDagangan, saveCurrentDate,saveCurrentTime, productRandomKey
            ,downloadImageUrl;
    private TextView judulTambah;
    private Button tambahProduk;
    private ImageView gambarDaganganPilih;
    private EditText inputNamaProduk, inputDeskripsiProduk, inputHargaProduk;
    private static final int GalleryPick =1;
    private Uri imageUri;
    private StorageReference gambarRef;
    private DatabaseReference ref;
    private ProgressDialog loadingBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tambah_dagangan);

        NamaKategori = getIntent().getExtras().get("kategori").toString();
        gambarRef = FirebaseStorage.getInstance().getReference().child("Gambar_Produk");
        ref = FirebaseDatabase.getInstance().getReference().child("Product");
        Toast.makeText(this,NamaKategori,Toast.LENGTH_SHORT).show();

        tambahProduk = (Button) findViewById(R.id.tombolTambahProduk);
        gambarDaganganPilih = (ImageView) findViewById(R.id.gambarDagangan);
        inputNamaProduk = (EditText) findViewById(R.id.namaProduk);
        inputDeskripsiProduk = (EditText) findViewById(R.id.deskripsiProduk);
        inputHargaProduk = (EditText) findViewById(R.id.hargaProduk);
        judulTambah = (TextView) findViewById(R.id.judulTambahDagangan);
        loadingBar = new ProgressDialog(this);

        judulTambah.setText("Tambah Produk "+NamaKategori);

        gambarDaganganPilih.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                OpenGallery();
            }
        });

        tambahProduk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                validasiDataProduk();
            }
        });


    }

    private void validasiDataProduk() {
        namaProdukDagangan = inputNamaProduk.getText().toString();
        deskripsiDagangan = inputDeskripsiProduk.getText().toString();
        hargaDagangan = inputHargaProduk.getText().toString();

        if (imageUri == null ){
            Toast.makeText(this,"Tidak ada gambar produk",Toast.LENGTH_SHORT);
        }
        else if(TextUtils.isEmpty(namaProdukDagangan)){
            Toast.makeText(this, "Inputkan Nama Produk .....",Toast.LENGTH_SHORT);
        }
        else if(TextUtils.isEmpty(deskripsiDagangan)){
            Toast.makeText(this, "Inputkan Deskripsi Produk .....",Toast.LENGTH_SHORT);
        }
        else if(TextUtils.isEmpty(hargaDagangan)){
            Toast.makeText(this, "Inputkan Harga Produk .....",Toast.LENGTH_SHORT);
        }
        else {
            penyimpananProduk();
        }
    }

    private void penyimpananProduk() {
        loadingBar.setTitle("Menambahkan Produk Dagangan");
        loadingBar.setMessage("Mohon menunggu, sedang dilakukan proses penambahan produk .....");
        loadingBar.setCanceledOnTouchOutside(false);
        loadingBar.show();

        Calendar kalender = Calendar.getInstance();

        SimpleDateFormat tanggal = new SimpleDateFormat("MMM dd, yyyy");
        saveCurrentDate = tanggal.format(kalender.getTime());

        SimpleDateFormat jam = new SimpleDateFormat("HH:mm:ss a");
        saveCurrentTime = jam.format(kalender.getTime());

        productRandomKey = saveCurrentDate + saveCurrentTime;

        final StorageReference filePath = gambarRef.child(imageUri.getLastPathSegment() + productRandomKey + "jpg");
        final UploadTask aplot = filePath.putFile(imageUri);

        aplot.addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                String pesan = e.toString();
                Toast.makeText(activity_tambah_dagangan.this, "Pesan Error : "+pesan, Toast.LENGTH_SHORT).show();
                loadingBar.dismiss();
            }
        }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                Toast.makeText(activity_tambah_dagangan.this, "Upload Gambar Berhasil", Toast.LENGTH_SHORT).show();
                Task<Uri> uriTask = aplot.continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
                    @Override
                    public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception {
                        if(!task.isSuccessful()){
                            throw task.getException();
                        }
                        downloadImageUrl = filePath.getDownloadUrl().toString();
                        return filePath.getDownloadUrl();

                    }
                }).addOnCompleteListener(new OnCompleteListener<Uri>() {
                    @Override
                    public void onComplete(@NonNull Task<Uri> task) {
                        if (task.isSuccessful()) {
                            downloadImageUrl = task.getResult().toString();
                            Toast.makeText(activity_tambah_dagangan.this, "Sukses Menerima Image Uri", Toast.LENGTH_SHORT).show();

                            SaveProductInfoToDatabase();
                        }
                    }
                });
            }
        });
    }

    private void SaveProductInfoToDatabase() {
        HashMap<String, Object> productMap = new HashMap<>();
        productMap.put("pid", productRandomKey);
        productMap.put("date", saveCurrentDate);
        productMap.put("time", saveCurrentTime);
        productMap.put("description", deskripsiDagangan);
        productMap.put("image", downloadImageUrl);
        productMap.put("category", NamaKategori);
        productMap.put("price", hargaDagangan);
        productMap.put("pname", namaProdukDagangan);

        ref.child(productRandomKey).updateChildren(productMap)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task)
                    {
                        if (task.isSuccessful())
                        {
                            Intent intent = new Intent(activity_tambah_dagangan.this, activity_admin_katagori.class);
                            startActivity(intent);

                            loadingBar.dismiss();
                            Toast.makeText(activity_tambah_dagangan.this, "Product is added successfully..", Toast.LENGTH_SHORT).show();
                        }
                        else
                        {
                            loadingBar.dismiss();
                            String message = task.getException().toString();
                            Toast.makeText(activity_tambah_dagangan.this, "Error: " + message, Toast.LENGTH_SHORT).show();
                        }
                    }
                });

    }

    private void OpenGallery() {
        Intent galleryUntent = new Intent();
        galleryUntent.setAction(Intent.ACTION_GET_CONTENT);
        galleryUntent.setType("image/*");
        startActivityForResult(galleryUntent,GalleryPick);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==GalleryPick && resultCode==RESULT_OK && data != null){
            imageUri = data.getData();
            gambarDaganganPilih.setImageURI(imageUri);
        }
    }
}
