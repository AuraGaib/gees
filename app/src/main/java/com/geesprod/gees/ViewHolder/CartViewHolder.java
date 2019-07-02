package com.geesprod.gees.ViewHolder;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.geesprod.gees.Interface.ItemClickListener;
import com.geesprod.gees.R;

public class CartViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
    public TextView txtProduxtName, txtProduxtPrice, txtProduxtQty;
    private ItemClickListener itemClickListener;

    public CartViewHolder(@NonNull View itemView) {
        super(itemView);

        txtProduxtName = itemView.findViewById(R.id.cart_nama_produk);
        txtProduxtPrice = itemView.findViewById(R.id.cart_harga_produk);
        txtProduxtQty = itemView.findViewById(R.id.cart_jumlah_pesanan);

    }

    @Override
    public void onClick(View view) {
        itemClickListener.onClick(view, getAdapterPosition(), false);

    }

    public void setItemClickListener(ItemClickListener itemClickListener) {
        this.itemClickListener = itemClickListener;
    }
}
