package com.android.atpic.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.android.atpic.CustomClass;
import com.android.atpic.ProductActivity;
import com.android.atpic.R;
import com.android.atpic.model.Product;
import com.bumptech.glide.Glide;

import java.util.ArrayList;

public class CartAdapter extends RecyclerView.Adapter<CartAdapter.CardViewViewHolder> {
    private Context context;
    private ArrayList<Product> productList;

    public CartAdapter(Context context){
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
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_landscapeview,parent,  false);
        return new CardViewViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CardViewViewHolder holder, int position) {
        holder.name.setText(getProductList().get(position).getName());
        String priceToString = String.valueOf(getProductList().get(position).getPrice());
        holder.price.setText(priceToString);
        String photoURL = CustomClass.removeLastChar(getProductList().get(position).getPhotoURL());
        String[] strs = photoURL.split(",");
        Glide.with(context)
                .load(strs[0])
                .into(holder.image);
        holder.landscapeView.setOnClickListener(new View.OnClickListener() {
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
        private ConstraintLayout landscapeView;

        public CardViewViewHolder(@NonNull View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.tv_productName);
            price = itemView.findViewById(R.id.tv_productPrice);
            image = itemView.findViewById(R.id.iv_productImage);
            landscapeView = itemView.findViewById(R.id.landscapeView);
        }
    }
}