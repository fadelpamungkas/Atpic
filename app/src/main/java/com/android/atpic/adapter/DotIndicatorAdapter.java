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
import androidx.recyclerview.widget.RecyclerView;

import com.android.atpic.CustomClass;
import com.android.atpic.R;
import com.bumptech.glide.Glide;
public class DotIndicatorAdapter extends RecyclerView.Adapter<DotIndicatorAdapter.CardViewViewHolder> {
    private Context context;
    private String[] strs;

    public DotIndicatorAdapter(Context context){
        this.context = context;
    }

    public String[] getStrs() {
        return strs;
    }

    public void setStrs(String[] strs) {
        this.strs = strs;
    }

    @NonNull
    @Override
    public CardViewViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.activity_product_header,parent,  false);
        return new CardViewViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CardViewViewHolder holder, int position) {
            Glide.with(context)
                    .load(strs[position])
                    .into(holder.image);
    }

    @Override
    public int getItemCount() {
        return strs.length;
    }

    public class CardViewViewHolder extends RecyclerView.ViewHolder {
        private ImageView image;

        public CardViewViewHolder(@NonNull View itemView) {
            super(itemView);
            image = itemView.findViewById(R.id.image_header);
        }
    }
}