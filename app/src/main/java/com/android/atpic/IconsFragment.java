package com.android.atpic;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

import com.android.atpic.adapter.ProductAdapter;
import com.android.atpic.model.Product;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class IconsFragment extends Fragment {

    private static final String TAG = "IconFragment";
    DatabaseReference database;
    ArrayList<Product> iconList;
    Product icon;
    ProductAdapter adapter;
    RecyclerView recyclerView;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_icons, container, false);

        recyclerView = view.findViewById(R.id.rv_icon);

        database = FirebaseDatabase.getInstance().getReference();
        iconList = new ArrayList<>();

        database.child("product").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                iconList.clear();

                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    icon = dataSnapshot.getValue(Product.class);
                    if (icon != null && icon.getId_category().equals("Icon")) {
                        iconList.add(icon);
                        Log.d(TAG, "onDataChange: icon added iconList");
                    }
                    adapter.setProductList(iconList);
                    recyclerView.setAdapter(adapter);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        return view;

    }
}