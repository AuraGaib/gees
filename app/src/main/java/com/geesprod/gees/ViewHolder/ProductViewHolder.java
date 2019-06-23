package com.geesprod.gees.ViewHolder;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.geesprod.gees.Interface.ItemClickListener;
import com.geesprod.gees.R;

public class ProductViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

    public TextView textProductName, textProductDescription, txtProductPrice;
    public ImageView imageViewProduct;
    public ItemClickListener listener;

    public ProductViewHolder(View itemView){
        super(itemView);

        imageViewProduct = (ImageView) itemView.findViewById(R.id.product_image);
        textProductName = (TextView) itemView.findViewById(R.id.namaProdukDagangan);
        textProductDescription = (TextView) itemView.findViewById(R.id.produkDeskripsi);
        txtProductPrice = (TextView) itemView.findViewById(R.id.produkHarga);
    }

    public void setItemClickListener(ItemClickListener listener){
        this.listener = listener;
    }

    @Override
    public void onClick(View view) {
        listener.onClick(view, getAdapterPosition(), false);

    }
}
