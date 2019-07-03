package com.geesprod.gees;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.geesprod.gees.Model.Product;
import com.geesprod.gees.ViewHolder.ProductViewHolder;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.squareup.picasso.Picasso;

public class activity_cari_produk extends AppCompatActivity {

    private Button cariTombol;
    private EditText dataDicari;
    private RecyclerView daftarCari;
    private String inputKata;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cari_produk);

        dataDicari = findViewById(R.id.cariProduk);
        cariTombol = findViewById(R.id.tombolCari);
        daftarCari = findViewById(R.id.daftarCari);
        daftarCari.setLayoutManager(new LinearLayoutManager(activity_cari_produk.this));

        cariTombol.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                inputKata = dataDicari.getText().toString();

                onStart();
            }
        });

    }

    @Override
    protected void onStart() {
        super.onStart();
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference().child("Product");

        FirebaseRecyclerOptions<Product> options =
                new FirebaseRecyclerOptions.Builder<Product>()
                .setQuery(reference.orderByChild("pname").startAt(inputKata), Product.class)
                .build();

        FirebaseRecyclerAdapter<Product, ProductViewHolder> adapter =
                new FirebaseRecyclerAdapter<Product, ProductViewHolder>(options) {
                    @Override
                    protected void onBindViewHolder(@NonNull ProductViewHolder holder, int position, @NonNull final Product model) {
                        holder.textProductName.setText(model.getPname());
                        holder.textProductDescription.setText(model.getDescription());
                        holder.txtProductPrice.setText("IDR "+model.getPrice());
                        Picasso.get().load(model.getImage()).into(holder.imageViewProduct);

                        holder.itemView.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                Intent intent = new Intent(activity_cari_produk.this, activity_detail_produk.class);
                                intent.putExtra("pid", model.getPid());
                                startActivity(intent);
                            }
                        });

                    }

                    @NonNull
                    @Override
                    public ProductViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int i) {

                        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.product_item, parent, false);
                        ProductViewHolder holder = new ProductViewHolder(view);
                        return holder;
                    }
                };
        daftarCari.setAdapter(adapter);
        adapter.startListening();
    }
}
