package com.geesprod.gees;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.geesprod.gees.Model.Cart;
import com.geesprod.gees.Prevalent.Prevalent;
import com.geesprod.gees.ViewHolder.CartViewHolder;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class activity_cart extends AppCompatActivity {
    private RecyclerView recyclerView;
    private RecyclerView.LayoutManager layoutManager;
    private Button nextProses;
    private TextView txtHarga, pesan;
    private int overTotal = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart);

        recyclerView = findViewById(R.id.cart_list);
        recyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);

        nextProses = (Button) findViewById(R.id.tombol_next_cart);
        txtHarga = (TextView) findViewById(R.id.harga_total);
        pesan = (TextView) findViewById(R.id.pesanSukses);

        nextProses.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                txtHarga.setText("Total Harga = IDR " + String.valueOf(overTotal));

                Intent intent = new Intent(activity_cart.this, activity_confirm.class);
                intent.putExtra("Total_Price", String.valueOf(overTotal));
                startActivity(intent);
                finish();
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        CheckOrderState();

        final DatabaseReference cartRef = FirebaseDatabase.getInstance().getReference().child("Daftar_Keranjang");

        FirebaseRecyclerOptions<Cart> options =
                new FirebaseRecyclerOptions.Builder<Cart>()
                .setQuery(cartRef.child("Transaksi_User")
                .child(Prevalent.currentOnlineUser.getPhone()).child("Product"), Cart.class)
                .build();

        FirebaseRecyclerAdapter<Cart, CartViewHolder> adapter
                = new FirebaseRecyclerAdapter<Cart, CartViewHolder>(options) {
            @Override
            protected void onBindViewHolder(@NonNull CartViewHolder holder, int position, @NonNull final Cart model) {

                holder.txtProduxtName.setText(model.getPname());
                holder.txtProduxtQty.setText("Jumlah : "+model.getBanyak());
                holder.txtProduxtPrice.setText("Harga : "+model.getPrice());

                int onProdukHarga = ((Integer.valueOf(model.getPrice()))) * Integer.valueOf(model.getBanyak());
                overTotal = overTotal  + onProdukHarga;

                holder.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                         CharSequence options[] = new CharSequence[]{
                                 "EDIT",
                                 "HAPUS"

                         };
                        AlertDialog.Builder builder = new AlertDialog.Builder(activity_cart.this);
                        builder.setTitle("Pengaturan Keranjang : ");
                        builder.setItems(options, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                if (i ==0 ){
                                    Intent intent = new Intent(activity_cart.this, activity_detail_produk.class);
                                    intent.putExtra("pid", model.getPid());
                                    startActivity(intent);
                                }
                                if (i ==1){
                                    cartRef.child("Transaksi_User")
                                            .child((Prevalent.currentOnlineUser.getPhone()))
                                            .child("Product")
                                            .child(model.getPid())
                                            .removeValue()
                                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                                                @Override
                                                public void onComplete(@NonNull Task<Void> task) {
                                                    if (task.isSuccessful()){
                                                        Toast.makeText(activity_cart.this, "Item Dihapus Dari Keranjang Anda", Toast.LENGTH_SHORT)
                                                                .show();
                                                        Intent intent = new Intent(activity_cart.this, activity_home_user.class);
                                                        startActivity(intent);
                                                    }

                                                }
                                            });
                                }

                            }
                        });
                        builder.show();
                    }
                });
            }

            @NonNull
            @Override
            public CartViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int i) {

                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.cart_item, parent, false);
                CartViewHolder holder = new CartViewHolder(view);
                return  holder;
            }
        };
        recyclerView.setAdapter(adapter);
        adapter.startListening();
    }
    private void CheckOrderState(){
        DatabaseReference orderRef;
        orderRef = FirebaseDatabase.getInstance().getReference().child("Pesanan").child(Prevalent.currentOnlineUser.getPhone());

        orderRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()){
                    String statusPengiriman = dataSnapshot.child("state").getValue().toString();
                    String userName = dataSnapshot.child("penerima").getValue().toString();

                    if (statusPengiriman.equals("Sudah")){
                        txtHarga.setText("Pelanggan, "+userName + "."+"\n Pesanan Anda Sudah Terkirim");
                        recyclerView.setVisibility(View.GONE);

                        pesan.setVisibility(View.VISIBLE);
                        pesan.setText("Pesanan Anda sedang dalam perjalanan ke alamat Anda, mohon kesediaannya untuk menunggu.");
                        nextProses.setVisibility(View.GONE);

                        Toast.makeText(activity_cart.this, "Keranjang Anda kosong, Silahkan berbelanja kembali. Happy Shopping !"
                        , Toast.LENGTH_SHORT).show();

                    }
                    else if (statusPengiriman.equals("Belum")){
                        txtHarga.setText("Pelanggan, "+userName + "\n Pesanan Anda Belum Terkirim");
                        recyclerView.setVisibility(View.GONE);

                        pesan.setVisibility(View.VISIBLE);
                        nextProses.setVisibility(View.GONE);

                        Toast.makeText(activity_cart.this, "Keranjang Anda kosong, Silahkan berbelanja kembali. Happy Shopping !"
                                , Toast.LENGTH_SHORT).show();
                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }
}
