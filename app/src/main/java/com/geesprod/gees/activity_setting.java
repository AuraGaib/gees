package com.geesprod.gees;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.view.menu.ActionMenuItemView;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.geesprod.gees.Prevalent.Prevalent;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.StorageTask;
import com.squareup.picasso.Picasso;
import com.theartofdev.edmodo.cropper.CropImage;

import java.util.HashMap;

import de.hdodenhof.circleimageview.CircleImageView;

public class activity_setting extends AppCompatActivity {
    private CircleImageView potoProfilSetting;
    private EditText gantiNama, gantiNomor,gantiAlamat;
    private TextView updateSetting, closeSetting, gantiPoto;
    private Uri imageUri;
    private String myUrl = "";
    private StorageTask uploadTask;
    private StorageReference refGambar;
    private String checker = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);

        refGambar = FirebaseStorage.getInstance().getReference().child("Poto_Profil");
        potoProfilSetting = (CircleImageView) findViewById(R.id.setting_image_profile);
        gantiNama = (EditText) findViewById(R.id.settings_nama);
        gantiNomor = (EditText) findViewById(R.id.settings_nomor);
        gantiAlamat = (EditText) findViewById(R.id.settings_address);
        updateSetting = (TextView) findViewById(R.id.update_setting);
        closeSetting = (TextView) findViewById(R.id.close_setting);
        gantiPoto = (TextView) findViewById(R.id.ganti_profil);

        userInfo(potoProfilSetting, gantiNama, gantiNomor, gantiAlamat);

        closeSetting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        updateSetting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (checker.equals("clicked")){
                    saveSetting();
                }
                else {
                    updateUserInfo();
                }

            }
        });

        gantiPoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                checker = "clicked";

                CropImage.activity(imageUri)
                        .setAspectRatio(1,1)
                        .start(activity_setting.this);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode==CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE && resultCode==RESULT_OK && data!=null){
            CropImage.ActivityResult result = CropImage.getActivityResult(data);
            imageUri = result.getUri();
            potoProfilSetting.setImageURI(imageUri);
        }
        else {
            Toast.makeText(this,"Crop Foto Error !", Toast.LENGTH_SHORT).show();
            startActivity(new Intent(activity_setting.this, activity_setting.class));
            finish();
        }
    }

    private void saveSetting() {
        if (TextUtils.isEmpty(gantiNama.getText().toString())){
            Toast.makeText(this,"Field Nama Wajib Diisi !", Toast.LENGTH_SHORT).show();
        }
        else if (TextUtils.isEmpty(gantiNomor.getText().toString())){
            Toast.makeText(this,"Field Nomor Handphone Wajib Diisi !", Toast.LENGTH_SHORT).show();
        }
        else if (TextUtils.isEmpty(gantiAlamat.getText().toString())){
            Toast.makeText(this,"Field Alamat Wajib Diisi !", Toast.LENGTH_SHORT).show();
        }
        else if (checker.equals("clicked")){
            uploadGambarSetting();
        }

    }

    private void uploadGambarSetting() {
        final ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setTitle("Update Foto Profil");
        progressDialog.setMessage("Mohon tunggu, sistem sedang memperbaharui foto profil anda.");
        progressDialog.setCanceledOnTouchOutside(false);
        progressDialog.show();

        if (imageUri !=null){
            final StorageReference fileRef = refGambar.child(Prevalent.currentOnlineUser.getPhone()
            +".jpg");
            uploadTask = fileRef.putFile(imageUri);
            uploadTask.continueWithTask(new Continuation() {
                @Override
                public Object then(@NonNull Task task) throws Exception {
                    if (!task.isSuccessful()){
                        throw  task.getException();
                    }
                    return fileRef.getDownloadUrl();
                }
            })
            .addOnCompleteListener(new OnCompleteListener<Uri>() {
                @Override
                public void onComplete(@NonNull Task<Uri> task) {
                    if (task.isSuccessful()){
                        Uri downloadUrl = task.getResult();
                        myUrl = downloadUrl.toString();

                        DatabaseReference ref = FirebaseDatabase.getInstance().getReference().child("Akun");
                        HashMap<String, Object> userMap = new HashMap<>();
                        userMap.put("name", gantiNama.getText().toString());
                        userMap.put("phone", gantiNomor.getText().toString());
                        userMap.put("address", gantiAlamat.getText().toString());
                        userMap.put("image", myUrl);
                        ref.child(Prevalent.currentOnlineUser.getPhone()).updateChildren(userMap);

                        progressDialog.dismiss();
                        startActivity(new Intent(activity_setting.this, MainActivity.class));
                        Toast.makeText(activity_setting.this,"Info Anda Sudah Diperbaharui, Silahkan Login Kembali", Toast.LENGTH_SHORT).show();

                    }
                    else {
                        progressDialog.dismiss();
                        Toast.makeText(activity_setting.this,"Info Anda Gagal Diperbaharui", Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }
        else {
            Toast.makeText(this, "Gambar gagal terpilih !", Toast.LENGTH_SHORT).show();
        }
    }

    private void updateUserInfo(){
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference().child("Akun");
        HashMap<String, Object> userMap = new HashMap<>();
        userMap.put("name", gantiNama.getText().toString());
        userMap.put("phone", gantiNomor.getText().toString());
        userMap.put("address", gantiAlamat.getText().toString());
        ref.child(Prevalent.currentOnlineUser.getPhone()).updateChildren(userMap);

        startActivity(new Intent(activity_setting.this, MainActivity.class));
        Toast.makeText(activity_setting.this,"Info Anda Sudah Diperbaharui, Silahkan Login Kembali", Toast.LENGTH_SHORT).show();

    }

    private void userInfo(final CircleImageView potoProfilSetting, final EditText gantiNama, final EditText gantiNomor, final EditText gantiAlamat) {
        DatabaseReference refUser;
        refUser = FirebaseDatabase.getInstance().getReference().child("Akun").child(Prevalent.currentOnlineUser.getPhone());

        refUser.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists()){
                    if (dataSnapshot.child("image").exists()){
                        String image = dataSnapshot.child("image").getValue().toString();
                        String name = dataSnapshot.child("name").getValue().toString();
                        String phone = dataSnapshot.child("phone").getValue().toString();
                        String address = dataSnapshot.child("address").getValue().toString();

                        Picasso.get().load(image).fit().into(potoProfilSetting);
                        gantiNama.setText(name);
                        gantiNomor.setText(phone);
                        gantiAlamat.setText(address);
                    }
                }

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }
}
