package com.android.atpic.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.android.atpic.R;
import com.android.atpic.model.Product;

import java.util.ArrayList;

public class ProductAdapter extends RecyclerView.Adapter<ProductAdapter.ViewHolder> {
    private Context context;
    private ArrayList<Product> productList = new ArrayList<>();

    public ProductAdapter(Context context){
        this.context = context;
    }

    public ArrayList<Product> getProductList() {
        return productList;
    }

    public void setProductList(ArrayList<Product> productList) {
        this.productList = productList;
    }

    public int getCount() {
        return productList.size();
    }

    public Object getItem(int i) {
        return productList.get(i);
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_cardview, viewGroup, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Product product = (Product) getItem(position);
        holder.name.setText(product.getName());
        holder.price.setText((int) product.getPrice());
    }
    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public int getItemCount() {
        return 0;
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        private TextView price, name;
        private ImageView image;

        ViewHolder(View view){
            super(view);
            name = view.findViewById(R.id.tv_productName);
            price = view.findViewById(R.id.tv_productPrice);
            image = view.findViewById(R.id.iv_productImage);
        }
    }
}
