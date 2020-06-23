package com.confortapp.leon.confortapp.ViewHolder;

import androidx.recyclerview.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.confortapp.leon.confortapp.Interface.ItemClickListener;
import com.confortapp.leon.confortapp.R;

/**
 * Created by Leon on 12.02.2018.
 */

public class ProductsViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

    public TextView products_name, products_price, products_discount;
    public ImageView products_image, fav_image, shareImage;

    private ItemClickListener itemClickListener;

    public void setItemClickListener(ItemClickListener itemClickListener) {
        this.itemClickListener = itemClickListener;
    }

    public ProductsViewHolder(View itemView) {
        super(itemView);

        products_name = (TextView) itemView.findViewById(R.id.product_name);
        products_image = (ImageView) itemView.findViewById(R.id.product_image);
        fav_image = (ImageView) itemView.findViewById(R.id.fav);
        shareImage = (ImageView) itemView.findViewById(R.id.btnShare);
        products_price = (TextView) itemView.findViewById(R.id.product_price);
        products_discount = (TextView) itemView.findViewById(R.id.products_discount);

        itemView.setOnClickListener(this);

    }

    @Override
    public void onClick(View view) {
        itemClickListener.onClick(view, getAdapterPosition(), false);
    }
}
