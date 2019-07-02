package com.geesprod.gees;

import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.geesprod.gees.Model.Cart;
import com.geesprod.gees.ViewHolder.CartViewHolder;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class activity_admin_produk extends AppCompatActivity {
    private RecyclerView detailPesanan;
    RecyclerView.LayoutManager layoutManager;
    private DatabaseReference produkref;
    private String userID = "";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_produk);

        userID = getIntent().getStringExtra("uid");
        detailPesanan = findViewById(R.id.dataOrder);
        detailPesanan.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(this);
        detailPesanan.setLayoutManager(layoutManager);

        produkref = FirebaseDatabase.getInstance().getReference().child("Daftar_Keranjang")
        .child("Transaksi_Admin").child(userID).child("Product");

    }

    @Override
    protected void onStart() {
        super.onStart();

        FirebaseRecyclerOptions<Cart> options =
                new FirebaseRecyclerOptions.Builder<Cart>()
                .setQuery(produkref, Cart.class)
                .build();

        FirebaseRecyclerAdapter<Cart, CartViewHolder> adapter =
                new FirebaseRecyclerAdapter<Cart, CartViewHolder>(options) {
                    @Override
                    protected void onBindViewHolder(@NonNull CartViewHolder holder, int position, @NonNull Cart model) {
                        holder.txtProduxtName.setText(model.getPname());
                        holder.txtProduxtQty.setText("Jumlah : "+model.getBanyak());
                        holder.txtProduxtPrice.setText("Harga : "+model.getPrice());

                    }

                    @NonNull
                    @Override
                    public CartViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int i) {
                        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.cart_item, parent, false);
                        CartViewHolder holder = new CartViewHolder(view);
                        return  holder;
                    }
                };
        detailPesanan.setAdapter(adapter);
        adapter.startListening();
    }
}
