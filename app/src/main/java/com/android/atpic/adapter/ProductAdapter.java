package com.android.atpic.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.android.atpic.CustomClass;
import com.android.atpic.ProductActivity;
import com.android.atpic.R;
import com.android.atpic.model.Product;
import com.bumptech.glide.Glide;
import com.google.android.material.card.MaterialCardView;

import java.util.ArrayList;

public class ProductAdapter extends RecyclerView.Adapter<ProductAdapter.CardViewViewHolder> {
    private Context context;
    private ArrayList<Product> productList;

    public ProductAdapter(Context context){
        this.context = context;
    }

    public ArrayList<Product> getProductList() {
        return productList;
    }

    public void setProductList(ArrayList<Product> productList) {
        this.productList = productList;
    }

    @NonNull
    @Override
    public CardViewViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_cardview,parent,  false);
        return new CardViewViewHolder(view);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull ProductAdapter.CardViewViewHolder holder, final int position) {

        holder.name.setText(getProductList().get(position).getName());
        holder.price.setText(String.valueOf(getProductList().get(position).getPrice()));
//        holder.image.setImageBitmap(getProductList().get(position).getPhotoUri().get(0));
        String photoURL = CustomClass.removeLastChar(getProductList().get(position).getPhotoURL());
        String[] strs = photoURL.split(",");
        Glide.with(context)
                .load(strs[0])
                .into(holder.image);
        holder.cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, ProductActivity.class);
                intent.putExtra(ProductActivity.EXTRA_PARCEL, getProductList().get(position));
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return getProductList().size();
    }

    public class CardViewViewHolder extends RecyclerView.ViewHolder {
        private TextView price, name;
        private ImageView image;
        private MaterialCardView cardView;

        public CardViewViewHolder(@NonNull View itemView) {
            super(itemView);
            cardView = itemView.findViewById(R.id.cardview);
            name = itemView.findViewById(R.id.tv_productName);
            price = itemView.findViewById(R.id.tv_productPrice);
            image = itemView.findViewById(R.id.iv_productImage);
        }
    }
}