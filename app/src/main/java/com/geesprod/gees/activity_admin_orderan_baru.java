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

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.geesprod.gees.Model.Order;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class activity_admin_orderan_baru extends AppCompatActivity {
    private RecyclerView orderList;
    private DatabaseReference orderRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_orderan_baru);

        orderRef = FirebaseDatabase.getInstance().getReference().child("Pesanan");
        orderList = findViewById(R.id.orderList);
        orderList.setLayoutManager(new LinearLayoutManager(this));
    }

    @Override
    protected void onStart() {
        super.onStart();
        FirebaseRecyclerOptions<Order> options = new FirebaseRecyclerOptions.Builder<Order>()
                .setQuery(orderRef, Order.class)
                .build();

        FirebaseRecyclerAdapter<Order,AdminOrderViewHolder> adapter =
                new FirebaseRecyclerAdapter<Order, AdminOrderViewHolder>(options) {
                    @Override
                    protected void onBindViewHolder(@NonNull AdminOrderViewHolder holder, final int position, @NonNull final Order model) {
                        holder.userName.setText("Nama : "+model.getPenerima());
                        holder.userNumber.setText("Telepon : "+model.getTelpon());
                        holder.userTotal.setText("Total : IDR "+model.getTotalAmount());
                        holder.userAddress.setText("Alamat : "+model.getAlamat()+", "+model.getKota());
                        holder.userDate.setText("Tranaksi : "+model.getDate()+"  "+model.getTime());

                        holder.tampilkanPesanan.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                String uID = getRef(position).getKey();

                                Intent intent = new Intent(activity_admin_orderan_baru.this, activity_admin_user_products.class);
                                intent.putExtra("uid",uID);
                                startActivity(intent);
                            }
                        });

                        holder.itemView.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                CharSequence options[] = new CharSequence[]{
                                        "YES",
                                        "NO"
                                };

                                AlertDialog.Builder builder = new AlertDialog.Builder(activity_admin_orderan_baru.this );
                                builder.setTitle("Apakah Pengiriman Sudah Dilakukan ?");
                                builder.setItems(options, new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialogInterface, int i) {
                                        if( i == 0){
                                            String uID = getRef(position).getKey();
                                            RemoverOrder(uID);
                                        }
                                        else{
                                            finish();
                                        }
                                    }
                                });
                                builder.show();
                            }
                        });

                    }

                    @NonNull
                    @Override
                    public AdminOrderViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int i) {
                        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_orderan, parent, false);
                        return new AdminOrderViewHolder(view);
                    }
                };
        orderList.setAdapter(adapter);
        adapter.startListening();
    }

    private void RemoverOrder(String uID) {
        orderRef.child(uID).removeValue();
    }

    public static class AdminOrderViewHolder extends RecyclerView.ViewHolder{
        public TextView  userName, userNumber, userTotal, userDate, userAddress;
        public Button tampilkanPesanan;
        public AdminOrderViewHolder (View itemView){
            super(itemView);

            userName = itemView.findViewById(R.id.cart_nama_pembeli);
            userNumber = itemView.findViewById(R.id.nomor_pembeli);
            userTotal = itemView.findViewById(R.id.total_belanja);
            userDate = itemView.findViewById(R.id.tanggalTransaksi);
            userAddress = itemView.findViewById(R.id.alamat_pembeli);
            tampilkanPesanan = itemView.findViewById(R.id.detailOrderAdmin);
        }
    }
}
